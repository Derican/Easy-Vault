package iskallia.vault.entity.ai.eyesore;

import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.math.vector.Vector3d;
import java.util.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import iskallia.vault.entity.EyesoreFireballEntity;
import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import iskallia.vault.world.vault.player.VaultRunner;
import net.minecraft.entity.player.ServerPlayerEntity;
import java.util.Optional;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

public class BasicAttackTask<T extends MobEntity> extends EyesoreTask<T>
{
    public int tick;
    
    public BasicAttackTask(final T entity) {
        super(entity);
        this.tick = 0;
    }

    @Override
    public void tick() {
        if (this.isFinished()) {
            return;
        }
        final Optional<ServerPlayerEntity> player;
        final List<Optional<ServerPlayerEntity>> players = this.getVault().getPlayers().stream().filter(player1 -> player1 instanceof VaultRunner).map(p -> p.getServerPlayer(this.getWorld().getServer())).collect(Collectors.toList());
        player = ((this.tick / 27 < players.size()) ? players.get(this.tick / 27) : Optional.empty());
        if (player.isPresent()) {
            final ServerPlayerEntity target = player.get();
            this.getEntity().getLookControl().setLookAt(target, 30.0f, 30.0f);
            if (this.tick % 9 == 0) {
                final EyesoreFireballEntity throwEntity = new EyesoreFireballEntity(this.getWorld(), this.getEntity());
                throwEntity.setPos(throwEntity.getX(), throwEntity.getY() - 5.0, throwEntity.getZ());
                final double d0 = target.getEyeY() - 1.100000023841858;
                final double d2 = target.getX() - this.getEntity().getX();
                final double d3 = d0 - throwEntity.getY();
                final double d4 = target.getZ() - this.getEntity().getZ();
                final float f = MathHelper.sqrt(d2 * d2 + d4 * d4) * 0.1f;
                this.shoot(throwEntity, d2, d3 + f, d4, 3.2f, 0.0f, this.getWorld().random);
                this.getWorld().playSound(null, this.getEntity().blockPosition(), SoundEvents.BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0f, 0.4f / (this.getWorld().random.nextFloat() * 0.4f + 0.8f));
                this.getWorld().addFreshEntity(throwEntity);
            }
        }
        ++this.tick;
    }
    
    public void shoot(final Entity projectile, final double x, final double y, final double z, final float velocity, final float inaccuracy, final Random rand) {
        final Vector3d vector3d = new Vector3d(x, y, z).normalize().add(rand.nextGaussian() * 0.007499999832361937 * inaccuracy, rand.nextGaussian() * 0.007499999832361937 * inaccuracy, rand.nextGaussian() * 0.007499999832361937 * inaccuracy).scale(velocity);
        projectile.setDeltaMovement(vector3d);
        final float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        projectile.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * 57.2957763671875);
        projectile.xRot = (float)(MathHelper.atan2(vector3d.y, f) * 57.2957763671875);
        projectile.yRotO = projectile.yRot;
        projectile.xRotO = projectile.xRot;
    }
    
    @Override
    public boolean isFinished() {
        if (this.getVault() == null) {
            return true;
        }
        final List<Optional<ServerPlayerEntity>> players = this.getVault().getPlayers().stream().map(p -> p.getServerPlayer(this.getWorld().getServer())).collect(Collectors.toList());
        return this.tick / 27 >= Math.min(players.size(), 2);
    }
    
    @Override
    public void reset() {
        this.tick = 0;
    }
}
