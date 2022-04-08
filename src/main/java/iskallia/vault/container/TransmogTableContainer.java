package iskallia.vault.container;

import iskallia.vault.container.base.RecipeContainer;
import iskallia.vault.container.base.RecipeInventory;
import iskallia.vault.container.inventory.TransmogTableInventory;
import iskallia.vault.container.slot.RecipeOutputSlot;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class TransmogTableContainer extends RecipeContainer {
    public TransmogTableContainer(int windowId, PlayerEntity player) {
        super(ModContainers.TRANSMOG_TABLE_CONTAINER, windowId, (RecipeInventory) new TransmogTableInventory(), player);
    }


    protected void addInternalInventorySlots() {
        addSlot(new Slot((IInventory) this.internalInventory, 0, 38, 51));
        addSlot(new Slot((IInventory) this.internalInventory, 1, 82, 51));
        addSlot(new Slot((IInventory) this.internalInventory, 2, 60, 29));
        addSlot((Slot) new RecipeOutputSlot((IInventory) this.internalInventory, this.internalInventory.outputSlotIndex(), 137, 52));
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public TransmogTableInventory getInternalInventory() {
        return (TransmogTableInventory) this.internalInventory;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\TransmogTableContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */