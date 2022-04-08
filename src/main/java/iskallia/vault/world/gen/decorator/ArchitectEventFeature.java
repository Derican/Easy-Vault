package iskallia.vault.world.gen.decorator;

import iskallia.vault.world.gen.structure.ArchitectEventStructure;
import iskallia.vault.world.gen.structure.JigsawGenerator;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class ArchitectEventFeature extends StructureFeature<ArchitectEventStructure.Config, Structure<ArchitectEventStructure.Config>> {
    public ArchitectEventFeature(Structure<ArchitectEventStructure.Config> structure, ArchitectEventStructure.Config config) {
        super(structure, (IFeatureConfig) config);
    }


    public StructureStart<?> generate(JigsawGenerator jigsaw, DynamicRegistries registry, ChunkGenerator gen, TemplateManager manager, int references, long worldSeed) {
        ArchitectEventStructure.Start start = (ArchitectEventStructure.Start) this.feature.getStartFactory().create(this.feature, jigsaw
                .getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4, MutableBoundingBox.getUnknownBox(), references, worldSeed);
        start.generate(jigsaw, registry, gen, manager);

        if (start.isValid()) {
            return (StructureStart<?>) start;
        }

        return StructureStart.INVALID_START;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\decorator\ArchitectEventFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */