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
    public VaultTreasureChestBlock(AbstractBlock.Properties builder) {
        super(builder, () -> ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY);
    }


    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        final TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof VaultTreasureChestTileEntity)) {
            return null;
        }
        final VaultTreasureChestTileEntity chest = (VaultTreasureChestTileEntity) te;
        return new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return ((VaultTreasureChestTileEntity) te).getDisplayName();
            }


            @Nullable
            public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player) {
                if (chest.canOpen(player)) {
                    chest.unpackLootTable(player);
                    return (Container) ChestContainer.sixRows(containerId, playerInventory, (IInventory) chest);
                }
                return null;
            }
        };
    }


    public TileEntity newBlockEntity(IBlockReader world) {
        return (TileEntity) new VaultTreasureChestTileEntity();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultTreasureChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */