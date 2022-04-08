package iskallia.vault.entity;

import iskallia.vault.entity.ai.ThrowProjectilesGoal;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.SkinProfile;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
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

public class FighterEntity extends ZombieEntity {
    static {
        SNOWBALLS = ((world1, shooter) -> new SnowballEntity(world1, shooter) {
            protected void onHitEntity(EntityRayTraceResult raycast) {
                Entity entity = raycast.getEntity();
                if (entity == shooter)
                    return;
                int i = (entity instanceof net.minecraft.entity.monster.BlazeEntity) ? 3 : 1;
                entity.hurt(DamageSource.indirectMobAttack((Entity) this, shooter), i);
            }
        });
    }

    public static final ThrowProjectilesGoal.Projectile SNOWBALLS;
    public SkinProfile skin;
    public String lastName = "Fighter";
    public float sizeMultiplier = 1.0F;

    public ServerBossInfo bossInfo;

    public FighterEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);

        if (!this.level.isClientSide) {
            changeSize(this.sizeMultiplier);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.random.nextFloat() * 0.15D + 0.2D);
        } else {
            this.skin = new SkinProfile();
        }

        this.bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
        this.bossInfo.setDarkenScreen(true);
        this.bossInfo.setVisible(false);

        setCustomName((ITextComponent) new StringTextComponent(this.lastName));
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
        if (this.dead)
            return;
        if (this.level.isClientSide) {
            String name = getCustomName().getString();

            String star = String.valueOf('✦');
            while (name.startsWith(star)) {
                name = name.substring(1);
            }
            name = name.trim();

            if (name.startsWith("[")) {
                String[] data = name.split(Pattern.quote("]"));
                name = data[1].trim();
            }

            if (!this.lastName.equals(name)) {
                this.skin.updateSkin(name);
                this.lastName = name;
            }
        } else {
            double amplitude = getDeltaMovement().distanceToSqr(0.0D, getDeltaMovement().y(), 0.0D);

            if (amplitude > 0.004D) {
                setSprinting(true);
            } else {

                setSprinting(false);
            }

            this.bossInfo.setPercent(getHealth() / getMaxHealth());
        }
    }


    protected SoundEvent getAmbientSound() {
        return null;
    }


    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }


    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PLAYER_HURT;
    }


    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(getDisplayName());
    }


    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("SizeMultiplier", this.sizeMultiplier);
    }


    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("SizeMultiplier", 5)) {
            changeSize(compound.getFloat("SizeMultiplier"));
        }

        this.bossInfo.setName(getDisplayName());
    }


    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }


    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }


    public EntitySize getDimensions(Pose pose) {
        return this.getDimensions(pose);
    }

    public float getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    public FighterEntity changeSize(float m) {
        this.sizeMultiplier = m;
        EntityHelper.changeSize((Entity) this, this.sizeMultiplier);
        if (!this.level.isClientSide()) {
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FighterSizeMessage((Entity) this, this.sizeMultiplier));
        }
        return this;
    }


    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return super.getStandingEyeHeight(pose, size) * this.sizeMultiplier;
    }


    protected void doUnderWaterConversion() {
    }


    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag) {
        setCustomName(getCustomName());
        setCanBreakDoors(true);
        setCanPickUpLoot(true);
        setPersistenceRequired();


        if (this.random.nextInt(100) == 0) {
            ChickenEntity chicken = (ChickenEntity) EntityType.CHICKEN.create(this.level);
            chicken.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
            chicken.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
            chicken.setChickenJockey(true);
            ((ServerWorld) this.level).addWithUUID((Entity) chicken);
            startRiding((Entity) chicken);
        }

        return spawnData;
    }


    protected void dropFromLootTable(DamageSource damageSource, boolean attackedRecently) {
        super.dropFromLootTable(damageSource, attackedRecently);
        if (this.level.isClientSide()) {
            return;
        }
    }


    public boolean doHurtTarget(Entity entity) {
        if (!this.level.isClientSide) {
            ((ServerWorld) this.level).sendParticles((IParticleData) ParticleTypes.SWEEP_ATTACK, getX(), getY(),
                    getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);

            this.level.playSound(null, blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, this.random
                    .nextFloat() - this.random.nextFloat());
        }

        return super.doHurtTarget(entity);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\FighterEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */