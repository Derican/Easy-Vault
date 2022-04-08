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
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class FragmentedJigsawGenerator
        implements VaultJigsawGenerator {
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
    private List<StructurePiece> pieces = new ArrayList<>();


    public FragmentedJigsawGenerator(MutableBoundingBox structureBoundingBox, BlockPos startPos, boolean generateTreasureRooms, JigsawPoolProvider jigsawPoolProvider, VaultRoomLayoutGenerator.Layout generatedLayout) {
        this.structureBoundingBox = structureBoundingBox;
        this.startPos = startPos;
        this.generateTreasureRooms = generateTreasureRooms;
        this.jigsawPoolProvider = jigsawPoolProvider;
        this.generatedLayout = generatedLayout;
    }


    public MutableBoundingBox getStructureBox() {
        return this.structureBoundingBox;
    }


    public BlockPos getStartPos() {
        return this.startPos;
    }

    public boolean generatesTreasureRooms() {
        return this.generateTreasureRooms;
    }


    public int getSize() {
        return 0;
    }

    public JigsawPoolProvider getJigsawPoolProvider() {
        return this.jigsawPoolProvider;
    }


    public List<StructurePiece> getGeneratedPieces() {
        return Collections.unmodifiableList(this.pieces);
    }

    public boolean removePiece(StructurePiece piece) {
        return this.pieces.remove(piece);
    }


    public void generate(DynamicRegistries registries, VillageConfig config, JigsawManager.IPieceFactory pieceFactory, ChunkGenerator gen, TemplateManager manager, List<StructurePiece> pieceList, Random random, boolean flag1, boolean generateOnSurface) {
        BlockPos startPos = getStartPos();
        MutableRegistry mutableRegistry = registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);

        Rotation startRotation = Rotation.getRandom(random);
        Rotation vaultGenerationRotation = startRotation.getRotated(Rotation.CLOCKWISE_90);

        JigsawPattern starts = getJigsawPoolProvider().getStartRoomPool((Registry) mutableRegistry);
        JigsawPiece startPiece = starts.getRandomTemplate(random);
        MutableBoundingBox startBoundingBox = startPiece.getBoundingBox(manager, startPos, startRotation);
        AbstractVillagePiece start = pieceFactory.create(manager, startPiece, startPos, startPiece.getGroundLevelDelta(), startRotation, startBoundingBox);
        pieceList.add(start);


        BlockPos centerRoomPos = startPos.offset((Vector3i) (new BlockPos(10, 0, 10)).rotate(startRotation)).relative(vaultGenerationRotation.rotate(Direction.EAST), 11).relative(vaultGenerationRotation.rotate(Direction.EAST), 23);

        List<StructurePiece> synchronizedPieces = Collections.synchronizedList(pieceList);

        JigsawPattern rooms = getJigsawPoolProvider().getRoomPool((Registry) mutableRegistry);
        this.generatedLayout.getRooms().parallelStream().forEach(room -> {
            JigsawPiece roomPiece = room.getRandomPiece(rooms, random);

            Rotation roomRotation = Rotation.getRandom(random);

            BlockPos roomPos = centerRoomPos.offset((Vector3i) room.getAbsoluteOffset(vaultGenerationRotation, roomRotation));

            JigsawPieceResolver resolver = JigsawPieceResolver.newResolver(roomPiece, roomPos).withRotation(roomRotation).andJigsawFilter(());

            if (room.getRoomOffset().equals(BlockPos.ZERO)) {
                resolver.addStructureBox(AxisAlignedBB.of(startBoundingBox).inflate(1.0D, 3.0D, 1.0D));
            }

            if (!generatesTreasureRooms() || !room.canGenerateTreasureRooms()) {
                resolver.andJigsawFilter(());
            }
            synchronizedPieces.addAll(resolver.resolveJigsawPieces(manager, registry));
        });
        JigsawPattern tunnels = getJigsawPoolProvider().getTunnelPool((Registry) mutableRegistry);
        for (VaultRoomLayoutGenerator.Tunnel tunnel : this.generatedLayout.getTunnels()) {
            JigsawPiece tunnelPiece = tunnel.getRandomPiece(tunnels, random);

            Rotation tunnelRotation = tunnel.getRandomConnectingRotation(random).getRotated(vaultGenerationRotation);
            BlockPos tunnelPos = centerRoomPos.offset((Vector3i) tunnel.getAbsoluteOffset(vaultGenerationRotation, tunnelRotation));


            JigsawPieceResolver resolver = JigsawPieceResolver.newResolver(tunnelPiece, tunnelPos).withRotation(tunnelRotation).andJigsawFilter(key -> !key.getPath().contains("rooms"));
            pieceList.addAll(resolver.resolveJigsawPieces(manager, (Registry) mutableRegistry));
        }

        this.pieces = pieceList;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\FragmentedJigsawGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */