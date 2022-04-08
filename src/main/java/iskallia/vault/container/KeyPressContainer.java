package iskallia.vault.container;

import iskallia.vault.container.base.RecipeContainer;
import iskallia.vault.container.base.RecipeInventory;
import iskallia.vault.container.inventory.KeyPressInventory;
import iskallia.vault.container.slot.RecipeOutputSlot;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class KeyPressContainer extends RecipeContainer {
    public KeyPressContainer(int windowId, PlayerEntity player) {
        super(ModContainers.KEY_PRESS_CONTAINER, windowId, (RecipeInventory) new KeyPressInventory(), player);
    }


    protected void addInternalInventorySlots() {
        addSlot(new Slot((IInventory) this.internalInventory, 0, 27, 47));
        addSlot(new Slot((IInventory) this.internalInventory, 1, 76, 47));
        addSlot((Slot) new RecipeOutputSlot((IInventory) this.internalInventory, this.internalInventory.outputSlotIndex(), 134, 47) {
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                ItemStack itemStack = super.onTake(player, stack);

                if (!player.level.isClientSide && !itemStack.isEmpty()) {
                    player.level.levelEvent(1030, player.blockPosition(), 0);
                }

                return itemStack;
            }
        });
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }


    public void onResultPicked(PlayerEntity player, int index) {
        player.level.levelEvent(1030, player.blockPosition(), 0);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\KeyPressContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */