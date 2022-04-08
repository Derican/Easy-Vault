package iskallia.vault.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.BiPredicate;


public class ConditionalReadSlot
        extends SlotItemHandler {
    private final BiPredicate<Integer, ItemStack> slotPredicate;

    public ConditionalReadSlot(IItemHandler inventory, int index, int xPosition, int yPosition, BiPredicate<Integer, ItemStack> slotPredicate) {
        super(inventory, index, xPosition, yPosition);
        this.slotPredicate = slotPredicate;
    }


    public boolean mayPlace(ItemStack stack) {
        return this.slotPredicate.test(Integer.valueOf(getSlotIndex()), stack);
    }


    public boolean mayPickup(PlayerEntity playerIn) {
        return this.slotPredicate.test(Integer.valueOf(getSlotIndex()), getItem());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\ConditionalReadSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */