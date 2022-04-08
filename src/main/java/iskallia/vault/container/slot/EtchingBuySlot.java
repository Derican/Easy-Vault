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

public class EtchingBuySlot
        extends SlotItemHandler {
    private final EtchingTradeContainer etchingTradeContainer;
    private final int tradeId;

    public EtchingBuySlot(EtchingTradeContainer etchingTradeContainer, IItemHandler itemHandler, int tradeId, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.etchingTradeContainer = etchingTradeContainer;
        this.tradeId = tradeId;
    }


    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }


    public boolean mayPickup(PlayerEntity player) {
        EtchingVendorControllerTileEntity.EtchingTrade trade = getAssociatedTrade();
        if (trade == null) {
            return false;
        }
        int count = getInputSlot().getItem().getCount();
        return (trade.getRequiredPlatinum() <= count && !trade.isSold());
    }

    public Slot getInputSlot() {
        return this.etchingTradeContainer.getSlot(36 + this.tradeId * 2);
    }

    @Nullable
    public EtchingVendorControllerTileEntity.EtchingTrade getAssociatedTrade() {
        EtchingVendorEntity vendor = this.etchingTradeContainer.getVendor();
        if (vendor == null) {
            return null;
        }
        EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return null;
        }
        return controllerTile.getTrade(this.tradeId);
    }


    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        EtchingVendorEntity vendor = this.etchingTradeContainer.getVendor();
        if (vendor == null) {
            return ItemStack.EMPTY;
        }
        EtchingVendorControllerTileEntity controllerTile = vendor.getControllerTile();
        if (controllerTile == null) {
            return ItemStack.EMPTY;
        }
        EtchingVendorControllerTileEntity.EtchingTrade trade = getAssociatedTrade();
        if (trade == null) {
            return ItemStack.EMPTY;
        }
        getInputSlot().remove(trade.getRequiredPlatinum());
        set(ItemStack.EMPTY);
        trade.setSold(true);
        controllerTile.sendUpdates();
        vendor.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, (vendor.level.random.nextFloat() - vendor.level.random.nextFloat()) * 0.2F + 1.0F);
        return stack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\slot\EtchingBuySlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */