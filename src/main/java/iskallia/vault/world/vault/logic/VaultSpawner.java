package iskallia.vault.world.vault.logic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.config.VaultMobsConfig;
import iskallia.vault.entity.AggressiveCowEntity;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.gson.IgnoreEmpty;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
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
    private Config config = new Config();
    private Config oldConfig = new Config();
    private VMapNBT<Integer, Config> configHistory = VMapNBT.ofInt(new LinkedHashMap<>(), Config::new);

    private VListNBT<UUID, StringNBT> spawnedMobIds = VListNBT.ofUUID();


    public Config getConfig() {
        return this.config;
    }

    public VaultSpawner configure(Config config) {
        return configure(oldConfig -> config);
    }

    public VaultSpawner configure(UnaryOperator<Config> operator) {
        this.oldConfig = this.config.copy();
        this.config = operator.apply(this.config);
        return this;
    }

    public int getMaxMobs() {
        return getConfig().getStartMaxMobs() + getConfig().getExtraMaxMobs();
    }

    public VaultSpawner addMaxMobs(int amount) {
        return configure(config -> config.withExtraMaxMobs(()));
    }


    public void execute(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        if (!this.config.equals(this.oldConfig)) {
            this.configHistory.put(Integer.valueOf(player.getTimer().getRunTime()), this.config.copy());
            this.oldConfig = this.config;
        }
        if (!world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return;
        }
        if (vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsMobSpawning)) {
            return;
        }

        player.runIfPresent(world.getServer(), playerEntity -> {
            updateMobIds(world, playerEntity);
            if (this.spawnedMobIds.size() > getMaxMobs() || getConfig().getMaxDistance() <= 0.0D)
                return;
            int i = 0;
            while (i < 50 && this.spawnedMobIds.size() < getMaxMobs()) {
                attemptSpawn(vault, world, player, world.getRandom());
                i++;
            }
        });
    }

    protected void updateMobIds(ServerWorld world, ServerPlayerEntity player) {
        this


                .spawnedMobIds = (VListNBT<UUID, StringNBT>) this.spawnedMobIds.stream().map(world::getEntity).filter(Objects::nonNull).filter(entity -> {
            double distanceSq = entity.distanceToSqr((Entity) player);
            double despawnDistance = getConfig().getDespawnDistance();
            if (distanceSq > despawnDistance * despawnDistance) {
                entity.remove();
                return false;
            }
            return true;
        }).map(Entity::getUUID).collect(Collectors.toCollection(VListNBT::ofUUID));
    }

    protected void attemptSpawn(VaultRaid vault, ServerWorld world, VaultPlayer player, Random random) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            double min = getConfig().getMinDistance();
            double max = getConfig().getMaxDistance();
            double angle = 6.283185307179586D * random.nextDouble();
            double distance = Math.sqrt(random.nextDouble() * (max * max - min * min) + min * min);
            int x = (int) Math.ceil(distance * Math.cos(angle));
            int z = (int) Math.ceil(distance * Math.sin(angle));
            double xzRadius = Math.sqrt((x * x + z * z));
            double yRange = Math.sqrt(max * max - xzRadius * xzRadius);
            int y = random.nextInt((int) Math.ceil(yRange) * 2 + 1) - (int) Math.ceil(yRange);
            BlockPos pos = playerEntity.blockPosition();
            int level = ((Integer) player.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
            LivingEntity spawned = spawnMob(vault, world, level, pos.getX() + x, pos.getY() + y, pos.getZ() + z, random);
            if (spawned != null) {
                this.spawnedMobIds.add(spawned.getUUID());
            }
        });
    }


    @Nullable
    public static LivingEntity spawnMob(VaultRaid vault, ServerWorld world, int vaultLevel, int x, int y, int z, Random random) {
        AggressiveCowEntity aggressiveCowEntity;
        LivingEntity entity = createMob(world, vaultLevel, random);
        if (((Boolean) vault.getProperties().getBaseOrDefault(VaultRaid.COW_VAULT, Boolean.valueOf(false))).booleanValue()) {
            AggressiveCowEntity replaced = VaultCowOverrides.replaceVaultEntity(vault, entity, world);
            if (replaced != null) {
                aggressiveCowEntity = replaced;
            }
        }

        BlockState state = world.getBlockState(new BlockPos(x, y - 1, z));
        if (!state.isValidSpawn((IBlockReader) world, new BlockPos(x, y - 1, z), aggressiveCowEntity.getType())) {
            return null;
        }

        AxisAlignedBB entityBox = aggressiveCowEntity.getType().getAABB(x + 0.5D, y, z + 0.5D);
        if (!world.noCollision(entityBox)) {
            return null;
        }

        aggressiveCowEntity.moveTo((x + 0.5F), (y + 0.2F), (z + 0.5F), (float) (random.nextDouble() * 2.0D * Math.PI), 0.0F);

        if (aggressiveCowEntity instanceof MobEntity) {
            ((MobEntity) aggressiveCowEntity).spawnAnim();
            ((MobEntity) aggressiveCowEntity).finalizeSpawn((IServerWorld) world, new DifficultyInstance(Difficulty.PEACEFUL, 13000L, 0L, 0.0F), SpawnReason.STRUCTURE, null, null);
        }


        GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
        EntityScaler.setScaledEquipment((LivingEntity) aggressiveCowEntity, vault, difficulty, vaultLevel, random, EntityScaler.Type.MOB);
        EntityScaler.setScaled((Entity) aggressiveCowEntity);

        world.addWithUUID((Entity) aggressiveCowEntity);
        return (LivingEntity) aggressiveCowEntity;
    }

    private static LivingEntity createMob(ServerWorld world, int vaultLevel, Random random) {
        return ((VaultMobsConfig.Mob) (ModConfigs.VAULT_MOBS.getForLevel(vaultLevel)).MOB_POOL.getRandom(random)).create((World) world);
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Config", (INBT) getConfig().serializeNBT());
        nbt.put("ConfigHistory", (INBT) this.configHistory.serializeNBT());
        nbt.put("SpawnedMobsIds", (INBT) this.spawnedMobIds.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.config.deserializeNBT(nbt.getCompound("Config"));
        this.configHistory.deserializeNBT(nbt.getList("ConfigHistory", 10));
        this.spawnedMobIds.deserializeNBT(nbt.getList("SpawnedMobsIds", 8));
    }

    public static VaultSpawner fromNBT(CompoundNBT nbt) {
        VaultSpawner spawner = new VaultSpawner();
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

        public Config(int startMaxMobs, int extraMaxMobs, double minDistance, double maxDistance, double despawnDistance) {
            this.startMaxMobs = startMaxMobs;
            this.extraMaxMobs = extraMaxMobs;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.despawnDistance = despawnDistance;
        }

        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        private double maxDistance;
        @Expose
        @JsonAdapter(IgnoreEmpty.DoubleAdapter.class)
        private double despawnDistance;

        public Config() {
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

        public Config withStartMaxMobs(int startMaxMobs) {
            return new Config(startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withExtraMaxMobs(int extraMaxMobs) {
            return new Config(this.startMaxMobs, extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMinDistance(double minDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMaxDistance(double maxDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, maxDistance, this.despawnDistance);
        }

        public Config withDespawnDistance(double despawnDistance) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, despawnDistance);
        }

        public Config withStartMaxMobs(IntUnaryOperator operator) {
            return new Config(operator.applyAsInt(this.startMaxMobs), this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withExtraMaxMobs(IntUnaryOperator operator) {
            return new Config(this.startMaxMobs, operator.applyAsInt(this.extraMaxMobs), this.minDistance, this.maxDistance, this.despawnDistance);
        }

        public Config withMinDistance(DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, operator.applyAsDouble(this.minDistance), this.maxDistance, this.despawnDistance);
        }

        public Config withMaxDistance(DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, operator.applyAsDouble(this.maxDistance), this.despawnDistance);
        }

        public Config withDespawnDistance(DoubleUnaryOperator operator) {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, operator.applyAsDouble(this.despawnDistance));
        }

        public Config copy() {
            return new Config(this.startMaxMobs, this.extraMaxMobs, this.minDistance, this.maxDistance, this.despawnDistance);
        }


        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("StartMaxMobs", this.startMaxMobs);
            nbt.putInt("ExtraMaxMobs", this.extraMaxMobs);
            nbt.putDouble("MinDistance", this.minDistance);
            nbt.putDouble("MaxDistance", this.maxDistance);
            nbt.putDouble("DespawnDistance", this.despawnDistance);
            return nbt;
        }


        public void deserializeNBT(CompoundNBT nbt) {
            this.startMaxMobs = nbt.getInt("StartMaxMobs");
            this.extraMaxMobs = nbt.getInt("ExtraMaxMobs");
            this.minDistance = nbt.getDouble("MinDistance");
            this.maxDistance = nbt.getDouble("MaxDistance");
            this.despawnDistance = nbt.getDouble("DespawnDistance");
        }


        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Config)) return false;
            Config config = (Config) other;
            return (getStartMaxMobs() == config.getStartMaxMobs() &&
                    getExtraMaxMobs() == config.getExtraMaxMobs() &&
                    getMinDistance() == config.getMinDistance() &&
                    getMaxDistance() == config.getMaxDistance() &&
                    getDespawnDistance() == config.getDespawnDistance());
        }


        public int hashCode() {
            return Objects.hash(new Object[]{
                    Integer.valueOf(getStartMaxMobs()), Integer.valueOf(getExtraMaxMobs()), Double.valueOf(getMinDistance()), Double.valueOf(getMaxDistance()),
                    Double.valueOf(getDespawnDistance())});
        }

        public static Config fromNBT(CompoundNBT nbt) {
            Config config = new Config();
            config.deserializeNBT(nbt);
            return config;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */