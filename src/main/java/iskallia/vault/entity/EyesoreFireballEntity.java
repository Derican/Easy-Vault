// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity;

import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.init.ModParticles;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.DamageSource;
import iskallia.vault.init.ModConfigs;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.RayTraceResult;
import iskallia.vault.init.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;

public class EyesoreFireballEntity extends ProjectileItemEntity
{
    public int explosionPower;
    
    public EyesoreFireballEntity(final EntityType<? extends ProjectileItemEntity> type, final World world) {
        super((EntityType)type, world);
        this.explosionPower = 1;
        this.setItem(new ItemStack((IItemProvider)Items.FIRE_CHARGE));
    }
    
    public EyesoreFireballEntity(final World world, final LivingEntity thrower) {
        super((EntityType)ModEntities.EYESORE_FIREBALL, thrower, world);
        this.explosionPower = 1;
        this.setItem(new ItemStack((IItemProvider)Items.FIRE_CHARGE));
    }
    
    protected void onHit(final RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, Explosion.Mode.NONE);
            this.remove();
        }
    }
    
    protected void onHitEntity(final EntityRayTraceResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            final Entity target = result.getEntity();
            final Entity shooter = this.getOwner();
            final DamageSource source = new IndirectEntityDamageSource("fireball", (Entity)this, shooter).setMagic();
            final float damage = ModConfigs.EYESORE.basicAttack.getDamage(this);
            target.hurt(source, damage);
            if (shooter instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)shooter, target);
            }
        }
    }
    
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.playEffects();
        }
    }
    
    public void playEffects() {
        final Vector3d vec = this.position();
        for (int i = 0; i < 5; ++i) {
            final Vector3d v = vec.add((double)(this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)), (double)(this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)), (double)(this.random.nextFloat() * 0.4f * (this.random.nextBoolean() ? 1 : -1)));
            this.level.addParticle((IParticleData)ModParticles.RED_FLAME.get(), v.x, v.y, v.z, 0.0, 0.0, 0.0);
            this.level.addParticle((IParticleData)ParticleTypes.FLAME, v.x, v.y, v.z, 0.0, 0.0, 0.0);
        }
    }
    
    public boolean canCollideWith(final Entity entity) {
        return super.canCollideWith(entity) && !(entity instanceof EyesoreEntity) && !(entity instanceof EyestalkEntity);
    }
    
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }
    
    public void addAdditionalSaveData(final CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ExplosionPower", this.explosionPower);
    }
    
    public void readAdditionalSaveData(final CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ExplosionPower", 99)) {
            this.explosionPower = nbt.getInt("ExplosionPower");
        }
    }
    
    public boolean isPickable() {
        return false;
    }
    
    public boolean hurt(final DamageSource source, final float amount) {
        return false;
    }
    
    public IPacket<?> getAddEntityPacket() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
}
