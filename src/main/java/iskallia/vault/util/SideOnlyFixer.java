package iskallia.vault.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;


public class SideOnlyFixer {
    public static int getSlotFor(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.items.size(); i++) {
            if (!((ItemStack) inventory.items.get(i)).isEmpty() &&
                    stackEqualExact(stack, (ItemStack) inventory.items.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return (stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\SideOnlyFixer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */