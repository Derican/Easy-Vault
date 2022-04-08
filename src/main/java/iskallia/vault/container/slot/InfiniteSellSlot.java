package iskallia.vault.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InfiniteSellSlot
        extends SellSlot {
    public InfiniteSellSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }


    public ItemStack remove(int amount) {
        return getItem().copy();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\InfiniteSellSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */