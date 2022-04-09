// 
// Decompiled by Procyon v0.6.0
// 

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

public class TankAbility<C extends TankConfig> extends AbilityEffect<C>
{
    @Override
    public String getAbilityGroupName() {
        return "Tank";
    }
    
    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        if (player.hasEffect(ModEffects.TANK)) {
            return false;
        }
        final EffectInstance newEffect = new EffectInstance(ModEffects.TANK, config.getDurationTicks(), config.getAmplifier(), false, config.getType().showParticles, config.getType().showIcon);
        player.level.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), ModSounds.TANK_SFX, SoundCategory.MASTER, 0.175f, 1.0f);
        player.playNotifySound(ModSounds.TANK_SFX, SoundCategory.MASTER, 0.175f, 1.0f);
        player.addEffect(newEffect);
        return false;
    }
    
    @SubscribeEvent
    public void onDamage(final LivingDamageEvent event) {
        final EffectInstance tank = event.getEntityLiving().getEffect(ModEffects.TANK);
        if (tank == null) {
            return;
        }
        if (event.getEntityLiving() instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            if (!player.getCommandSenderWorld().isClientSide() && player.getCommandSenderWorld() instanceof ServerWorld) {
                final ServerWorld world = (ServerWorld)player.getCommandSenderWorld();
                final AbilityTree abilities = PlayerAbilitiesData.get(world).getAbilities(player);
                final AbilityNode<?, ?> tankNode = abilities.getNodeByName("Tank");
                if (tankNode.getAbility() == this && tankNode.isLearned()) {
                    final TankConfig cfg = (TankConfig)tankNode.getAbilityConfig();
                    if (cfg != null) {
                        event.setAmount(event.getAmount() * (1.0f - cfg.getDamageReductionPercent()));
                    }
                }
            }
        }
    }
}
