package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.ExecuteConfig;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExecuteAbility<C extends ExecuteConfig> extends AbilityEffect<C> {
    @Override
    public String getAbilityGroupName() {
        return "Execute";
    }

    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        if (player.hasEffect(ModEffects.EXECUTE)) {
            return false;
        }
        final EffectInstance newEffect = new EffectInstance(config.getEffect(), config.getEffectDuration(), config.getAmplifier(), false, config.getType().showParticles, config.getType().showIcon);
        player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.EXECUTION_SFX, SoundCategory.PLAYERS, 0.4f, 1.0f);
        player.playNotifySound(ModSounds.EXECUTION_SFX, SoundCategory.PLAYERS, 0.4f, 1.0f);
        player.addEffect(newEffect);
        return false;
    }

    @SubscribeEvent
    public void onDamage(final LivingDamageEvent event) {
        if (!this.doCulling() || event.getEntity().getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof PlayerEntity)) {
            return;
        }
        final PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        if (!(player.getCommandSenderWorld() instanceof ServerWorld)) {
            return;
        }
        final ServerWorld world = (ServerWorld) player.getCommandSenderWorld();
        final EffectInstance execute = player.getEffect(ModEffects.EXECUTE);
        if (execute == null) {
            return;
        }
        final PlayerAbilitiesData data = PlayerAbilitiesData.get(world);
        final AbilityTree abilities = data.getAbilities(player);
        final AbilityNode<?, ?> node = abilities.getNodeByName("Execute");
        if (node.getAbility() == this && !node.isLearned()) {
            return;
        }
        final C cfg = (C) node.getAbilityConfig();
        final LivingEntity entity = event.getEntityLiving();
        final float dmgDealt = entity.getMaxHealth() * cfg.getHealthPercentage();
        event.setAmount(event.getAmount() + dmgDealt);
        player.getMainHandItem().hurtAndBreak(1, (LivingEntity) player, playerEntity -> {
        });
        player.level.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this.removeEffect(cfg)) {
            player.removeEffect(ModEffects.EXECUTE);
        } else {
//            execute.duration = cfg.getEffectDuration();
        }
    }

    protected boolean removeEffect(final C cfg) {
        return true;
    }

    protected boolean doCulling() {
        return true;
    }
}
