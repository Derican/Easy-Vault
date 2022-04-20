package iskallia.vault.container.inventory;

import iskallia.vault.block.entity.CatalystDecryptionTableTileEntity;
import iskallia.vault.container.slot.FilteredSlot;
import iskallia.vault.init.ModContainers;
import iskallia.vault.item.VaultCatalystItem;
import iskallia.vault.item.VaultInhibitorItem;
import iskallia.vault.item.crystal.VaultCrystalItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CatalystDecryptionContainer extends Container {
    private final BlockPos tilePos;
    private final List<Slot> catalystSlots;

    public CatalystDecryptionContainer(final int windowId, final World world, final BlockPos pos, final PlayerInventory playerInventory) {
        super((ContainerType) ModContainers.CATALYST_DECRYPTION_CONTAINER, windowId);
        this.catalystSlots = new ArrayList<Slot>();
        this.tilePos = pos;
        final TileEntity te = world.getBlockEntity(pos);
        if (te instanceof CatalystDecryptionTableTileEntity) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(tableInventory -> this.initSlots(tableInventory, playerInventory));
        }
    }

    private void initSlots(final IItemHandler tableInventory, final PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 152 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 210));
        }
        final Predicate<ItemStack> catalystFilter = stack -> stack.getItem() instanceof VaultCatalystItem || stack.getItem() instanceof VaultInhibitorItem;
        final Predicate<ItemStack> crystalFilter = stack -> stack.getItem() instanceof VaultCrystalItem;
        for (int slotY = 0; slotY < 5; ++slotY) {
            this.addCatalystSlot((Slot) new FilteredSlot(tableInventory, slotY * 2, 56, 15 + slotY * 26, catalystFilter));
            this.addCatalystSlot((Slot) new FilteredSlot(tableInventory, slotY * 2 + 1, 104, 15 + slotY * 26, catalystFilter));
        }
        this.addSlot((Slot) new FilteredSlot(tableInventory, 10, 80, 67, crystalFilter));
    }

    private void addCatalystSlot(final Slot slot) {
        this.catalystSlots.add(slot);
        this.addSlot(slot);
    }

    public List<Slot> getCatalystSlots() {
        return this.catalystSlots;
    }

    public boolean stillValid(final PlayerEntity player) {
        final World world = player.getCommandSenderWorld();
        return world.getBlockEntity(this.tilePos) instanceof CatalystDecryptionTableTileEntity && player.distanceToSqr(this.tilePos.getX() + 0.5, this.tilePos.getY() + 0.5, this.tilePos.getZ() + 0.5) <= 64.0;
    }

    public ItemStack quickMoveStack(final PlayerEntity playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            final ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 47, false)) {
                return itemstack;
            }
            if (index >= 0 && index < 27) {
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 36, false)) {
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
            slot.onTake(playerIn, slotStack);
        }
        return itemstack;
    }
}
