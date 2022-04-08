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
    public VaultPearlEntity(World worldIn, LivingEntity throwerIn) {
        super(worldIn, throwerIn);
    }


    protected void onHit(RayTraceResult result) {
        RayTraceResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            onHitEntity((EntityRayTraceResult) result);
        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            onHitBlock((BlockRayTraceResult) result);
        }
        Entity entity = getOwner();

        for (int i = 0; i < 32; i++) {
            this.level.addParticle((IParticleData) ParticleTypes.PORTAL, getX(), getY() + this.random.nextDouble() * 2.0D, getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.level.isClientSide && !this.removed) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entity;
                if (serverplayerentity.connection.getConnection().isConnected() && serverplayerentity.level == this.level && !serverplayerentity.isSleeping()) {
                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }
                    entity.teleportTo(getX(), getY(), getZ());
                    entity.fallDistance = 0.0F;
                }

            } else if (entity != null) {
                entity.teleportTo(getX(), getY(), getZ());
                entity.fallDistance = 0.0F;
            }

            remove();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\entity\VaultPearlEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */