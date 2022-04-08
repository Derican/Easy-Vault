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
    private static final Random rand = new Random();
    public static int generationPlacementCount = 0;

    private final ServerWorld world;

    private final JigsawPieceResolver resolver;
    private final TemplateManager templateManager;
    private final StructureManager structureManager;
    private final ChunkGenerator chunkGenerator;
    private final Registry<JigsawPattern> jigsawPatternRegistry;

    private JigsawPiecePlacer(JigsawPiece piece, ServerWorld world, BlockPos pos) {
        this.world = world;
        this.resolver = JigsawPieceResolver.newResolver(piece, pos);

        this.templateManager = world.getStructureManager();
        this.structureManager = world.structureFeatureManager();
        this.chunkGenerator = (world.getChunkSource()).generator;
        this.jigsawPatternRegistry = (Registry<JigsawPattern>) world.getServer().registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
    }

    public static JigsawPiecePlacer newPlacer(JigsawPiece piece, ServerWorld world, BlockPos pos) {
        return new JigsawPiecePlacer(piece, world, pos);
    }

    public JigsawPiecePlacer withRotation(Rotation rotation) {
        this.resolver.withRotation(rotation);
        return this;
    }

    public JigsawPiecePlacer andJigsawFilter(Predicate<ResourceLocation> filter) {
        this.resolver.andJigsawFilter(filter);
        return this;
    }

    public List<VaultPiece> placeJigsaw() {
        List<AbstractVillagePiece> resolvedPieces = this.resolver.resolveJigsawPieces(this.templateManager, this.jigsawPatternRegistry);

        resolvedPieces.forEach(this::placeStructurePiece);
        return (List<VaultPiece>) resolvedPieces.stream()
                .flatMap(piece -> VaultPiece.of((StructurePiece) piece).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void placeStructurePiece(AbstractVillagePiece structurePiece) {
        MutableBoundingBox structureBox = structurePiece.getBoundingBox();
        Vector3i center = structureBox.getCenter();
        BlockPos generationPos = new BlockPos(center.getX(), structureBox.y0, center.getZ());
        JigsawPiece toGenerate = structurePiece.getElement();
        try {
            generationPlacementCount++;
            placeJigsawPiece(toGenerate, structurePiece.getPosition(), generationPos, structurePiece.getRotation(), structureBox);
        } finally {
            generationPlacementCount--;
        }
    }

    private void placeJigsawPiece(JigsawPiece jigsawPiece, BlockPos seedPos, BlockPos generationPos, Rotation pieceRotation, MutableBoundingBox pieceBox) {
        if (jigsawPiece instanceof PalettedListPoolElement) {
            ((PalettedListPoolElement) jigsawPiece).generate(this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, rand, false, 18);
        } else if (jigsawPiece instanceof PalettedSinglePoolElement) {
            ((PalettedSinglePoolElement) jigsawPiece).generate(null, this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, rand, false, 18);
        } else {

            jigsawPiece.place(this.templateManager, (ISeedReader) this.world, this.structureManager, this.chunkGenerator, seedPos, generationPos, pieceRotation, pieceBox, rand, false);
        }
    }


    public static boolean isPlacingRoom() {
        return (generationPlacementCount > 0);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\JigsawPiecePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */