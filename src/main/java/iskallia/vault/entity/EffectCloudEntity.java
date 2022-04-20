package iskallia.vault.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.event.ActiveFlags;
import iskallia.vault.init.ModEntities;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.block.material.PushReaction;
import net.minecraft.command.arguments.ParticleArgument;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectCloudEntity extends Entity {
    private static final Logger PRIVATE_LOGGER;
    private static final DataParameter<Float> RADIUS;
    private static final DataParameter<Integer> COLOR;
    private static final DataParameter<Boolean> IGNORE_RADIUS;
    private static final DataParameter<IParticleData> PARTICLE;
    private Potion potion;
    private final List<EffectInstance> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean affectsOwner;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private LivingEntity owner;
    private UUID ownerUniqueId;

    public EffectCloudEntity(final EntityType<? extends EffectCloudEntity> cloud, final World world) {
        super((EntityType) cloud, world);
        this.potion = Potions.EMPTY;
        this.effects = Lists.newArrayList();
        this.reapplicationDelayMap = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.affectsOwner = true;
        this.noPhysics = true;
        this.setRadius(3.0f);
    }

    public EffectCloudEntity(final World world, final double x, final double y, final double z) {
        this(ModEntities.EFFECT_CLOUD, world);
        this.setPos(x, y, z);
    }

    protected void defineSynchedData() {
        this.getEntityData().define((DataParameter) EffectCloudEntity.COLOR, 0);
        this.getEntityData().define((DataParameter) EffectCloudEntity.RADIUS, 0.5f);
        this.getEntityData().define((DataParameter) EffectCloudEntity.IGNORE_RADIUS, false);
        this.getEntityData().define((DataParameter) EffectCloudEntity.PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(final float radiusIn) {
        if (!this.level.isClientSide) {
            this.getEntityData().set((DataParameter) EffectCloudEntity.RADIUS, radiusIn);
        }
    }

    public void refreshDimensions() {
        final double d0 = this.getX();
        final double d2 = this.getY();
        final double d3 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d2, d3);
    }

    public float getRadius() {
        return (float) this.getEntityData().get((DataParameter) EffectCloudEntity.RADIUS);
    }

    public void setPotion(final Potion potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }

    private void updateFixedColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getEntityData().set((DataParameter) EffectCloudEntity.COLOR, 0);
        } else {
            this.getEntityData().set((DataParameter) EffectCloudEntity.COLOR, PotionUtils.getColor((Collection) PotionUtils.getAllEffects(this.potion, (Collection) this.effects)));
        }
    }

    public void addEffect(final EffectInstance effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }

    public int getColor() {
        return (int) this.getEntityData().get((DataParameter) EffectCloudEntity.COLOR);
    }

    public void setColor(final int colorIn) {
        this.colorSet = true;
        this.getEntityData().set((DataParameter) EffectCloudEntity.COLOR, colorIn);
    }

    public boolean affectsOwner() {
        return this.affectsOwner;
    }

    private void setAffectsOwner(final boolean affectsOwner) {
        this.affectsOwner = affectsOwner;
    }

    public IParticleData getParticleData() {
        return (IParticleData) this.getEntityData().get((DataParameter) EffectCloudEntity.PARTICLE);
    }

    public void setParticleData(final IParticleData particleData) {
        this.getEntityData().set((DataParameter) EffectCloudEntity.PARTICLE, particleData);
    }

    protected void setIgnoreRadius(final boolean ignoreRadius) {
        this.getEntityData().set((DataParameter) EffectCloudEntity.IGNORE_RADIUS, ignoreRadius);
    }

    public boolean shouldIgnoreRadius() {
        return (boolean) this.getEntityData().get((DataParameter) EffectCloudEntity.IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(final int durationIn) {
        this.duration = durationIn;
    }

    public void setRemainingFireTicks(final int seconds) {
        super.setRemainingFireTicks(0);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.tickParticles();
        } else {
            final boolean ignoreRadius = this.shouldIgnoreRadius();
            float radius = this.getRadius();
            if (this.tickCount >= this.waitTime + this.duration) {
                this.remove();
                return;
            }
            final boolean flag1 = this.tickCount < this.waitTime;
            if (ignoreRadius != flag1) {
                this.setIgnoreRadius(flag1);
            }
            if (flag1) {
                return;
            }
            if (this.radiusPerTick != 0.0f) {
                radius += this.radiusPerTick;
                if (radius < 0.5f) {
                    this.remove();
                    return;
                }
                this.setRadius(radius);
            }
            if (this.tickCount % 5 == 0) {
                this.reapplicationDelayMap.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());
                final List<EffectInstance> effectsToApply = Lists.newArrayList();
                for (final EffectInstance effect : this.potion.getEffects()) {
                    effectsToApply.add(new EffectInstance(effect.getEffect(), effect.getDuration() / 4, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                }
                effectsToApply.addAll(this.effects);
                if (effectsToApply.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    final List<LivingEntity> entitiesInRadius = this.level.getEntitiesOfClass((Class) LivingEntity.class, this.getBoundingBox());
                    if (!entitiesInRadius.isEmpty()) {
                        for (final LivingEntity livingentity : entitiesInRadius) {
                            if (!this.canApplyEffects(livingentity)) {
                                continue;
                            }
                            if (this.reapplicationDelayMap.containsKey(livingentity) || !livingentity.isAffectedByPotions()) {
                                continue;
                            }
                            final double xDiff = livingentity.getX() - this.getX();
                            final double zDiff = livingentity.getZ() - this.getZ();
                            final double distance = xDiff * xDiff + zDiff * zDiff;
                            if (distance > radius * radius) {
                                continue;
                            }
                            this.reapplicationDelayMap.put((Entity) livingentity, this.tickCount + this.reapplicationDelay);
                            for (final EffectInstance effectinstance : effectsToApply) {
                                if (effectinstance.getEffect().isInstantenous()) {
                                    ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> effectinstance.getEffect().applyInstantenousEffect((Entity) this, (Entity) this.getOwner(), livingentity, effectinstance.getAmplifier(), 0.5));
                                } else {
                                    livingentity.addEffect(new EffectInstance(effectinstance));
                                }
                            }
                            if (this.radiusOnUse != 0.0f) {
                                radius += this.radiusOnUse;
                                if (radius < 0.5f) {
                                    this.remove();
                                    return;
                                }
                                this.setRadius(radius);
                            }
                            if (this.durationOnUse == 0) {
                                continue;
                            }
                            this.duration += this.durationOnUse;
                            if (this.duration <= 0) {
                                this.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private void tickParticles() {
        final boolean ignoreRadius = this.shouldIgnoreRadius();
        final float radius = this.getRadius();
        final IParticleData iparticledata = this.getParticleData();
        if (ignoreRadius) {
            if (this.random.nextBoolean()) {
                for (int i = 0; i < 2; ++i) {
                    final float randomRad = this.random.nextFloat() * 6.2831855f;
                    final float randomDst = MathHelper.sqrt(this.random.nextFloat()) * 0.2f;
                    final float xOffset = MathHelper.cos(randomRad) * randomDst;
                    final float zOffset = MathHelper.sin(randomRad) * randomDst;
                    if (iparticledata.getType() == ParticleTypes.ENTITY_EFFECT) {
                        final int color = this.random.nextBoolean() ? 16777215 : this.getColor();
                        final int r = color >> 16 & 0xFF;
                        final int g = color >> 8 & 0xFF;
                        final int b = color & 0xFF;
                        this.level.addAlwaysVisibleParticle(iparticledata, this.getX() + xOffset, this.getY(), this.getZ() + zOffset, (double) (r / 255.0f), (double) (g / 255.0f), (double) (b / 255.0f));
                    } else {
                        this.level.addAlwaysVisibleParticle(iparticledata, this.getX() + xOffset, this.getY(), this.getZ() + zOffset, 0.0, 0.0, 0.0);
                    }
                }
            }
        } else {
            final float distance = 3.1415927f * radius * radius;
            for (int j = 0; j < distance; ++j) {
                final float randomRad2 = this.random.nextFloat() * 6.2831855f;
                final float randomDst2 = MathHelper.sqrt(this.random.nextFloat()) * radius;
                final float xOffset2 = MathHelper.cos(randomRad2) * randomDst2;
                final float zOffset2 = MathHelper.sin(randomRad2) * randomDst2;
                if (iparticledata.getType() == ParticleTypes.ENTITY_EFFECT) {
                    final int color2 = this.getColor();
                    final int r2 = color2 >> 16 & 0xFF;
                    final int g2 = color2 >> 8 & 0xFF;
                    final int b2 = color2 & 0xFF;
                    this.level.addAlwaysVisibleParticle(iparticledata, this.getX() + xOffset2, this.getY(), this.getZ() + zOffset2, (double) (r2 / 255.0f), (double) (g2 / 255.0f), (double) (b2 / 255.0f));
                } else {
                    this.level.addAlwaysVisibleParticle(iparticledata, this.getX() + xOffset2, this.getY(), this.getZ() + zOffset2, (0.5 - this.random.nextDouble()) * 0.15, 0.009999999776482582, (0.5 - this.random.nextDouble()) * 0.15);
                }
            }
        }
    }

    protected boolean canApplyEffects(final LivingEntity target) {
        if (!this.affectsOwner()) {
            if (this.ownerUniqueId == null) {
                return true;
            }
            final UUID targetUUID = target.getUUID();
            if (targetUUID.equals(this.ownerUniqueId)) {
                return false;
            }
            UUID ownerUUID = this.ownerUniqueId;
            final World world = this.getCommandSenderWorld();
            if (!(world instanceof ServerWorld)) {
                return true;
            }
            final ServerWorld sWorld = (ServerWorld) world;
            final LivingEntity owner = this.getOwner();
            if (owner instanceof EternalEntity) {
                final UUID eternalOwnerUUID = (UUID) ((EternalEntity) owner).getOwner().map(Function.identity(), Entity::getUUID);
                if (targetUUID.equals(eternalOwnerUUID)) {
                    return false;
                }
                final VaultPartyData.Party party = VaultPartyData.get(sWorld).getParty(eternalOwnerUUID).orElse(null);
                if (party != null && party.hasMember(targetUUID)) {
                    return false;
                }
                ownerUUID = eternalOwnerUUID;
            }
            if (target instanceof EternalEntity) {
                final UUID eternalTargetOwnerUUID = (UUID) ((EternalEntity) target).getOwner().map(Function.identity(), Entity::getUUID);
                if (eternalTargetOwnerUUID.equals(ownerUUID)) {
                    return false;
                }
                final VaultPartyData.Party party = VaultPartyData.get(sWorld).getParty(eternalTargetOwnerUUID).orElse(null);
                if (party != null && party.hasMember(ownerUUID)) {
                    return false;
                }
            }
            final VaultPartyData.Party party2 = VaultPartyData.get(sWorld).getParty(ownerUUID).orElse(null);
            if (party2 != null && party2.hasMember(targetUUID)) {
                return false;
            }
        }
        return true;
    }

    public void setRadiusOnUse(final float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(final float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(final int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable final LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ((ownerIn == null) ? null : ownerIn.getUUID());
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            final Entity entity = ((ServerWorld) this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    protected void readAdditionalSaveData(final CompoundNBT compound) {
        this.tickCount = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }
        if (compound.contains("Particle", 8)) {
            try {
                this.setParticleData(ParticleArgument.readParticle(new StringReader(compound.getString("Particle"))));
            } catch (final CommandSyntaxException commandsyntaxexception) {
                EffectCloudEntity.PRIVATE_LOGGER.warn("Couldn't load custom particle {}", compound.getString("Particle"), commandsyntaxexception);
            }
        }
        if (compound.contains("Color", 99)) {
            this.setColor(compound.getInt("Color"));
        }
        if (compound.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(compound));
        }
        if (compound.contains("Effects", 9)) {
            final ListNBT listnbt = compound.getList("Effects", 10);
            this.effects.clear();
            for (int i = 0; i < listnbt.size(); ++i) {
                final EffectInstance effectinstance = EffectInstance.load(listnbt.getCompound(i));
                if (effectinstance != null) {
                    this.addEffect(effectinstance);
                }
            }
        }
    }

    protected void addAdditionalSaveData(final CompoundNBT compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("ReapplicationDelay", this.reapplicationDelay);
        compound.putInt("DurationOnUse", this.durationOnUse);
        compound.putFloat("RadiusOnUse", this.radiusOnUse);
        compound.putFloat("RadiusPerTick", this.radiusPerTick);
        compound.putFloat("Radius", this.getRadius());
        compound.putString("Particle", this.getParticleData().writeToString());
        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }
        if (this.colorSet) {
            compound.putInt("Color", this.getColor());
        }
        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }
        if (!this.effects.isEmpty()) {
            final ListNBT listnbt = new ListNBT();
            for (final EffectInstance effectinstance : this.effects) {
                listnbt.add(effectinstance.save(new CompoundNBT()));
            }
            compound.put("Effects", (INBT) listnbt);
        }
    }

    public void onSyncedDataUpdated(final DataParameter<?> key) {
        if (EffectCloudEntity.RADIUS.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated((DataParameter) key);
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public IPacket<?> getAddEntityPacket() {
        return (IPacket<?>) NetworkHooks.getEntitySpawningPacket((Entity) this);
    }

    public EntitySize getDimensions(final Pose poseIn) {
        return EntitySize.scalable(this.getRadius() * 2.0f, 0.5f);
    }

    public static EffectCloudEntity fromConfig(final World world, final LivingEntity owner, final double x, final double y, final double z, final Config config) {
        final EffectCloudEntity cloud = new EffectCloudEntity(world, x, y, z);
        cloud.setPotion(config.getPotion());
        config.getEffects().forEach(effect -> cloud.addEffect(effect.create()));
        if (config.getDuration() >= 0) {
            cloud.setDuration(config.getDuration());
        }
        if (config.getRadius() >= 0.0f) {
            cloud.setRadius(config.getRadius());
        }
        if (config.getColor() >= 0) {
            cloud.setColor(config.getColor());
        }
        cloud.setAffectsOwner(config.affectsOwner());
        cloud.setOwner(owner);
        return cloud;
    }

    static {
        PRIVATE_LOGGER = LogManager.getLogger();
        RADIUS = EntityDataManager.defineId((Class) EffectCloudEntity.class, DataSerializers.FLOAT);
        COLOR = EntityDataManager.defineId((Class) EffectCloudEntity.class, DataSerializers.INT);
        IGNORE_RADIUS = EntityDataManager.defineId((Class) EffectCloudEntity.class, DataSerializers.BOOLEAN);
        PARTICLE = EntityDataManager.defineId((Class) EffectCloudEntity.class, DataSerializers.PARTICLE);
    }

    public static class Config implements INBTSerializable<CompoundNBT> {
        @Expose
        private String name;
        @Expose
        private String potion;
        @Expose
        private List<CloudEffect> effects;
        @Expose
        private int duration;
        @Expose
        private float radius;
        @Expose
        private int color;
        @Expose
        private boolean affectsOwner;
        @Expose
        private float chance;

        @Override
        public String toString() {
            return "Config{name='" + this.name + '\'' + ", potion='" + this.potion + '\'' + ", effects=" + this.effects + '}';
        }

        public Config() {
            this("Dummy", Potions.EMPTY, new ArrayList<CloudEffect>(), 600, 3.0f, -1, true, 1.0f);
        }

        public Config(final String name, final Potion potion, final List<CloudEffect> effects, final int duration, final float radius, final int color, final boolean affectsOwner, final float chance) {
            this.name = name;
            this.potion = potion.getRegistryName().toString();
            this.effects = effects;
            this.duration = duration;
            this.radius = radius;
            this.color = color;
            this.affectsOwner = affectsOwner;
            this.chance = chance;
        }

        public static Config fromNBT(final CompoundNBT nbt) {
            final Config config = new Config();
            config.deserializeNBT(nbt);
            return config;
        }

        public String getName() {
            return this.name;
        }

        public Potion getPotion() {
            return Registry.POTION.getOptional(new ResourceLocation(this.potion)).orElse(Potions.EMPTY);
        }

        public List<CloudEffect> getEffects() {
            return this.effects;
        }

        public int getDuration() {
            return this.duration;
        }

        public float getRadius() {
            return this.radius;
        }

        public int getColor() {
            return this.color;
        }

        public boolean affectsOwner() {
            return this.affectsOwner;
        }

        public float getChance() {
            return this.chance;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Name", this.name);
            nbt.putString("Potion", this.potion);
            final ListNBT effectsList = new ListNBT();
            this.effects.forEach(cloudEffect -> effectsList.add(cloudEffect.serializeNBT()));
            nbt.put("Effects", (INBT) effectsList);
            nbt.putInt("Duration", this.duration);
            nbt.putFloat("Radius", this.radius);
            nbt.putInt("Color", this.color);
            nbt.putBoolean("AffectsOwner", this.affectsOwner);
            nbt.putFloat("Chance", this.chance);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.name = nbt.getString("Name");
            this.potion = nbt.getString("Potion");
            final ListNBT effectsList = nbt.getList("Effects", 10);
            this.effects = effectsList.stream().map(inbt -> CloudEffect.fromNBT((CompoundNBT) inbt)).collect(Collectors.toList());
            this.duration = nbt.getInt("Duration");
            this.radius = nbt.getFloat("Radius");
            this.color = nbt.getInt("Color");
            this.affectsOwner = nbt.getBoolean("AffectsOwner");
            this.chance = nbt.getFloat("Chance");
        }

        public static class CloudEffect implements INBTSerializable<CompoundNBT> {
            @Expose
            private String effect;
            @Expose
            private int duration;
            @Expose
            private int amplifier;
            @Expose
            private boolean showParticles;
            @Expose
            private boolean showIcon;

            protected CloudEffect() {
            }

            public CloudEffect(final Effect effect, final int duration, final int amplifier, final boolean showParticles, final boolean showIcon) {
                this.effect = effect.getRegistryName().toString();
                this.duration = duration;
                this.amplifier = amplifier;
                this.showParticles = false;
                this.showIcon = true;
            }

            public static CloudEffect fromNBT(final CompoundNBT nbt) {
                final CloudEffect effect = new CloudEffect();
                effect.deserializeNBT(nbt);
                return effect;
            }

            public Effect getEffect() {
                return (Effect) Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
            }

            public int getDuration() {
                return this.duration;
            }

            public int getAmplifier() {
                return this.amplifier;
            }

            public boolean showParticles() {
                return this.showParticles;
            }

            public boolean showIcon() {
                return this.showIcon;
            }

            public EffectInstance create() {
                return new EffectInstance(this.getEffect(), this.getDuration(), this.getAmplifier(), false, this.showParticles(), this.showIcon());
            }

            public CompoundNBT serializeNBT() {
                final CompoundNBT nbt = new CompoundNBT();
                nbt.putString("Effect", this.effect);
                nbt.putInt("Duration", this.duration);
                nbt.putInt("Amplifier", this.amplifier);
                nbt.putBoolean("ShowParticles", this.showParticles);
                nbt.putBoolean("ShowIcon", this.showIcon);
                return nbt;
            }

            public void deserializeNBT(final CompoundNBT nbt) {
                this.effect = nbt.getString("Effect");
                this.duration = nbt.getInt("Duration");
                this.amplifier = nbt.getInt("Amplifier");
                this.showParticles = nbt.getBoolean("ShowParticles");
                this.showIcon = nbt.getBoolean("ShowIcon");
            }
        }
    }
}
