package iskallia.vault.container;

import iskallia.vault.block.entity.VaultCrateTileEntity;
import iskallia.vault.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
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

public class VaultCrateContainer extends Container {
    public IItemHandler crateInventory;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private final BlockPos tilePos;

    public VaultCrateContainer(final int windowId, final World world, final BlockPos pos, final PlayerInventory playerInventory, final PlayerEntity player) {
        super(ModContainers.VAULT_CRATE_CONTAINER, windowId);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.tilePos = pos;
        final TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.crateInventory = h;
                final int i = 36;
                for (int j = 0; j < 6; ++j) {
                    for (int k = 0; k < 9; ++k) {
                        this.addSlot(new SlotItemHandler(h, k + j * 9, 8 + k * 18, 18 + j * 18));
                    }
                }
                for (int l = 0; l < 3; ++l) {
                    for (int j2 = 0; j2 < 9; ++j2) {
                        this.addSlot(new Slot(playerInventory, j2 + l * 9 + 9, 8 + j2 * 18, 103 + l * 18 + i));
                    }
                }
                for (int i2 = 0; i2 < 9; ++i2) {
                    this.addSlot(new Slot(playerInventory, i2, 8 + i2 * 18, 161 + i));
                }
            });
        }
    }

    public BlockPos getTilePos() {
        return this.tilePos;
    }

    public ItemStack quickMoveStack(final PlayerEntity playerIn, final int index) {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            final ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();
            if (index < this.crateInventory.getSlots()) {
                if (!this.moveItemStackTo(stackInSlot, this.crateInventory.getSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, this.crateInventory.getSlots(), false)) {
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

    public boolean stillValid(final PlayerEntity player) {
        final World world = player.getCommandSenderWorld();
        return world.getBlockEntity(this.tilePos) instanceof VaultCrateTileEntity && player.distanceToSqr(this.tilePos.getX() + 0.5, this.tilePos.getY() + 0.5, this.tilePos.getZ() + 0.5) <= 64.0;
    }

    private int addSlotBox(final IItemHandler handler, int index, final int x, int y, final int horAmount, final int dx, final int verAmount, final int dy) {
        for (int j = 0; j < verAmount; ++j) {
            index = this.addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private int addSlotRange(final IItemHandler handler, int index, int x, final int y, final int amount, final int dx) {
        for (int i = 0; i < amount; ++i) {
            this.addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            ++index;
        }
        return index;
    }

    public void removed(final PlayerEntity player) {
        super.removed(player);
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BARREL_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
