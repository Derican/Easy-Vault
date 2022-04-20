package iskallia.vault.container.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FilteredSlotWrapper extends Slot {
    private final Slot decorated;
    private final Predicate<ItemStack> canInsert;

    public FilteredSlotWrapper(final Slot decorated, final Predicate<ItemStack> canInsert) {
        super(decorated.container, decorated.getSlotIndex(), decorated.x, decorated.y);
        this.canInsert = canInsert;
        this.index = decorated.index;
        this.decorated = decorated;
    }

    public void onQuickCraft(final ItemStack oldStackIn, final ItemStack newStackIn) {
        this.decorated.onQuickCraft(oldStackIn, newStackIn);
    }

    public ItemStack onTake(final PlayerEntity thePlayer, final ItemStack stack) {
        return this.decorated.onTake(thePlayer, stack);
    }

    public boolean mayPlace(final ItemStack stack) {
        return this.canInsert.test(stack) && this.decorated.mayPlace(stack);
    }

    public ItemStack getItem() {
        return this.decorated.getItem();
    }

    public boolean hasItem() {
        return this.decorated.hasItem();
    }

    public void set(final ItemStack stack) {
        this.decorated.set(stack);
    }

    public void setChanged() {
        this.decorated.setChanged();
    }

    public int getMaxStackSize() {
        return this.decorated.getMaxStackSize();
    }

    public int getMaxStackSize(final ItemStack stack) {
        return this.decorated.getMaxStackSize(stack);
    }

    @Nullable
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return (Pair<ResourceLocation, ResourceLocation>) this.decorated.getNoItemIcon();
    }

    public ItemStack remove(final int amount) {
        return this.decorated.remove(amount);
    }

    public boolean mayPickup(final PlayerEntity playerIn) {
        return this.decorated.mayPickup(playerIn);
    }

    public boolean isActive() {
        return this.decorated.isActive();
    }

    public int getSlotIndex() {
        return this.decorated.getSlotIndex();
    }

    public boolean isSameInventory(final Slot other) {
        return this.decorated.isSameInventory(other);
    }

    public Slot setBackground(final ResourceLocation atlas, final ResourceLocation sprite) {
        return this.decorated.setBackground(atlas, sprite);
    }
}
