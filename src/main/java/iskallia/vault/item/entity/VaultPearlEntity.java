package iskallia.vault.item.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class VaultPearlEntity extends EnderPearlEntity {
    public VaultPearlEntity(final World worldIn, final LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }

    protected void onHit(final RayTraceResult result) {
        final RayTraceResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            this.onHitEntity((EntityRayTraceResult) result);
        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            this.onHitBlock((BlockRayTraceResult) result);
        }
        final Entity entity = this.getOwner();
        for (int i = 0; i < 32; ++i) {
            this.level.addParticle((IParticleData) ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian());
        }
        if (!this.level.isClientSide && !this.removed) {
            if (entity instanceof ServerPlayerEntity) {
                final ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entity;
                if (serverplayerentity.connection.getConnection().isConnected() && serverplayerentity.level == this.level && !serverplayerentity.isSleeping()) {
                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }
                    entity.teleportTo(this.getX(), this.getY(), this.getZ());
                    entity.fallDistance = 0.0f;
                }
            } else if (entity != null) {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.fallDistance = 0.0f;
            }
            this.remove();
        }
    }
}
