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

public class ExecuteDamageAbility
        extends ExecuteAbility<ExecuteDamageConfig> {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide()) {
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
        if (node.getAbility() == this && node.isLearned()) {
            ExecuteDamageConfig cfg = (ExecuteDamageConfig) node.getAbilityConfig();

            float missingHealth = event.getEntityLiving().getMaxHealth() - event.getEntityLiving().getHealth();
            float damageDealt = missingHealth * cfg.getDamagePercentPerMissingHealthPercent();
            event.setAmount(event.getAmount() + damageDealt);
        }
    }


    protected boolean doCulling() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\ExecuteDamageAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */