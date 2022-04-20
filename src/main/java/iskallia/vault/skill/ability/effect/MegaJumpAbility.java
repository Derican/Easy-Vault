package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;

public class MegaJumpAbility<C extends MegaJumpConfig> extends AbilityEffect<C> {
    @Override
    public String getAbilityGroupName() {
        return "Mega Jump";
    }

    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        final double magnitude = config.getHeight() * 0.15;
        final double addY = -Math.min(0.0, player.getDeltaMovement().y());
        player.push(0.0, addY + magnitude, 0.0);
        player.startFallFlying();
        player.hurtMarked = true;
        player.playNotifySound(ModSounds.MEGA_JUMP_SFX, SoundCategory.MASTER, 0.3f, 1.0f);
        ((ServerWorld) player.level).sendParticles((IParticleData) ParticleTypes.POOF, player.getX(), player.getY(), player.getZ(), 50, 1.0, 0.5, 1.0, 0.0);
        return true;
    }
}
