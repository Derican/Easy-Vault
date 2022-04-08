package iskallia.vault.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class VaultBedrockBlock extends Block {
    public VaultBedrockBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(-1.0F, 3600000.0F)
                .noDrops().isValidSpawn((a, b, c, d) -> false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultBedrockBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */