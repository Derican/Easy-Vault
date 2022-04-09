// 
// Decompiled by Procyon v0.6.0
// 

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

public interface VaultJigsawGenerator
{
    BlockPos getStartPos();
    
    MutableBoundingBox getStructureBox();
    
    int getSize();
    
    List<StructurePiece> getGeneratedPieces();
    
    void generate(final DynamicRegistries p0, final VillageConfig p1, final JigsawManager.IPieceFactory p2, final ChunkGenerator p3, final TemplateManager p4, final List<StructurePiece> p5, final Random p6, final boolean p7, final boolean p8);
}
