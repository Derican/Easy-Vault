package iskallia.vault.block;

import iskallia.vault.block.entity.VaultTreasureChestTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class VaultTreasureChestBlock extends VaultChestBlock {
    public VaultTreasureChestBlock(final AbstractBlock.Properties builder) {
        super(builder, () -> ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY);
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(final BlockState state, final World world, final BlockPos pos) {
        final TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof VaultTreasureChestTileEntity)) {
            return null;
        }
        final VaultTreasureChestTileEntity chest = (VaultTreasureChestTileEntity) te;
        return (INamedContainerProvider) new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return ((VaultTreasureChestTileEntity) te).getDisplayName();
            }

            @Nullable
            public Container createMenu(final int containerId, final PlayerInventory playerInventory, final PlayerEntity player) {
                if (chest.canOpen(player)) {
                    chest.unpackLootTable(player);
                    return ChestContainer.sixRows(containerId, playerInventory, (IInventory) chest);
                }
                return null;
            }
        };
    }

    @Override
    public TileEntity newBlockEntity(final IBlockReader world) {
        return (TileEntity) new VaultTreasureChestTileEntity();
    }
}
