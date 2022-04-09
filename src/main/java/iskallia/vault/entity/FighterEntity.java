// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity;

import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.SkinProfile;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.regex.Pattern;

public class FighterEntity extends ZombieEntity
{
    public static final ThrowProjectilesGoal.Projectile SNOWBALLS;
    public SkinProfile skin;
    public String lastName;
    public float sizeMultiplier;
    public ServerBossInfo bossInfo;
    
    public FighterEntity(final EntityType<? extends ZombieEntity> type, final World world) {
        super((EntityType)type, world);
        this.lastName = "Fighter";
        this.sizeMultiplier = 1.0f;
        if (!this.level.isClientSide) {
            this.changeSize(this.sizeMultiplier);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.random.nextFloat() * 0.15 + 0.2);
        }
        else {
            this.skin = new SkinProfile();
        }
        (this.bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
        this.bossInfo.setVisible(false);
        this.setCustomName((ITextComponent)new StringTextComponent(this.lastName));
    }
    
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        return this.skin.getLocationSkin();
    }
    
    public boolean isBaby() {
        return false;
    }
    
    protected boolean isSunSensitive() {
        return false;
    }
    
    public void tick() {
        super.tick();
        if (this.dead) {
            return;
        }
        if (this.level.isClientSide) {
            String name = this.getCustomName().getString();
            for (String star = String.valueOf('\u2726'); name.startsWith(star); name = name.substring(1)) {}
            name = name.trim();
            if (name.startsWith("[")) {
                final String[] data = name.split(Pattern.quote("]"));
                name = data[1].trim();
            }
            if (!this.lastName.equals(name)) {
                this.skin.updateSkin(name);
                this.lastName = name;
            }
        }
        else {
            final double amplitude = this.getDeltaMovement().distanceToSqr(0.0, this.getDeltaMovement().y(), 0.0);
            if (amplitude > 0.004) {
                this.setSprinting(true);
            }
            else {
                this.setSprinting(false);
            }
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }
    
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }
    
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.PLAYER_HURT;
    }
    
    public void setCustomName(final ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }
    
    public void addAdditionalSaveData(final CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("SizeMultiplier", this.sizeMultiplier);
    }
    
    public void readAdditionalSaveData(final CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("SizeMultiplier", 5)) {
            this.changeSize(compound.getFloat("SizeMultiplier"));
        }
        this.bossInfo.setName(this.getDisplayName());
    }
    
    public void startSeenByPlayer(final ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    
    public void stopSeenByPlayer(final ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    
    public EntitySize getDimensions(final Pose pose) {
        return this.getDimensions(pose);
    }
    
    public float getSizeMultiplier() {
        return this.sizeMultiplier;
    }
    
    public FighterEntity changeSize(final float m) {
        EntityHelper.changeSize(this, this.sizeMultiplier = m);
        if (!this.level.isClientSide()) {
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FighterSizeMessage((Entity)this, this.sizeMultiplier));
        }
        return this;
    }
    
    protected float getStandingEyeHeight(final Pose pose, final EntitySize size) {
        return super.getStandingEyeHeight(pose, size) * this.sizeMultiplier;
    }
    
    protected void doUnderWaterConversion() {
    }
    
    public ILivingEntityData finalizeSpawn(final IServerWorld world, final DifficultyInstance difficulty, final SpawnReason reason, final ILivingEntityData spawnData, final CompoundNBT dataTag) {
        this.setCustomName(this.getCustomName());
        this.setCanBreakDoors(true);
        this.setCanPickUpLoot(true);
        this.setPersistenceRequired();
        if (this.random.nextInt(100) == 0) {
            final ChickenEntity chicken = (ChickenEntity)EntityType.CHICKEN.create(this.level);
            chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
            chicken.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
            chicken.setChickenJockey(true);
            ((ServerWorld)this.level).addWithUUID((Entity)chicken);
            this.startRiding((Entity)chicken);
        }
        return spawnData;
    }
    
    protected void dropFromLootTable(final DamageSource damageSource, final boolean attackedRecently) {
        super.dropFromLootTable(damageSource, attackedRecently);
        if (this.level.isClientSide()) {
            return;
        }
    }
    
    public boolean doHurtTarget(final Entity entity) {
        if (!this.level.isClientSide) {
            ((ServerWorld)this.level).sendParticles((IParticleData)ParticleTypes.SWEEP_ATTACK, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.level.playSound((PlayerEntity)null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, this.random.nextFloat() - this.random.nextFloat());
        }
        return super.doHurtTarget(entity);
    }
    
    static {
        SNOWBALLS = ((world1, shooter) -> new SnowballEntity(world1, shooter) {
            
            protected void onHitEntity(final EntityRayTraceResult raycast) {
                final Entity entity = raycast.getEntity();
                if (entity == shooter) {
                    return;
                }
                final int i = (entity instanceof BlazeEntity) ? 3 : 1;
                entity.hurt(DamageSource.indirectMobAttack((Entity)this, shooter), (float)i);
            }
        });
    }
}
