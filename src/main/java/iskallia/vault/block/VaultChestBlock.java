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

public class VaultChestBlock extends ChestBlock {
    protected VaultChestBlock(final AbstractBlock.Properties builder, final Supplier<TileEntityType<? extends ChestTileEntity>> tileSupplier) {
        super(builder, (Supplier) tileSupplier);
    }

    public VaultChestBlock(final AbstractBlock.Properties builder) {
        this(builder, () -> ModBlocks.VAULT_CHEST_TILE_ENTITY);
    }

    public boolean removedByPlayer(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final boolean willHarvest, final FluidState fluid) {
        final TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof VaultChestTileEntity) || player.isCreative()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        if (this != ModBlocks.VAULT_BONUS_CHEST && this != ModBlocks.VAULT_COOP_CHEST) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        final VaultChestTileEntity chest = (VaultChestTileEntity) te;
        if (chest.isEmpty()) {
            return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
        }
        this.getBlock().playerWillDestroy(world, pos, state, player);
        return true;
    }

    public void playerDestroy(final World world, final PlayerEntity player, final BlockPos pos, final BlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (this != ModBlocks.VAULT_BONUS_CHEST && this != ModBlocks.VAULT_COOP_CHEST) {
            super.playerDestroy(world, player, pos, state, te, stack);
            return;
        }
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005f);
        if (te instanceof VaultChestTileEntity) {
            final VaultChestTileEntity chest = (VaultChestTileEntity) te;
            for (int slot = 0; slot < chest.getContainerSize(); ++slot) {
                final ItemStack invStack = chest.getItem(slot);
                if (!invStack.isEmpty()) {
                    Block.popResource(world, pos, invStack);
                    chest.setItem(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    public TileEntity newBlockEntity(final IBlockReader world) {
        return (TileEntity) new VaultChestTileEntity();
    }

    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockState state = super.getStateForPlacement(context);
        return (state == null) ? null : (state.setValue(VaultChestBlock.TYPE, ChestType.SINGLE));
    }
}
