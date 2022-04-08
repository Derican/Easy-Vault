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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectCloudEntity extends Entity {
    private static final Logger PRIVATE_LOGGER = LogManager.getLogger();
    private static final DataParameter<Float> RADIUS = EntityDataManager.defineId(EffectCloudEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EffectCloudEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.defineId(EffectCloudEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<IParticleData> PARTICLE = EntityDataManager.defineId(EffectCloudEntity.class, DataSerializers.PARTICLE);
    private Potion potion = Potions.EMPTY;
    private final List<EffectInstance> effects = Lists.newArrayList();
    private final Map<Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
    private int duration = 600;
    private int waitTime = 20;
    private int reapplicationDelay = 20;
    private boolean affectsOwner = true;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private LivingEntity owner;
    private UUID ownerUniqueId;

    public EffectCloudEntity(EntityType<? extends EffectCloudEntity> cloud, World world) {
        super(cloud, world);
        this.noPhysics = true;
        setRadius(3.0F);
    }

    public EffectCloudEntity(World world, double x, double y, double z) {
        this(ModEntities.EFFECT_CLOUD, world);
        setPos(x, y, z);
    }

    protected void defineSynchedData() {
        getEntityData().define(COLOR, Integer.valueOf(0));
        getEntityData().define(RADIUS, Float.valueOf(0.5F));
        getEntityData().define(IGNORE_RADIUS, Boolean.valueOf(false));
        getEntityData().define(PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(float radiusIn) {
        if (!this.level.isClientSide) {
            getEntityData().set(RADIUS, Float.valueOf(radiusIn));
        }
    }


    public void refreshDimensions() {
        double d0 = getX();
        double d1 = getY();
        double d2 = getZ();
        super.refreshDimensions();
        setPos(d0, d1, d2);
    }

    public float getRadius() {
        return ((Float) getEntityData().get(RADIUS)).floatValue();
    }

    public void setPotion(Potion potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            updateFixedColor();
        }
    }


    private void updateFixedColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            getEntityData().set(COLOR, Integer.valueOf(0));
        } else {
            getEntityData().set(COLOR, Integer.valueOf(PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects))));
        }
    }


    public void addEffect(EffectInstance effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            updateFixedColor();
        }
    }


    public int getColor() {
        return ((Integer) getEntityData().get(COLOR)).intValue();
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        getEntityData().set(COLOR, Integer.valueOf(colorIn));
    }


    public boolean affectsOwner() {
        return this.affectsOwner;
    }

    private void setAffectsOwner(boolean affectsOwner) {
        this.affectsOwner = affectsOwner;
    }

    public IParticleData getParticleData() {
        return (IParticleData) getEntityData().get(PARTICLE);
    }

    public void setParticleData(IParticleData particleData) {
        getEntityData().set(PARTICLE, particleData);
    }


    protected void setIgnoreRadius(boolean ignoreRadius) {
        getEntityData().set(IGNORE_RADIUS, Boolean.valueOf(ignoreRadius));
    }


    public boolean shouldIgnoreRadius() {
        return ((Boolean) getEntityData().get(IGNORE_RADIUS)).booleanValue();
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }


    public void setRemainingFireTicks(int seconds) {
        super.setRemainingFireTicks(0);
    }


    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            tickParticles();
        } else {
            boolean ignoreRadius = shouldIgnoreRadius();
            float radius = getRadius();
            if (this.tickCount >= this.waitTime + this.duration) {
                remove();

                return;
            }
            boolean flag1 = (this.tickCount < this.waitTime);
            if (ignoreRadius != flag1) {
                setIgnoreRadius(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                radius += this.radiusPerTick;
                if (radius < 0.5F) {
                    remove();

                    return;
                }
                setRadius(radius);
            }

            if (this.tickCount % 5 == 0) {
                this.reapplicationDelayMap.entrySet().removeIf(entry -> (this.tickCount >= ((Integer) entry.getValue()).intValue()));

                List<EffectInstance> effectsToApply = Lists.newArrayList();
                for (EffectInstance effect : this.potion.getEffects()) {
                    effectsToApply.add(new EffectInstance(effect.getEffect(), effect.getDuration() / 4, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                }
                effectsToApply.addAll(this.effects);

                if (effectsToApply.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    List<LivingEntity> entitiesInRadius = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox());
                    if (!entitiesInRadius.isEmpty()) {
                        for (LivingEntity livingentity : entitiesInRadius) {
                            if (!canApplyEffects(livingentity)) {
                                continue;
                            }

                            if (!this.reapplicationDelayMap.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
                                double xDiff = livingentity.getX() - getX();
                                double zDiff = livingentity.getZ() - getZ();
                                double distance = xDiff * xDiff + zDiff * zDiff;
                                if (distance <= (radius * radius)) {
                                    this.reapplicationDelayMap.put(livingentity, Integer.valueOf(this.tickCount + this.reapplicationDelay));

                                    for (EffectInstance effectinstance : effectsToApply) {
                                        if (effectinstance.getEffect().isInstantenous()) {
                                            ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> effectinstance.getEffect().applyInstantenousEffect(this, (Entity) getOwner(), livingentity, effectinstance.getAmplifier(), 0.5D));

                                            continue;
                                        }
                                        livingentity.addEffect(new EffectInstance(effectinstance));
                                    }


                                    if (this.radiusOnUse != 0.0F) {
                                        radius += this.radiusOnUse;
                                        if (radius < 0.5F) {
                                            remove();

                                            return;
                                        }
                                        setRadius(radius);
                                    }

                                    if (this.durationOnUse != 0) {
                                        this.duration += this.durationOnUse;
                                        if (this.duration <= 0) {
                                            remove();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void tickParticles() {
        boolean ignoreRadius = shouldIgnoreRadius();
        float radius = getRadius();
        IParticleData iparticledata = getParticleData();
        if (ignoreRadius) {
            if (this.random.nextBoolean()) {
                for (int i = 0; i < 2; i++) {
                    float randomRad = this.random.nextFloat() * 6.2831855F;
                    float randomDst = MathHelper.sqrt(this.random.nextFloat()) * 0.2F;
                    float xOffset = MathHelper.cos(randomRad) * randomDst;
                    float zOffset = MathHelper.sin(randomRad) * randomDst;
                    if (iparticledata.getType() == ParticleTypes.ENTITY_EFFECT) {
                        int color = this.random.nextBoolean() ? 16777215 : getColor();
                        int r = color >> 16 & 0xFF;
                        int g = color >> 8 & 0xFF;
                        int b = color & 0xFF;
                        this.level.addAlwaysVisibleParticle(iparticledata, getX() + xOffset, getY(), getZ() + zOffset, (r / 255.0F), (g / 255.0F), (b / 255.0F));
                    } else {
                        this.level.addAlwaysVisibleParticle(iparticledata, getX() + xOffset, getY(), getZ() + zOffset, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else {
            float distance = 3.1415927F * radius * radius;

            for (int i = 0; i < distance; i++) {
                float randomRad = this.random.nextFloat() * 6.2831855F;
                float randomDst = MathHelper.sqrt(this.random.nextFloat()) * radius;
                float xOffset = MathHelper.cos(randomRad) * randomDst;
                float zOffset = MathHelper.sin(randomRad) * randomDst;
                if (iparticledata.getType() == ParticleTypes.ENTITY_EFFECT) {
                    int color = getColor();
                    int r = color >> 16 & 0xFF;
                    int g = color >> 8 & 0xFF;
                    int b = color & 0xFF;
                    this.level.addAlwaysVisibleParticle(iparticledata, getX() + xOffset, getY(), getZ() + zOffset, (r / 255.0F), (g / 255.0F), (b / 255.0F));
                } else {
                    this.level.addAlwaysVisibleParticle(iparticledata, getX() + xOffset, getY(), getZ() + zOffset, (0.5D - this.random.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.random.nextDouble()) * 0.15D);
                }
            }
        }
    }

    protected boolean canApplyEffects(LivingEntity target) {
        if (!affectsOwner()) {

            if (this.ownerUniqueId == null) {
                return true;
            }
            UUID targetUUID = target.getUUID();
            if (targetUUID.equals(this.ownerUniqueId)) {
                return false;
            }
            UUID ownerUUID = this.ownerUniqueId;

            World world = getCommandSenderWorld();
            if (!(world instanceof ServerWorld)) {
                return true;
            }
            ServerWorld sWorld = (ServerWorld) world;

            LivingEntity owner = getOwner();
            if (owner instanceof EternalEntity) {
                UUID eternalOwnerUUID = (UUID) ((EternalEntity) owner).getOwner().map(Function.identity(), Entity::getUUID);
                if (targetUUID.equals(eternalOwnerUUID)) {
                    return false;
                }
                VaultPartyData.Party party1 = VaultPartyData.get(sWorld).getParty(eternalOwnerUUID).orElse(null);
                if (party1 != null && party1.hasMember(targetUUID)) {
                    return false;
                }
                ownerUUID = eternalOwnerUUID;
            }
            if (target instanceof EternalEntity) {
                UUID eternalTargetOwnerUUID = (UUID) ((EternalEntity) target).getOwner().map(Function.identity(), Entity::getUUID);
                if (eternalTargetOwnerUUID.equals(ownerUUID)) {
                    return false;
                }
                VaultPartyData.Party party1 = VaultPartyData.get(sWorld).getParty(eternalTargetOwnerUUID).orElse(null);
                if (party1 != null && party1.hasMember(ownerUUID)) {
                    return false;
                }
            }

            VaultPartyData.Party party = VaultPartyData.get(sWorld).getParty(ownerUUID).orElse(null);
            if (party != null && party.hasMember(targetUUID)) {
                return false;
            }
        }
        return true;
    }

    public void setRadiusOnUse(float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = (ownerIn == null) ? null : ownerIn.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld) this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }


    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.tickCount = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUniqueId = compound.getUUID("Owner");
        }

        if (compound.contains("Particle", 8)) {
            try {
                setParticleData(ParticleArgument.readParticle(new StringReader(compound.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                PRIVATE_LOGGER.warn("Couldn't load custom particle {}", compound.getString("Particle"), commandsyntaxexception);
            }
        }

        if (compound.contains("Color", 99)) {
            setColor(compound.getInt("Color"));
        }

        if (compound.contains("Potion", 8)) {
            setPotion(PotionUtils.getPotion(compound));
        }

        if (compound.contains("Effects", 9)) {
            ListNBT listnbt = compound.getList("Effects", 10);
            this.effects.clear();

            for (int i = 0; i < listnbt.size(); i++) {
                EffectInstance effectinstance = EffectInstance.load(listnbt.getCompound(i));
                if (effectinstance != null) {
                    addEffect(effectinstance);
                }
            }
        }
    }


    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("Duration", this.duration);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("ReapplicationDelay", this.reapplicationDelay);
        compound.putInt("DurationOnUse", this.durationOnUse);
        compound.putFloat("RadiusOnUse", this.radiusOnUse);
        compound.putFloat("RadiusPerTick", this.radiusPerTick);
        compound.putFloat("Radius", getRadius());
        compound.putString("Particle", getParticleData().writeToString());
        if (this.ownerUniqueId != null) {
            compound.putUUID("Owner", this.ownerUniqueId);
        }

        if (this.colorSet) {
            compound.putInt("Color", getColor());
        }

        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            ListNBT listnbt = new ListNBT();

            for (EffectInstance effectinstance : this.effects) {
                listnbt.add(effectinstance.save(new CompoundNBT()));
            }

            compound.put("Effects", (INBT) listnbt);
        }
    }


    public void onSyncedDataUpdated(DataParameter<?> key) {
        if (RADIUS.equals(key)) {
            refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }


    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntitySize getDimensions(Pose poseIn) {
        return EntitySize.scalable(getRadius() * 2.0F, 0.5F);
    }

    public static EffectCloudEntity fromConfig(World world, LivingEntity owner, double x, double y, double z, Config config) {
        EffectCloudEntity cloud = new EffectCloudEntity(world, x, y, z);
        cloud.setPotion(config.getPotion());
        config.getEffects().forEach(effect -> cloud.addEffect(effect.create()));
        if (config.getDuration() >= 0) cloud.setDuration(config.getDuration());
        if (config.getRadius() >= 0.0F) cloud.setRadius(config.getRadius());
        if (config.getColor() >= 0) cloud.setColor(config.getColor());
        cloud.setAffectsOwner(config.affectsOwner());
        cloud.setOwner(owner);
        return cloud;
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

        public String toString() {
            return "Config{name='" + this.name + '\'' + ", potion='" + this.potion + '\'' + ", effects=" + this.effects + '}';
        }


        public Config() {
            this("Dummy", Potions.EMPTY, new ArrayList<>(), 600, 3.0F, -1, true, 1.0F);
        }

        public Config(String name, Potion potion, List<CloudEffect> effects, int duration, float radius, int color, boolean affectsOwner, float chance) {
            this.name = name;
            this.potion = potion.getRegistryName().toString();
            this.effects = effects;
            this.duration = duration;
            this.radius = radius;
            this.color = color;
            this.affectsOwner = affectsOwner;
            this.chance = chance;
        }

        public static Config fromNBT(CompoundNBT nbt) {
            Config config = new Config();
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
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Name", this.name);
            nbt.putString("Potion", this.potion);

            ListNBT effectsList = new ListNBT();
            this.effects.forEach(cloudEffect -> effectsList.add(cloudEffect.serializeNBT()));
            nbt.put("Effects", (INBT) effectsList);

            nbt.putInt("Duration", this.duration);
            nbt.putFloat("Radius", this.radius);
            nbt.putInt("Color", this.color);
            nbt.putBoolean("AffectsOwner", this.affectsOwner);
            nbt.putFloat("Chance", this.chance);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.name = nbt.getString("Name");
            this.potion = nbt.getString("Potion");

            ListNBT effectsList = nbt.getList("Effects", 10);
            this.effects = (List<CloudEffect>) effectsList.stream().map(inbt -> CloudEffect.fromNBT((CompoundNBT) inbt)).collect(Collectors.toList());

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

            public CloudEffect(Effect effect, int duration, int amplifier, boolean showParticles, boolean showIcon) {
                this.effect = effect.getRegistryName().toString();
                this.duration = duration;
                this.amplifier = amplifier;
                this.showParticles = false;
                this.showIcon = true;
            }

            public static CloudEffect fromNBT(CompoundNBT nbt) {
                CloudEffect effect = new CloudEffect();
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
                return new EffectInstance(getEffect(), getDuration(), getAmplifier(), false, showParticles(), showIcon());
            }


            public CompoundNBT serializeNBT() {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("Effect", this.effect);
                nbt.putInt("Duration", this.duration);
                nbt.putInt("Amplifier", this.amplifier);
                nbt.putBoolean("ShowParticles", this.showParticles);
                nbt.putBoolean("ShowIcon", this.showIcon);
                return nbt;
            }

            public void deserializeNBT(CompoundNBT nbt) {
                this.effect = nbt.getString("Effect");
                this.duration = nbt.getInt("Duration");
                this.amplifier = nbt.getInt("Amplifier");
                this.showParticles = nbt.getBoolean("ShowParticles");
                this.showIcon = nbt.getBoolean("ShowIcon");
            }
        }
    }

    public static class CloudEffect implements INBTSerializable<CompoundNBT> {
        public void deserializeNBT(CompoundNBT nbt) {
            this.effect = nbt.getString("Effect");
            this.duration = nbt.getInt("Duration");
            this.amplifier = nbt.getInt("Amplifier");
            this.showParticles = nbt.getBoolean("ShowParticles");
            this.showIcon = nbt.getBoolean("ShowIcon");
        }


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

        public CloudEffect(Effect effect, int duration, int amplifier, boolean showParticles, boolean showIcon) {
            this.effect = effect.getRegistryName().toString();
            this.duration = duration;
            this.amplifier = amplifier;
            this.showParticles = false;
            this.showIcon = true;
        }

        public static CloudEffect fromNBT(CompoundNBT nbt) {
            CloudEffect effect = new CloudEffect();
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
            return new EffectInstance(getEffect(), getDuration(), getAmplifier(), false, showParticles(), showIcon());
        }

        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Effect", this.effect);
            nbt.putInt("Duration", this.duration);
            nbt.putInt("Amplifier", this.amplifier);
            nbt.putBoolean("ShowParticles", this.showParticles);
            nbt.putBoolean("ShowIcon", this.showIcon);
            return nbt;
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\EffectCloudEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */