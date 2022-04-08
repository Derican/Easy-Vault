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
        BlockPos rawSpawnPoint = player.getRespawnPosition();


        final Optional<Vector3d> spawnPoint = (rawSpawnPoint != null) ? PlayerEntity.findRespawnPositionAndUseSpawnBlock(world, rawSpawnPoint, player.getRespawnAngle(), player.isRespawnForced(), true) : Optional.<Vector3d>empty();

        RegistryKey<World> targetDim = world.dimension();
        RegistryKey<World> sourceDim = player.getCommandSenderWorld().dimension();


        if (!targetDim.equals(sourceDim)) {
            player.changeDimension(world, new ITeleporter() {
                public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                    Entity repositionedEntity = repositionEntity.apply(Boolean.valueOf(false));
                    if (spawnPoint.isPresent()) {
                        Vector3d spawnPos = spawnPoint.get();
                        repositionedEntity.teleportTo(spawnPos.x(), spawnPos.y(), spawnPos.z());
                    } else {
                        VaultUtils.moveToWorldSpawn(world, player);
                    }
                    if (repositionedEntity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) repositionedEntity).getLevel().getServer().tell((Runnable) new TickDelayedTask(20, () -> ((ServerPlayerEntity) repositionedEntity).giveExperiencePoints(0)));
                    }


                    return repositionedEntity;
                }
            });
        } else if (spawnPoint.isPresent()) {
            BlockState blockstate = world.getBlockState(rawSpawnPoint);
            Vector3d spawnPos = spawnPoint.get();

            if (!blockstate.is((ITag) BlockTags.BEDS) && !blockstate.is(Blocks.RESPAWN_ANCHOR)) {
                player.teleportTo(world, spawnPos.x, spawnPos.y, spawnPos.z, player.getRespawnAngle(), 0.0F);
            } else {
                Vector3d vector3d1 = Vector3d.atBottomCenterOf((Vector3i) rawSpawnPoint).subtract(spawnPos).normalize();
                player.teleportTo(world, spawnPos.x, spawnPos.y, spawnPos.z,
                        (float) MathHelper.wrapDegrees(MathHelper.atan2(vector3d1.z, vector3d1.x) * 57.29577951308232D - 90.0D), 0.0F);
            }

            player.teleportTo(spawnPos.x, spawnPos.y, spawnPos.z);
        } else {
            moveToWorldSpawn(world, player);
        }
    }


    public static void moveTo(ServerWorld world, Entity entity, final Vector3d pos) {
        RegistryKey<World> targetDim = world.dimension();
        RegistryKey<World> sourceDim = entity.getCommandSenderWorld().dimension();

        entity.changeDimension(world, new ITeleporter() {
            public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity repositionedEntity = repositionEntity.apply(Boolean.valueOf(false));
                repositionedEntity.teleportTo(pos.x(), pos.y(), pos.z());

                if (repositionedEntity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) repositionedEntity).getLevel().getServer().tell((Runnable) new TickDelayedTask(20, () -> ((ServerPlayerEntity) repositionedEntity).giveExperiencePoints(0)));
                }


                return repositionedEntity;
            }
        });
    }

    public static void moveToWorldSpawn(ServerWorld world, ServerPlayerEntity player) {
        BlockPos blockpos = world.getSharedSpawnPos();

        if (!world.dimensionType().hasSkyLight() || world.getServer().getWorldData().getGameType() == GameType.ADVENTURE) {
            player.teleportTo(world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F);
            player.teleportTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());

            while (!world.noCollision((Entity) player) && player.getY() < 255.0D) {
                player.teleportTo(world, player.getX(), player.getY() + 1.0D, player.getZ(), 0.0F, 0.0F);
                player.teleportTo(player.getX(), player.getY(), player.getZ());
            }

            return;
        }

        int i = Math.max(0, world.getServer().getSpawnRadius(world));
        int j = MathHelper.floor(world.getWorldBorder().getDistanceToBorder(blockpos.getX(), blockpos.getZ()));

        if (j < i) i = j;
        if (j <= 1) i = 1;

        long k = (i * 2 + 1);
        long l = k * k;
        int i1 = (l > 2147483647L) ? Integer.MAX_VALUE : (int) l;
        int j1 = (i1 <= 16) ? (i1 - 1) : 17;
        int k1 = (new Random()).nextInt(i1);

        for (int l1 = 0; l1 < i1; l1++) {
            int i2 = (k1 + j1 * l1) % i1;
            int j2 = i2 % (i * 2 + 1);
            int k2 = i2 / (i * 2 + 1);

            BlockPos pos = new BlockPos(blockpos.getX() + j2 - i, 0, blockpos.getZ() + k2 - i);
            OptionalInt height = getSpawnHeight(world, pos.getX(), pos.getZ());

            if (height.isPresent()) {
                player.teleportTo(world, pos.getX(), height.getAsInt(), pos.getZ(), 0.0F, 0.0F);
                player.teleportTo(pos.getX(), height.getAsInt(), pos.getZ());
                if (world.noCollision((Entity) player))
                    break;
            }
        }
    }

    public static OptionalInt getSpawnHeight(ServerWorld world, int posX, int posZ) {
        BlockPos.Mutable pos = new BlockPos.Mutable(posX, 0, posZ);
        Chunk chunk = world.getChunk(posX >> 4, posZ >> 4);


        int top = world.dimensionType().hasCeiling() ? world.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, posX & 0xF, posZ & 0xF);

        if (top >= 0) {
            int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, posX & 0xF, posZ & 0xF);

            if (j > top || j <= chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, posX & 0xF, posZ & 0xF)) {
                for (int k = top + 1; k >= 0; k--) {
                    pos.set(posX, k, posZ);
                    BlockState state = world.getBlockState((BlockPos) pos);

                    if (!state.getFluidState().isEmpty()) {
                        break;
                    }

                    if (state.equals(world.getBiome((BlockPos) pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial())) {
                        return OptionalInt.of(pos.above().getY());
                    }
                }
            }
        }


        return OptionalInt.empty();
    }

    public static boolean matchesDimension(VaultRaid vault, World world) {
        return vault.getProperties().getBase(VaultRaid.DIMENSION).filter(key -> (key == world.dimension())).isPresent();
    }

    public static boolean inVault(VaultRaid vault, Entity entity) {
        return inVault(vault, entity.getCommandSenderWorld(), entity.blockPosition());
    }

    public static boolean inVault(VaultRaid vault, World world, BlockPos pos) {
        if (vault == null) return false;
        Optional<RegistryKey<World>> dimension = vault.getProperties().getBase(VaultRaid.DIMENSION);
        if (!dimension.isPresent() || world.dimension() != dimension.get()) return false;
        return ((Boolean) vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).map(box -> Boolean.valueOf(box.isInside((Vector3i) pos))).orElse(Boolean.valueOf(false))).booleanValue();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\VaultUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */