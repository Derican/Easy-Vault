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


public class VaultCharmControllerContainer
        extends Container {
    public IInventory visibleItems;
    private final int inventorySize;
    private final List<ResourceLocation> whitelist;
    private final int invStartIndex;
    private final int invEndIndex;
    private int currentStart = 0;
    private int currentEnd = 53;

    private float scrollDelta = 0.0F;

    public VaultCharmControllerContainer(int windowId, PlayerInventory playerInventory, CompoundNBT data) {
        super(ModContainers.VAULT_CHARM_CONTROLLER_CONTAINER, windowId);

        VaultCharmData.VaultCharmInventory vaultCharmInventory = VaultCharmData.VaultCharmInventory.fromNbt(data);
        this.inventorySize = vaultCharmInventory.getSize();
        this.whitelist = vaultCharmInventory.getWhitelist();

        initVisibleItems();
        initPlayerInventorySlots(playerInventory);
        initCharmControllerSlots();

        this.invStartIndex = 36;
        this.invEndIndex = 36 + Math.min(54, this.inventorySize);
    }


    private void initVisibleItems() {
        this.visibleItems = (IInventory) new Inventory(this.inventorySize);
        int index = 0;
        for (ResourceLocation id : this.whitelist) {
            this.visibleItems.setItem(index, new ItemStack((IItemProvider) ForgeRegistries.ITEMS.getValue(id)));
            index++;
        }
    }

    private void initPlayerInventorySlots(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 9 + column * 18, 140 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 9 + hotbarSlot * 18, 198));
        }
    }

    private void initCharmControllerSlots() {
        int rows = Math.min(6, this.inventorySize / 9);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new VaultCharmControllerSlot(this.visibleItems, column + row * 9, 9 + column * 18, 18 + row * 18));
            }
        }
    }


    public boolean canScroll() {
        return (this.inventorySize > 54);
    }

    public void scrollTo(float scroll) {
        if (scroll >= 1.0F && this.scrollDelta >= 1.0F)
            return;
        shiftInventoryIndexes((this.scrollDelta - scroll < 0.0F));
        updateVisibleItems();

        this.scrollDelta = scroll;
    }

    private void shiftInventoryIndexes(boolean ascending) {
        if (ascending) {
            this.currentStart = Math.min(this.inventorySize - 54, this.currentStart + 9);
            this.currentEnd = Math.min(this.currentStart + 54, this.inventorySize);
        } else {
            this.currentStart = Math.max(0, this.currentStart - 9);
            this.currentEnd = Math.max(54, this.currentEnd - 9);
        }
    }

    private void updateVisibleItems() {
        for (int i = 0; i < getInventorySize() &&
                i < 54; i++) {
            int whitelistIndex = this.currentStart + i;
            if (whitelistIndex >= this.whitelist.size()) {
                this.visibleItems.setItem(i, ItemStack.EMPTY);
//                this.lastSlots.set(i, ItemStack.EMPTY);
            } else {

                ResourceLocation id = this.whitelist.get(whitelistIndex);
                ItemStack stack = new ItemStack((IItemProvider) ForgeRegistries.ITEMS.getValue(id));
                this.visibleItems.setItem(i, stack);
//                this.lastSlots.add(i, stack);
            }
        }
    }


    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }


    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);

        if (!slot.hasItem()) return stack;
        ItemStack slotStack = slot.getItem();
        stack = slotStack.copy();

        if (slot instanceof VaultCharmControllerSlot) {
            this.whitelist.remove(slot.getItem().getItem().getRegistryName());
            slot.set(ItemStack.EMPTY);
            updateVisibleItems();
            return ItemStack.EMPTY;
        }
        if (this.whitelist.size() < this.inventorySize &&
                !this.whitelist.contains(stack.getItem().getRegistryName())) {
            this.whitelist.add(stack.getItem().getRegistryName());
            float pitch = MathUtilities.randomFloat(0.9F, 1.1F);
            playerIn.level.playSound(null, playerIn.blockPosition(), SoundEvents.FUNGUS_BREAK, SoundCategory.PLAYERS, 0.7F, pitch);
            updateVisibleItems();
            return ItemStack.EMPTY;
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

        if (slotStack.getCount() == stack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(playerIn, slotStack);
        updateVisibleItems();
        return stack;
    }


    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        Slot slot = (slotId >= 0) ? getSlot(slotId) : null;
        if (slot instanceof VaultCharmControllerSlot) {
            if (slot.hasItem()) {
                this.whitelist.remove(slot.getItem().getItem().getRegistryName());
                slot.set(ItemStack.EMPTY);
                updateVisibleItems();
                return ItemStack.EMPTY;
            }
            if (!player.inventory.getCarried().isEmpty()) {
                ItemStack stack = player.inventory.getCarried().copy();
                if (!this.whitelist.contains(stack.getItem().getRegistryName())) {

                    this.whitelist.add(stack.getItem().getRegistryName());
                    updateVisibleItems();
                    return ItemStack.EMPTY;
                }
            }
        }

        return super.clicked(slotId, dragType, clickTypeIn, player);
    }


    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        if (slot.index >= this.invStartIndex) return false;
        return super.canTakeItemForPickAll(stack, slot);
    }


    public void removed(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
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

    public class VaultCharmControllerSlot
            extends Slot {
        public VaultCharmControllerSlot(IInventory inventory, int index, int xPosition, int yPosition) {
            super(inventory, index, xPosition, yPosition);
        }


        public boolean mayPlace(@Nonnull ItemStack stack) {
            if (hasItem()) {
                return false;
            }
            if (stack.getItem() == ModItems.VAULT_CHARM) return false;

            ResourceLocation id = stack.getItem().getRegistryName();
            return !VaultCharmControllerContainer.this.whitelist.contains(id);
        }


        public int getMaxStackSize() {
            return 1;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\VaultCharmControllerContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */