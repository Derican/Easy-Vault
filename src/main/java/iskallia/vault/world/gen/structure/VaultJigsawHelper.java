package iskallia.vault.world.gen.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import iskallia.vault.util.data.WeightedList;

import java.util.function.Supplier;

import iskallia.vault.world.vault.gen.VaultRoomLevelRestrictions;

import javax.annotation.Nonnull;

import iskallia.vault.Vault;

import java.util.Iterator;

import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.util.Rotation;

import java.util.function.Consumer;

import net.minecraft.util.math.vector.Vector3i;
import iskallia.vault.world.vault.gen.VaultGenerator;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nullable;

import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import iskallia.vault.world.vault.gen.piece.VaultPiece;

import java.util.List;

import net.minecraft.util.Direction;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;
import java.util.Random;

public class VaultJigsawHelper {
    private static final Random rand;
    public static final int ROOM_WIDTH_HEIGHT = 47;
    public static final int TUNNEL_LENGTH = 48;
    private static final Predicate<ResourceLocation> TUNNEL_FILTER;
    private static final Predicate<ResourceLocation> ROOM_FILTER;

    public static List<VaultPiece> expandVault(final VaultRaid vault, final ServerWorld sWorld, final VaultRoom fromRoom, final Direction targetDir) {
        return expandVault(vault, sWorld, fromRoom, targetDir, null, null);
    }

    public static List<VaultPiece> expandVault(final VaultRaid vault, final ServerWorld sWorld, final VaultRoom fromRoom, final Direction targetDir, @Nullable final JigsawPiece roomToGenerate, @Nullable final JigsawPiece tunnelToGenerate) {
        if (targetDir.getAxis() == Direction.Axis.Y) {
            return Collections.emptyList();
        }
        final BlockPos side = fromRoom.getTunnelConnectorPos(targetDir);
        final BlockPos.Mutable mutableGenPos = side.mutable();
        final VaultGenerator generator = vault.getGenerator();
        final int vaultLevel = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final JigsawPiece roomPiece = (roomToGenerate == null) ? getRandomVaultRoom(vaultLevel) : roomToGenerate;
        final JigsawPiece tunnelPiece = (tunnelToGenerate == null) ? getRandomVaultTunnel() : tunnelToGenerate;
        final List<VaultPiece> pieces = new ArrayList<VaultPiece>();
        pieces.addAll(placeRandomTunnel(sWorld, mutableGenPos, targetDir, tunnelPiece));
        pieces.addAll(placeRandomRoom(sWorld, mutableGenPos, targetDir, roomPiece));
        generator.addPieces(pieces);
        return pieces;
    }

    public static List<VaultPiece> expandTenosFinalVault(final VaultRaid vault, final ServerWorld sWorld, final VaultRoom fromRoom, final Direction targetDir, @Nullable final JigsawPiece roomToGenerate, @Nullable final JigsawPiece tunnelToGenerate, final boolean generateObelisk) {
        if (targetDir.getAxis() == Direction.Axis.Y) {
            return Collections.emptyList();
        }
        final BlockPos side = fromRoom.getTunnelConnectorPos(targetDir);
        final BlockPos.Mutable mutableGenPos = side.mutable();
        final VaultGenerator generator = vault.getGenerator();
        final int vaultLevel = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final JigsawPiece roomPiece = (roomToGenerate == null) ? getRandomVaultRoom(vaultLevel) : roomToGenerate;
        final JigsawPiece tunnelPiece = (tunnelToGenerate == null) ? getRandomVaultTunnel() : tunnelToGenerate;
        final List<VaultPiece> pieces = new ArrayList<VaultPiece>();
        pieces.addAll(placeRandomTunnel(sWorld, mutableGenPos, targetDir, tunnelPiece));
        pieces.addAll(placeRandomRoom(sWorld, mutableGenPos, targetDir, roomPiece, roomPlacer -> {
            if (!generateObelisk) {
                roomPlacer.andJigsawFilter(key -> !key.getPath().contains("obelisk"));
            }
            return;
        }));
        generator.addPieces(pieces);
        return pieces;
    }

    public static boolean canExpand(final VaultRaid vault, final VaultRoom fromRoom, final Direction targetDir) {
        final BlockPos roomCenter = new BlockPos(fromRoom.getCenter());
        if (!vault.getGenerator().getPiecesAt(roomCenter.relative(targetDir, 28)).isEmpty()) {
            return false;
        }
        final BlockPos nextRoom = roomCenter.relative(targetDir, 95);
        final BlockPos nextRoomEdge = nextRoom.relative(targetDir, 24);
        return !vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).map(box -> !box.isInside((Vector3i) nextRoomEdge)).orElse(true) && vault.getGenerator().getPiecesAt(nextRoom).isEmpty();
    }

    private static List<VaultPiece> placeRandomRoom(final ServerWorld sWorld, final BlockPos.Mutable generationPos, final Direction toCenter, final JigsawPiece roomToGenerate) {
        return placeRandomRoom(sWorld, generationPos, toCenter, roomToGenerate, placer -> {
        });
    }

    private static List<VaultPiece> placeRandomRoom(final ServerWorld sWorld, final BlockPos.Mutable generationPos, final Direction toCenter, final JigsawPiece roomToGenerate, final Consumer<JigsawPiecePlacer> filterAdditions) {
        final int jigsawGroundOffset = 22;
        final BlockPos at = generationPos.immutable();
        final Rotation roomRotation = Rotation.getRandom(VaultJigsawHelper.rand);
        final TemplateManager tplMgr = sWorld.getStructureManager();
        final MutableBoundingBox jigsawBox = roomToGenerate.getBoundingBox(tplMgr, at, roomRotation);
        final Vector3i size = jigsawBox.getLength();
        final BlockPos directionOffset = new BlockPos(toCenter.getStepX() * size.getX() / 2, -jigsawGroundOffset + size.getY() / 2, toCenter.getStepZ() * size.getZ() / 2);
        BlockPos genPos = at.offset((Vector3i) directionOffset);
        final Vector3i center = jigsawBox.getCenter();
        genPos = genPos.offset((Vector3i) at.subtract(center));
        final JigsawPiecePlacer placer = JigsawPiecePlacer.newPlacer(roomToGenerate, sWorld, genPos).withRotation(roomRotation).andJigsawFilter(VaultJigsawHelper.ROOM_FILTER);
        filterAdditions.accept(placer);
        final List<VaultPiece> vaultPieces = placer.placeJigsaw();
        generationPos.move(toCenter, 23);
        return vaultPieces;
    }

    private static List<VaultPiece> placeRandomTunnel(final ServerWorld sWorld, final BlockPos.Mutable generationPos, final Direction targetDir, final JigsawPiece roomToGenerate) {
        final int jigsawGroundOffset = 2;
        final BlockPos at = generationPos.immutable();
        final Rotation tunnelRotation = getTunnelRotation(targetDir);
        final Direction shift = targetDir.getClockWise();
        final TemplateManager tplMgr = sWorld.getStructureManager();
        final MutableBoundingBox jigsawBox = roomToGenerate.getBoundingBox(tplMgr, at, tunnelRotation);
        final Vector3i size = jigsawBox.getLength();
        BlockPos directionOffset = new BlockPos(targetDir.getStepX() * size.getX() / 2, 0, targetDir.getStepZ() * size.getZ() / 2);
        directionOffset = directionOffset.offset(-(shift.getStepX() * size.getX()) / 2, -size.getY() / 2 + jigsawGroundOffset, -(shift.getStepZ() * size.getZ()) / 2);
        final BlockPos genPos = at.offset((Vector3i) directionOffset);
        final List<VaultPiece> vaultPieces = JigsawPiecePlacer.newPlacer(roomToGenerate, sWorld, genPos).withRotation(tunnelRotation).andJigsawFilter(VaultJigsawHelper.TUNNEL_FILTER).placeJigsaw();
        generationPos.move(targetDir.getStepX() * size.getX() / 2, 0, targetDir.getStepZ() * size.getZ() / 2).move(targetDir);
        return vaultPieces;
    }

    private static Rotation getTunnelRotation(final Direction direction) {
        switch (direction) {
            case SOUTH: {
                return Rotation.CLOCKWISE_180;
            }
            case WEST: {
                return Rotation.COUNTERCLOCKWISE_90;
            }
            case EAST: {
                return Rotation.CLOCKWISE_90;
            }
            default: {
                return Rotation.NONE;
            }
        }
    }

    public static void preloadVaultRooms(final FMLServerStartedEvent event) {
        final Random rand = new Random();
        final TemplateManager mgr = event.getServer().getStructureManager();
        getVaultRoomList(Integer.MAX_VALUE).forEach((piece, weight) -> {
            if (piece instanceof PalettedListPoolElement) {

                for (JigsawPiece listPiece : ((PalettedListPoolElement) piece).getElements()) {
                    listPiece.getShuffledJigsawBlocks(mgr, BlockPos.ZERO, Rotation.getRandom(rand), rand);
                }
            } else {
                piece.getShuffledJigsawBlocks(mgr, BlockPos.ZERO, Rotation.getRandom(rand), rand);
            }
        });
    }

    @Nonnull
    public static JigsawPiece getRandomVaultTunnel() {
        return getRandomPiece(Vault.id("vault/tunnels"));
    }

    @Nonnull
    public static JigsawPiece getRandomVaultRoom(final int vaultLevel) {
        final WeightedList<JigsawPiece> rooms = getRoomList(Vault.id("vault/rooms"));
        return rooms.copyFiltered(piece -> VaultRoomLevelRestrictions.canGenerate(piece, vaultLevel)).getOptionalRandom(VaultJigsawHelper.rand).orElseThrow(RuntimeException::new);
    }

    @Nonnull
    public static JigsawPiece getArchitectRoom() {
        return getRandomPiece(Vault.id("architect_event/rooms"));
    }

    @Nonnull
    public static JigsawPiece getRaidChallengeRoom() {
        return getRandomPiece(Vault.id("raid/rooms"));
    }

    @Nonnull
    public static WeightedList<JigsawPiece> getVaultRoomList(final int vaultLevel) {
        final WeightedList<JigsawPiece> rooms = getRoomList(Vault.id("vault/rooms"));
        return rooms.copyFiltered(piece -> VaultRoomLevelRestrictions.canGenerate(piece, vaultLevel));
    }

    @Nonnull
    private static WeightedList<JigsawPiece> getRoomList(final ResourceLocation key) {
        final JigsawPattern roomPool = getPool(key);
        final WeightedList<JigsawPiece> pool = new WeightedList<JigsawPiece>();
//        roomPool.rawTemplates.forEach(weightedPiece -> pool.add(weightedPiece.getFirst(), (int) weightedPiece.getSecond()));
//        TODO: check if the following overwrite is runnable
        roomPool.getShuffledTemplates(rand).forEach(pool::addOne);
        return pool;
    }

    @Nonnull
    private static WeightedList<JigsawPiece> getRoomList(final ResourceLocation key, final int level) {
        final JigsawPattern roomPool = getPool(key);
        final WeightedList<JigsawPiece> pool = new WeightedList<JigsawPiece>();
//        roomPool.rawTemplates.forEach(weightedPiece -> pool.add(weightedPiece.getFirst(), (int) weightedPiece.getSecond()));
//        TODO: check if the following overwrite is runnable
        roomPool.getShuffledTemplates(rand).forEach(pool::addOne);
        return pool.copyFiltered(piece -> VaultRoomLevelRestrictions.canGenerate(piece, level));
    }

    @Nonnull
    private static JigsawPattern getPool(final ResourceLocation key) {
        final MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        final MutableRegistry<JigsawPattern> jigsawRegistry = (MutableRegistry<JigsawPattern>) srv.registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        return jigsawRegistry.getOptional(key).orElseThrow(RuntimeException::new);
    }

    @Nonnull
    public static JigsawPiece getRandomPiece(final ResourceLocation key) {
        return getRoomList(key).getOptionalRandom(VaultJigsawHelper.rand).orElseThrow(RuntimeException::new);
    }

    @Nonnull
    public static JigsawPiece getRandomPiece(final ResourceLocation key, final int level) {
        return getRoomList(key, level).getOptionalRandom(VaultJigsawHelper.rand).orElseThrow(RuntimeException::new);
    }

    static {
        rand = new Random();
        TUNNEL_FILTER = (key -> {
            final String path = key.getPath();
            return !path.contains("treasure_rooms") && !path.contains("rooms");
        });
        ROOM_FILTER = (key -> {
            final String path2 = key.getPath();
            return !path2.contains("treasure_rooms") && !path2.contains("tunnels");
        });
    }
}
