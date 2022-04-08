package iskallia.vault.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Mixin({Template.Palette.class})
public class MixinTemplatePalette {
    @Shadow
    @Final
    private Map<Block, List<Template.BlockInfo>> cache;
    @Shadow
    @Final
    private List<Template.BlockInfo> blocks;

    @Overwrite
    public List<Template.BlockInfo> blocks(Block block) {
        return this.cache.computeIfAbsent(block, filterBlock -> {
            if (block == Blocks.JIGSAW) {
                List<Template.BlockInfo> prioritizedJigsawPieces = new ArrayList<>();
                List<Template.BlockInfo> jigsawBlocks = (List<Template.BlockInfo>) this.blocks.stream().filter(()).filter(()).collect(Collectors.toList());
                prioritizedJigsawPieces.addAll(jigsawBlocks);
                return prioritizedJigsawPieces;
            }
            return (List) this.blocks.stream().filter(()).collect(Collectors.toList());
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinTemplatePalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */