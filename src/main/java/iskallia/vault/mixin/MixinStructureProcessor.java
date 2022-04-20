package iskallia.vault.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin({StructureProcessor.class})
public abstract class MixinStructureProcessor {
    @Shadow
    @Deprecated
    @Nullable
    public abstract Template.BlockInfo processBlock(final IWorldReader p0, final BlockPos p1, final BlockPos p2, final Template.BlockInfo p3, final Template.BlockInfo p4, final PlacementSettings p5);

    @Inject(method = {"process"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    protected void process(final IWorldReader world, final BlockPos pos1, final BlockPos pos2, final Template.BlockInfo info1, final Template.BlockInfo info2, final PlacementSettings settings, @Nullable final Template template, final CallbackInfoReturnable<Template.BlockInfo> ci) {
        try {
            ci.setReturnValue(this.processBlock(world, pos1, pos2, info1, info2, settings));
        } catch (final Exception e) {
            ci.setReturnValue(null);
        }
    }
}
