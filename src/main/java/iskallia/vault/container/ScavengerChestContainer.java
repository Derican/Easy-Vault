package iskallia.vault.container;

import iskallia.vault.container.slot.FilteredSlotWrapper;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Slot;


public class ScavengerChestContainer
        extends ChestContainer {
    private final IInventory chestOwner;

    public ScavengerChestContainer(int id, PlayerInventory playerInventory, IInventory chestOwner, IInventory scavengerOwner) {
        super(ModContainers.SCAVENGER_CHEST_CONTAINER, id, playerInventory, scavengerOwner, 5);

        this.chestOwner = chestOwner;
    }

    protected Slot addSlot(Slot slot) {
        FilteredSlotWrapper filteredSlotWrapper = null;
        if (!(slot.container instanceof PlayerInventory)) {
            filteredSlotWrapper = new FilteredSlotWrapper(slot, stack -> stack.getItem() instanceof iskallia.vault.item.BasicScavengerItem);
        }
        return super.addSlot((Slot) filteredSlotWrapper);
    }


    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        if (!(getContainer() instanceof net.minecraft.tileentity.ChestTileEntity))
            this.chestOwner.stopOpen(playerIn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\ScavengerChestContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */