package iskallia.vault.mixin;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.JigsawReplacementStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;


@Mixin(value = {SingleJigsawPiece.class}, priority = 1001)
public abstract class MixinSingleJigsawPiece
        extends JigsawPiece {
    protected MixinSingleJigsawPiece(JigsawPattern.PlacementBehaviour projection) {
        super(projection);
    }

    @Shadow
    @Final
    protected Supplier<StructureProcessorList> processors;

    @Overwrite
    protected PlacementSettings getSettings(Rotation p_230379_1_, MutableBoundingBox p_230379_2_, boolean p_230379_3_) {
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
        getProjection().getProcessors().forEach(placementsettings::addProcessor);
        return placementsettings;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinSingleJigsawPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */