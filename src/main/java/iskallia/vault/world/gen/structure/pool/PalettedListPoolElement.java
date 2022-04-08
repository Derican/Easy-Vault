package iskallia.vault.world.gen.structure.pool;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
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
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App) JigsawPiece.CODEC.listOf().fieldOf("elements").forGetter(()), (App) projection(), (App) processors()).apply((Applicative) instance, PalettedListPoolElement::new));
    }


    public static final Codec<PalettedListPoolElement> CODEC;

    private final List<JigsawPiece> elements;
    protected final List<Supplier<StructureProcessorList>> processors;

    public PalettedListPoolElement(List<JigsawPiece> elements, JigsawPattern.PlacementBehaviour behaviour, List<Supplier<StructureProcessorList>> processors) {
        super(behaviour);

        if (elements.isEmpty() &&
                FMLEnvironment.production) {
            throw new IllegalArgumentException("Elements are empty");
        }


        this.elements = elements;
        this.processors = processors;
        setProjectionOnEachElement(behaviour);
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


    public List<Template.BlockInfo> getShuffledJigsawBlocks(TemplateManager templateManager, BlockPos pos, Rotation rotation, Random random) {
        return this.elements.isEmpty() ? new ArrayList<>() : ((JigsawPiece) this.elements.get(0)).getShuffledJigsawBlocks(templateManager, pos, rotation, random);
    }


    public MutableBoundingBox getBoundingBox(TemplateManager templateManager, BlockPos pos, Rotation rotation) {
        MutableBoundingBox mutableboundingbox = MutableBoundingBox.getUnknownBox();
        this.elements.stream().map(piece -> piece.getBoundingBox(templateManager, pos, rotation)).forEach(mutableboundingbox::expand);
        return mutableboundingbox;
    }


    public boolean place(TemplateManager templateManager, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, BlockPos pos1, BlockPos pos2, Rotation rotation, MutableBoundingBox box, Random random, boolean keepJigsaws) {
        return generate(templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws, 18);
    }


    public boolean generate(TemplateManager templateManager, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, BlockPos pos1, BlockPos pos2, Rotation rotation, MutableBoundingBox box, Random random, boolean keepJigsaws, int updateFlags) {
        Supplier<StructureProcessorList> extra = getRandomProcessor(world, pos1);

        for (JigsawPiece piece : this.elements) {
            if (piece instanceof PalettedSinglePoolElement) {
                if (!((PalettedSinglePoolElement) piece).generate(extra, templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws, updateFlags)) {
                    return false;
                }
                continue;
            }
            if (!piece.place(templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws)) {
                return false;
            }
        }


        return true;
    }

    @Nullable
    public Supplier<StructureProcessorList> getRandomProcessor(ISeedReader world, BlockPos pos) {
        if (this.processors.isEmpty()) {
            return null;
        }
        SharedSeedRandom seedRand = new SharedSeedRandom();
        seedRand.setLargeFeatureSeed(world.getSeed(), pos.getX(), pos.getZ());
        return this.processors.get(seedRand.nextInt(this.processors.size()));
    }


    public IJigsawDeserializer<?> getType() {
        return ModStructures.PoolElements.PALETTED_LIST_POOL_ELEMENT;
    }


    public JigsawPiece setProjection(JigsawPattern.PlacementBehaviour placementBehaviour) {
        super.setProjection(placementBehaviour);
        setProjectionOnEachElement(placementBehaviour);
        return this;
    }


    public String toString() {
        return "PalettedList[" + (String) this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }

    private void setProjectionOnEachElement(JigsawPattern.PlacementBehaviour p_214864_1_) {
        this.elements.forEach(p_214863_1_ -> p_214863_1_.setProjection(p_214864_1_));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\pool\PalettedListPoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */