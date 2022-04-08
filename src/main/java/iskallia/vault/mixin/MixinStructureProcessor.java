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
    public abstract Template.BlockInfo processBlock(IWorldReader paramIWorldReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Template.BlockInfo paramBlockInfo1, Template.BlockInfo paramBlockInfo2, PlacementSettings paramPlacementSettings);

    @Inject(method = {"process"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    protected void process(IWorldReader world, BlockPos pos1, BlockPos pos2, Template.BlockInfo info1, Template.BlockInfo info2, PlacementSettings settings, @Nullable Template template, CallbackInfoReturnable<Template.BlockInfo> ci) {
        try {
            ci.setReturnValue(processBlock(world, pos1, pos2, info1, info2, settings));
        } catch (Exception e) {
            ci.setReturnValue(null);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinStructureProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */