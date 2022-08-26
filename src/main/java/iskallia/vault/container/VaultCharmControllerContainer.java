package iskallia.vault.container;

import iskallia.vault.init.ModContainers;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.VaultCharmData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class VaultCharmControllerContainer extends Container {
    public IInventory visibleItems;
    private final int inventorySize;
    private final List<ResourceLocation> whitelist;
    private final int invStartIndex;
    private final int invEndIndex;
    private int currentStart;
    private int currentEnd;
    private float scrollDelta;

    public VaultCharmControllerContainer(final int windowId, final PlayerInventory playerInventory, final CompoundNBT data) {
        super(ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER, windowId);
        this.currentStart = 0;
        this.currentEnd = 53;
        this.scrollDelta = 0.0f;
        final VaultCharmData.VaultCharmInventory vaultCharmInventory = VaultCharmData.VaultCharmInventory.fromNbt(data);
        this.inventorySize = vaultCharmInventory.getSize();
        this.whitelist = vaultCharmInventory.getWhitelist();
        this.initVisibleItems();
        this.initPlayerInventorySlots(playerInventory);
        this.initCharmControllerSlots();
        this.invStartIndex = 36;
        this.invEndIndex = 36 + Math.min(54, this.inventorySize);
    }

    private void initVisibleItems() {
        this.visibleItems = new Inventory(this.inventorySize);
        int index = 0;
        for (final ResourceLocation id : this.whitelist) {
            this.visibleItems.setItem(index, new ItemStack(ForgeRegistries.ITEMS.getValue(id)));
            ++index;
        }
    }

    private void initPlayerInventorySlots(final PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 9 + column * 18, 140 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(playerInventory, hotbarSlot, 9 + hotbarSlot * 18, 198));
        }
    }

    private void initCharmControllerSlots() {
        for (int rows = Math.min(6, this.inventorySize / 9), row = 0; row < rows; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new VaultCharmControllerSlot(this.visibleItems, column + row * 9, 9 + column * 18, 18 + row * 18));
            }
        }
    }

    public boolean canScroll() {
        return this.inventorySize > 54;
    }

    public void scrollTo(final float scroll) {
        if (scroll >= 1.0f && this.scrollDelta >= 1.0f) {
            return;
        }
        this.shiftInventoryIndexes(this.scrollDelta - scroll < 0.0f);
        this.updateVisibleItems();
        this.scrollDelta = scroll;
    }

    private void shiftInventoryIndexes(final boolean ascending) {
        if (ascending) {
            this.currentStart = Math.min(this.inventorySize - 54, this.currentStart + 9);
            this.currentEnd = Math.min(this.currentStart + 54, this.inventorySize);
        } else {
            this.currentStart = Math.max(0, this.currentStart - 9);
            this.currentEnd = Math.max(54, this.currentEnd - 9);
        }
    }

    private void updateVisibleItems() {
        for (int i = 0; i < this.getInventorySize() && i < 54; ++i) {
            final int whitelistIndex = this.currentStart + i;
            if (whitelistIndex >= this.whitelist.size()) {
                this.visibleItems.setItem(i, ItemStack.EMPTY);
//                this.lastSlots.set(i, ItemStack.EMPTY);
            } else {
                final ResourceLocation id = this.whitelist.get(whitelistIndex);
                final ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(id));
                this.visibleItems.setItem(i, stack);
//                this.lastSlots.add(i, stack);
            }
        }
    }

    public boolean stillValid(final PlayerEntity playerIn) {
        return true;
    }

    public ItemStack quickMoveStack(final PlayerEntity playerIn, final int index) {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = this.getSlot(index);
        if (!slot.hasItem()) {
            return stack;
        }
        final ItemStack slotStack = slot.getItem();
        stack = slotStack.copy();
        if (slot instanceof VaultCharmControllerSlot) {
            this.whitelist.remove(slot.getItem().getItem().getRegistryName());
            slot.set(ItemStack.EMPTY);
            this.updateVisibleItems();
            return ItemStack.EMPTY;
        }
        if (this.whitelist.size() < this.inventorySize && !this.whitelist.contains(stack.getItem().getRegistryName())) {
            this.whitelist.add(stack.getItem().getRegistryName());
            final float pitch = MathUtilities.randomFloat(0.9f, 1.1f);
            playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.FUNGUS_BREAK, SoundCategory.PLAYERS, 0.7f, pitch);
            this.updateVisibleItems();
            return ItemStack.EMPTY;
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
        if (slotStack.getCount() == stack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(playerIn, slotStack);
        this.updateVisibleItems();
        return stack;
    }

    public ItemStack clicked(final int slotId, final int dragType, final ClickType clickTypeIn, final PlayerEntity player) {
        final Slot slot = (slotId >= 0) ? this.getSlot(slotId) : null;
        if (slot instanceof VaultCharmControllerSlot) {
            if (slot.hasItem()) {
                this.whitelist.remove(slot.getItem().getItem().getRegistryName());
                slot.set(ItemStack.EMPTY);
                this.updateVisibleItems();
                return ItemStack.EMPTY;
            }
            if (!player.inventory.getCarried().isEmpty()) {
                final ItemStack stack = player.inventory.getCarried().copy();
                if (!this.whitelist.contains(stack.getItem().getRegistryName())) {
                    this.whitelist.add(stack.getItem().getRegistryName());
                    this.updateVisibleItems();
                    return ItemStack.EMPTY;
                }
            }
        }
        return super.clicked(slotId, dragType, clickTypeIn, player);
    }

    public boolean canTakeItemForPickAll(final ItemStack stack, final Slot slot) {
        return slot.index < this.invStartIndex && super.canTakeItemForPickAll(stack, slot);
    }

    public void removed(final PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
            VaultCharmData.get(sPlayer.getLevel()).updateWhitelist(sPlayer, this.whitelist);
        }
        super.removed(player);
    }

    public int getInventorySize() {
        return this.inventorySize;
    }

    public List<ResourceLocation> getWhitelist() {
        return this.whitelist;
    }

    public int getCurrentAmountWhitelisted() {
        return this.whitelist.size();
    }

    public class VaultCharmControllerSlot extends Slot {
        public VaultCharmControllerSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }

        public boolean mayPlace(@Nonnull final ItemStack stack) {
            if (this.hasItem()) {
                return false;
            }
            if (stack.getItem() == ModItems.VAULT_CHARM) {
                return false;
            }
            final ResourceLocation id = stack.getItem().getRegistryName();
            return !VaultCharmControllerContainer.this.whitelist.contains(id);
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
