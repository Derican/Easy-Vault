package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class OtherSideConfig extends Config {
    @Expose
    public String[] VALID_BLOCKS;

    @Override
    public String getName() {
        return "other_side";
    }

    @Override
    protected void reset() {
        this.VALID_BLOCKS = new String[]{Blocks.QUARTZ_BLOCK.getRegistryName().toString(), Blocks.QUARTZ_BRICKS.getRegistryName().toString(), Blocks.QUARTZ_PILLAR.getRegistryName().toString(), Blocks.SMOOTH_QUARTZ.getRegistryName().toString(), Blocks.CHISELED_QUARTZ_BLOCK.getRegistryName().toString()};
    }

    public Block[] getValidFrameBlocks() {
        final Block[] blocks = new Block[this.VALID_BLOCKS.length];
        int i = 0;
        for (final String s : this.VALID_BLOCKS) {
            final ResourceLocation res = new ResourceLocation(s);
            blocks[i++] = (Block) ForgeRegistries.BLOCKS.getValue(res);
        }
        return blocks;
    }
}
