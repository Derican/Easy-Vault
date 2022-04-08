package iskallia.vault.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SellSlot
        extends Slot {
    public SellSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }


    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\SellSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */