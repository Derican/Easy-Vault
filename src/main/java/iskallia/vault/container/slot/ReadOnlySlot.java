// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ReadOnlySlot extends Slot
{
    public ReadOnlySlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    public boolean mayPlace(final ItemStack stack) {
        return false;
    }
    
    public boolean mayPickup(final PlayerEntity playerIn) {
        return false;
    }
}
