package iskallia.vault.container.inventory;

import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.container.slot.player.ArmorEditSlot;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModContainers;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class CryochamberContainer extends Container {
    private final BlockPos tilePos;

    public CryochamberContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(ModContainers.CRYOCHAMBER_CONTAINER, windowId);
        Inventory inventory = null;
        this.tilePos = pos;

        CryoChamberTileEntity cryoChamber = getCryoChamber(world);

        if (world instanceof ServerWorld && cryoChamber != null) {
            EternalData.EquipmentInventory equipmentInventory = EternalsData.get((ServerWorld) world).getEternalEquipmentInventory(cryoChamber.getEternalId(), cryoChamber::sendUpdates);
            if (equipmentInventory == null) {
                return;
            }
        } else {
            inventory = new Inventory(5);
        }

        initSlots((IInventory) inventory, playerInventory);
    }


    private void initSlots(IInventory equipmentInventory, PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 129 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 187));
        }


        addSlot((Slot) new ArmorEditSlot(equipmentInventory, EquipmentSlotType.MAINHAND, 0, 151, 26));
        int offsetY = 98;
        int index = 1;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() != EquipmentSlotType.Group.HAND) {


                addSlot((Slot) new ArmorEditSlot(equipmentInventory, slot, index, 151, offsetY));
                offsetY -= 18;
                index++;
            }
        }
    }

    public boolean stillValid(PlayerEntity player) {
        if (getCryoChamber(player.getCommandSenderWorld()) == null) {
            return false;
        }
        return (player.distanceToSqr(Vector3d.atCenterOf((Vector3i) this.tilePos)) <= 64.0D);
    }

    @Nullable
    public CryoChamberTileEntity getCryoChamber(World world) {
        BlockState state = world.getBlockState(this.tilePos);
        if (!state.is((Block) ModBlocks.CRYO_CHAMBER)) {
            return null;
        }
        return CryoChamberBlock.getCryoChamberTileEntity(world, this.tilePos, state);
    }


    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (index >= 0 && index < 36 &&
                    moveItemStackTo(slotStack, 36, 41, false)) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\CryochamberContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */