package iskallia.vault.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class SideOnlyFixer {
    public static int getSlotFor(final PlayerInventory inventory, final ItemStack stack) {
        for (int i = 0; i < inventory.items.size(); ++i) {
            if (!inventory.items.get(i).isEmpty() && stackEqualExact(stack, inventory.items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private static boolean stackEqualExact(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }
}
