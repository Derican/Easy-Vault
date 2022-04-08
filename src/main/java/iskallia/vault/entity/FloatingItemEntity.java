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


public class FloatingItemEntity
        extends ItemEntity {
    private static final DataParameter<Integer> COLOR1 = EntityDataManager.defineId(FloatingItemEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> COLOR2 = EntityDataManager.defineId(FloatingItemEntity.class, DataSerializers.INT);

    public FloatingItemEntity(EntityType<? extends ItemEntity> type, World world) {
        super(type, world);
        addTag("PreventMagnetMovement");
    }

    public FloatingItemEntity(World worldIn, double x, double y, double z) {
        this(ModEntities.FLOATING_ITEM, worldIn);
        setPos(x, y, z);
        this.yRot = this.random.nextFloat() * 360.0F;
        setDeltaMovement(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
    }

    public FloatingItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        setItem(stack);
        this.lifespan = Integer.MAX_VALUE;
    }

    public static FloatingItemEntity create(World world, BlockPos pos, ItemStack stack) {
        return new FloatingItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();

        getEntityData().define(COLOR1, Integer.valueOf(16777215));
        getEntityData().define(COLOR2, Integer.valueOf(16777215));
    }

    public void setColor(int color) {
        setColor(color, color);
    }

    public void setColor(int color1, int color2) {
        this.entityData.set(COLOR1, Integer.valueOf(color1));
        this.entityData.set(COLOR2, Integer.valueOf(color2));
    }


    public void tick() {
        setDeltaMovement(Vector3d.ZERO);
        super.tick();

        if (isAlive() && getCommandSenderWorld().isClientSide()) {
            playEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
        Vector3d thisPos = position().add(0.0D, (getBbHeight() / 4.0F), 0.0D);

        int color1 = ((Integer) getEntityData().get(COLOR1)).intValue();
        int color2 = ((Integer) getEntityData().get(COLOR2)).intValue();

        if (color1 == 16777215 && color2 == 16777215) {
            Optional<Color> override = ColorizationHelper.getCustomColorOverride(getItem());
            if (override.isPresent()) {
                color1 = ((Color) override.get()).getRGB();
                color2 = color1;
            } else {
                color1 = ((Integer) ColorizationHelper.getColor(getItem()).map(Color::getRGB).orElse(Integer.valueOf(16777215))).intValue();
                this.entityData.set(COLOR1, Integer.valueOf(color1));
                int r = Math.min((color1 >> 16 & 0xFF) * 2, 255);
                int g = Math.min((color1 >> 8 & 0xFF) * 2, 255);
                int b = Math.min((color1 >> 0 & 0xFF) * 2, 255);
                color2 = r << 16 | g << 8 | b;
                this.entityData.set(COLOR2, Integer.valueOf(color2));
            }
        }


        if (this.random.nextInt(3) == 0) {
            Vector3d rPos = thisPos.add(((this.random
                    .nextFloat() - this.random.nextFloat()) * this.random.nextFloat() * 8.0F), ((this.random
                    .nextFloat() - this.random.nextFloat()) * this.random.nextFloat() * 8.0F), ((this.random
                    .nextFloat() - this.random.nextFloat()) * this.random.nextFloat() * 8.0F));
            SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, rPos.x, rPos.y, rPos.z, 0.0D, 0.0D, 0.0D);


            if (p != null) {
//                p.baseGravity = 0.0F;
                p.setColor(MiscUtils.blendColors(color1, color2, this.random.nextFloat()));
            }
        }

        if (this.random.nextBoolean()) {
            SimpleAnimatedParticle p = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, thisPos.x, thisPos.y, thisPos.z, (this.random

                    .nextFloat() - this.random.nextFloat()) * 0.2D, (this.random
                    .nextFloat() - this.random.nextFloat()) * 0.2D, (this.random
                    .nextFloat() - this.random.nextFloat()) * 0.2D);
            if (p != null) {
//                p.baseGravity = 0.0F;
                p.setColor(MiscUtils.blendColors(color1, color2, this.random.nextFloat()));
            }
        }
    }


    public void playerTouch(PlayerEntity player) {
        boolean wasAlive = isAlive();
        super.playerTouch(player);
        if (wasAlive && !isAlive()) {
            player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.6F, 1.0F);
        }
    }


    public boolean isNoGravity() {
        return true;
    }


    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket((Entity) this);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\FloatingItemEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */