// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InfiniteSellSlot extends SellSlot
{
    public InfiniteSellSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    public ItemStack remove(final int amount) {
        return this.getItem().copy();
    }
}
