// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RecipeOutputSlot extends Slot
{
    public RecipeOutputSlot(final IInventory inventory, final int index, final int x, final int y) {
        super(inventory, index, x, y);
    }
    
    public boolean mayPlace(final ItemStack itemStack) {
        return false;
    }
}
