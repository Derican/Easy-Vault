package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.TankConfig;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TankAbility<C extends TankConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Tank";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        if (player.hasEffect(ModEffects.TANK)) {
            return false;
        }


        EffectInstance newEffect = new EffectInstance(ModEffects.TANK, config.getDurationTicks(), config.getAmplifier(), false, (config.getType()).showParticles, (config.getType()).showIcon);

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TANK_SFX, SoundCategory.MASTER, 0.175F, 1.0F);

        player.playNotifySound(ModSounds.TANK_SFX, SoundCategory.MASTER, 0.175F, 1.0F);

        player.addEffect(newEffect);
        return false;
    }

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
                    TankConfig cfg = (TankConfig) tankNode.getAbilityConfig();
                    if (cfg != null)
                        event.setAmount(event.getAmount() * (1.0F - cfg.getDamageReductionPercent()));
                }
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\TankAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */