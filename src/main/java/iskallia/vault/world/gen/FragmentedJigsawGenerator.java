package iskallia.vault.world.gen;

import iskallia.vault.world.gen.structure.JigsawPieceResolver;
import iskallia.vault.world.vault.gen.layout.JigsawPoolProvider;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;

public class FragmentedJigsawGenerator implements VaultJigsawGenerator {
    public static final int START_X_Z = 21;
    public static final int ROOM_X_Z = 47;
    public static final int ROOM_Y_OFFSET = -13;
    public static final int TUNNEL_Z = 48;
    public static final int TUNNEL_X = 11;
    public static final int TUNNEL_Y_OFFSET = 6;
    private final MutableBoundingBox structureBoundingBox;
    private final BlockPos startPos;
    private final boolean generateTreasureRooms;
    private final JigsawPoolProvider jigsawPoolProvider;
    private final VaultRoomLayoutGenerator.Layout generatedLayout;
    private List<StructurePiece> pieces;

    public FragmentedJigsawGenerator(final MutableBoundingBox structureBoundingBox, final BlockPos startPos, final boolean generateTreasureRooms, final JigsawPoolProvider jigsawPoolProvider, final VaultRoomLayoutGenerator.Layout generatedLayout) {
        this.pieces = new ArrayList<StructurePiece>();
        this.structureBoundingBox = structureBoundingBox;
        this.startPos = startPos;
        this.generateTreasureRooms = generateTreasureRooms;
        this.jigsawPoolProvider = jigsawPoolProvider;
        this.generatedLayout = generatedLayout;
    }

    @Override
    public MutableBoundingBox getStructureBox() {
        return this.structureBoundingBox;
    }

    @Override
    public BlockPos getStartPos() {
        return this.startPos;
    }

    public boolean generatesTreasureRooms() {
        return this.generateTreasureRooms;
    }

    @Override
    public int getSize() {
        return 0;
    }

    public JigsawPoolProvider getJigsawPoolProvider() {
        return this.jigsawPoolProvider;
    }

    @Override
    public List<StructurePiece> getGeneratedPieces() {
        return Collections.unmodifiableList(this.pieces);
    }

    public boolean removePiece(final StructurePiece piece) {
        return this.pieces.remove(piece);
    }

    @Override
    public void generate(final DynamicRegistries registries, final VillageConfig config, final JigsawManager.IPieceFactory pieceFactory, final ChunkGenerator gen, final TemplateManager manager, final List<StructurePiece> pieceList, final Random random, final boolean flag1, final boolean generateOnSurface) {
        final BlockPos startPos = this.getStartPos();
        final Registry<JigsawPattern> registry = registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        final Rotation startRotation = Rotation.getRandom(random);
        final Rotation vaultGenerationRotation = startRotation.getRotated(Rotation.CLOCKWISE_90);
        final JigsawPattern starts = this.getJigsawPoolProvider().getStartRoomPool(registry);
        final JigsawPiece startPiece = starts.getRandomTemplate(random);
        final MutableBoundingBox startBoundingBox = startPiece.getBoundingBox(manager, startPos, startRotation);
        final AbstractVillagePiece start = pieceFactory.create(manager, startPiece, startPos, startPiece.getGroundLevelDelta(), startRotation, startBoundingBox);
        pieceList.add(start);
        final BlockPos centerRoomPos = startPos.offset(new BlockPos(10, 0, 10).rotate(startRotation)).relative(vaultGenerationRotation.rotate(Direction.EAST), 11).relative(vaultGenerationRotation.rotate(Direction.EAST), 23);
        final List<StructurePiece> synchronizedPieces = Collections.synchronizedList(pieceList);
        final JigsawPattern rooms = this.getJigsawPoolProvider().getRoomPool(registry);
        this.generatedLayout.getRooms().parallelStream().forEach(room -> {
            final JigsawPiece roomPiece = room.getRandomPiece(rooms, random);
            final Rotation roomRotation = Rotation.getRandom(random);
            final BlockPos roomPos = centerRoomPos.offset(room.getAbsoluteOffset(vaultGenerationRotation, roomRotation));
            final JigsawPieceResolver resolver2 = JigsawPieceResolver.newResolver(roomPiece, roomPos).withRotation(roomRotation).andJigsawFilter(key -> !key.getPath().contains("tunnels"));
            if (room.getRoomOffset().equals(BlockPos.ZERO)) {
                resolver2.addStructureBox(AxisAlignedBB.of(startBoundingBox).inflate(1.0, 3.0, 1.0));
            }
            if (!this.generatesTreasureRooms() || !room.canGenerateTreasureRooms()) {
                resolver2.andJigsawFilter(key -> !key.getPath().contains("treasure_rooms"));
            }
            synchronizedPieces.addAll(resolver2.resolveJigsawPieces(manager, registry));
        });
        final JigsawPattern tunnels = this.getJigsawPoolProvider().getTunnelPool(registry);
        for (final VaultRoomLayoutGenerator.Tunnel tunnel : this.generatedLayout.getTunnels()) {
            final JigsawPiece tunnelPiece = tunnel.getRandomPiece(tunnels, random);
            final Rotation tunnelRotation = tunnel.getRandomConnectingRotation(random).getRotated(vaultGenerationRotation);
            final BlockPos tunnelPos = centerRoomPos.offset(tunnel.getAbsoluteOffset(vaultGenerationRotation, tunnelRotation));
            final JigsawPieceResolver resolver = JigsawPieceResolver.newResolver(tunnelPiece, tunnelPos).withRotation(tunnelRotation).andJigsawFilter(key -> !key.getPath().contains("rooms"));
            pieceList.addAll(resolver.resolveJigsawPieces(manager, registry));
        }
        this.pieces = pieceList;
    }
}
