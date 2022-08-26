package iskallia.vault.world.vault;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Function;

public class VaultUtils {
    public static void exitSafely(final ServerWorld world, final ServerPlayerEntity player) {
        final BlockPos rawSpawnPoint = player.getRespawnPosition();
        final Optional<Vector3d> spawnPoint = (rawSpawnPoint != null) ? PlayerEntity.findRespawnPositionAndUseSpawnBlock(world, rawSpawnPoint, player.getRespawnAngle(), player.isRespawnForced(), true) : Optional.empty();
        final RegistryKey<World> targetDim = world.dimension();
        final RegistryKey<World> sourceDim = player.getCommandSenderWorld().dimension();
        if (!targetDim.equals(sourceDim)) {
            player.changeDimension(world, new ITeleporter() {
                public Entity placeEntity(final Entity entity, final ServerWorld currentWorld, final ServerWorld destWorld, final float yaw, final Function<Boolean, Entity> repositionEntity) {
                    final Entity repositionedEntity = repositionEntity.apply(false);
                    if (spawnPoint.isPresent()) {
                        final Vector3d spawnPos = spawnPoint.get();
                        repositionedEntity.teleportTo(spawnPos.x(), spawnPos.y(), spawnPos.z());
                    } else {
                        VaultUtils.moveToWorldSpawn(world, player);
                    }
                    if (repositionedEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) repositionedEntity).getLevel().getServer().tell(new TickDelayedTask(20, () -> ((ServerPlayerEntity) repositionedEntity).giveExperiencePoints(0)));
                    }
                    return repositionedEntity;
                }
            });
        } else if (spawnPoint.isPresent()) {
            final BlockState blockstate = world.getBlockState(rawSpawnPoint);
            final Vector3d spawnPos = spawnPoint.get();
            if (!blockstate.is(BlockTags.BEDS) && !blockstate.is(Blocks.RESPAWN_ANCHOR)) {
                player.teleportTo(world, spawnPos.x, spawnPos.y, spawnPos.z, player.getRespawnAngle(), 0.0f);
            } else {
                final Vector3d vector3d1 = Vector3d.atBottomCenterOf(rawSpawnPoint).subtract(spawnPos).normalize();
                player.teleportTo(world, spawnPos.x, spawnPos.y, spawnPos.z, (float) MathHelper.wrapDegrees(MathHelper.atan2(vector3d1.z, vector3d1.x) * 57.29577951308232 - 90.0), 0.0f);
            }
            player.teleportTo(spawnPos.x, spawnPos.y, spawnPos.z);
        } else {
            moveToWorldSpawn(world, player);
        }
    }

    public static void moveTo(final ServerWorld world, final Entity entity, final Vector3d pos) {
        final RegistryKey<World> targetDim = world.dimension();
        final RegistryKey<World> sourceDim = entity.getCommandSenderWorld().dimension();
        entity.changeDimension(world, new ITeleporter() {
            public Entity placeEntity(final Entity entity, final ServerWorld currentWorld, final ServerWorld destWorld, final float yaw, final Function<Boolean, Entity> repositionEntity) {
                final Entity repositionedEntity = repositionEntity.apply(false);
                repositionedEntity.teleportTo(pos.x(), pos.y(), pos.z());
                if (repositionedEntity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) repositionedEntity).getLevel().getServer().tell(new TickDelayedTask(20, () -> ((ServerPlayerEntity) repositionedEntity).giveExperiencePoints(0)));
                }
                return repositionedEntity;
            }
        });
    }

    public static void moveToWorldSpawn(final ServerWorld world, final ServerPlayerEntity player) {
        final BlockPos blockpos = world.getSharedSpawnPos();
        if (!world.dimensionType().hasSkyLight() || world.getServer().getWorldData().getGameType() == GameType.ADVENTURE) {
            player.teleportTo(world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0f, 0.0f);
            player.teleportTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            while (!world.noCollision(player) && player.getY() < 255.0) {
                player.teleportTo(world, player.getX(), player.getY() + 1.0, player.getZ(), 0.0f, 0.0f);
                player.teleportTo(player.getX(), player.getY(), player.getZ());
            }
            return;
        }
        int i = Math.max(0, world.getServer().getSpawnRadius(world));
        final int j = MathHelper.floor(world.getWorldBorder().getDistanceToBorder(blockpos.getX(), blockpos.getZ()));
        if (j < i) {
            i = j;
        }
        if (j <= 1) {
            i = 1;
        }
        final long k = i * 2 + 1;
        final long l = k * k;
        final int i2 = (l > 2147483647L) ? Integer.MAX_VALUE : ((int) l);
        final int j2 = (i2 <= 16) ? (i2 - 1) : 17;
        final int k2 = new Random().nextInt(i2);
        for (int l2 = 0; l2 < i2; ++l2) {
            final int i3 = (k2 + j2 * l2) % i2;
            final int j3 = i3 % (i * 2 + 1);
            final int k3 = i3 / (i * 2 + 1);
            final BlockPos pos = new BlockPos(blockpos.getX() + j3 - i, 0, blockpos.getZ() + k3 - i);
            final OptionalInt height = getSpawnHeight(world, pos.getX(), pos.getZ());
            if (height.isPresent()) {
                player.teleportTo(world, pos.getX(), height.getAsInt(), pos.getZ(), 0.0f, 0.0f);
                player.teleportTo(pos.getX(), height.getAsInt(), pos.getZ());
                if (world.noCollision(player)) {
                    break;
                }
            }
        }
    }

    public static OptionalInt getSpawnHeight(final ServerWorld world, final int posX, final int posZ) {
        final BlockPos.Mutable pos = new BlockPos.Mutable(posX, 0, posZ);
        final Chunk chunk = world.getChunk(posX >> 4, posZ >> 4);
        final int top = world.dimensionType().hasCeiling() ? world.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, posX & 0xF, posZ & 0xF);
        if (top >= 0) {
            final int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, posX & 0xF, posZ & 0xF);
            if (j > top || j <= chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, posX & 0xF, posZ & 0xF)) {
                for (int k = top + 1; k >= 0; --k) {
                    pos.set(posX, k, posZ);
                    final BlockState state = world.getBlockState(pos);
                    if (!state.getFluidState().isEmpty()) {
                        break;
                    }
                    if (state.equals(world.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial())) {
                        return OptionalInt.of(pos.above().getY());
                    }
                }
            }
        }
        return OptionalInt.empty();
    }

    public static boolean matchesDimension(final VaultRaid vault, final World world) {
        return vault.getProperties().getBase(VaultRaid.DIMENSION).filter(key -> key == world.dimension()).isPresent();
    }

    public static boolean inVault(final VaultRaid vault, final Entity entity) {
        return inVault(vault, entity.getCommandSenderWorld(), entity.blockPosition());
    }

    public static boolean inVault(final VaultRaid vault, final World world, final BlockPos pos) {
        if (vault == null) {
            return false;
        }
        final Optional<RegistryKey<World>> dimension = vault.getProperties().getBase(VaultRaid.DIMENSION);
        return dimension.isPresent() && world.dimension() == dimension.get() && vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).map(box -> box.isInside(pos)).orElse(false);
    }
}
