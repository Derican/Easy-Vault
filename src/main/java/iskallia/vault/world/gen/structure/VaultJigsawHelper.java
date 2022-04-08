package iskallia.vault.world.gen.structure;

import iskallia.vault.Vault;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.gen.VaultRoomLevelRestrictions;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class VaultJigsawHelper {
    public static final int ROOM_WIDTH_HEIGHT = 47;
    public static final int TUNNEL_LENGTH = 48;
    private static final Random rand = new Random();
    private static final Predicate<ResourceLocation> TUNNEL_FILTER;
    private static final Predicate<ResourceLocation> ROOM_FILTER;

    static {
        TUNNEL_FILTER = (key -> {
            String path = key.getPath();
            return (!path.contains("treasure_rooms") && !path.contains("rooms"));
        });
        ROOM_FILTER = (key -> {
            String path = key.getPath();
            return (!path.contains("treasure_rooms") && !path.contains("tunnels"));
        });
    }

    public static List<VaultPiece> expandVault(VaultRaid vault, ServerWorld sWorld, VaultRoom fromRoom, Direction targetDir) {
        return expandVault(vault, sWorld, fromRoom, targetDir, null);
    }

    public static List<VaultPiece> expandVault(VaultRaid vault, ServerWorld sWorld, VaultRoom fromRoom, Direction targetDir, @Nullable JigsawPiece roomToGenerate) {
        if (targetDir.getAxis() == Direction.Axis.Y) {
            return Collections.emptyList();
        }

        BlockPos side = fromRoom.getTunnelConnectorPos(targetDir);
        BlockPos.Mutable mutableGenPos = side.mutable();

        VaultGenerator generator = vault.getGenerator();
        int vaultLevel = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        JigsawPiece roomPiece = (roomToGenerate == null) ? getRandomVaultRoom(vaultLevel) : roomToGenerate;

        List<VaultPiece> pieces = new ArrayList<>();
        pieces.addAll(placeRandomTunnel(sWorld, mutableGenPos, targetDir));
        pieces.addAll(placeRandomRoom(sWorld, mutableGenPos, targetDir, roomPiece));
        generator.addPieces(pieces);
        return pieces;
    }

    public static boolean canExpand(VaultRaid vault, VaultRoom fromRoom, Direction targetDir) {
        BlockPos roomCenter = new BlockPos(fromRoom.getCenter());

        if (!vault.getGenerator().getPiecesAt(roomCenter.relative(targetDir, 28)).isEmpty()) {
            return false;
        }
        BlockPos nextRoom = roomCenter.relative(targetDir, 95);
        BlockPos nextRoomEdge = nextRoom.relative(targetDir, 24);
        if (((Boolean) vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).map(box -> Boolean.valueOf(!box.isInside((Vector3i) nextRoomEdge))).orElse(Boolean.valueOf(true))).booleanValue()) {
            return false;
        }
        return vault.getGenerator().getPiecesAt(nextRoom).isEmpty();
    }


    private static List<VaultPiece> placeRandomRoom(ServerWorld sWorld, BlockPos.Mutable generationPos, Direction toCenter, JigsawPiece roomToGenerate) {
        int jigsawGroundOffset = 22;

        BlockPos at = generationPos.immutable();

        Rotation roomRotation = Rotation.getRandom(rand);
        TemplateManager tplMgr = sWorld.getStructureManager();
        MutableBoundingBox jigsawBox = roomToGenerate.getBoundingBox(tplMgr, at, roomRotation);

        Vector3i size = jigsawBox.getLength();
        BlockPos directionOffset = new BlockPos(toCenter.getStepX() * size.getX() / 2, -jigsawGroundOffset + size.getY() / 2, toCenter.getStepZ() * size.getZ() / 2);
        BlockPos genPos = at.offset((Vector3i) directionOffset);

        Vector3i center = jigsawBox.getCenter();
        genPos = genPos.offset((Vector3i) at.subtract(center));


        List<VaultPiece> vaultPieces = JigsawPiecePlacer.newPlacer(roomToGenerate, sWorld, genPos).withRotation(roomRotation).andJigsawFilter(ROOM_FILTER).placeJigsaw();

        generationPos.move(toCenter, 23);
        return vaultPieces;
    }


    private static List<VaultPiece> placeRandomTunnel(ServerWorld sWorld, BlockPos.Mutable generationPos, Direction targetDir) {
        int jigsawGroundOffset = 2;

        JigsawPiece tunnel = getRandomVaultTunnel();
        BlockPos at = generationPos.immutable();

        Rotation tunnelRotation = getTunnelRotation(targetDir);
        Direction shift = targetDir.getClockWise();
        TemplateManager tplMgr = sWorld.getStructureManager();
        MutableBoundingBox jigsawBox = tunnel.getBoundingBox(tplMgr, at, tunnelRotation);
        Vector3i size = jigsawBox.getLength();
        BlockPos directionOffset = new BlockPos(targetDir.getStepX() * size.getX() / 2, 0, targetDir.getStepZ() * size.getZ() / 2);
        directionOffset = directionOffset.offset(-(shift.getStepX() * size.getX()) / 2, -size.getY() / 2 + jigsawGroundOffset, -(shift.getStepZ() * size.getZ()) / 2);
        BlockPos genPos = at.offset((Vector3i) directionOffset);


        List<VaultPiece> vaultPieces = JigsawPiecePlacer.newPlacer(tunnel, sWorld, genPos).withRotation(tunnelRotation).andJigsawFilter(TUNNEL_FILTER).placeJigsaw();

        generationPos.move(targetDir.getStepX() * size.getX() / 2, 0, targetDir.getStepZ() * size.getZ() / 2)
                .move(targetDir);
        return vaultPieces;
    }

    private static Rotation getTunnelRotation(Direction direction) {
        switch (direction) {
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            case EAST:
                return Rotation.CLOCKWISE_90;
        }

        return Rotation.NONE;
    }


    public static void preloadVaultRooms(FMLServerStartedEvent event) {
        Random rand = new Random();
        TemplateManager mgr = event.getServer().getStructureManager();

        getVaultRoomList(2147483647).forEach((piece, weight) -> {
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
    public static JigsawPiece getRandomVaultRoom(int vaultLevel) {
        WeightedList<JigsawPiece> rooms = getRoomList(Vault.id("vault/rooms"));
        return (JigsawPiece) rooms.copyFiltered(piece -> VaultRoomLevelRestrictions.canGenerate(piece, vaultLevel))
                .getOptionalRandom(rand).orElseThrow(RuntimeException::new);
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
    public static WeightedList<JigsawPiece> getVaultRoomList(int vaultLevel) {
        WeightedList<JigsawPiece> rooms = getRoomList(Vault.id("vault/rooms"));
        return rooms.copyFiltered(piece -> VaultRoomLevelRestrictions.canGenerate(piece, vaultLevel));
    }

    @Nonnull
    private static WeightedList<JigsawPiece> getRoomList(ResourceLocation key) {
        JigsawPattern roomPool = getPool(key);
        WeightedList<JigsawPiece> pool = new WeightedList();
        roomPool.rawTemplates.forEach(weightedPiece -> pool.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));


        return pool;
    }

    @Nonnull
    private static JigsawPattern getPool(ResourceLocation key) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        MutableRegistry<JigsawPattern> jigsawRegistry = srv.registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        return (JigsawPattern) jigsawRegistry.getOptional(key).orElseThrow(RuntimeException::new);
    }

    @Nonnull
    private static JigsawPiece getRandomPiece(ResourceLocation key) {
        return (JigsawPiece) getRoomList(key).getOptionalRandom(rand).orElseThrow(RuntimeException::new);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\VaultJigsawHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */