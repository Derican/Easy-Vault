package iskallia.vault.world.gen.structure;

import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JigsawPiecePlacer {
    private static final Random rand;
    public static int generationPlacementCount;
    private final ServerWorld world;
    private final JigsawPieceResolver resolver;
    private final TemplateManager templateManager;
    private final StructureManager structureManager;
    private final ChunkGenerator chunkGenerator;
    private final Registry<JigsawPattern> jigsawPatternRegistry;

    private JigsawPiecePlacer(final JigsawPiece piece, final ServerWorld world, final BlockPos pos) {
        this.world = world;
        this.resolver = JigsawPieceResolver.newResolver(piece, pos);
        this.templateManager = world.getStructureManager();
        this.structureManager = world.structureFeatureManager();
        this.chunkGenerator = world.getChunkSource().generator;
        this.jigsawPatternRegistry = (Registry<JigsawPattern>) world.getServer().registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
    }

    public static JigsawPiecePlacer newPlacer(final JigsawPiece piece, final ServerWorld world, final BlockPos pos) {
        return new JigsawPiecePlacer(piece, world, pos);
    }

    public JigsawPiecePlacer withRotation(final Rotation rotation) {
        this.resolver.withRotation(rotation);
        return this;
    }

    public JigsawPiecePlacer andJigsawFilter(final Predicate<ResourceLocation> filter) {
        this.resolver.andJigsawFilter(filter);
        return this;
    }

    public List<VaultPiece> placeJigsaw() {
        final List<AbstractVillagePiece> resolvedPieces = this.resolver.resolveJigsawPieces(this.templateManager, this.jigsawPatternRegistry);
        resolvedPieces.forEach(this::placeStructurePiece);
        return resolvedPieces.stream().flatMap(piece -> VaultPiece.of((StructurePiece) piece).stream()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void placeStructurePiece(final AbstractVillagePiece structurePiece) {
        final MutableBoundingBox structureBox = structurePiece.getBoundingBox();
        final Vector3i center = structureBox.getCenter();
        final BlockPos generationPos = new BlockPos(center.getX(), structureBox.y0, center.getZ());
        final JigsawPiece toGenerate = structurePiece.getElement();
        try {
            ++JigsawPiecePlacer.generationPlacementCount;
            this.placeJigsawPiece(toGenerate, structurePiece.getPosition(), generationPos, structurePiece.getRotation(), structureBox);
        } finally {
            --JigsawPiecePlacer.generationPlacementCount;
        }
    }

    private void placeJigsawPiece(final JigsawPiece jigsawPiece, final BlockPos seedPos, final BlockPos generationPos, final Rotation pieceRotation, final MutableBoundingBox pieceBox) {
        if (jigsawPiece instanceof PalettedListPoolElement) {
            ((PalettedListPoolElement) jigsawPiece).generate(this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, JigsawPiecePlacer.rand, false, 18);
        } else if (jigsawPiece instanceof PalettedSinglePoolElement) {
            ((PalettedSinglePoolElement) jigsawPiece).generate(null, this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, JigsawPiecePlacer.rand, false, 18);
        } else {
            jigsawPiece.place(this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, JigsawPiecePlacer.rand, false);
        }
    }

    public static boolean isPlacingRoom() {
        return JigsawPiecePlacer.generationPlacementCount > 0;
    }

    static {
        rand = new Random();
        JigsawPiecePlacer.generationPlacementCount = 0;
    }
}
