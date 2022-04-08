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

public class ExecuteAbility<C extends ExecuteConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Execute";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        if (player.hasEffect(ModEffects.EXECUTE)) {
            return false;
        }


        EffectInstance newEffect = new EffectInstance(config.getEffect(), config.getEffectDuration(), config.getAmplifier(), false, (config.getType()).showParticles, (config.getType()).showIcon);

        player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.EXECUTION_SFX, SoundCategory.PLAYERS, 0.4F, 1.0F);

        player.playNotifySound(ModSounds.EXECUTION_SFX, SoundCategory.PLAYERS, 0.4F, 1.0F);

        player.addEffect(newEffect);
        return false;
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        if (!doCulling() || event.getEntity().getCommandSenderWorld().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        if (!(player.getCommandSenderWorld() instanceof ServerWorld)) {
            return;
        }

        ServerWorld world = (ServerWorld) player.getCommandSenderWorld();
        EffectInstance execute = player.getEffect(ModEffects.EXECUTE);
        if (execute == null) {
            return;
        }
        PlayerAbilitiesData data = PlayerAbilitiesData.get(world);
        AbilityTree abilities = data.getAbilities(player);
        AbilityNode<?, ?> node = abilities.getNodeByName("Execute");
        if (node.getAbility() == this && !node.isLearned()) {
            return;
        }
        ExecuteConfig executeConfig = (ExecuteConfig) node.getAbilityConfig();

        LivingEntity entity = event.getEntityLiving();
        float dmgDealt = entity.getMaxHealth() * executeConfig.getHealthPercentage();
        event.setAmount(event.getAmount() + dmgDealt);
        player.getMainHandItem().hurtAndBreak(1, (LivingEntity) player, playerEntity -> {

        });
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.MASTER, 1.0F, 1.0F);

        if (removeEffect((C) executeConfig)) {
            player.removeEffect(ModEffects.EXECUTE);
        } else {
            execute.duration = executeConfig.getEffectDuration();
        }
    }

    protected boolean removeEffect(C cfg) {
        return true;
    }

    protected boolean doCulling() {
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\ExecuteAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */