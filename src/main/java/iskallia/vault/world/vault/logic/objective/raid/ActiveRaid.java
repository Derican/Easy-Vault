package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.config.RaidConfig;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.raid.modifier.MonsterAmountModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.MonsterLevelModifier;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IEntityReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ActiveRaid {
    private static final Random rand;
    private final BlockPos controller;
    private final AxisAlignedBB raidBox;
    private final RaidPreset preset;
    private int wave;
    private int startDelay;
    private final List<UUID> activeEntities;
    private int totalWaveEntities;
    private final List<UUID> participatingPlayers;

    private ActiveRaid(final BlockPos controller, final AxisAlignedBB raidBox, final RaidPreset preset) {
        this.wave = -1;
        this.startDelay = 200;
        this.activeEntities = new ArrayList<UUID>();
        this.totalWaveEntities = 0;
        this.participatingPlayers = new ArrayList<UUID>();
        this.controller = controller;
        this.raidBox = raidBox;
        this.preset = preset;
    }

    @Nullable
    public static ActiveRaid create(final VaultRaid vault, final ServerWorld world, final BlockPos controller) {
        final int raidIndex = vault.getProperties().getBaseOrDefault(VaultRaid.RAID_INDEX, 0);
        final RaidPreset preset = vault.getProperties().exists(VaultRaid.PARENT) ? RaidPreset.randomFromFinalConfig(raidIndex) : RaidPreset.randomFromConfig();
        vault.getProperties().create(VaultRaid.RAID_INDEX, raidIndex + 1);
        if (preset == null) {
            return null;
        }
        final VaultRoom room = (VaultRoom) vault.getGenerator().getPiecesAt(controller, VaultRoom.class).stream().findFirst().orElse(null);
        if (room == null) {
            return null;
        }
        final AxisAlignedBB raidBox = AxisAlignedBB.of(room.getBoundingBox());
        final ActiveRaid raid = new ActiveRaid(controller, raidBox, preset);
        world.getEntitiesOfClass(PlayerEntity.class, raidBox).forEach(player -> raid.participatingPlayers.add(player.getUUID()));
        vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> raidObjective.onRaidStart(vault, world, raid, controller));
        raid.playSoundToPlayers((IEntityReader) world, SoundEvents.EVOKER_PREPARE_SUMMON, 1.0f, 0.7f);
        return raid;
    }

    public void tick(final VaultRaid vault, final ServerWorld world) {
        if (this.activeEntities.isEmpty() && this.startDelay <= 0) {
            ++this.wave;
            final RaidPreset.CompoundWaveSpawn wave = this.preset.getWave(this.wave);
            if (wave != null) {
                this.spawnWave(wave, vault, world);
            }
        }
        if (this.startDelay > 0) {
            --this.startDelay;
        }
        this.activeEntities.removeIf(entityUid -> {
            final Entity e = world.getEntity(entityUid);
            if (!(e instanceof MobEntity)) {
                return true;
            } else {
                final MobEntity mob = (MobEntity) e;
                mob.setPersistenceRequired();
                if (!vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
                    mob.setGlowing(true);
                }
                if (!(mob.getTarget() instanceof PlayerEntity)) {
                    final List<PlayerEntity> players = this.participatingPlayers.stream().map(world::getPlayerByUUID).filter(Objects::nonNull).filter(player -> this.raidBox.inflate(10.0).contains(player.position())).collect(Collectors.toList());
                    PlayerEntity player = null;
                    if (!players.isEmpty()) {
                        player = MiscUtils.getRandomEntry(players, ActiveRaid.rand);
                        mob.setTarget((LivingEntity) player);
                    }
                }
                return false;
            }
        });
    }

    public void spawnWave(final RaidPreset.CompoundWaveSpawn wave, final VaultRaid vault, final ServerWorld world) {
        int participantLevel = -1;
        for (final PlayerEntity player : world.getEntitiesOfClass(PlayerEntity.class, this.getRaidBoundingBox())) {
            final int playerLevel = PlayerVaultStatsData.get(world).getVaultStats(player).getVaultLevel();
            if (participantLevel == -1) {
                participantLevel = playerLevel;
            } else {
                if (participantLevel <= playerLevel) {
                    continue;
                }
                participantLevel = playerLevel;
            }
        }
        if (participantLevel == -1) {
            participantLevel = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        }
        final int scalingLevel = participantLevel;
        final int playerCount = this.participatingPlayers.size();
        wave.getWaveSpawns().forEach(spawn -> {
            RaidConfig.MobPool pool;
            if (vault.getProperties().exists(VaultRaid.PARENT)) {
                pool = ModConfigs.FINAL_RAID.getPool(spawn.getMobPool(), scalingLevel);
            }
            else {
                pool = ModConfigs.RAID.getPool(spawn.getMobPool(), scalingLevel);
            }
            if (pool == null) {
                return;
            }
            else {
                final int spawnCount = spawn.getMobCount();
                int spawnCount2 = (int)(spawnCount * ((1.0 + vault.getActiveObjective(RaidChallengeObjective.class).map(raidObjective -> raidObjective.getModifiersOfType(MonsterAmountModifier.class).values().stream().mapToDouble(Float::doubleValue).sum()).orElse(0.0)) * playerCount));
                if (!vault.getProperties().exists(VaultRaid.PARENT)) {
                    spawnCount2 *= (int)ModConfigs.RAID.getMobCountMultiplier(scalingLevel);
                }
                for (int i = 0; i < spawnCount2; ++i) {
                    final String mobType = pool.getRandomMob();
                    final EntityType type = (EntityType) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(mobType));
                    if (type != null) {
                        if (!(!type.canSummon())) {
                            final Vector3f center = new Vector3f(this.controller.getX() + 0.5f, (float) this.controller.getY(), this.controller.getZ() + 0.5f);
                            final Vector3f randomPos = MiscUtils.getRandomCirclePosition(center, new Vector3f(0.0f, 1.0f, 0.0f), 8.0f + ActiveRaid.rand.nextFloat() * 6.0f);
                            final BlockPos spawnAt = MiscUtils.getEmptyNearby((IWorldReader) world, new BlockPos((double) randomPos.x(), (double) randomPos.y(), (double) randomPos.z())).orElse(BlockPos.ZERO);
                            if (!spawnAt.equals(BlockPos.ZERO)) {
                                final Entity spawned = type.spawn(world, (ItemStack) null, (PlayerEntity) null, spawnAt, SpawnReason.EVENT, true, false);
                                if (spawned instanceof MobEntity) {
                                    final GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
                                    final MobEntity mob = (MobEntity) spawned;
                                    this.processSpawnedMob(mob, vault, difficulty, scalingLevel);
                                    this.activeEntities.add(mob.getUUID());
                                }
                            }
                        }
                    }
                }
                return;
            }
        });
        this.totalWaveEntities = this.activeEntities.size();
        this.playSoundToPlayers((IEntityReader) world, SoundEvents.RAID_HORN, 64.0f, 1.0f);
    }

    private void processSpawnedMob(final MobEntity mob, final VaultRaid vault, final GlobalDifficultyData.Difficulty difficulty, int level) {
        level += vault.getActiveObjective(RaidChallengeObjective.class).map(raidObjective -> raidObjective.getModifiersOfType(MonsterLevelModifier.class).entrySet().stream().mapToInt(entry -> entry.getKey().getLevelAdded(entry.getValue())).sum()).orElse(0);
        mob.setPersistenceRequired();
        EntityScaler.setScaledEquipment((LivingEntity) mob, vault, difficulty, level, new Random(), EntityScaler.Type.MOB);
        EntityScaler.setScaled((Entity) mob);
        vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> raidObjective.getAllModifiers().forEach((modifier, value) -> modifier.affectRaidMob(mob, value)));
    }

    public boolean isFinished() {
        return this.wave >= 0 && this.preset.getWave(this.wave) == null;
    }

    List<UUID> getActiveEntities() {
        return this.activeEntities;
    }

    public boolean isPlayerInRaid(final PlayerEntity player) {
        return this.isPlayerInRaid(player.getUUID());
    }

    public boolean isPlayerInRaid(final UUID playerId) {
        return this.participatingPlayers.contains(playerId);
    }

    public AxisAlignedBB getRaidBoundingBox() {
        return this.raidBox;
    }

    public int getWave() {
        return this.wave;
    }

    public int getTotalWaves() {
        return this.preset.getWaves();
    }

    public int getAliveEntities() {
        return this.activeEntities.size();
    }

    public int getTotalWaveEntities() {
        return this.totalWaveEntities;
    }

    public int getStartDelay() {
        return this.startDelay;
    }

    void setStartDelay(final int startDelay) {
        this.startDelay = startDelay;
    }

    boolean hasNextWave() {
        return this.preset.getWave(this.wave + 1) != null;
    }

    public void finish(final VaultRaid raid, final ServerWorld world) {
        raid.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidChallenge -> raidChallenge.onRaidFinish(raid, world, this, this.controller));
        this.playSoundToPlayers((IEntityReader) world, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 0.5f);
    }

    private void playSoundToPlayers(final IEntityReader world, final SoundEvent event, final float volume, final float pitch) {
        this.participatingPlayers.forEach(playerId -> {
            final PlayerEntity player = world.getPlayerByUUID(playerId);
            if (player instanceof ServerPlayerEntity) {
                final SPlaySoundEffectPacket pkt = new SPlaySoundEffectPacket(event, SoundCategory.BLOCKS, player.getX(), player.getY(), player.getZ(), volume, pitch);
                ((ServerPlayerEntity) player).connection.send((IPacket) pkt);
            }
        });
    }

    public BlockPos getController() {
        return this.controller;
    }

    public void serialize(final CompoundNBT tag) {
        tag.put("pos", (INBT) NBTHelper.serializeBlockPos(this.controller));
        tag.put("boxFrom", (INBT) NBTHelper.serializeBlockPos(new BlockPos(this.raidBox.minX, this.raidBox.minY, this.raidBox.minZ)));
        tag.put("boxTo", (INBT) NBTHelper.serializeBlockPos(new BlockPos(this.raidBox.maxX, this.raidBox.maxY, this.raidBox.maxZ)));
        tag.putInt("wave", this.wave);
        tag.put("waves", (INBT) this.preset.serialize());
        tag.putInt("startDelay", this.startDelay);
        tag.putInt("totalWaveEntities", this.totalWaveEntities);
        NBTHelper.writeList(tag, "entities", (Collection<UUID>) this.activeEntities, StringNBT.class, uuid -> StringNBT.valueOf(uuid.toString()));
        NBTHelper.writeList(tag, "players", (Collection<UUID>) this.participatingPlayers, StringNBT.class, uuid -> StringNBT.valueOf(uuid.toString()));
    }

    public static ActiveRaid deserializeNBT(final CompoundNBT nbt) {
        final BlockPos controller = NBTHelper.deserializeBlockPos(nbt.getCompound("pos"));
        final BlockPos from = NBTHelper.deserializeBlockPos(nbt.getCompound("boxFrom"));
        final BlockPos to = NBTHelper.deserializeBlockPos(nbt.getCompound("boxTo"));
        final RaidPreset waves = RaidPreset.deserialize(nbt.getCompound("waves"));
        final ActiveRaid raid = new ActiveRaid(controller, new AxisAlignedBB(from, to), waves);
        raid.startDelay = nbt.getInt("startDelay");
        raid.wave = nbt.getInt("wave");
        raid.totalWaveEntities = nbt.getInt("totalWaveEntities");
        raid.activeEntities.addAll(NBTHelper.readList(nbt, "entities", StringNBT.class, idString -> UUID.fromString(idString.getAsString())));
        raid.participatingPlayers.addAll(NBTHelper.readList(nbt, "players", StringNBT.class, idString -> UUID.fromString(idString.getAsString())));
        return raid;
    }

    static {
        rand = new Random();
    }
}
