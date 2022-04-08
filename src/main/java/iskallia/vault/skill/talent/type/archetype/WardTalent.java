package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.calc.AbsorptionHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class WardTalent extends ArchetypeTalent {
    private static final Map<UUID, Long> lastAttackedTick = new HashMap<>();

    @Expose
    protected int startRegenAfterCombatSeconds;


    public WardTalent(int cost, int startRegenAfterCombatSeconds, EffectTalent fullAbsorptionEffect, float additionalParryChance) {
        super(cost);
        this.startRegenAfterCombatSeconds = startRegenAfterCombatSeconds;
        this.fullAbsorptionEffect = fullAbsorptionEffect;
        this.additionalParryChance = additionalParryChance;
    }

    @Expose
    protected EffectTalent fullAbsorptionEffect;
    @Expose
    protected float additionalParryChance;

    public int getStartRegenAfterCombatSeconds() {
        return this.startRegenAfterCombatSeconds;
    }

    @Nullable
    public EffectTalent getFullAbsorptionEffect() {
        return this.fullAbsorptionEffect;
    }

    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }

    public static boolean isGrantedFullAbsorptionEffect(ServerWorld world, PlayerEntity sPlayer) {
        TalentTree tree = PlayerTalentsData.get(world).getTalents(sPlayer);
        if (tree.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.WARD)) {
            float max = AbsorptionHelper.getMaxAbsorption(sPlayer);
            return (sPlayer.getAbsorptionAmount() / max >= 0.9F);
        }
        return false;
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent event) {
        LivingEntity attacked = event.getEntityLiving();
        if (attacked.getCommandSenderWorld().isClientSide() || !(attacked instanceof ServerPlayerEntity)) {
            return;
        }
        lastAttackedTick.put(attacked.getUUID(), Long.valueOf(attacked.getServer().overworld().getGameTime()));
    }

    @SubscribeEvent
    public static void onChangeDim(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        if (!event.getDimension().equals(Vault.VAULT_KEY)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        player.setAbsorptionAmount(0.0F);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START ||
                !event.side.isServer() || event.player.tickCount % 20 != 0) {
            return;
        }


        if (!(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        if (!ArchetypeTalent.isEnabled(event.player.level)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        UUID playerUUID = sPlayer.getUUID();
        float maxAbsorption = AbsorptionHelper.getMaxAbsorption((PlayerEntity) sPlayer);
        if (maxAbsorption <= 0.1F) {
            return;
        }

        TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);


        int startSeconds = tree.getLearnedNodes(WardTalent.class).stream().mapToInt(node -> ((WardTalent) node.getTalent()).getStartRegenAfterCombatSeconds()).min().orElse(-1);
        if (startSeconds < 0) {
            return;
        }

        if (lastAttackedTick.containsKey(playerUUID)) {
            long lastAttacked = ((Long) lastAttackedTick.get(playerUUID)).longValue();
            long current = sPlayer.getServer().overworld().getGameTime();
            if (lastAttacked >= current - (startSeconds * 20)) {
                return;
            }
        }

        float absorption = sPlayer.getAbsorptionAmount();
        if (absorption < maxAbsorption)
            sPlayer.setAbsorptionAmount(Math.min(absorption + 2.0F, maxAbsorption));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\archetype\WardTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */