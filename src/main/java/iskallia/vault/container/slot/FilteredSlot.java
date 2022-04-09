// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class FilteredSlot extends SlotItemHandler
{
    private final Predicate<ItemStack> stackFilter;
    
    public FilteredSlot(final IItemHandler itemHandler, final int index, final int xPosition, final int yPosition, final Predicate<ItemStack> stackFilter) {
        super(itemHandler, index, xPosition, yPosition);
        this.stackFilter = stackFilter;
    }
    
    public boolean mayPlace(final ItemStack stack) {
        return this.stackFilter.test(stack) && super.mayPlace(stack);
    }
}
