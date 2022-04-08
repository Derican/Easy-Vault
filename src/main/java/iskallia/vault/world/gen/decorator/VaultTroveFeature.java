package iskallia.vault.world.gen.decorator;

import iskallia.vault.world.gen.structure.JigsawGenerator;
import iskallia.vault.world.gen.structure.VaultTroveStructure;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class VaultTroveFeature extends StructureFeature<VaultTroveStructure.Config, Structure<VaultTroveStructure.Config>> {
    public VaultTroveFeature(Structure<VaultTroveStructure.Config> structure, VaultTroveStructure.Config config) {
        super(structure, (IFeatureConfig) config);
    }


    public StructureStart<?> generate(JigsawGenerator jigsaw, DynamicRegistries registry, ChunkGenerator gen, TemplateManager manager, int references, long worldSeed) {
        VaultTroveStructure.Start start = (VaultTroveStructure.Start) this.feature.getStartFactory().create(this.feature, jigsaw
                .getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4, MutableBoundingBox.getUnknownBox(), references, worldSeed);
        start.generate(jigsaw, registry, gen, manager);

        if (start.isValid()) {
            return (StructureStart<?>) start;
        }

        return StructureStart.INVALID_START;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\decorator\VaultTroveFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */