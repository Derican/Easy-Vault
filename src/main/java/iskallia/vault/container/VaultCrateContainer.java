package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class VaultCrateContainer
        extends Container {
    public IItemHandler crateInventory;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private BlockPos tilePos;

    public VaultCrateContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.VAULT_CRATE_CONTAINER, windowId);
        this.playerEntity = player;
        this.playerInventory = (IItemHandler) new InvWrapper((IInventory) playerInventory);
        this.tilePos = pos;

        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.crateInventory = h;
                int i = 36;
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 9; k++) {
                        addSlot((Slot) new SlotItemHandler(h, k + j * 9, 8 + k * 18, 18 + j * 18));
                    }
                }
                for (int l = 0; l < 3; l++) {
                    for (int j1 = 0; j1 < 9; j1++) {
                        addSlot(new Slot((IInventory) playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
                    }
                }
                for (int i1 = 0; i1 < 9; i1++) {
                    addSlot(new Slot((IInventory) playerInventory, i1, 8 + i1 * 18, 161 + i));
                }
            });
        }
    }


    public BlockPos getTilePos() {
        return this.tilePos;
    }


    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();
            if (index < this.crateInventory.getSlots()) {
                if (!moveItemStackTo(stackInSlot, this.crateInventory.getSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, this.crateInventory.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }


    public boolean stillValid(PlayerEntity player) {
        World world = player.getCommandSenderWorld();
        if (!(world.getBlockEntity(this.tilePos) instanceof iskallia.vault.block.entity.VaultCrateTileEntity)) {
            return false;
        }
        return (player.distanceToSqr(this.tilePos.getX() + 0.5D, this.tilePos.getY() + 0.5D, this.tilePos.getZ() + 0.5D) <= 64.0D);
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot((Slot) new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }


    public void removed(PlayerEntity player) {
        super.removed(player);
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BARREL_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\VaultCrateContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */