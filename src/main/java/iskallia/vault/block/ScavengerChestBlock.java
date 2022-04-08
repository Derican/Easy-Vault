package iskallia.vault.block;

import iskallia.vault.block.entity.ScavengerChestTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class ScavengerChestBlock
        extends ChestBlock {
    protected ScavengerChestBlock(AbstractBlock.Properties builder, Supplier<TileEntityType<? extends ChestTileEntity>> tileEntityTypeIn) {
        super(builder, tileEntityTypeIn);
    }

    public ScavengerChestBlock(AbstractBlock.Properties builder) {
        this(builder, () -> ModBlocks.SCAVENGER_CHEST_TILE_ENTITY);
    }


    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return (TileEntity) new ScavengerChestTileEntity();
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return (state == null) ? null : (BlockState) state.setValue((Property) TYPE, (Comparable) ChestType.SINGLE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\ScavengerChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */