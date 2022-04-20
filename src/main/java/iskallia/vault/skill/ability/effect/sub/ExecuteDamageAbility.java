package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.init.ModEffects;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.ExecuteDamageConfig;
import iskallia.vault.skill.ability.effect.ExecuteAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExecuteDamageAbility extends ExecuteAbility<ExecuteDamageConfig> {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(final LivingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) {
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
        if (node.getAbility() == this && node.isLearned()) {
            final ExecuteDamageConfig cfg = (ExecuteDamageConfig) node.getAbilityConfig();
            final float missingHealth = event.getEntityLiving().getMaxHealth() - event.getEntityLiving().getHealth();
            final float damageDealt = missingHealth * cfg.getDamagePercentPerMissingHealthPercent();
            event.setAmount(event.getAmount() + damageDealt);
        }
    }

    @Override
    protected boolean doCulling() {
        return false;
    }
}
