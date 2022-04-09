// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container;

import iskallia.vault.container.slot.FilteredSlotWrapper;
import iskallia.vault.init.ModContainers;
import iskallia.vault.item.BasicScavengerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.ChestTileEntity;

public class ScavengerChestContainer extends ChestContainer
{
    private final IInventory chestOwner;
    
    public ScavengerChestContainer(final int id, final PlayerInventory playerInventory, final IInventory chestOwner, final IInventory scavengerOwner) {
        super((ContainerType)ModContainers.SCAVENGER_CHEST_CONTAINER, id, playerInventory, scavengerOwner, 5);
        this.chestOwner = chestOwner;
    }
    
    protected Slot addSlot(Slot slot) {
        if (!(slot.container instanceof PlayerInventory)) {
            slot = new FilteredSlotWrapper(slot, stack -> stack.getItem() instanceof BasicScavengerItem);
        }
        return super.addSlot(slot);
    }
    
    public void removed(final PlayerEntity playerIn) {
        super.removed(playerIn);
        if (!(this.getContainer() instanceof ChestTileEntity)) {
            this.chestOwner.stopOpen(playerIn);
        }
    }
}
