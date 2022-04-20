package iskallia.vault.world.gen.decorator;

import iskallia.vault.world.gen.structure.JigsawGenerator;
import iskallia.vault.world.gen.structure.RaidChallengeStructure;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class RaidChallengeFeature extends StructureFeature<RaidChallengeStructure.Config, Structure<RaidChallengeStructure.Config>> {
    public RaidChallengeFeature(final Structure<RaidChallengeStructure.Config> structure, final RaidChallengeStructure.Config config) {
        super((Structure) structure, config);
    }

    public StructureStart<?> generate(final JigsawGenerator jigsaw, final DynamicRegistries registry, final ChunkGenerator gen, final TemplateManager manager, final int references, final long worldSeed) {
        final RaidChallengeStructure.Start start = (RaidChallengeStructure.Start) this.feature.getStartFactory().create(this.feature, jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4, MutableBoundingBox.getUnknownBox(), references, worldSeed);
        start.generate(jigsaw, registry, gen, manager);
        if (start.isValid()) {
            return (StructureStart<?>) start;
        }
        return (StructureStart<?>) StructureStart.INVALID_START;
    }
}
