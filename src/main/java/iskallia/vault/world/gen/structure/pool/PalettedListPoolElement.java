package iskallia.vault.world.gen.structure.pool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import iskallia.vault.init.ModStructures;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PalettedListPoolElement extends JigsawPiece {
    public static final Codec<PalettedListPoolElement> CODEC;
    private final List<JigsawPiece> elements;
    protected final List<Supplier<StructureProcessorList>> processors;

    public PalettedListPoolElement(final List<JigsawPiece> elements, final JigsawPattern.PlacementBehaviour behaviour, final List<Supplier<StructureProcessorList>> processors) {
        super(behaviour);
        if (elements.isEmpty() && FMLEnvironment.production) {
            throw new IllegalArgumentException("Elements are empty");
        }
        this.elements = elements;
        this.processors = processors;
        this.setProjectionOnEachElement(behaviour);
    }

    public List<JigsawPiece> getElements() {
        return this.elements;
    }

    protected static <E extends JigsawPiece> RecordCodecBuilder<E, JigsawPattern.PlacementBehaviour> projection() {
        return JigsawPattern.PlacementBehaviour.CODEC.fieldOf("projection").forGetter(JigsawPiece::getProjection);
    }

    protected static <E extends PalettedListPoolElement> RecordCodecBuilder<E, List<Supplier<StructureProcessorList>>> processors() {
        return IStructureProcessorType.LIST_CODEC.listOf().fieldOf("processors").forGetter(piece -> piece.processors);
    }

    public List<Template.BlockInfo> getShuffledJigsawBlocks(final TemplateManager templateManager, final BlockPos pos, final Rotation rotation, final Random random) {
        return this.elements.isEmpty() ? new ArrayList<Template.BlockInfo>() : this.elements.get(0).getShuffledJigsawBlocks(templateManager, pos, rotation, random);
    }

    public MutableBoundingBox getBoundingBox(final TemplateManager templateManager, final BlockPos pos, final Rotation rotation) {
        final MutableBoundingBox mutableboundingbox = MutableBoundingBox.getUnknownBox();
        this.elements.stream().map(piece -> piece.getBoundingBox(templateManager, pos, rotation)).forEach(mutableboundingbox::expand);
        return mutableboundingbox;
    }

    public boolean place(final TemplateManager templateManager, final ISeedReader world, final StructureManager structureManager, final ChunkGenerator chunkGen, final BlockPos pos1, final BlockPos pos2, final Rotation rotation, final MutableBoundingBox box, final Random random, final boolean keepJigsaws) {
        return this.generate(templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws, 18);
    }

    public boolean generate(final TemplateManager templateManager, final ISeedReader world, final StructureManager structureManager, final ChunkGenerator chunkGen, final BlockPos pos1, final BlockPos pos2, final Rotation rotation, final MutableBoundingBox box, final Random random, final boolean keepJigsaws, final int updateFlags) {
        final Supplier<StructureProcessorList> extra = this.getRandomProcessor(world, pos1);
        for (final JigsawPiece piece : this.elements) {
            if (piece instanceof PalettedSinglePoolElement) {
                if (!((PalettedSinglePoolElement) piece).generate(extra, templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws, updateFlags)) {
                    return false;
                }
                continue;
            } else {
                if (!piece.place(templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }

    @Nullable
    public Supplier<StructureProcessorList> getRandomProcessor(final ISeedReader world, final BlockPos pos) {
        if (this.processors.isEmpty()) {
            return null;
        }
        final SharedSeedRandom seedRand = new SharedSeedRandom();
        seedRand.setLargeFeatureSeed(world.getSeed(), pos.getX(), pos.getZ());
        return this.processors.get(seedRand.nextInt(this.processors.size()));
    }

    public IJigsawDeserializer<?> getType() {
        return ModStructures.PoolElements.PALETTED_LIST_POOL_ELEMENT;
    }

    public JigsawPiece setProjection(final JigsawPattern.PlacementBehaviour placementBehaviour) {
        super.setProjection(placementBehaviour);
        this.setProjectionOnEachElement(placementBehaviour);
        return this;
    }

    public String toString() {
        return "PalettedList[" + this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }

    private void setProjectionOnEachElement(final JigsawPattern.PlacementBehaviour p_214864_1_) {
        this.elements.forEach(p_214863_1_ -> p_214863_1_.setProjection(p_214864_1_));
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(JigsawPiece.CODEC.listOf().fieldOf("elements").forGetter(piece -> piece.elements), projection(), processors()).apply(instance, PalettedListPoolElement::new));
    }
}
