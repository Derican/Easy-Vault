package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class GlassCannonTalent
        extends ArchetypeTalent {
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap = new HashMap<>();

    @Expose
    protected float damageTakenMultiplier;

    public GlassCannonTalent(int cost, float damageTakenMultiplier, float damageDealtMultiplier) {
        super(cost);
        this.damageTakenMultiplier = damageTakenMultiplier;
        this.damageDealtMultiplier = damageDealtMultiplier;
    }

    @Expose
    protected float damageDealtMultiplier;

    public float getDamageDealtMultiplier() {
        return this.damageDealtMultiplier;
    }

    public float getDamageTakenMultiplier() {
        return this.damageTakenMultiplier;
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        if (!ArchetypeTalent.isEnabled(world)) {
            return;
        }

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) entity;
            TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);

            for (TalentNode<GlassCannonTalent> node : (Iterable<TalentNode<GlassCannonTalent>>) talents.getLearnedNodes(GlassCannonTalent.class)) {
                GlassCannonTalent talent = (GlassCannonTalent) node.getTalent();
                event.setAmount(event.getAmount() * talent.getDamageTakenMultiplier());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);

        if (talents.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.GLASS_CANNON) && isEnabled((World) sPlayer.getLevel())) {
            float damageMultiplier = ((GlassCannonTalent) talents.getNodeOf((TalentGroup) ModConfigs.TALENTS.GLASS_CANNON).getTalent()).getDamageDealtMultiplier();
            PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(sPlayer.getUUID());
            if (existing != null) {
                if (existing.getMultiplier() == damageMultiplier) {
                    existing.refreshDuration(sPlayer.getServer());
                } else {
                    PlayerDamageHelper.removeMultiplier(sPlayer, existing);
                    existing = null;
                }
            }
            if (existing == null) {
                existing = PlayerDamageHelper.applyMultiplier(sPlayer, damageMultiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                multiplierMap.put(sPlayer.getUUID(), existing);
            }
        } else {
            removeExistingDamageBuff(sPlayer);
        }
    }

    private static void removeExistingDamageBuff(ServerPlayerEntity player) {
        PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(player.getUUID());
        if (existing != null)
            PlayerDamageHelper.removeMultiplier(player, existing);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\archetype\GlassCannonTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */