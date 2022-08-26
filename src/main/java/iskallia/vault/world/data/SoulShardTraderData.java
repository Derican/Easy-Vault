package iskallia.vault.world.data;

import iskallia.vault.Vault;
import iskallia.vault.config.SoulShardConfig;
import iskallia.vault.container.inventory.ShardTradeContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.ItemVaultCrystalSeal;
import iskallia.vault.network.message.ShardTradeMessage;
import iskallia.vault.util.MathUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class SoulShardTraderData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_SoulShardTrader";
    private static final Random rand;
    private long nextReset;
    private long seed;
    private final Map<Integer, SelectedTrade> trades;

    public SoulShardTraderData() {
        this("the_vault_SoulShardTrader");
    }

    public SoulShardTraderData(final String name) {
        super(name);
        this.nextReset = 0L;
        this.seed = 0L;
        this.trades = new HashMap<Integer, SelectedTrade>();
    }

    public void resetDailyTrades() {
        this.resetTrades();
        Vault.LOGGER.info("Reset SoulShard Trades!");
    }

    public void resetTrades() {
        this.trades.clear();
        for (int i = 0; i < 3; ++i) {
            this.trades.put(i, new SelectedTrade(ModConfigs.SOUL_SHARD.getRandomTrade(SoulShardTraderData.rand)));
        }
        if (ModConfigs.RAID_EVENT_CONFIG.isEnabled()) {
            final ItemStack eventSeal = new ItemStack(ModItems.CRYSTAL_SEAL_RAID);
            ItemVaultCrystalSeal.setEventKey(eventSeal, "raid");
            final SelectedTrade eventTrade = new SelectedTrade(eventSeal, ModConfigs.RAID_EVENT_CONFIG.getSoulShardTradeCost());
            eventTrade.isInfinite = true;
            this.trades.put(0, eventTrade);
        }
        this.nextReset = System.currentTimeMillis() / 1000L + Duration.ofDays(1L).getSeconds();
        this.setDirty();
    }

    public boolean useTrade(final int tradeId) {
        final SelectedTrade trade = this.trades.get(tradeId);
        if (trade != null && trade.isInfinite) {
            return true;
        }
        this.trades.remove(tradeId);
        this.setDirty();
        return true;
    }

    public Map<Integer, SelectedTrade> getTrades() {
        return Collections.unmodifiableMap(this.trades);
    }

    public long getSeed() {
        return this.seed;
    }

    public void nextSeed() {
        final Random r = new Random(this.seed);
        for (int i = 0; i < 3; ++i) {
            r.nextLong();
        }
        this.seed = r.nextLong();
        this.setDirty();
    }

    public void setDirty() {
        super.setDirty();
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), this.getUpdatePacket());
    }

    public ShardTradeMessage getUpdatePacket() {
        return new ShardTradeMessage(ModConfigs.SOUL_SHARD.getShardTradePrice(), this.seed, this.getTrades());
    }

    public void syncTo(final ServerPlayerEntity player) {
        ModNetwork.CHANNEL.sendTo(this.getUpdatePacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void openTradeContainer(final ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Soul Shard Trading");
            }

            public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player) {
                return new ShardTradeContainer(windowId, playerInventory, new TraderInventory());
            }
        });
    }

    public void load(final CompoundNBT tag) {
        this.trades.clear();
        this.seed = tag.getLong("seed");
        final ListNBT list = tag.getList("trades", 10);
        for (int i = 0; i < list.size(); ++i) {
            final CompoundNBT tradeTag = list.getCompound(i);
            this.trades.put(tradeTag.getInt("index"), new SelectedTrade(tradeTag.getCompound("trade")));
        }
        this.nextReset = tag.getLong("nextReset");
        if (this.nextReset < System.currentTimeMillis() / 1000L) {
            this.seed = SoulShardTraderData.rand.nextLong();
            this.resetTrades();
        }
    }

    public CompoundNBT save(final CompoundNBT tag) {
        final ListNBT list = new ListNBT();
        this.trades.forEach((index, trade) -> {
            final CompoundNBT tradeTag = new CompoundNBT();
            tradeTag.putInt("index", index);
            tradeTag.put("trade", trade.serialize());
            list.add(tradeTag);
            return;
        });
        tag.put("trades", list);
        tag.putLong("seed", this.seed);
        tag.putLong("nextReset", this.nextReset);
        return tag;
    }

    public static SoulShardTraderData get(final ServerWorld world) {
        return get(world.getServer());
    }

    public static SoulShardTraderData get(final MinecraftServer server) {
        return (SoulShardTraderData) server.overworld().getDataStorage().computeIfAbsent((Supplier) SoulShardTraderData::new, "the_vault_SoulShardTrader");
    }

    static {
        rand = new Random();
    }

    public class TraderInventory implements IInventory {
        public int getContainerSize() {
            return 4;
        }

        public boolean isEmpty() {
            return false;
        }

        public ItemStack getItem(final int index) {
            if (index == 0) {
                return new ItemStack(ModItems.UNKNOWN_ITEM);
            }
            final SelectedTrade trade = SoulShardTraderData.this.trades.get(index - 1);
            if (trade != null) {
                return trade.getStack();
            }
            return ItemStack.EMPTY;
        }

        public ItemStack removeItem(final int index, final int count) {
            if (count <= 0) {
                return ItemStack.EMPTY;
            }
            if (index == 0) {
                return new ItemStack(ModItems.UNKNOWN_ITEM);
            }
            if (count > 0) {
                final SelectedTrade trade = SoulShardTraderData.this.trades.get(index - 1);
                if (trade != null) {
                    return trade.getStack();
                }
            }
            return ItemStack.EMPTY;
        }

        public ItemStack removeItemNoUpdate(final int index) {
            if (index == 0) {
                return new ItemStack(ModItems.UNKNOWN_ITEM);
            }
            final SelectedTrade trade = SoulShardTraderData.this.trades.get(index - 1);
            if (trade != null) {
                return trade.getStack();
            }
            return ItemStack.EMPTY;
        }

        public void setItem(final int index, final ItemStack stack) {
        }

        public void setChanged() {
            SoulShardTraderData.this.setDirty();
        }

        public boolean stillValid(final PlayerEntity player) {
            return true;
        }

        public void clearContent() {
        }
    }

    public static class SelectedTrade {
        private final ItemStack stack;
        private final int shardCost;
        private boolean isInfinite;

        public SelectedTrade(final SoulShardConfig.ShardTrade trade) {
            this.isInfinite = false;
            this.stack = trade.getItem();
            this.shardCost = MathUtilities.getRandomInt(trade.getMinPrice(), trade.getMaxPrice() + 1);
        }

        public SelectedTrade(final ItemStack stack, final int shardCost) {
            this.isInfinite = false;
            this.stack = stack;
            this.shardCost = shardCost;
        }

        public SelectedTrade(final CompoundNBT tag) {
            this.isInfinite = false;
            this.stack = ItemStack.of(tag.getCompound("stack"));
            this.shardCost = tag.getInt("cost");
            this.isInfinite = tag.getBoolean("infinite");
        }

        public int getShardCost() {
            return this.shardCost;
        }

        public ItemStack getStack() {
            return this.stack.copy();
        }

        public boolean isInfinite() {
            return this.isInfinite;
        }

        public CompoundNBT serialize() {
            final CompoundNBT tag = new CompoundNBT();
            tag.put("stack", this.stack.serializeNBT());
            tag.putInt("cost", this.shardCost);
            tag.putBoolean("infinite", this.isInfinite);
            return tag;
        }
    }
}
