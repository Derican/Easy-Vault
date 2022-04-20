package iskallia.vault.container;

import iskallia.vault.container.base.RecipeContainer;
import iskallia.vault.container.inventory.TransmogTableInventory;
import iskallia.vault.container.slot.RecipeOutputSlot;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class TransmogTableContainer extends RecipeContainer {
    public TransmogTableContainer(final int windowId, final PlayerEntity player) {
        super(ModContainers.TRANSMOG_TABLE_CONTAINER, windowId, new TransmogTableInventory(), player);
    }

    @Override
    protected void addInternalInventorySlots() {
        this.addSlot(new Slot((IInventory) this.internalInventory, 0, 38, 51));
        this.addSlot(new Slot((IInventory) this.internalInventory, 1, 82, 51));
        this.addSlot(new Slot((IInventory) this.internalInventory, 2, 60, 29));
        this.addSlot((Slot) new RecipeOutputSlot((IInventory) this.internalInventory, this.internalInventory.outputSlotIndex(), 137, 52));
    }

    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public TransmogTableInventory getInternalInventory() {
        return (TransmogTableInventory) this.internalInventory;
    }
}
