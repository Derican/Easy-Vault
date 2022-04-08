package iskallia.vault.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class FilteredSlot
        extends SlotItemHandler {
    private final Predicate<ItemStack> stackFilter;

    public FilteredSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> stackFilter) {
        super(itemHandler, index, xPosition, yPosition);
        this.stackFilter = stackFilter;
    }


    public boolean mayPlace(ItemStack stack) {
        if (!this.stackFilter.test(stack)) {
            return false;
        }
        return super.mayPlace(stack);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\FilteredSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */