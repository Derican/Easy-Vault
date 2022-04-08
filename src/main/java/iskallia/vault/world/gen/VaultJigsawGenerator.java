package iskallia.vault.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public interface VaultJigsawGenerator {
    BlockPos getStartPos();

    MutableBoundingBox getStructureBox();

    int getSize();

    List<StructurePiece> getGeneratedPieces();

    void generate(DynamicRegistries paramDynamicRegistries, VillageConfig paramVillageConfig, JigsawManager.IPieceFactory paramIPieceFactory, ChunkGenerator paramChunkGenerator, TemplateManager paramTemplateManager, List<StructurePiece> paramList, Random paramRandom, boolean paramBoolean1, boolean paramBoolean2);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\VaultJigsawGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */