// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class SideOnlyFixer
{
    public static int getSlotFor(final PlayerInventory inventory, final ItemStack stack) {
        for (int i = 0; i < inventory.items.size(); ++i) {
            if (!((ItemStack)inventory.items.get(i)).isEmpty() && stackEqualExact(stack, (ItemStack)inventory.items.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean stackEqualExact(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }
}
