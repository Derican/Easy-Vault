package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;


public class OtherSideConfig
        extends Config {
    @Expose
    public String[] VALID_BLOCKS;

    public String getName() {
        return "other_side";
    }


    protected void reset() {
        this


                .VALID_BLOCKS = new String[]{Blocks.QUARTZ_BLOCK.getRegistryName().toString(), Blocks.QUARTZ_BRICKS.getRegistryName().toString(), Blocks.QUARTZ_PILLAR.getRegistryName().toString(), Blocks.SMOOTH_QUARTZ.getRegistryName().toString(), Blocks.CHISELED_QUARTZ_BLOCK.getRegistryName().toString()};
    }


    public Block[] getValidFrameBlocks() {
        Block[] blocks = new Block[this.VALID_BLOCKS.length];
        int i = 0;
        for (String s : this.VALID_BLOCKS) {
            ResourceLocation res = new ResourceLocation(s);
            blocks[i++] = (Block) ForgeRegistries.BLOCKS.getValue(res);
        }
        return blocks;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\OtherSideConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */