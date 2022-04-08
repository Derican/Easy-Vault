package iskallia.vault.container.inventory;

import iskallia.vault.client.ClientShardTradeData;
import iskallia.vault.config.SoulShardConfig;
import iskallia.vault.container.base.AbstractPlayerSensitiveContainer;
import iskallia.vault.container.slot.InfiniteSellSlot;
import iskallia.vault.container.slot.PlayerSensitiveSlot;
import iskallia.vault.container.slot.SellSlot;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModContainers;
import iskallia.vault.item.ItemShardPouch;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.LogicalSide;

import java.util.Random;

public class ShardTradeContainer
        extends AbstractPlayerSensitiveContainer {
    public ShardTradeContainer(int windowId, PlayerInventory inventory) {
        this(windowId, inventory, (IInventory) new Inventory(4));
    }

    public ShardTradeContainer(int windowId, PlayerInventory inventory, IInventory tradeView) {
        super(ModContainers.SHARD_TRADE_CONTAINER, windowId);

        initSlots(inventory, tradeView);
    }


    private void initSlots(PlayerInventory playerInventory, IInventory tradeView) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 102 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 160));
        }


        addSlot((Slot) new RandomSellSlot(tradeView, 0, 34, 36));

        addSlot((Slot) new ShardSellSlot(tradeView, 1, 146, 10));
        addSlot((Slot) new ShardSellSlot(tradeView, 2, 146, 38));
        addSlot((Slot) new ShardSellSlot(tradeView, 3, 146, 66));
    }


    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            if (slot instanceof PlayerSensitiveSlot) {
                slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, true);
            }
            itemstack = slotStack.copy();

            if (index >= 0 && index < 36 &&
                    !mergeItemStack(slot, player, slotStack, 36, 40)) {
                return ItemStack.EMPTY;
            }

            if (index >= 0 && index < 27) {
                if (!mergeItemStack(slot, player, slotStack, 27, 36)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!mergeItemStack(slot, player, slotStack, 0, 27)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slot, player, slotStack, 0, 36)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() > 0) {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (slot instanceof PlayerSensitiveSlot) {
                ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, false);
            }
            slot.onTake(player, slotStack);
        }

        return itemstack;
    }

    protected boolean mergeItemStack(Slot fromSlot, PlayerEntity player, ItemStack stack, int startIndex, int endIndex) {
        boolean didMerge = false;
        int i;
        for (i = startIndex; i < endIndex && !stack.isEmpty(); i++) {
            Slot targetSlot = this.slots.get(i);
            ItemStack slotStack = targetSlot.getItem();

            if (targetSlot.mayPlace(stack)) {

                if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.tagMatches(stack, slotStack)) {
                    int targetSize = slotStack.getCount() + stack.getCount();
                    int targetMaxSize = targetSlot.getMaxStackSize(slotStack);

                    if (targetSize <= targetMaxSize) {
                        stack.shrink(stack.getCount());
                        fromSlot.remove(stack.getCount());

                        slotStack.setCount(targetSize);
                        targetSlot.setChanged();
                        didMerge = true;
                    } else if (slotStack.getCount() < targetMaxSize) {
                        int takenAmount = targetMaxSize - slotStack.getCount();
                        stack.shrink(takenAmount);
                        fromSlot.remove(takenAmount);

                        slotStack.setCount(targetMaxSize);
                        targetSlot.setChanged();
                        didMerge = true;
                    }
                }
            }
        }

        if (stack.isEmpty()) {
            return didMerge;
        }

        for (i = startIndex; i < endIndex; i++) {
            Slot targetSlot = this.slots.get(i);
            ItemStack slotStack = targetSlot.getItem();

            if (slotStack.isEmpty() && targetSlot.mayPlace(stack)) {
                if (stack.getCount() > targetSlot.getMaxStackSize(stack)) {
                    targetSlot.set(stack.split(targetSlot.getMaxStackSize(stack)));
                } else {
                    targetSlot.set(stack.split(stack.getCount()));
                }

                targetSlot.setChanged();
                didMerge = true;

                break;
            }
        }
        return didMerge;
    }


    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public static class ShardSellSlot
            extends SellSlot implements PlayerSensitiveSlot {
        public ShardSellSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }


        public boolean mayPickup(PlayerEntity player) {
            int shardCost;
            if (player instanceof ServerPlayerEntity) {
                SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) tradeData.getTrades().get(Integer.valueOf(getSlotIndex() - 1));
                if (trade == null) {
                    return false;
                }
                shardCost = trade.getShardCost();
            } else {
                Tuple<ItemStack, Integer> trade = ClientShardTradeData.getTradeInfo(getSlotIndex() - 1);
                if (trade == null) {
                    return false;
                }
                shardCost = ((Integer) trade.getB()).intValue();
            }
            int count = ItemShardPouch.getShardCount(player.inventory);
            return (count >= shardCost);
        }


        public ItemStack modifyTakenStack(PlayerEntity player, ItemStack taken, LogicalSide side, boolean simulate) {
            int shardCost;
            if (player instanceof ServerPlayerEntity) {
                SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) tradeData.getTrades().get(Integer.valueOf(getSlotIndex() - 1));
                if (trade == null) {
                    return ItemStack.EMPTY;
                }
                shardCost = trade.getShardCost();
            } else {
                Tuple<ItemStack, Integer> trade = ClientShardTradeData.getTradeInfo(getSlotIndex() - 1);
                if (trade == null) {
                    return ItemStack.EMPTY;
                }
                shardCost = ((Integer) trade.getB()).intValue();
            }
            if (ItemShardPouch.reduceShardAmount(player.inventory, shardCost, simulate)) {
                if (side.isServer() && !simulate) {
                    if (player instanceof ServerPlayerEntity) {
                        SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                        tradeData.useTrade(getSlotIndex() - 1);

                        SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) tradeData.getTrades().get(Integer.valueOf(getSlotIndex() - 1));
                        if (trade != null && trade.isInfinite()) {

//                            player.containerMenu.lastSlots.set(this.index, ItemStack.EMPTY);
                            getItem().grow(1);
                        }
                    }
                    if (player.containerMenu != null) {
                        player.containerMenu.broadcastChanges();
                    }
                }
                return taken;
            }
            return ItemStack.EMPTY;
        }
    }

    public static class RandomSellSlot
            extends InfiniteSellSlot implements PlayerSensitiveSlot {
        public RandomSellSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }


        public boolean mayPickup(PlayerEntity player) {
            int shardCost, count = ItemShardPouch.getShardCount(player.inventory);

            if (player.getCommandSenderWorld().isClientSide()) {
                shardCost = ClientShardTradeData.getRandomTradeCost();
            } else {
                shardCost = ModConfigs.SOUL_SHARD.getShardTradePrice();
            }
            return (count >= shardCost);
        }

        public ItemStack modifyTakenStack(PlayerEntity player, ItemStack taken, LogicalSide side, boolean simulate) {
            long tradeSeed;
            SoulShardConfig.ShardTrade trade;
            if (player instanceof ServerPlayerEntity) {
                SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                tradeSeed = tradeData.getSeed();
                if (!simulate) {
                    tradeData.nextSeed();
                }
            } else {
                tradeSeed = ClientShardTradeData.getTradeSeed();
                if (!simulate) {
                    ClientShardTradeData.nextSeed();
                }
            }

            Random rand = new Random(tradeSeed);

            if (player.getCommandSenderWorld().isClientSide()) {
                trade = (SoulShardConfig.ShardTrade) ClientShardTradeData.getShardTrades().getRandom(rand);
            } else {
                trade = ModConfigs.SOUL_SHARD.getRandomTrade(rand);
            }
            if (trade != null) {
                int shardCost;
                if (player.getCommandSenderWorld().isClientSide()) {
                    shardCost = ClientShardTradeData.getRandomTradeCost();
                } else {
                    shardCost = ModConfigs.SOUL_SHARD.getShardTradePrice();
                }
                if (ItemShardPouch.reduceShardAmount(player.inventory, shardCost, simulate)) {
                    if (side.isServer() && !simulate &&
                            player.containerMenu != null) {
                        player.containerMenu.broadcastChanges();
                    }

                    return trade.getItem().copy();
                }
            }
            return ItemStack.EMPTY;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\ShardTradeContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */