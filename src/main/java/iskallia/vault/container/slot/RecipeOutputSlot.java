package iskallia.vault.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RecipeOutputSlot
        extends Slot {
    public RecipeOutputSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }


    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\RecipeOutputSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */