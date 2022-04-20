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

public class ShardTradeContainer extends AbstractPlayerSensitiveContainer {
    public ShardTradeContainer(final int windowId, final PlayerInventory inventory) {
        this(windowId, inventory, (IInventory) new Inventory(4));
    }

    public ShardTradeContainer(final int windowId, final PlayerInventory inventory, final IInventory tradeView) {
        super(ModContainers.SHARD_TRADE_CONTAINER, windowId);
        this.initSlots(inventory, tradeView);
    }

    private void initSlots(final PlayerInventory playerInventory, final IInventory tradeView) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot((IInventory) playerInventory, column + row * 9 + 9, 8 + column * 18, 102 + row * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot((IInventory) playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 160));
        }
        this.addSlot((Slot) new RandomSellSlot(tradeView, 0, 34, 36));
        this.addSlot((Slot) new ShardSellSlot(tradeView, 1, 146, 10));
        this.addSlot((Slot) new ShardSellSlot(tradeView, 2, 146, 38));
        this.addSlot((Slot) new ShardSellSlot(tradeView, 3, 146, 66));
    }

    public ItemStack quickMoveStack(final PlayerEntity player, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            if (slot instanceof PlayerSensitiveSlot) {
                slotStack = ((PlayerSensitiveSlot) slot).modifyTakenStack(player, slotStack, true);
            }
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && !this.mergeItemStack(slot, player, slotStack, 36, 40)) {
                return ItemStack.EMPTY;
            }
            if (index >= 0 && index < 27) {
                if (!this.mergeItemStack(slot, player, slotStack, 27, 36)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!this.mergeItemStack(slot, player, slotStack, 0, 27)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slot, player, slotStack, 0, 36)) {
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

    protected boolean mergeItemStack(final Slot fromSlot, final PlayerEntity player, final ItemStack stack, final int startIndex, final int endIndex) {
        boolean didMerge = false;
        for (int i = startIndex; i < endIndex && !stack.isEmpty(); ++i) {
            final Slot targetSlot = this.slots.get(i);
            final ItemStack slotStack = targetSlot.getItem();
            if (targetSlot.mayPlace(stack)) {
                if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.tagMatches(stack, slotStack)) {
                    final int targetSize = slotStack.getCount() + stack.getCount();
                    final int targetMaxSize = targetSlot.getMaxStackSize(slotStack);
                    if (targetSize <= targetMaxSize) {
                        stack.shrink(stack.getCount());
                        fromSlot.remove(stack.getCount());
                        slotStack.setCount(targetSize);
                        targetSlot.setChanged();
                        didMerge = true;
                    } else if (slotStack.getCount() < targetMaxSize) {
                        final int takenAmount = targetMaxSize - slotStack.getCount();
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
        for (int i = startIndex; i < endIndex; ++i) {
            final Slot targetSlot = this.slots.get(i);
            final ItemStack slotStack = targetSlot.getItem();
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

    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public static class ShardSellSlot extends SellSlot implements PlayerSensitiveSlot {
        public ShardSellSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        public boolean mayPickup(final PlayerEntity player) {
            int shardCost;
            if (player instanceof ServerPlayerEntity) {
                final SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                final SoulShardTraderData.SelectedTrade trade = tradeData.getTrades().get(this.getSlotIndex() - 1);
                if (trade == null) {
                    return false;
                }
                shardCost = trade.getShardCost();
            } else {
                final Tuple<ItemStack, Integer> trade2 = ClientShardTradeData.getTradeInfo(this.getSlotIndex() - 1);
                if (trade2 == null) {
                    return false;
                }
                shardCost = (int) trade2.getB();
            }
            final int count = ItemShardPouch.getShardCount(player.inventory);
            return count >= shardCost;
        }

        @Override
        public ItemStack modifyTakenStack(final PlayerEntity player, final ItemStack taken, final LogicalSide side, final boolean simulate) {
            int shardCost;
            if (player instanceof ServerPlayerEntity) {
                final SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                final SoulShardTraderData.SelectedTrade trade = tradeData.getTrades().get(this.getSlotIndex() - 1);
                if (trade == null) {
                    return ItemStack.EMPTY;
                }
                shardCost = trade.getShardCost();
            } else {
                final Tuple<ItemStack, Integer> trade2 = ClientShardTradeData.getTradeInfo(this.getSlotIndex() - 1);
                if (trade2 == null) {
                    return ItemStack.EMPTY;
                }
                shardCost = (int) trade2.getB();
            }
            if (ItemShardPouch.reduceShardAmount(player.inventory, shardCost, simulate)) {
                if (side.isServer() && !simulate) {
                    if (player instanceof ServerPlayerEntity) {
                        final SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
                        tradeData.useTrade(this.getSlotIndex() - 1);
                        final SoulShardTraderData.SelectedTrade trade = tradeData.getTrades().get(this.getSlotIndex() - 1);
                        if (trade != null && trade.isInfinite()) {
//                            player.containerMenu.lastSlots.set(this.index, ItemStack.EMPTY);
                            this.getItem().grow(1);
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

    public static class RandomSellSlot extends InfiniteSellSlot implements PlayerSensitiveSlot {
        public RandomSellSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        public boolean mayPickup(final PlayerEntity player) {
            final int count = ItemShardPouch.getShardCount(player.inventory);
            int shardCost;
            if (player.getCommandSenderWorld().isClientSide()) {
                shardCost = ClientShardTradeData.getRandomTradeCost();
            } else {
                shardCost = ModConfigs.SOUL_SHARD.getShardTradePrice();
            }
            return count >= shardCost;
        }

        @Override
        public ItemStack modifyTakenStack(final PlayerEntity player, final ItemStack taken, final LogicalSide side, final boolean simulate) {
            long tradeSeed;
            if (player instanceof ServerPlayerEntity) {
                final SoulShardTraderData tradeData = SoulShardTraderData.get(((ServerPlayerEntity) player).getLevel());
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
            final Random rand = new Random(tradeSeed);
            SoulShardConfig.ShardTrade trade;
            if (player.getCommandSenderWorld().isClientSide()) {
                trade = ClientShardTradeData.getShardTrades().getRandom(rand);
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
                    if (side.isServer() && !simulate && player.containerMenu != null) {
                        player.containerMenu.broadcastChanges();
                    }
                    return trade.getItem().copy();
                }
            }
            return ItemStack.EMPTY;
        }
    }
}
