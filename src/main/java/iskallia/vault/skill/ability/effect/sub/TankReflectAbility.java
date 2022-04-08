package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.init.ModEffects;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.TankReflectConfig;
import iskallia.vault.skill.ability.effect.TankAbility;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TankReflectAbility
        extends TankAbility<TankReflectConfig> {
    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        EffectInstance tank = event.getEntityLiving().getEffect(ModEffects.TANK);
        if (tank == null) {
            return;
        }
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.getCommandSenderWorld().isClientSide() && player.getCommandSenderWorld() instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) player.getCommandSenderWorld();
                AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities(player);
                AbilityNode<?, ?> tankNode = abilities.getNodeByName("Tank");

                if (tankNode.getAbility() == this && tankNode.isLearned()) {
                    TankReflectConfig config = (TankReflectConfig) tankNode.getAbilityConfig();

                    Entity attacker = event.getSource().getEntity();
                    if (attacker instanceof LivingEntity && ((LivingEntity) attacker)
                            .getHealth() > 0.0F && rand
                            .nextFloat() < config.getDamageReflectChance()) {
                        float damage = event.getAmount() * config.getDamageReflectPercent();
                        attacker.hurt(DamageSource.thorns((Entity) player), damage);
                    }
                }
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\TankReflectAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */