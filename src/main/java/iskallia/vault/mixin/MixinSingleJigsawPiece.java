// 
// Decompiled by Procyon v0.6.0
// 

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

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(value = { SingleJigsawPiece.class }, priority = 1001)
public abstract class MixinSingleJigsawPiece extends JigsawPiece
{
    @Shadow
    @Final
    protected Supplier<StructureProcessorList> processors;
    
    protected MixinSingleJigsawPiece(final JigsawPattern.PlacementBehaviour projection) {
        super(projection);
    }
    
    /**
     * @author
     * @reason
     */
    @Overwrite
    protected PlacementSettings getSettings(final Rotation p_230379_1_, final MutableBoundingBox p_230379_2_, final boolean p_230379_3_) {
        final PlacementSettings placementsettings = new PlacementSettings();
        placementsettings.setBoundingBox(p_230379_2_);
        placementsettings.setRotation(p_230379_1_);
        placementsettings.setKnownShape(true);
        placementsettings.setIgnoreEntities(false);
        placementsettings.setFinalizeEntities(true);
        if (!p_230379_3_) {
            placementsettings.addProcessor((StructureProcessor)JigsawReplacementStructureProcessor.INSTANCE);
        }
        this.processors.get().list().forEach(placementsettings::addProcessor);
        this.getProjection().getProcessors().forEach(placementsettings::addProcessor);
        return placementsettings;
    }
}
