package iskallia.vault.container.slot;

import iskallia.vault.block.entity.EtchingVendorControllerTileEntity;
import iskallia.vault.container.inventory.EtchingTradeContainer;
import iskallia.vault.entity.EtchingVendorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EtchingBuySlot extends SlotItemHandler {
    private final EtchingTradeContainer etchingTradeContainer;
    private final int tradeId;

    public EtchingBuySlot(final EtchingTradeContainer etchingTradeContainer, final IItemHandler itemHandler, final int tradeId, final int index, final int xPosition, final int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.etchingTradeContainer = etchingTradeContainer;
        this.tradeId = tradeId;
    }

    public boolean mayPlace(@Nonnull final ItemStack stack) {
        return false;
    }

    public boolean mayPickup(final PlayerEntity player) {
        final EtchingVendorControllerTileEntity.EtchingTrade trade = this.getAssociatedTrade();
        if (trade == null) {
            return false;
        }
        final int count = this.getInputSlot().getItem().getCount();
        return trade.getRequiredPlatinum() <= count && !trade.isSold();
    }

    public Slot getInputSlot() {
        return this.etchingTradeContainer.getSlot(36 + this.tradeId * 2);
    }

    @Nullable
    public EtchingVendorControllerTileEntity.EtchingTrade getAssociatedTrade() {
        final EtchingVendorEntity vendor = this.etchingTradeContainer.getVendor();
        if (vendor == null) {
            return null;
        }
        final EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return null;
        }
        return controllerTile.getTrade(this.tradeId);
    }

    public ItemStack onTake(final PlayerEntity player, final ItemStack stack) {
        final EtchingVendorEntity vendor = this.etchingTradeContainer.getVendor();
        if (vendor == null) {
            return ItemStack.EMPTY;
        }
        final EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return ItemStack.EMPTY;
        }
        final EtchingVendorControllerTileEntity.EtchingTrade trade = this.getAssociatedTrade();
        if (trade == null) {
            return ItemStack.EMPTY;
        }
        this.getInputSlot().remove(trade.getRequiredPlatinum());
        this.set(ItemStack.EMPTY);
        trade.setSold(true);
        controllerTile.sendUpdates();
        vendor.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0f, (vendor.level.random.nextFloat() - vendor.level.random.nextFloat()) * 0.2f + 1.0f);
        return stack;
    }
}
