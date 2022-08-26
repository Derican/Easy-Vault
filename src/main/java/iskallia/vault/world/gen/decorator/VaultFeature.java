package iskallia.vault.world.gen.decorator;

import iskallia.vault.world.gen.VaultJigsawGenerator;
import iskallia.vault.world.gen.structure.VaultStructure;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class VaultFeature extends StructureFeature<VaultStructure.Config, Structure<VaultStructure.Config>> {
    public VaultFeature(final Structure<VaultStructure.Config> p_i231937_1_, final VaultStructure.Config p_i231937_2_) {
        super(p_i231937_1_, p_i231937_2_);
    }

    public StructureStart<?> generate(final VaultJigsawGenerator jigsaw, final DynamicRegistries registry, final ChunkGenerator gen, final TemplateManager manager, final int references, final long worldSeed) {
        final VaultStructure.Start start = (VaultStructure.Start) this.feature.getStartFactory().create(this.feature, jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4, MutableBoundingBox.getUnknownBox(), references, worldSeed);
        start.generate(jigsaw, registry, gen, manager);
        if (start.isValid()) {
            return start;
        }
        return StructureStart.INVALID_START;
    }
}
