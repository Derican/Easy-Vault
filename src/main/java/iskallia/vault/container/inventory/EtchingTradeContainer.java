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
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;


public class EtchingTradeContainer
        extends Container {
    private final IInventory tradeInventory = (IInventory) new Inventory(6);
    private final World world;
    private final int vendorEntityId;

    public EtchingTradeContainer(int containerId, PlayerInventory playerInventory, int vendorEntityId) {
        super(ModContainers.ETCHING_TRADE_CONTAINER, containerId);
        this.world = playerInventory.player.level;
        this.vendorEntityId = vendorEntityId;

        initPlayerSlots(playerInventory);
        initTradeSlots();
    }

    private void initPlayerSlots(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 102 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 160));
        }
    }

    private void initTradeSlots() {
        for (int i = 0; i < 3; i++) {
            addSlot((Slot) new FilteredSlot((IItemHandler) new InvWrapper(this.tradeInventory), i * 2, 53, 10 + i * 28, stack -> (stack.getItem() == ModItems.VAULT_PLATINUM)));
            addSlot((Slot) new EtchingBuySlot(this, (IItemHandler) new InvWrapper(this.tradeInventory), i, i * 2 + 1, 107, 10 + i * 28));
        }

        EtchingVendorEntity vendor = getVendor();
        if (vendor == null) {
            return;
        }
        EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return;
        }
        for (int j = 0; j < 3; j++) {
            EtchingVendorControllerTileEntity.EtchingTrade trade = controllerTile.getTrade(j);
            if (trade != null && !trade.isSold()) {


                Slot outSlot = getSlot(37 + j * 2);
                outSlot.set(trade.getSoldEtching().copy());
            }
        }
    }

    @Nullable
    public EtchingVendorEntity getVendor() {
        return (EtchingVendorEntity) this.world.getEntity(this.vendorEntityId);
    }


    public void removed(PlayerEntity player) {
        super.removed(player);

        this.tradeInventory.setItem(1, ItemStack.EMPTY);
        this.tradeInventory.setItem(3, ItemStack.EMPTY);
        this.tradeInventory.setItem(5, ItemStack.EMPTY);
        clearContainer(player, player.level, this.tradeInventory);
    }


    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (index >= 0 && index < 36 &&
                    moveItemStackTo(slotStack, 36, 42, false)) {
                return itemstack;
            }

            if (index >= 0 && index < 27) {
                if (!moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemstack;
    }


    public boolean stillValid(PlayerEntity player) {
        EtchingVendorEntity vendor = getVendor();
        return (vendor != null && vendor.isValid());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\EtchingTradeContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */