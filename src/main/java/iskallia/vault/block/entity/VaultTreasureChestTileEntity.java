// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class VaultTreasureChestTileEntity extends VaultChestTileEntity
{
    public VaultTreasureChestTileEntity() {
        super(ModBlocks.VAULT_TREASURE_CHEST_TILE_ENTITY);
        this.setItems(NonNullList.withSize(54, ItemStack.EMPTY));
    }
    
    public int getContainerSize() {
        return 54;
    }
}
