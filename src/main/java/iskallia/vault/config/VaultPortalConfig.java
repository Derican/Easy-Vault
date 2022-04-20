package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class VaultPortalConfig extends Config {
    @Expose
    public String[] VALID_BLOCKS;

    @Override
    public String getName() {
        return "vault_portal";
    }

    @Override
    protected void reset() {
        this.VALID_BLOCKS = new String[]{Blocks.BLACKSTONE.getRegistryName().toString(), Blocks.POLISHED_BLACKSTONE.getRegistryName().toString(), Blocks.POLISHED_BLACKSTONE_BRICKS.getRegistryName().toString(), Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getRegistryName().toString()};
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
