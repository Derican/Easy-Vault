package iskallia.vault.util;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.RageSyncMessage;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlayerRageHelper {
    public static final int MAX_RAGE = 100;
    public static final int RAGE_DEGEN_INTERVAL = 10;
    private static final Map<UUID, Integer> lastAttackTick;
    private static final Map<UUID, Integer> currentRage;
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap;
    private static int clientRageInfo;

    @SubscribeEvent
    public static void onChangeDim(final EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        if (!event.getDimension().equals(Vault.VAULT_KEY)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        PlayerRageHelper.lastAttackTick.remove(player.getUUID());
        setCurrentRage(player, 0);
    }

    @SubscribeEvent
    public static void onGainRage(final LivingHurtEvent event) {
        final World world = event.getEntityLiving().getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        final Entity source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity attacker = (ServerPlayerEntity) source;
        final UUID playerUUID = attacker.getUUID();
        final int lastAttack = PlayerRageHelper.lastAttackTick.getOrDefault(playerUUID, 0);
        if (lastAttack <= attacker.tickCount - 10) {
            final TalentTree tree = PlayerTalentsData.get(attacker.getLevel()).getTalents((PlayerEntity) attacker);
            final TalentNode<BarbaricTalent> node = tree.getNodeOf(ModConfigs.TALENTS.BARBARIC);
            if (ArchetypeTalent.isEnabled(world) && node.isLearned()) {
                final int rage = getCurrentRage(playerUUID, LogicalSide.SERVER);
                setCurrentRage(attacker, rage + node.getTalent().getRagePerAttack());
                refreshDamageBuff(attacker, node.getTalent().getDamageMultiplierPerRage());
                PlayerRageHelper.lastAttackTick.put(playerUUID, attacker.tickCount);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(final LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            PlayerRageHelper.lastAttackTick.remove(event.getEntityLiving().getUUID());
        }
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        final UUID playerUUID = sPlayer.getUUID();
        final int rage = getCurrentRage((PlayerEntity) sPlayer, LogicalSide.SERVER);
        if (rage <= 0) {
            removeExistingDamageBuff(sPlayer);
            return;
        }
        if (!canGenerateRage(sPlayer)) {
            setCurrentRage(sPlayer, 0);
            removeExistingDamageBuff(sPlayer);
            return;
        }
        final TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        final BarbaricTalent talent = tree.getNodeOf(ModConfigs.TALENTS.BARBARIC).getTalent();
        if (ArchetypeTalent.isEnabled((World) sPlayer.getLevel())) {
            final int lastTick = PlayerRageHelper.lastAttackTick.getOrDefault(playerUUID, 0);
            if (lastTick < sPlayer.tickCount - talent.getRageDegenTickDelay() && sPlayer.tickCount % 10 == 0) {
                setCurrentRage(sPlayer, rage - 1);
                refreshDamageBuff(sPlayer, talent.getDamageMultiplierPerRage());
            }
        }
    }

    private static void setCurrentRage(final ServerPlayerEntity player, int rage) {
        rage = MathHelper.clamp(rage, 0, 100);
        PlayerRageHelper.currentRage.put(player.getUUID(), rage);
        ModNetwork.CHANNEL.sendTo(new RageSyncMessage(rage), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static int getCurrentRage(final PlayerEntity player, final LogicalSide side) {
        return getCurrentRage(player.getUUID(), side);
    }

    public static int getCurrentRage(final UUID playerUUID, final LogicalSide side) {
        if (side.isServer()) {
            return PlayerRageHelper.currentRage.getOrDefault(playerUUID, 0);
        }
        return PlayerRageHelper.clientRageInfo;
    }

    private static boolean canGenerateRage(final ServerPlayerEntity sPlayer) {
        final TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        return tree.hasLearnedNode(ModConfigs.TALENTS.BARBARIC) && ArchetypeTalent.isEnabled((World) sPlayer.getLevel());
    }

    private static void refreshDamageBuff(final ServerPlayerEntity sPlayer, final float dmgMultiplier) {
        final UUID playerUUID = sPlayer.getUUID();
        final int rage = getCurrentRage(playerUUID, LogicalSide.SERVER);
        removeExistingDamageBuff(sPlayer);
        PlayerRageHelper.multiplierMap.put(playerUUID, PlayerDamageHelper.applyMultiplier(sPlayer, rage * dmgMultiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY));
    }

    private static void removeExistingDamageBuff(final ServerPlayerEntity player) {
        final PlayerDamageHelper.DamageMultiplier existing = PlayerRageHelper.multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientCache() {
        PlayerRageHelper.clientRageInfo = 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static void receiveRageUpdate(final RageSyncMessage msg) {
        PlayerRageHelper.clientRageInfo = msg.getRage();
    }

    static {
        lastAttackTick = new HashMap<UUID, Integer>();
        currentRage = new HashMap<UUID, Integer>();
        multiplierMap = new HashMap<UUID, PlayerDamageHelper.DamageMultiplier>();
        PlayerRageHelper.clientRageInfo = 0;
    }
}
