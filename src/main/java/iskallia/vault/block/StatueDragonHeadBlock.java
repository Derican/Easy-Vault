// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class StatueDragonHeadBlock extends Block
{
    public StatueDragonHeadBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.0f, 3600000.0f).noOcclusion().noCollission());
    }
}
