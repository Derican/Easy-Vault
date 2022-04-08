package iskallia.vault.block;

import iskallia.vault.block.entity.VaultChestTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.properties.ChestType;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;


public class VaultChestBlock
        extends ChestBlock {
    protected VaultChestBlock(AbstractBlock.Properties builder, Supplier<TileEntityType<? extends ChestTileEntity>> tileSupplier) {
        super(builder, tileSupplier);
    }

    public VaultChestBlock(AbstractBlock.Properties builder) {
        this(builder, () -> ModBlocks.VAULT_CHEST_TILE_ENTITY);
    }


    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof VaultChestTileEntity) || player.isCreative()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        if (this != ModBlocks.VAULT_BONUS_CHEST && this != ModBlocks.VAULT_COOP_CHEST) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        VaultChestTileEntity chest = (VaultChestTileEntity) te;
        if (chest.isEmpty()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        getBlock().playerWillDestroy(world, pos, state, player);
        return true;
    }


    public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (this != ModBlocks.VAULT_BONUS_CHEST && this != ModBlocks.VAULT_COOP_CHEST) {
            super.playerDestroy(world, player, pos, state, te, stack);
            return;
        }
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if (te instanceof VaultChestTileEntity) {
            VaultChestTileEntity chest = (VaultChestTileEntity) te;
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack invStack = chest.getItem(slot);
                if (!invStack.isEmpty()) {
                    Block.popResource(world, pos, invStack);
                    chest.setItem(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }


    public TileEntity newBlockEntity(IBlockReader world) {
        return (TileEntity) new VaultChestTileEntity();
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return (state == null) ? null : (BlockState) state.setValue((Property) TYPE, (Comparable) ChestType.SINGLE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */