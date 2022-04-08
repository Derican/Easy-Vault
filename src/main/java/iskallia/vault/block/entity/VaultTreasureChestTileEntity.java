package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class VaultTreasureChestTileEntity
        extends VaultChestTileEntity {
    public VaultTreasureChestTileEntity() {
        super(ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY);
        setItems(NonNullList.withSize(54, ItemStack.EMPTY));
    }


    public int getContainerSize() {
        return 54;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultTreasureChestTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */