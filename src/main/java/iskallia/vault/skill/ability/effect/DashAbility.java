package iskallia.vault.skill.ability.effect;

import iskallia.vault.easteregg.GrasshopperNinja;
import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.DashConfig;
import iskallia.vault.util.MathUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class DashAbility<C extends DashConfig> extends AbilityEffect<C> {
    @Override
    public String getAbilityGroupName() {
        return "Dash";
    }

    @Override
    public boolean onAction(final C config, final ServerPlayerEntity player, final boolean active) {
        final Vector3d lookVector = player.getLookAngle();
        final double magnitude = (10 + config.getExtraRadius()) * 0.15;
        final double extraPitch = 10.0;
        Vector3d dashVector = new Vector3d(lookVector.x(), lookVector.y(), lookVector.z());
        final float initialYaw = (float) MathUtilities.extractYaw(dashVector);
        dashVector = MathUtilities.rotateYaw(dashVector, initialYaw);
        double dashPitch = Math.toDegrees(MathUtilities.extractPitch(dashVector));
        if (dashPitch + extraPitch > 90.0) {
            dashVector = new Vector3d(0.0, 1.0, 0.0);
            dashPitch = 90.0;
        } else {
            dashVector = MathUtilities.rotateRoll(dashVector, (float) Math.toRadians(-extraPitch));
            dashVector = MathUtilities.rotateYaw(dashVector, -initialYaw);
            dashVector = dashVector.normalize();
        }
        final double coef = 1.6 - MathUtilities.map(Math.abs(dashPitch), 0.0, 90.0, 0.6, 1.0);
        dashVector = dashVector.scale(magnitude * coef);
        player.push(dashVector.x(), dashVector.y(), dashVector.z());
        player.hurtMarked = true;
        ((ServerWorld) player.level).sendParticles((IParticleData) ParticleTypes.POOF, player.getX(), player.getY(), player.getZ(), 50, 1.0, 0.5, 1.0, 0.0);
        if (GrasshopperNinja.isGrasshopperShape(player)) {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GRASSHOPPER_BRRR, SoundCategory.PLAYERS, 0.2f, 1.0f);
            player.playNotifySound(ModSounds.GRASSHOPPER_BRRR, SoundCategory.PLAYERS, 0.2f, 1.0f);
            GrasshopperNinja.achieve(player);
        } else {
            player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.DASH_SFX, SoundCategory.PLAYERS, 0.2f, 1.0f);
            player.playNotifySound(ModSounds.DASH_SFX, SoundCategory.PLAYERS, 0.2f, 1.0f);
        }
        return true;
    }
}
