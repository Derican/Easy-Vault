// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity;

import iskallia.vault.client.util.ColorizationHelper;
import iskallia.vault.init.ModEntities;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.awt.*;
import java.util.Optional;
import java.util.function.Function;

public class FloatingItemEntity extends ItemEntity
{
    private static final DataParameter<Integer> COLOR1;
    private static final DataParameter<Integer> COLOR2;
    
    public FloatingItemEntity(final EntityType<? extends ItemEntity> type, final World world) {
        super((EntityType)type, world);
        this.addTag("PreventMagnetMovement");
    }
    
    public FloatingItemEntity(final World worldIn, final double x, final double y, final double z) {
        this(ModEntities.FLOATING_ITEM, worldIn);
        this.setPos(x, y, z);
        this.yRot = this.random.nextFloat() * 360.0f;
        this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
    }
    
    public FloatingItemEntity(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        this(worldIn, x, y, z);
        this.setItem(stack);
        this.lifespan = Integer.MAX_VALUE;
    }
    
    public static FloatingItemEntity create(final World world, final BlockPos pos, final ItemStack stack) {
        return new FloatingItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define((DataParameter)FloatingItemEntity.COLOR1, 16777215);
        this.getEntityData().define((DataParameter)FloatingItemEntity.COLOR2, 16777215);
    }
    
    public void setColor(final int color) {
        this.setColor(color, color);
    }
    
    public void setColor(final int color1, final int color2) {
        this.entityData.set((DataParameter)FloatingItemEntity.COLOR1, color1);
        this.entityData.set((DataParameter)FloatingItemEntity.COLOR2, color2);
    }
    
    public void tick() {
        this.setDeltaMovement(Vector3d.ZERO);
        super.tick();
        if (this.isAlive() && this.getCommandSenderWorld().isClientSide()) {
            this.playEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        final ParticleManager mgr = Minecraft.getInstance().particleEngine;
        final Vector3d thisPos = this.position().add(0.0, (double)(this.getBbHeight() / 4.0f), 0.0);
        int color1 = (int)this.getEntityData().get((DataParameter)FloatingItemEntity.COLOR1);
        int color2 = (int)this.getEntityData().get((DataParameter)FloatingItemEntity.COLOR2);
        if (color1 == 16777215 && color2 == 16777215) {
            final Optional<Color> override = ColorizationHelper.getCustomColorOverride(this.getItem());
            if (override.isPresent()) {
                color1 = (color2 = override.get().getRGB());
            }
            else {
                color1 = ColorizationHelper.getColor(this.getItem()).map(Color::getRGB).orElse(16777215);
                this.entityData.set((DataParameter)FloatingItemEntity.COLOR1, color1);
                final int r = Math.min((color1 >> 16 & 0xFF) * 2, 255);
                final int g = Math.min((color1 >> 8 & 0xFF) * 2, 255);
                final int b = Math.min((color1 >> 0 & 0xFF) * 2, 255);
                color2 = (r << 16 | g << 8 | b);
                this.entityData.set((DataParameter)FloatingItemEntity.COLOR2, color2);
            }
        }
        if (this.random.nextInt(3) == 0) {
            final Vector3d rPos = thisPos.add((double)((this.random.nextFloat() - this.random.nextFloat()) * (this.random.nextFloat() * 8.0f)), (double)((this.random.nextFloat() - this.random.nextFloat()) * (this.random.nextFloat() * 8.0f)), (double)((this.random.nextFloat() - this.random.nextFloat()) * (this.random.nextFloat() * 8.0f)));
            final SimpleAnimatedParticle p = (SimpleAnimatedParticle)mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0, 0.0, 0.0);
            if (p != null) {
//                p.baseGravity = 0.0f;
                p.setColor(MiscUtils.blendColors(color1, color2, this.random.nextFloat()));
            }
        }
        if (this.random.nextBoolean()) {
            final SimpleAnimatedParticle p = (SimpleAnimatedParticle)mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, thisPos.x, thisPos.y, thisPos.z, (this.random.nextFloat() - this.random.nextFloat()) * 0.2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2, (this.random.nextFloat() - this.random.nextFloat()) * 0.2);
            if (p != null) {
//                p.baseGravity = 0.0f;
                p.setColor(MiscUtils.blendColors(color1, color2, this.random.nextFloat()));
            }
        }
    }
    
    public void playerTouch(final PlayerEntity player) {
        final boolean wasAlive = this.isAlive();
        super.playerTouch(player);
        if (wasAlive && !this.isAlive()) {
            player.getCommandSenderWorld().playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6f, 1.0f);
        }
    }
    
    public boolean isNoGravity() {
        return true;
    }
    
    public IPacket<?> getAddEntityPacket() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        COLOR1 = EntityDataManager.defineId((Class)FloatingItemEntity.class, DataSerializers.INT);
        COLOR2 = EntityDataManager.defineId((Class)FloatingItemEntity.class, DataSerializers.INT);
    }
}
