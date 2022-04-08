package iskallia.vault.skill.ability.effect;

import iskallia.vault.init.ModSounds;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;

public class MegaJumpAbility<C extends MegaJumpConfig>
        extends AbilityEffect<C> {
    public String getAbilityGroupName() {
        return "Mega Jump";
    }


    public boolean onAction(C config, ServerPlayerEntity player, boolean active) {
        double magnitude = config.getHeight() * 0.15D;

        double addY = -Math.min(0.0D, player.getDeltaMovement().y());
        player.push(0.0D, addY + magnitude, 0.0D);
        player.startFallFlying();
        player.hurtMarked = true;

        player.playNotifySound(ModSounds.MEGA_JUMP_SFX, SoundCategory.MASTER, 0.3F, 1.0F);
        ((ServerWorld) player.level).sendParticles((IParticleData) ParticleTypes.POOF, player
                .getX(), player.getY(), player.getZ(), 50, 1.0D, 0.5D, 1.0D, 0.0D);

        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\MegaJumpAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */