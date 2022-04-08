package iskallia.vault.entity;

import com.mojang.datafixers.util.Either;
import iskallia.vault.aura.AuraManager;
import iskallia.vault.aura.AuraProvider;
import iskallia.vault.aura.EntityAuraProvider;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.entity.ai.FollowEntityGoal;
import iskallia.vault.entity.eternal.ActiveEternalData;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.SummonEternalDebuffConfig;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.skill.talent.type.archetype.CommanderTalent;
import iskallia.vault.util.DamageUtil;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.SkinProfile;
import iskallia.vault.world.data.EternalsData;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class EternalEntity extends ZombieEntity {
    private static final DataParameter<String> ETERNAL_NAME = EntityDataManager.defineId(EternalEntity.class, DataSerializers.STRING);

    public SkinProfile skin;
    public float sizeMultiplier = 1.0F;
    private boolean ancient = false;
    private long despawnTime = Long.MAX_VALUE;
    private final ServerBossInfo bossInfo;
    private UUID owner;
    private UUID eternalId;
    private String providedAura;

    public EternalEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
        if (this.level.isClientSide) {
            this.skin = new SkinProfile();
        }

        this.bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);
        this.bossInfo.setDarkenScreen(true);
        this.bossInfo.setVisible(false);

        setCanPickUpLoot(false);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ETERNAL_NAME, "Eternal");
    }


    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, (Goal) new ZombieAttackGoal(this, 1.1D, false));
        this.goalSelector.addGoal(6, (Goal) new MoveThroughVillageGoal((CreatureEntity) this, 1.1D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, (Goal) new WaterAvoidingRandomWalkingGoal((CreatureEntity) this, 1.1D));
        this.targetSelector.addGoal(2, (Goal) new FollowEntityGoal((MobEntity) this, 1.1D, 32.0F, 3.0F, false, () -> getOwner().right()));
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        return this.skin.getLocationSkin();
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setSkinName(String skinName) {
        this.entityData.set(ETERNAL_NAME, skinName);
    }

    public String getSkinName() {
        return (String) this.entityData.get(ETERNAL_NAME);
    }

    public void setAncient(boolean ancient) {
        this.ancient = ancient;
    }

    public boolean isAncient() {
        return this.ancient;
    }

    public void setEternalId(UUID eternalId) {
        this.eternalId = eternalId;
    }

    public UUID getEternalId() {
        return this.eternalId;
    }

    public void setProvidedAura(String providedAura) {
        this.providedAura = providedAura;
    }

    public String getProvidedAura() {
        return this.providedAura;
    }

    public void setDespawnTime(long despawnTime) {
        this.despawnTime = despawnTime;
    }


    public boolean isBaby() {
        return false;
    }


    protected boolean isSunSensitive() {
        return false;
    }


    protected void doUnderWaterConversion() {
    }


    protected boolean convertsInWater() {
        return false;
    }


    public boolean isInvertedHealAndHarm() {
        return false;
    }


    public EntityClassification getClassification(boolean forSpawnCount) {
        return EntityClassification.MONSTER;
    }

    public Either<UUID, ServerPlayerEntity> getOwner() {
        if (this.level.isClientSide()) {
            return Either.left(this.owner);
        }
        ServerPlayerEntity player = getServer().getPlayerList().getPlayer(this.owner);
        return (player == null) ? Either.left(this.owner) : Either.right(player);
    }


    public void tick() {
        super.tick();

        if (this.level instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) this.level;
            int tickCounter = sWorld.getServer().getTickCount();

            if (tickCounter < this.despawnTime) {
                ActiveEternalData.getInstance().updateEternal(this);
            }
            if (this.dead) {
                return;
            }
            if (tickCounter >= this.despawnTime) {
                kill();
            }

            double amplitude = getDeltaMovement().distanceToSqr(0.0D, getDeltaMovement().y(), 0.0D);

            if (amplitude > 0.004D) {
                setSprinting(true);
            } else {

                setSprinting(false);
            }

            this.bossInfo.setPercent(getHealth() / getMaxHealth());
            if (this.tickCount % 10 == 0) {
                updateAttackTarget();
            }
            if (this.providedAura != null && this.tickCount % 4 == 0) {
                getOwner().ifRight(sPlayer -> {
                    EternalAuraConfig.AuraConfig auraCfg = ModConfigs.ETERNAL_AURAS.getByName(this.providedAura);

                    if (auraCfg != null) {
                        AuraManager.getInstance().provideAura((AuraProvider) EntityAuraProvider.ofEntity((LivingEntity) this, auraCfg));
                    }
                });
            }
            Map<Effect, EffectTalent.CombinedEffects> combinedEffects = EffectTalent.getGearEffectData((LivingEntity) this);
            EffectTalent.applyEffects((LivingEntity) this, combinedEffects);
        } else {
            if (this.dead) {
                return;
            }
            if (!Objects.equals(getSkinName(), this.skin.getLatestNickname())) {
                this.skin.updateSkin(getSkinName());
            }
        }
    }


    protected void tickDeath() {
        super.tickDeath();
    }


    public void setTarget(LivingEntity entity) {
        if (entity == getOwner().right().orElse(null) || entity instanceof EternalEntity || entity instanceof PlayerEntity || entity instanceof EtchingVendorEntity) {
            return;
        }

        super.setTarget(entity);
    }


    public void setLastHurtByMob(LivingEntity entity) {
        if (entity == getOwner().right().orElse(null) || entity instanceof EternalEntity || entity instanceof PlayerEntity || entity instanceof EtchingVendorEntity) {
            return;
        }

        super.setLastHurtByMob(entity);
    }

    private void updateAttackTarget() {
        AxisAlignedBB box = getBoundingBox().inflate(32.0D);

        this.level.getLoadedEntitiesOfClass(LivingEntity.class, box, e -> {
                    Either<UUID, ServerPlayerEntity> owner = getOwner();

                    return (owner.right().isPresent() && owner.right().get() == e) ? false : (


                            (!(e instanceof EternalEntity) && !(e instanceof PlayerEntity) && !(e instanceof EtchingVendorEntity)));


                }).stream()
                .sorted(Comparator.comparingDouble(e -> e.position().distanceTo(position())))
                .findFirst()
                .ifPresent(this::setTarget);
    }

    private Predicate<LivingEntity> ignoreEntities() {
        return e -> (!(e instanceof EternalEntity) && !(e instanceof PlayerEntity));
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


    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean("ancient", this.ancient);
        tag.putFloat("SizeMultiplier", this.sizeMultiplier);
        tag.putLong("DespawnTime", this.despawnTime);

        if (this.providedAura != null) {
            tag.putString("providedAura", this.providedAura);
        }
        if (this.owner != null) {
            tag.putString("Owner", this.owner.toString());
        }
        if (this.eternalId != null) {
            tag.putString("eternalId", this.eternalId.toString());
        }
    }


    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);

        this.ancient = tag.getBoolean("ancient");
        this.sizeMultiplier = tag.getFloat("SizeMultiplier");
        changeSize(this.sizeMultiplier);
        this.despawnTime = tag.getLong("DespawnTime");

        if (tag.contains("providedAura", 8)) {
            this.providedAura = tag.getString("providedAura");
        }
        if (tag.contains("Owner", 8)) {
            this.owner = UUID.fromString(tag.getString("Owner"));
        }
        if (tag.contains("eternalId", 8)) {
            this.eternalId = UUID.fromString(tag.getString("eternalId"));
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

    public EternalEntity changeSize(float m) {
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


    public void die(DamageSource cause) {
        super.die(cause);
        if (this.level instanceof ServerWorld && this.dead && this.owner != null && this.eternalId != null && !cause.isBypassInvul()) {
            ServerWorld sWorld = (ServerWorld) this.level;

            TalentTree tree = PlayerTalentsData.get(sWorld).getTalents(this.owner);
            if (tree.hasLearnedNode((TalentGroup) ModConfigs.TALENTS.COMMANDER) &&
                    !((CommanderTalent) tree.getNodeOf((TalentGroup) ModConfigs.TALENTS.COMMANDER).getTalent()).doEternalsUnaliveWhenDead()) {
                return;
            }

            EternalData eternal = EternalsData.get(sWorld).getEternal(this.eternalId);
            if (eternal != null) {
                eternal.setAlive(false);
            }
        }
    }


    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag) {
        setCustomName(getCustomName());
        setCanBreakDoors(true);
        setCanPickUpLoot(false);
        setPersistenceRequired();


        if (this.random.nextInt(100) == 0) {
            ChickenEntity chicken = (ChickenEntity) EntityType.CHICKEN.create(this.level);

            if (chicken != null) {
                chicken.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
                chicken.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
                chicken.setChickenJockey(true);
                ((ServerWorld) this.level).addWithUUID((Entity) chicken);
                startRiding((Entity) chicken);
            }
        }

        return spawnData;
    }


    public boolean hurt(DamageSource source, float amount) {
        Entity src = source.getEntity();
        if (src instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) src;
            if (player.getUUID().equals(this.owner)) {
                return false;
            }
        }
        return super.hurt(source, amount);
    }


    public boolean doHurtTarget(Entity entity) {
        if (!this.level.isClientSide() && this.level instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) this.level;

            sWorld.sendParticles((IParticleData) ParticleTypes.SWEEP_ATTACK, getX(), getY(),
                    getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.level.playSound(null, blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, this.random
                    .nextFloat() - this.random.nextFloat());

            if (entity instanceof LivingEntity) {
                getOwner().ifRight(owner -> {
                    AbilityTree abilityData = PlayerAbilitiesData.get(sWorld).getAbilities((PlayerEntity) owner);

                    AbilityNode<?, ?> eternalsNode = abilityData.getNodeByName("Summon Eternal");
                    if ("Summon Eternal_Debuffs".equals(eternalsNode.getSpecialization())) {
                        SummonEternalDebuffConfig cfg = (SummonEternalDebuffConfig) eternalsNode.getAbilityConfig();
                        if (this.random.nextFloat() < cfg.getApplyDebuffChance()) {
                            Effect debuff = (Effect) MiscUtils.eitherOf(this.random, (Object[]) new Effect[]{Effects.POISON, Effects.WITHER, Effects.MOVEMENT_SLOWDOWN, Effects.DIG_SLOWDOWN});
                            ((LivingEntity) entity).addEffect(new EffectInstance(debuff, cfg.getDebuffDurationTicks(), cfg.getDebuffAmplifier()));
                        }
                    }
                });
            }
        }
        return ((Boolean) DamageUtil.shotgunAttackApply(entity, x$0 -> Boolean.valueOf(super.doHurtTarget(x$0)))).booleanValue();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\EternalEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */