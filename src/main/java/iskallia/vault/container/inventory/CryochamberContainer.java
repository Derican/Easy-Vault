// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.container.inventory;

import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.container.slot.player.ArmorEditSlot;
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
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class CryochamberContainer extends Container
{
    private final BlockPos tilePos;
    
    public CryochamberContainer(final int windowId, final World world, final BlockPos pos, final PlayerInventory playerInventory) {
        super((ContainerType)ModContainers.CRYOCHAMBER_CONTAINER, windowId);
        this.tilePos = pos;
        final CryoChamberTileEntity cryoChamber = this.getCryoChamber(world);
        IInventory equipmentInventory;
        if (world instanceof ServerWorld && cryoChamber != null) {
            equipmentInventory = (IInventory)EternalsData.get((ServerWorld)world).getEternalEquipmentInventory(cryoChamber.getEternalId(), cryoChamber::sendUpdates);
            if (equipmentInventory == null) {
                return;
            }
        }
        else {
            equipmentInventory = (IInventory)new Inventory(5);
        }
        this.initSlots(equipmentInventory, playerInventory);
    }
    
    private void initSlots(final IInventory equipmentInventory, final PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot((IInventory)playerInventory, column + row * 9 + 9, 8 + column * 18, 129 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot((IInventory)playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 187));
        }
        this.addSlot((Slot)new ArmorEditSlot(equipmentInventory, EquipmentSlotType.MAINHAND, 0, 151, 26));
        int offsetY = 98;
        int index = 1;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() != EquipmentSlotType.Group.HAND) {
                this.addSlot((Slot)new ArmorEditSlot(equipmentInventory, slot, index, 151, offsetY));
                offsetY -= 18;
                ++index;
            }
        }
    }
    
    public boolean stillValid(final PlayerEntity player) {
        return this.getCryoChamber(player.getCommandSenderWorld()) != null && player.distanceToSqr(Vector3d.atCenterOf((Vector3i)this.tilePos)) <= 64.0;
    }
    
    @Nullable
    public CryoChamberTileEntity getCryoChamber(final World world) {
        final BlockState state = world.getBlockState(this.tilePos);
        if (!state.is((Block)ModBlocks.CRYO_CHAMBER)) {
            return null;
        }
        return CryoChamberBlock.getCryoChamberTileEntity(world, this.tilePos, state);
    }
    
    public ItemStack quickMoveStack(final PlayerEntity playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            final ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && this.moveItemStackTo(slotStack, 36, 41, false)) {
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
            slot.onTake(playerIn, slotStack);
        }
        return itemstack;
    }
}
