// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.inventory;

import iskallia.vault.block.entity.EtchingVendorControllerTileEntity;
import iskallia.vault.container.slot.EtchingBuySlot;
import iskallia.vault.container.slot.FilteredSlot;
import iskallia.vault.entity.EtchingVendorEntity;
import iskallia.vault.init.ModContainers;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class EtchingTradeContainer extends Container
{
    private final IInventory tradeInventory;
    private final World world;
    private final int vendorEntityId;
    
    public EtchingTradeContainer(final int containerId, final PlayerInventory playerInventory, final int vendorEntityId) {
        super((ContainerType)ModContainers.ETCHING_TRADE_CONTAINER, containerId);
        this.tradeInventory = (IInventory)new Inventory(6);
        this.world = playerInventory.player.level;
        this.vendorEntityId = vendorEntityId;
        this.initPlayerSlots(playerInventory);
        this.initTradeSlots();
    }
    
    private void initPlayerSlots(final PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot((IInventory)playerInventory, column + row * 9 + 9, 8 + column * 18, 102 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot((IInventory)playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 160));
        }
    }
    
    private void initTradeSlots() {
        for (int i = 0; i < 3; ++i) {
            this.addSlot((Slot)new FilteredSlot((IItemHandler)new InvWrapper(this.tradeInventory), i * 2, 53, 10 + i * 28, stack -> stack.getItem() == ModItems.VAULT_PLATINUM));
            this.addSlot((Slot)new EtchingBuySlot(this, (IItemHandler)new InvWrapper(this.tradeInventory), i, i * 2 + 1, 107, 10 + i * 28));
        }
        final EtchingVendorEntity vendor = this.getVendor();
        if (vendor == null) {
            return;
        }
        final EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }
        for (int j = 0; j < 3; ++j) {
            final EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(j);
            if (trade != null) {
                if (!trade.isSold()) {
                    final Slot outSlot = this.getSlot(37 + j * 2);
                    outSlot.set(trade.getSoldEtching().copy());
                }
            }
        }
    }
    
    @Nullable
    public EtchingVendorEntity getVendor() {
        return (EtchingVendorEntity)this.world.getEntity(this.vendorEntityId);
    }
    
    public void removed(final PlayerEntity player) {
        super.removed(player);
        this.tradeInventory.setItem(1, ItemStack.EMPTY);
        this.tradeInventory.setItem(3, ItemStack.EMPTY);
        this.tradeInventory.setItem(5, ItemStack.EMPTY);
        this.clearContainer(player, player.level, this.tradeInventory);
    }
    
    public ItemStack quickMoveStack(final PlayerEntity player, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            final ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 42, false)) {
                return itemstack;
            }
            if (index >= 0 && index < 27) {
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 27 && index < 36) {
                if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return itemstack;
    }
    
    public boolean stillValid(final PlayerEntity player) {
        final EtchingVendorEntity vendor = this.getVendor();
        return vendor != null && vendor.isValid();
    }
}
