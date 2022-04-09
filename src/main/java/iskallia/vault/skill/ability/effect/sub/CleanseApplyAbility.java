// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.CleanseApplyConfig;
import iskallia.vault.skill.ability.effect.CleanseAbility;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;

import java.util.List;

public class CleanseApplyAbility extends CleanseAbility<CleanseApplyConfig>
{
    @Override
    protected void removeEffects(final CleanseApplyConfig config, final ServerPlayerEntity player, final List<EffectInstance> effects) {
        super.removeEffects(config, player, effects);
        for (final EffectInstance effect : effects) {
            final List<LivingEntity> other = EntityHelper.getNearby((IWorld)player.getCommandSenderWorld(), (Vector3i)player.blockPosition(), (float)config.getApplyRadius(), LivingEntity.class);
            other.removeIf(e -> e instanceof PlayerEntity);
            LivingEntity e = null;
            if (!other.isEmpty()) {
                e = other.get(CleanseApplyAbility.rand.nextInt(other.size()));
                e.addEffect(effect);
            }
        }
    }
}
