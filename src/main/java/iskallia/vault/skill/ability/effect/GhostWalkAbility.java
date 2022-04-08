package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.GhostWalkConfig;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class GhostWalkAbility<C extends GhostWalkConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Ghost Walk";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        if (player.hasEffect(ModEffects.GHOST_WALK)) {
            return false;
        }


        EffectInstance newEffect = new EffectInstance(config.getEffect(), config.getDurationTicks(), config.getAmplifier(), false, (config.getType()).showParticles, (config.getType()).showIcon);

        player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.GHOST_WALK_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.playNotifySound(ModSounds.GHOST_WALK_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.addEffect(newEffect);
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDamage(LivingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayerEntity && doRemoveWhenDealingDamage()) {
            ServerPlayerEntity player = (ServerPlayerEntity) attacker;
            EffectInstance ghostWalk = player.getEffect(ModEffects.GHOST_WALK);
            if (ghostWalk != null) {
                ServerWorld world = (ServerWorld) player.getCommandSenderWorld();

                PlayerAbilitiesData data = PlayerAbilitiesData.get(world);
                AbilityTree abilities = data.getAbilities((PlayerEntity) player);
                AbilityNode<?, ?> node = abilities.getNodeOf(this);
                if (node.getAbility() == this && node.isLearned()) {
                    player.removeEffect(ModEffects.GHOST_WALK);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onHurt(LivingHurtEvent event) {
        if (isInvulnerable(event.getEntityLiving(), event.getSource())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAttack(LivingAttackEvent event) {
        if (isInvulnerable(event.getEntityLiving(), event.getSource())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof MobEntity && isInvulnerable(event.getTarget(), null)) {
            ((MobEntity) event.getEntityLiving()).setTarget(null);
        }
    }

    private boolean isInvulnerable(@Nullable LivingEntity entity, @Nullable DamageSource source) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            EffectInstance ghostWalk = player.getEffect(ModEffects.GHOST_WALK);
            if (ghostWalk != null && preventsDamage() && (source == null || !source.isBypassInvul())) {
                ServerWorld world = (ServerWorld) player.getCommandSenderWorld();

                PlayerAbilitiesData data = PlayerAbilitiesData.get(world);
                AbilityTree abilities = data.getAbilities((PlayerEntity) player);
                AbilityNode<?, ?> node = abilities.getNodeOf(this);
                if (node.getAbility() == this && node.isLearned()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean preventsDamage() {
        return true;
    }

    protected boolean doRemoveWhenDealingDamage() {
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\GhostWalkAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */