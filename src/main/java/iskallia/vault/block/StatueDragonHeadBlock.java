package iskallia.vault.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class StatueDragonHeadBlock extends Block {
    public StatueDragonHeadBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(1.0F, 3600000.0F)
                .noOcclusion()
                .noCollission());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\StatueDragonHeadBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */