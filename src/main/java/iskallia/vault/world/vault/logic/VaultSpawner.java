package iskallia.vault.world.vault.logic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.entity.AggressiveCowEntity;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.gson.IgnoreEmpty;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class VaultSpawner implements INBTSerializable<CompoundNBT>, IVaultTask {
    private Config config;
    private Config oldConfig;
    private VMapNBT<Integer, Config> configHistory;
    private VListNBT<UUID, StringNBT> spawnedMobIds;

    public VaultSpawner() {
        this.config = new Config();
        this.oldConfig = new Config();
        this.configHistory = VMapNBT.ofInt(new LinkedHashMap<Integer, Config>(), Config::new);
        this.spawnedMobIds = VListNBT.ofUUID();
    }

    public Config getConfig() {
        return this.config;
    }

    public VaultSpawner configure(final Config config) {
        return this.configure(oldConfig -> config);
    }

    public VaultSpawner configure(final UnaryOperator<Config> operator) {
        this.oldConfig = this.config.copy();
        this.config = operator.apply(this.config);
        return this;
    }

    public int getMaxMobs() {
        return this.getConfig().getStartMaxMobs() + this.getConfig().getExtraMaxMobs();
    }

    public VaultSpawner addMaxMobs(final int amount) {
        return this.configure(config -> config.withExtraMaxMobs(i -> i + amount));
    }

    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        if (!this.config.equals(this.oldConfig)) {
            this.configHistory.put(player.getTimer().getRunTime(), this.config.copy());
            this.oldConfig = this.config;
        }
        if (!world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return;
        }
        if (vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsMobSpawning)) {
            return;
        }
        player.runIfPresent(world.getServer(), playerEntity -> {
            this.updateMobIds(world, playerEntity);
            if (this.spawnedMobIds.size() <= this.getMaxMobs() && this.getConfig().getMaxDistance() > 0.0) {
                for (int i = 0; i < 50 && this.spawnedMobIds.size() < this.getMaxMobs(); ++i) {
                    this.attemptSpawn(vault, world, player, world.getRandom());
                }
            }
        });
    }

    protected void updateMobIds(final ServerWorld world, final ServerPlayerEntity player) {
        this.spawnedMobIds = this.spawnedMobIds.stream().map(world::getEntity).filter(Objects::nonNull).filter(entity -> {
            final double distanceSq = entity.distanceToSqr((Entity) player);
            final double despawnDistance = this.getConfig().getDespawnDistance();
            if (distanceSq > despawnDistance * despawnDistance) {
                entity.remove();
                return false;
            } else {
                return true;
            }
        }).map(Entity::getUUID).collect(Collectors.toCollection(VListNBT::ofUUID));
    }

    protected void attemptSpawn(final VaultRaid vault, final ServerWorld world, final VaultPlayer player, final Random random) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            final double min = this.getConfig().getMinDistance();
            final double max = this.getConfig().getMaxDistance();
            final double angle = 6.283185307179586 * random.nextDouble();
            final double distance = Math.sqrt(random.nextDouble() * (max * max - min * min) + min * min);
            final int x = (int) Math.ceil(distance * Math.cos(angle));
            final int z = (int) Math.ceil(distance * Math.sin(angle));
            final double xzRadius = Math.sqrt(x * x + z * z);
            final double yRange = Math.sqrt(max * max - xzRadius * xzRadius);
            final int y = random.nextInt((int) Math.ceil(yRange) * 2 + 1) - (int) Math.ceil(yRange);
            final BlockPos pos = playerEntity.blockPosition();
            final int level = player.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            final LivingEntity spawned = spawnMob(vault, world, level, pos.getX() + x, pos.getY() + y, pos.getZ() + z, random);
            if (spawned != null) {
                this.spawnedMobIds.add(spawned.getUUID());
            }
        });
    }

    @Nullable
    public static LivingEntity spawnMob(final VaultRaid vault, final ServerWorld world, final int vaultLevel, final int x, final int y, final int z, final Random random) {
        LivingEntity entity = createMob(world, vaultLevel, random);
        if (vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, false)) {
            final AggressiveCowEntity replaced = VaultCowOverrides.replaceVaultEntity(vault, entity, world);
            if (replaced != null) {
                entity = (LivingEntity) replaced;
            }
        }
        final BlockState state = world.getBlockState(new BlockPos(x, y - 1, z));
        if (!state.isValidSpawn((IBlockReader) world, new BlockPos(x, y - 1, z), entity.getType())) {
            return null;
        }
        final AxisAlignedBB entityBox = entity.getType().getAABB(x + 0.5, (double) y, z + 0.5);
        if (!world.noCollision(entityBox)) {
            return null;
        }
        entity.moveTo((double) (x + 0.5f), (double) (y + 0.2f), (double) (z + 0.5f), (float) (random.nextDouble() * 2.0 * 3.141592653589793), 0.0f);
        if (entity instanceof MobEntity) {
            ((MobEntity) entity).spawnAnim();
            ((MobEntity) entity).finalizeSpawn((IServerWorld) world, new DifficultyInstance(Difficulty.PEACEFUL, 13000L, 0L, 0.0f), SpawnReason.STRUCTURE, (ILivingEntityData) null, (CompoundNBT) null);
        }
        final GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
        EntityScaler.setScaledEquipment(entity, vault, difficulty, vaultLevel, random, EntityScaler.Type.MOB);
        EntityScaler.setScaled((Entity) entity);
        world.addWithUUID((Entity) entity);
        return entity;
    }

    private static LivingEntity createMob(final ServerWorld world, final int vaultLevel, final Random random) {
        return ModConfigs.VAULT_MOBS.getForLevel(vaultLevel).MOB_POOL.getRandom(random).create((World) world);
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("Config", (INBT) this.getConfig().serializeNBT());
        nbt.put("ConfigHistory", (INBT) this.configHistory.serializeNBT());
        nbt.put("SpawnedMobsIds", (INBT) this.spawnedMobIds.serializeNBT());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.config.deserializeNBT(nbt.getCompound("Config"));
        this.configHistory.deserializeNBT(nbt.getList("ConfigHistory", 10));
        this.spawnedMobIds.deserializeNBT(nbt.getList("SpawnedMobsIds", 8));
    }

    public static VaultSpawner fromNBT(final CompoundNBT nbt) {
        final VaultSpawner spawner = new VaultSpawner();
        spawner.deserializeNBT(nbt);
        return spawner;
    }

    public static class Config implements INBTSerializable<CompoundNBT> {
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        private int startMaxMobs;
        @Expose
        @JsonAdapter(IgnoreEmpty.IntegerAdapter.class)
        private int extraMaxMobs;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        private double minDistance;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        private double maxDistance;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        private double despawnDistance;

        public Config() {
        }

        public Config(final int startMaxMobs, final int extraMaxMobs, final double minDistance, final double maxDistance, final double despawnDistance) {
            this.startMaxMobs = startMaxMobs;
            this.extraMaxMobs = extraMaxMobs;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.despawnDistance = despawnDistance;
        }

        public int getStartMaxMobs() {
            return this.startMaxMobs;
        }

        public int getExtraMaxMobs() {
            return this.extraMaxMobs;
        }

        public double getMinDistance() {
            return this.minDistance;
        }

        public double getMaxDistance() {
            return this.maxDistance;
        }

        public double getDespawnDistance() {
            return this.despawnDistance;
        }

        public Config withStartMaxMobs(final int startMaxMobs) {
            return new Config(startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withExtraMaxMobs(final int extraMaxMobs) {
            return new Config(this.startMaxMobs, extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMinDistance(final double minDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMaxDistance(final double maxDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, maxDistance, this.despawnDistance);
        }

        public Config withDespawnDistance(final double despawnDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, despawnDistance);
        }

        public Config withStartMaxMobs(final IntUnaryOperator operator) {
            return new Config(operator.applyAsInt(this.startMaxMobs), this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withExtraMaxMobs(final IntUnaryOperator operator) {
            return new Config(this.startMaxMobs, operator.applyAsInt(this.extraMaxMobs), this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMinDistance(final DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, operator.applyAsDouble(this.minDistance), this.maxDistance, this.despawnDistance);
        }

        public Config withMaxDistance(final DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, operator.applyAsDouble(this.maxDistance), this.despawnDistance);
        }

        public Config withDespawnDistance(final DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, operator.applyAsDouble(this.despawnDistance));
        }

        public Config copy() {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("StartMaxMobs", this.startMaxMobs);
            nbt.putInt("ExtraMaxMobs", this.extraMaxMobs);
            nbt.putDouble("MinDistance", this.minDistance);
            nbt.putDouble("MaxDistance", this.maxDistance);
            nbt.putDouble("DespawnDistance", this.despawnDistance);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.startMaxMobs = nbt.getInt("StartMaxMobs");
            this.extraMaxMobs = nbt.getInt("ExtraMaxMobs");
            this.minDistance = nbt.getDouble("MinDistance");
            this.maxDistance = nbt.getDouble("MaxDistance");
            this.despawnDistance = nbt.getDouble("DespawnDistance");
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Config)) {
                return false;
            }
            final Config config = (Config) other;
            return this.getStartMaxMobs() == config.getStartMaxMobs() && this.getExtraMaxMobs() == config.getExtraMaxMobs() && this.getMinDistance() == config.getMinDistance() && this.getMaxDistance() == config.getMaxDistance() && this.getDespawnDistance() == config.getDespawnDistance();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getStartMaxMobs(), this.getExtraMaxMobs(), this.getMinDistance(), this.getMaxDistance(), this.getDespawnDistance());
        }

        public static Config fromNBT(final CompoundNBT nbt) {
            final Config config = new Config();
            config.deserializeNBT(nbt);
            return config;
        }
    }
}
