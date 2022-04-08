package iskallia.vault.container.inventory;

import iskallia.vault.container.slot.FilteredSlot;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
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


public class CatalystDecryptionContainer
        extends Container {
    private final BlockPos tilePos;
    private final List<Slot> catalystSlots = new ArrayList<>();

    public CatalystDecryptionContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(ModContainers.CATALYST_DECRYPTION_CONTAINER, windowId);
        this.tilePos = pos;

        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof iskallia.vault.block.entity.CatalystDecryptionTableTileEntity) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(tableInventory -> initSlots(tableInventory, playerInventory));
        }
    }


    private void initSlots(IItemHandler tableInventory, PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 152 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 210));
        }

        Predicate<ItemStack> catalystFilter = stack -> (stack.getItem() instanceof iskallia.vault.item.VaultCatalystItem || stack.getItem() instanceof iskallia.vault.item.VaultInhibitorItem);
        Predicate<ItemStack> crystalFilter = stack -> stack.getItem() instanceof iskallia.vault.item.crystal.VaultCrystalItem;


        for (int slotY = 0; slotY < 5; slotY++) {
            addCatalystSlot((Slot) new FilteredSlot(tableInventory, slotY * 2, 56, 15 + slotY * 26, catalystFilter));
            addCatalystSlot((Slot) new FilteredSlot(tableInventory, slotY * 2 + 1, 104, 15 + slotY * 26, catalystFilter));
        }
        addSlot((Slot) new FilteredSlot(tableInventory, 10, 80, 67, crystalFilter));
    }

    private void addCatalystSlot(Slot slot) {
        this.catalystSlots.add(slot);
        addSlot(slot);
    }

    public List<Slot> getCatalystSlots() {
        return this.catalystSlots;
    }


    public boolean stillValid(PlayerEntity player) {
        World world = player.getCommandSenderWorld();
        if (!(world.getBlockEntity(this.tilePos) instanceof iskallia.vault.block.entity.CatalystDecryptionTableTileEntity)) {
            return false;
        }
        return (player.distanceToSqr(this.tilePos.getX() + 0.5D, this.tilePos.getY() + 0.5D, this.tilePos.getZ() + 0.5D) <= 64.0D);
    }


    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (index >= 0 && index < 36 &&
                    moveItemStackTo(slotStack, 36, 47, false)) {
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

            slot.onTake(playerIn, slotStack);
        }

        return itemstack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\CatalystDecryptionContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */