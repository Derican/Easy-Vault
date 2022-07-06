// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.gen.decorator;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.util.registry.DynamicRegistries;
import iskallia.vault.world.gen.VaultJigsawGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import iskallia.vault.world.gen.structure.FinalVaultBossStructure;
import net.minecraft.world.gen.feature.StructureFeature;

public class FinalVaultBossFeature extends StructureFeature<FinalVaultBossStructure.Config, Structure<FinalVaultBossStructure.Config>>
{
    public FinalVaultBossFeature(final Structure<FinalVaultBossStructure.Config> p_i231937_1_, final FinalVaultBossStructure.Config p_i231937_2_) {
        super(p_i231937_1_, p_i231937_2_);
    }
    
    public StructureStart<?> generate(final VaultJigsawGenerator jigsaw, final DynamicRegistries registry, final ChunkGenerator gen, final TemplateManager manager, final int references, final long worldSeed) {
        final FinalVaultBossStructure.Start start = (FinalVaultBossStructure.Start)this.feature.getStartFactory().create(this.feature, jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4, MutableBoundingBox.getUnknownBox(), references, worldSeed);
        start.generate(jigsaw, registry, gen, manager);
        if (start.isValid()) {
            return (StructureStart<?>)start;
        }
        return (StructureStart<?>)StructureStart.INVALID_START;
    }
}
