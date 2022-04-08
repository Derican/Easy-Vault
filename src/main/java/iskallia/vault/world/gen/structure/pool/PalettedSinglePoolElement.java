package iskallia.vault.world.gen.structure.pool;

import com.google.common.collect.Lists;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class PalettedSinglePoolElement extends JigsawPiece {
    private static final Codec<Either<ResourceLocation, Template>> TEMPLATE_CODEC = Codec.of(PalettedSinglePoolElement::encodeTemplate, ResourceLocation.CODEC.map(Either::left));
    public static final Codec<PalettedSinglePoolElement> CODEC;
    protected final Either<ResourceLocation, Template> template;
    protected final Supplier<StructureProcessorList> processors;

    static {
        CODEC = RecordCodecBuilder.create(p_236841_0_ -> p_236841_0_.group((App) templateCodec(), (App) processorsCodec(), (App) projectionCodec()).apply((Applicative) p_236841_0_, PalettedSinglePoolElement::new));
    }


    private static <T> DataResult<T> encodeTemplate(Either<ResourceLocation, Template> p_236840_0_, DynamicOps<T> p_236840_1_, T p_236840_2_) {
        Optional<ResourceLocation> optional = p_236840_0_.left();
        return !optional.isPresent() ? DataResult.error("Can not serialize a runtime pool element") : ResourceLocation.CODEC.encode(optional.get(), p_236840_1_, p_236840_2_);
    }

    protected static <E extends PalettedSinglePoolElement> RecordCodecBuilder<E, Supplier<StructureProcessorList>> processorsCodec() {
        return IStructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(p_236845_0_ -> p_236845_0_.processors);
    }


    protected static <E extends PalettedSinglePoolElement> RecordCodecBuilder<E, Either<ResourceLocation, Template>> templateCodec() {
        return TEMPLATE_CODEC.fieldOf("location").forGetter(p_236842_0_ -> p_236842_0_.template);
    }


    protected PalettedSinglePoolElement(Either<ResourceLocation, Template> p_i242008_1_, Supplier<StructureProcessorList> p_i242008_2_, JigsawPattern.PlacementBehaviour p_i242008_3_) {
        super(p_i242008_3_);
        this.template = p_i242008_1_;
        this.processors = p_i242008_2_;
    }

    public PalettedSinglePoolElement(Template p_i242009_1_) {
        this(Either.right(p_i242009_1_), () -> ProcessorLists.EMPTY, JigsawPattern.PlacementBehaviour.RIGID);
    }


    public Either<ResourceLocation, Template> getTemplate() {
        return this.template;
    }

    public Template getTemplate(TemplateManager manager) {
        return (Template) this.template.map(manager::getOrCreate, Function.identity());
    }

    public List<Template.BlockInfo> getDataMarkers(TemplateManager p_214857_1_, BlockPos p_214857_2_, Rotation p_214857_3_, boolean p_214857_4_) {
        Template template = getTemplate(p_214857_1_);
        List<Template.BlockInfo> list = template.filterBlocks(p_214857_2_, (new PlacementSettings()).setRotation(p_214857_3_), Blocks.STRUCTURE_BLOCK, p_214857_4_);
        List<Template.BlockInfo> list1 = Lists.newArrayList();

        for (Template.BlockInfo template$blockinfo : list) {
            if (template$blockinfo.nbt != null) {
                StructureMode structuremode = StructureMode.valueOf(template$blockinfo.nbt.getString("mode"));
                if (structuremode == StructureMode.DATA) {
                    list1.add(template$blockinfo);
                }
            }
        }

        return list1;
    }


    public List<Template.BlockInfo> getShuffledJigsawBlocks(TemplateManager templateManager, BlockPos pos, Rotation rotation, Random random) {
        Template template = getTemplate(templateManager);
        List<Template.BlockInfo> list = template.filterBlocks(pos, (new PlacementSettings()).setRotation(rotation), Blocks.JIGSAW, true);
        Collections.shuffle(list, random);
        return list;
    }


    public MutableBoundingBox getBoundingBox(TemplateManager templateManager, BlockPos pos, Rotation rotation) {
        Template template = getTemplate(templateManager);
        return template.getBoundingBox((new PlacementSettings()).setRotation(rotation), pos);
    }


    public boolean place(TemplateManager templateManager, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, BlockPos pos1, BlockPos pos2, Rotation rotation, MutableBoundingBox box, Random random, boolean keepJigsaws) {
        return generate((Supplier<StructureProcessorList>) null, templateManager, world, structureManager, chunkGen, pos1, pos2, rotation, box, random, keepJigsaws, 18);
    }


    public boolean generate(@Nullable Supplier<StructureProcessorList> extra, TemplateManager templateManager, ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, BlockPos pos1, BlockPos pos2, Rotation rotation, MutableBoundingBox box, Random random, boolean keepJigsaws, int updateFlags) {
        Template template = getTemplate(templateManager);
        PlacementSettings placementsettings = getSettings(extra, rotation, box, keepJigsaws);
        if (!template.placeInWorld((IServerWorld) world, pos1, pos2, placementsettings, random, updateFlags)) {
            return false;
        }
        for (Template.BlockInfo info : Template.processBlockInfos((IWorld) world, pos1, pos2, placementsettings,
                getDataMarkers(templateManager, pos1, rotation, false), template)) {
            handleDataMarker((IWorld) world, info, pos1, rotation, random, box);
        }

        return true;
    }


    protected PlacementSettings getSettings(@Nullable Supplier<StructureProcessorList> extra, Rotation p_230379_1_, MutableBoundingBox p_230379_2_, boolean p_230379_3_) {
        PlacementSettings placementsettings = new PlacementSettings();
        placementsettings.setBoundingBox(p_230379_2_);
        placementsettings.setRotation(p_230379_1_);
        placementsettings.setKnownShape(true);
        placementsettings.setIgnoreEntities(false);

        placementsettings.setFinalizeEntities(true);
        if (!p_230379_3_) {
            placementsettings.addProcessor((StructureProcessor) JigsawReplacementStructureProcessor.INSTANCE);
        }

        ((StructureProcessorList) this.processors.get()).list().forEach(placementsettings::addProcessor);
        if (extra != null) ((StructureProcessorList) extra.get()).list().forEach(placementsettings::addProcessor);
        getProjection().getProcessors().forEach(placementsettings::addProcessor);
        return placementsettings;
    }


    public IJigsawDeserializer<?> getType() {
        return ModStructures.PoolElements.PALETTED_SINGLE_POOL_ELEMENT;
    }


    public String toString() {
        return "PalettedSingle[" + this.template + "]";
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\pool\PalettedSinglePoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */