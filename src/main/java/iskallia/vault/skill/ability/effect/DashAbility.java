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

public class DashAbility<C extends DashConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Dash";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        Vector3d lookVector = player.getLookAngle();

        double magnitude = (10 + config.getExtraRadius()) * 0.15D;
        double extraPitch = 10.0D;

        Vector3d dashVector = new Vector3d(lookVector.x(), lookVector.y(), lookVector.z());

        float initialYaw = (float) MathUtilities.extractYaw(dashVector);

        dashVector = MathUtilities.rotateYaw(dashVector, initialYaw);

        double dashPitch = Math.toDegrees(MathUtilities.extractPitch(dashVector));

        if (dashPitch + extraPitch > 90.0D) {
            dashVector = new Vector3d(0.0D, 1.0D, 0.0D);
            dashPitch = 90.0D;
        } else {
            dashVector = MathUtilities.rotateRoll(dashVector, (float) Math.toRadians(-extraPitch));
            dashVector = MathUtilities.rotateYaw(dashVector, -initialYaw);
            dashVector = dashVector.normalize();
        }

        double coef = 1.6D - MathUtilities.map(Math.abs(dashPitch), 0.0D, 90.0D, 0.6D, 1.0D);
        dashVector = dashVector.scale(magnitude * coef);
        player.push(dashVector.x(), dashVector.y(), dashVector.z());

        player.hurtMarked = true;

        ((ServerWorld) player.level).sendParticles((IParticleData) ParticleTypes.POOF, player
                .getX(), player.getY(), player.getZ(), 50, 1.0D, 0.5D, 1.0D, 0.0D);


        if (GrasshopperNinja.isGrasshopperShape((PlayerEntity) player)) {
            player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.GRASSHOPPER_BRRR, SoundCategory.PLAYERS, 0.2F, 1.0F);

            player.playNotifySound(ModSounds.GRASSHOPPER_BRRR, SoundCategory.PLAYERS, 0.2F, 1.0F);
            GrasshopperNinja.achieve(player);
        } else {

            player.level.playSound((PlayerEntity) player, player.getX(), player.getY(), player.getZ(), ModSounds.DASH_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);

            player.playNotifySound(ModSounds.DASH_SFX, SoundCategory.PLAYERS, 0.2F, 1.0F);
        }

        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\DashAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */