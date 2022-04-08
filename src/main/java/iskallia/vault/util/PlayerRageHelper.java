package iskallia.vault.util;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.RageSyncMessage;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.ArchetypeTalent;
import iskallia.vault.skill.talent.type.archetype.BarbaricTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@EventBusSubscriber
public class PlayerRageHelper {
    public static final int MAX_RAGE = 100;
    public static final int RAGE_DEGEN_INTERVAL = 10;
    private static final Map<UUID, Integer> lastAttackTick = new HashMap<>();
    private static final Map<UUID, Integer> currentRage = new HashMap<>();

    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap = new HashMap<>();

    private static int clientRageInfo = 0;

    @SubscribeEvent
    public static void onChangeDim(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        if (!event.getDimension().equals(Vault.VAULT_KEY)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        lastAttackTick.remove(player.getUUID());
        setCurrentRage(player, 0);
    }

    @SubscribeEvent
    public static void onGainRage(LivingHurtEvent event) {
        World world = event.getEntityLiving().getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        Entity source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity attacker = (ServerPlayerEntity) source;
        UUID playerUUID = attacker.getUUID();

        int lastAttack = ((Integer) lastAttackTick.getOrDefault(playerUUID, Integer.valueOf(0))).intValue();
        if (lastAttack <= attacker.tickCount - 10) {
            TalentTree tree = PlayerTalentsData.get(attacker.getLevel()).getTalents((PlayerEntity) attacker);
            TalentNode<BarbaricTalent> node = tree.getNodeOf((TalentGroup) ModConfigs.TALENTS.BARBARIC);
            if (ArchetypeTalent.isEnabled(world) && node.isLearned()) {
                int rage = getCurrentRage(playerUUID, LogicalSide.SERVER);
                setCurrentRage(attacker, rage + ((BarbaricTalent) node.getTalent()).getRagePerAttack());

                refreshDamageBuff(attacker, ((BarbaricTalent) node.getTalent()).getDamageMultiplierPerRage());

                lastAttackTick.put(playerUUID, Integer.valueOf(attacker.tickCount));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            lastAttackTick.remove(event.getEntityLiving().getUUID());
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        UUID playerUUID = sPlayer.getUUID();

        int rage = getCurrentRage((PlayerEntity) sPlayer, LogicalSide.SERVER);
        if (rage <= 0) {
            removeExistingDamageBuff(sPlayer);

            return;
        }
        if (!canGenerateRage(sPlayer)) {
            setCurrentRage(sPlayer, 0);
            removeExistingDamageBuff(sPlayer);

            return;
        }
        TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        BarbaricTalent talent = (BarbaricTalent) tree.getNodeOf((TalentGroup) ModConfigs.TALENTS.BARBARIC).getTalent();

        if (ArchetypeTalent.isEnabled((World) sPlayer.getLevel())) {
            int lastTick = ((Integer) lastAttackTick.getOrDefault(playerUUID, Integer.valueOf(0))).intValue();
            if (lastTick < sPlayer.tickCount - talent.getRageDegenTickDelay() && sPlayer.tickCount % 10 == 0) {
                setCurrentRage(sPlayer, rage - 1);

                refreshDamageBuff(sPlayer, talent.getDamageMultiplierPerRage());
            }
        }
    }

    private static void setCurrentRage(ServerPlayerEntity player, int rage) {
        rage = MathHelper.clamp(rage, 0, 100);
        currentRage.put(player.getUUID(), Integer.valueOf(rage));

        ModNetwork.CHANNEL.sendTo(new RageSyncMessage(rage), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static int getCurrentRage(PlayerEntity player, LogicalSide side) {
        return getCurrentRage(player.getUUID(), side);
    }

    public static int getCurrentRage(UUID playerUUID, LogicalSide side) {
        if (side.isServer()) {
            return ((Integer) currentRage.getOrDefault(playerUUID, Integer.valueOf(0))).intValue();
        }
        return clientRageInfo;
    }


    private static boolean canGenerateRage(ServerPlayerEntity sPlayer) {
        TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        return (tree.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.BARBARIC) && ArchetypeTalent.isEnabled((World) sPlayer.getLevel()));
    }

    private static void refreshDamageBuff(ServerPlayerEntity sPlayer, float dmgMultiplier) {
        UUID playerUUID = sPlayer.getUUID();
        int rage = getCurrentRage(playerUUID, LogicalSide.SERVER);
        removeExistingDamageBuff(sPlayer);
        multiplierMap.put(playerUUID, PlayerDamageHelper.applyMultiplier(sPlayer, rage * dmgMultiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY));
    }

    private static void removeExistingDamageBuff(ServerPlayerEntity player) {
        PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientCache() {
        clientRageInfo = 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static void receiveRageUpdate(RageSyncMessage msg) {
        clientRageInfo = msg.getRage();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\PlayerRageHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */