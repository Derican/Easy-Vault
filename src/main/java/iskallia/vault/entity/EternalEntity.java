package iskallia.vault.entity;

import com.mojang.datafixers.util.Either;
import iskallia.vault.aura.AuraManager;
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
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.EffectTalent;
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
    private static final DataParameter<String> ETERNAL_NAME;
    public SkinProfile skin;
    public float sizeMultiplier;
    private boolean ancient;
    private long despawnTime;
    private final ServerBossInfo bossInfo;
    private UUID owner;
    private UUID eternalId;
    private String providedAura;

    public EternalEntity(final EntityType<? extends ZombieEntity> type, final World world) {
        super(type, world);
        this.sizeMultiplier = 1.0f;
        this.ancient = false;
        this.despawnTime = Long.MAX_VALUE;
        if (this.level.isClientSide) {
            this.skin = new SkinProfile();
        }
        (this.bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
        this.bossInfo.setVisible(false);
        this.setCanPickUpLoot(false);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EternalEntity.ETERNAL_NAME, "Eternal");
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.1, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.1));
        this.targetSelector.addGoal(2, new FollowEntityGoal<>(this, 1.1, 32.0f, 3.0f, false, () -> this.getOwner().right()));
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        return this.skin.getLocationSkin();
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public void setSkinName(final String skinName) {
        this.entityData.set(EternalEntity.ETERNAL_NAME, skinName);
    }

    public String getSkinName() {
        return (String) this.entityData.get((DataParameter) EternalEntity.ETERNAL_NAME);
    }

    public void setAncient(final boolean ancient) {
        this.ancient = ancient;
    }

    public boolean isAncient() {
        return this.ancient;
    }

    public void setEternalId(final UUID eternalId) {
        this.eternalId = eternalId;
    }

    public UUID getEternalId() {
        return this.eternalId;
    }

    public void setProvidedAura(final String providedAura) {
        this.providedAura = providedAura;
    }

    public String getProvidedAura() {
        return this.providedAura;
    }

    public void setDespawnTime(final long despawnTime) {
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

    public EntityClassification getClassification(final boolean forSpawnCount) {
        return EntityClassification.MONSTER;
    }

    public UUID getOwnerUUID() {
        return this.owner;
    }

    public Either<UUID, ServerPlayerEntity> getOwner() {
        if (this.level.isClientSide()) {
            return Either.left(this.owner);
        }
        final ServerPlayerEntity player = this.getServer().getPlayerList().getPlayer(this.owner);
        return (player == null) ? Either.left(this.owner) : Either.right(player);
    }

    public void tick() {
        super.tick();
        if (this.level instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld) this.level;
            final int tickCounter = sWorld.getServer().getTickCount();
            if (tickCounter < this.despawnTime) {
                ActiveEternalData.getInstance().updateEternal(this);
            }
            if (this.dead) {
                return;
            }
            if (tickCounter >= this.despawnTime) {
                this.kill();
            }
            final double amplitude = this.getDeltaMovement().distanceToSqr(0.0, this.getDeltaMovement().y(), 0.0);
            this.setSprinting(amplitude > 0.004);
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            if (this.tickCount % 10 == 0) {
                this.updateAttackTarget();
            }
            if (this.providedAura != null && this.tickCount % 4 == 0) {
                this.getOwner().ifRight(sPlayer -> {
                    final EternalAuraConfig.AuraConfig auraCfg = ModConfigs.ETERNAL_AURAS.getByName(this.providedAura);
                    if (auraCfg != null) {
                        AuraManager.getInstance().provideAura(EntityAuraProvider.ofEntity(this, auraCfg));
                    }
                    return;
                });
            }
            final Map<Effect, EffectTalent.CombinedEffects> combinedEffects = EffectTalent.getGearEffectData(this);
            EffectTalent.applyEffects(this, combinedEffects);
        } else {
            if (this.dead) {
                return;
            }
            if (!Objects.equals(this.getSkinName(), this.skin.getLatestNickname())) {
                this.skin.updateSkin(this.getSkinName());
            }
        }
    }

    protected void tickDeath() {
        super.tickDeath();
    }

    public void setTarget(final LivingEntity entity) {
        if (entity == this.getOwner().right().orElse(null) || entity instanceof EternalEntity || entity instanceof PlayerEntity || entity instanceof EtchingVendorEntity) {
            return;
        }
        super.setTarget(entity);
    }

    public void setLastHurtByMob(final LivingEntity entity) {
        if (entity == this.getOwner().right().orElse(null) || entity instanceof EternalEntity || entity instanceof PlayerEntity || entity instanceof EtchingVendorEntity) {
            return;
        }
        super.setLastHurtByMob(entity);
    }

    private void updateAttackTarget() {
        final AxisAlignedBB box = this.getBoundingBox().inflate(32.0);
        this.level.getLoadedEntitiesOfClass(LivingEntity.class, box, e -> {
            final Either<UUID, ServerPlayerEntity> owner = this.getOwner();
            if (owner.right().isPresent() && owner.right().get() == e) {
                return false;
            } else {
                return !(e instanceof EternalEntity) && !(e instanceof PlayerEntity) && !(e instanceof EtchingVendorEntity);
            }
        }).stream().sorted(Comparator.comparingDouble(e -> e.position().distanceTo(this.position()))).findFirst().ifPresent(this::setTarget);
    }

    private Predicate<LivingEntity> ignoreEntities() {
        return e -> !(e instanceof EternalEntity) && !(e instanceof PlayerEntity);
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

    public void addAdditionalSaveData(final CompoundNBT tag) {
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

    public void readAdditionalSaveData(final CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.ancient = tag.getBoolean("ancient");
        this.changeSize(this.sizeMultiplier = tag.getFloat("SizeMultiplier"));
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

    public EternalEntity changeSize(final float m) {
        EntityHelper.changeSize(this, this.sizeMultiplier = m);
        if (!this.level.isClientSide()) {
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FighterSizeMessage(this, this.sizeMultiplier));
        }
        return this;
    }

    protected float getStandingEyeHeight(final Pose pose, final EntitySize size) {
        return super.getStandingEyeHeight(pose, size) * this.sizeMultiplier;
    }

    public void die(final DamageSource cause) {
        super.die(cause);
        if (this.level instanceof ServerWorld && this.dead && this.owner != null && this.eternalId != null && !cause.isBypassInvul()) {
            final ServerWorld sWorld = (ServerWorld) this.level;
            final TalentTree tree = PlayerTalentsData.get(sWorld).getTalents(this.owner);
            if (tree.hasLearnedNode(ModConfigs.TALENTS.COMMANDER) && !tree.getNodeOf(ModConfigs.TALENTS.COMMANDER).getTalent().doEternalsUnaliveWhenDead()) {
                return;
            }
            final EternalData eternal = EternalsData.get(sWorld).getEternal(this.eternalId);
            if (eternal != null) {
                eternal.setAlive(false);
            }
        }
    }

    public ILivingEntityData finalizeSpawn(final IServerWorld world, final DifficultyInstance difficulty, final SpawnReason reason, final ILivingEntityData spawnData, final CompoundNBT dataTag) {
        this.setCustomName(this.getCustomName());
        this.setCanBreakDoors(true);
        this.setCanPickUpLoot(false);
        this.setPersistenceRequired();
        if (this.random.nextInt(100) == 0) {
            final ChickenEntity chicken = EntityType.CHICKEN.create(this.level);
            if (chicken != null) {
                chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0f);
                chicken.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
                chicken.setChickenJockey(true);
                ((ServerWorld) this.level).addWithUUID(chicken);
                this.startRiding(chicken);
            }
        }
        return spawnData;
    }

    public boolean hurt(final DamageSource source, final float amount) {
        final Entity src = source.getEntity();
        if (src instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) src;
            if (player.getUUID().equals(this.owner)) {
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    public boolean doHurtTarget(final Entity entity) {
        if (!this.level.isClientSide() && this.level instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld) this.level;
            sWorld.sendParticles((IParticleData) ParticleTypes.SWEEP_ATTACK, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, this.random.nextFloat() - this.random.nextFloat());
            if (entity instanceof LivingEntity) {
                this.getOwner().ifRight(owner -> {
                    final AbilityTree abilityData = PlayerAbilitiesData.get(sWorld).getAbilities(owner);
                    final AbilityNode<?, ?> eternalsNode = abilityData.getNodeByName("Summon Eternal");
                    if ("Summon Eternal_Debuffs".equals(eternalsNode.getSpecialization())) {
                        final SummonEternalDebuffConfig cfg = (SummonEternalDebuffConfig) eternalsNode.getAbilityConfig();
                        if (this.random.nextFloat() < cfg.getApplyDebuffChance()) {
                            final Effect debuff = MiscUtils.eitherOf(this.random, Effects.POISON, Effects.WITHER, Effects.MOVEMENT_SLOWDOWN, Effects.DIG_SLOWDOWN);
                            ((LivingEntity) entity).addEffect(new EffectInstance(debuff, cfg.getDebuffDurationTicks(), cfg.getDebuffAmplifier()));
                        }
                    }
                    return;
                });
            }
        }
        return DamageUtil.shotgunAttackApply(entity, x$0 -> super.doHurtTarget(x$0));
    }

    static {
        ETERNAL_NAME = EntityDataManager.defineId(EternalEntity.class, DataSerializers.STRING);
    }
}
