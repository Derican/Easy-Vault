package iskallia.vault.block;

import iskallia.vault.block.entity.ScavengerChestTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class ScavengerChestBlock extends ChestBlock {
    protected ScavengerChestBlock(final AbstractBlock.Properties builder, final Supplier<TileEntityType<? extends ChestTileEntity>> tileEntityTypeIn) {
        super(builder, (Supplier) tileEntityTypeIn);
    }

    public ScavengerChestBlock(final AbstractBlock.Properties builder) {
        this(builder, () -> ModBlocks.SCAVENGER_CHEST_TILE_ENTITY);
    }

    public TileEntity newBlockEntity(final IBlockReader worldIn) {
        return (TileEntity) new ScavengerChestTileEntity();
    }

    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockState state = super.getStateForPlacement(context);
        return (state == null) ? null : (state.setValue(ScavengerChestBlock.TYPE, ChestType.SINGLE));
    }
}
