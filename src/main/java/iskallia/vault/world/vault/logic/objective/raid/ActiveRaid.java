package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.config.RaidConfig;
import iskallia.vault.entity.EntityScaler;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.data.GlobalDifficultyData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.raid.modifier.MonsterLevelModifier;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ActiveRaid {
    private static final Random rand = new Random();

    private final BlockPos controller;
    private final AxisAlignedBB raidBox;
    private final RaidPreset preset;
    private int wave = -1;
    private int startDelay = 200;

    private final List<UUID> activeEntities = new ArrayList<>();
    private int totalWaveEntities = 0;

    private final List<UUID> participatingPlayers = new ArrayList<>();

    private ActiveRaid(BlockPos controller, AxisAlignedBB raidBox, RaidPreset preset) {
        this.controller = controller;
        this.raidBox = raidBox;
        this.preset = preset;
    }

    @Nullable
    public static ActiveRaid create(VaultRaid vault, ServerWorld world, BlockPos controller) {
        RaidPreset preset = RaidPreset.randomFromConfig();
        if (preset == null) {
            return null;
        }
        VaultRoom room = vault.getGenerator().getPiecesAt(controller, VaultRoom.class).stream().findFirst().orElse(null);
        if (room == null) {
            return null;
        }
        AxisAlignedBB raidBox = AxisAlignedBB.of(room.getBoundingBox());
        ActiveRaid raid = new ActiveRaid(controller, raidBox, preset);
        world.getEntitiesOfClass(PlayerEntity.class, raidBox).forEach(player -> raid.participatingPlayers.add(player.getUUID()));


        vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> raidObjective.onRaidStart(vault, world, raid, controller));


        raid.playSoundToPlayers((IEntityReader) world, SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 0.7F);
        return raid;
    }

    public void tick(VaultRaid vault, ServerWorld world) {
        if (this.activeEntities.isEmpty() && this.startDelay <= 0) {
            this.wave++;
            RaidPreset.CompoundWaveSpawn wave = this.preset.getWave(this.wave);
            if (wave != null) {
                spawnWave(wave, vault, world);
            }
        }

        if (this.startDelay > 0) {
            this.startDelay--;
        }

        this.activeEntities.removeIf(entityUid -> {
            Entity e = world.getEntity(entityUid);
            if (!(e instanceof MobEntity)) {
                return true;
            }
            MobEntity mob = (MobEntity) e;
            mob.setPersistenceRequired();
            if (!vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
                mob.setGlowing(true);
            }
            if (!(mob.getTarget() instanceof PlayerEntity)) {
                List<PlayerEntity> players = (List<PlayerEntity>) this.participatingPlayers.stream().map(world::getPlayerByUUID).filter(Objects::nonNull).filter(()).collect(Collectors.toList());
                if (!players.isEmpty()) {
                    PlayerEntity player = (PlayerEntity) MiscUtils.getRandomEntry(players, rand);
                    mob.setTarget((LivingEntity) player);
                }
            }
            return false;
        });
    }


    public void spawnWave(RaidPreset.CompoundWaveSpawn wave, VaultRaid vault, ServerWorld world) {
        int participantLevel = -1;
        for (PlayerEntity player : world.getEntitiesOfClass(PlayerEntity.class, getRaidBoundingBox())) {
            int playerLevel = PlayerVaultStatsData.get(world).getVaultStats(player).getVaultLevel();
            if (participantLevel == -1) {
                participantLevel = playerLevel;
                continue;
            }
            if (participantLevel > playerLevel) {
                participantLevel = playerLevel;
            }
        }
        if (participantLevel == -1) {
            participantLevel = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        }
        int scalingLevel = participantLevel;

        int playerCount = this.participatingPlayers.size();
        wave.getWaveSpawns().forEach(spawn -> {
            RaidConfig.MobPool pool = ModConfigs.RAID_CONFIG.getPool(spawn.getMobPool(), scalingLevel);

            if (pool == null) {
                return;
            }

            int spawnCount = spawn.getMobCount();

            spawnCount = (int) (spawnCount * (1.0D + ((Double) vault.getActiveObjective(RaidChallengeObjective.class).map(()).orElse(Double.valueOf(0.0D))).doubleValue()) * playerCount);

            spawnCount = (int) (spawnCount * ModConfigs.RAID_CONFIG.getMobCountMultiplier(scalingLevel));

            for (int i = 0; i < spawnCount; i++) {
                String mobType = pool.getRandomMob();

                EntityType<?> type = (EntityType) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(mobType));

                if (type != null && type.canSummon()) {
                    Vector3f center = new Vector3f(this.controller.getX() + 0.5F, this.controller.getY(), this.controller.getZ() + 0.5F);

                    Vector3f randomPos = MiscUtils.getRandomCirclePosition(center, new Vector3f(0.0F, 1.0F, 0.0F), 8.0F + rand.nextFloat() * 6.0F);
                    BlockPos spawnAt = MiscUtils.getEmptyNearby((IWorldReader) world, new BlockPos(randomPos.x(), randomPos.y(), randomPos.z())).orElse(BlockPos.ZERO);
                    if (!spawnAt.equals(BlockPos.ZERO)) {
                        Entity spawned = type.spawn(world, null, null, spawnAt, SpawnReason.EVENT, true, false);
                        if (spawned instanceof MobEntity) {
                            GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.get(world).getVaultDifficulty();
                            MobEntity mob = (MobEntity) spawned;
                            processSpawnedMob(mob, vault, difficulty, scalingLevel);
                            this.activeEntities.add(mob.getUUID());
                        }
                    }
                }
            }
        });
        this.totalWaveEntities = this.activeEntities.size();

        playSoundToPlayers((IEntityReader) world, SoundEvents.RAID_HORN, 64.0F, 1.0F);
    }

    private void processSpawnedMob(MobEntity mob, VaultRaid vault, GlobalDifficultyData.Difficulty difficulty, int level) {
        level += ((Integer) vault.getActiveObjective(RaidChallengeObjective.class).map(raidObjective -> Integer.valueOf(raidObjective.<MonsterLevelModifier>getModifiersOfType(MonsterLevelModifier.class).entrySet().stream().mapToInt(()).sum()))


                .orElse(Integer.valueOf(0))).intValue();

        mob.setPersistenceRequired();
        EntityScaler.setScaledEquipment((LivingEntity) mob, vault, difficulty, level, new Random(), EntityScaler.Type.MOB);
        EntityScaler.setScaled((Entity) mob);

        vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidObjective -> raidObjective.getAllModifiers().forEach(()));
    }


    public boolean isFinished() {
        return (this.wave >= 0 && this.preset.getWave(this.wave) == null);
    }

    List<UUID> getActiveEntities() {
        return this.activeEntities;
    }

    public boolean isPlayerInRaid(PlayerEntity player) {
        return isPlayerInRaid(player.getUUID());
    }

    public boolean isPlayerInRaid(UUID playerId) {
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

    void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    boolean hasNextWave() {
        return (this.preset.getWave(this.wave + 1) != null);
    }

    public void finish(VaultRaid raid, ServerWorld world) {
        raid.getActiveObjective(RaidChallengeObjective.class).ifPresent(raidChallenge -> raidChallenge.onRaidFinish(raid, world, this, this.controller));


        playSoundToPlayers((IEntityReader) world, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.7F, 0.5F);
    }

    private void playSoundToPlayers(IEntityReader world, SoundEvent event, float volume, float pitch) {
        this.participatingPlayers.forEach(playerId -> {
            PlayerEntity player = world.getPlayerByUUID(playerId);
            if (player instanceof ServerPlayerEntity) {
                SPlaySoundEffectPacket pkt = new SPlaySoundEffectPacket(event, SoundCategory.BLOCKS, player.getX(), player.getY(), player.getZ(), volume, pitch);
                ((ServerPlayerEntity) player).connection.send((IPacket) pkt);
            }
        });
    }


    public BlockPos getController() {
        return this.controller;
    }

    public void serialize(CompoundNBT tag) {
        tag.put("pos", (INBT) NBTHelper.serializeBlockPos(this.controller));
        tag.put("boxFrom", (INBT) NBTHelper.serializeBlockPos(new BlockPos(this.raidBox.minX, this.raidBox.minY, this.raidBox.minZ)));
        tag.put("boxTo", (INBT) NBTHelper.serializeBlockPos(new BlockPos(this.raidBox.maxX, this.raidBox.maxY, this.raidBox.maxZ)));
        tag.putInt("wave", this.wave);
        tag.put("waves", (INBT) this.preset.serialize());
        tag.putInt("startDelay", this.startDelay);

        tag.putInt("totalWaveEntities", this.totalWaveEntities);
        NBTHelper.writeList(tag, "entities", this.activeEntities, StringNBT.class, uuid -> StringNBT.valueOf(uuid.toString()));


        NBTHelper.writeList(tag, "players", this.participatingPlayers, StringNBT.class, uuid -> StringNBT.valueOf(uuid.toString()));
    }


    public static ActiveRaid deserializeNBT(CompoundNBT nbt) {
        BlockPos controller = NBTHelper.deserializeBlockPos(nbt.getCompound("pos"));
        BlockPos from = NBTHelper.deserializeBlockPos(nbt.getCompound("boxFrom"));
        BlockPos to = NBTHelper.deserializeBlockPos(nbt.getCompound("boxTo"));
        RaidPreset waves = RaidPreset.deserialize(nbt.getCompound("waves"));

        ActiveRaid raid = new ActiveRaid(controller, new AxisAlignedBB(from, to), waves);
        raid.startDelay = nbt.getInt("startDelay");
        raid.wave = nbt.getInt("wave");
        raid.totalWaveEntities = nbt.getInt("totalWaveEntities");
        raid.activeEntities.addAll(NBTHelper.readList(nbt, "entities", StringNBT.class, idString -> UUID.fromString(idString.getAsString())));


        raid.participatingPlayers.addAll(NBTHelper.readList(nbt, "players", StringNBT.class, idString -> UUID.fromString(idString.getAsString())));


        return raid;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\ActiveRaid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */