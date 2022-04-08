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

public class SoulShardTraderData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_SoulShardTrader";
    private static final Random rand = new Random();

    private long nextReset = 0L;
    private long seed = 0L;
    private final Map<Integer, SelectedTrade> trades = new HashMap<>();

    public SoulShardTraderData() {
        this("the_vault_SoulShardTrader");
    }

    public SoulShardTraderData(String name) {
        super(name);
    }

    public void resetDailyTrades() {
        resetTrades();
        Vault.LOGGER.info("Reset SoulShard Trades!");
    }

    public void resetTrades() {
        this.trades.clear();
        for (int i = 0; i < 3; i++) {
            this.trades.put(Integer.valueOf(i), new SelectedTrade(ModConfigs.SOUL_SHARD.getRandomTrade(rand)));
        }
        if (ModConfigs.RAID_EVENT_CONFIG.isEnabled()) {
            ItemStack eventSeal = new ItemStack((IItemProvider) ModItems.CRYSTAL_SEAL_RAID);
            ItemVaultCrystalSeal.setEventKey(eventSeal, "raid");
            SelectedTrade eventTrade = new SelectedTrade(eventSeal, ModConfigs.RAID_EVENT_CONFIG.getSoulShardTradeCost());
            eventTrade.isInfinite = true;
            this.trades.put(Integer.valueOf(0), eventTrade);
        }

        this.nextReset = System.currentTimeMillis() / 1000L + Duration.ofDays(1L).getSeconds();
        setDirty();
    }

    public boolean useTrade(int tradeId) {
        SelectedTrade trade = this.trades.get(Integer.valueOf(tradeId));
        if (trade != null && trade.isInfinite) {
            return true;
        }
        this.trades.remove(Integer.valueOf(tradeId));
        setDirty();
        return true;
    }

    public Map<Integer, SelectedTrade> getTrades() {
        return Collections.unmodifiableMap(this.trades);
    }

    public long getSeed() {
        return this.seed;
    }

    public void nextSeed() {
        Random r = new Random(this.seed);
        for (int i = 0; i < 3; i++) {
            r.nextLong();
        }
        this.seed = r.nextLong();
        setDirty();
    }


    public void setDirty() {
        super.setDirty();
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), getUpdatePacket());
    }

    public ShardTradeMessage getUpdatePacket() {
        return new ShardTradeMessage(ModConfigs.SOUL_SHARD.getShardTradePrice(), this.seed, getTrades());
    }

    public void syncTo(ServerPlayerEntity player) {
        ModNetwork.CHANNEL.sendTo(getUpdatePacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void openTradeContainer(ServerPlayerEntity player) {
        NetworkHooks.openGui(player, new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return (ITextComponent) new StringTextComponent("Soul Shard Trading");
            }


            public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
                return (Container) new ShardTradeContainer(windowId, playerInventory, new SoulShardTraderData.TraderInventory());
            }
        });
    }


    public void load(CompoundNBT tag) {
        this.trades.clear();

        this.seed = tag.getLong("seed");
        ListNBT list = tag.getList("trades", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT tradeTag = list.getCompound(i);
            this.trades.put(Integer.valueOf(tradeTag.getInt("index")), new SelectedTrade(tradeTag.getCompound("trade")));
        }
        this.nextReset = tag.getLong("nextReset");
        if (this.nextReset < System.currentTimeMillis() / 1000L) {
            this.seed = rand.nextLong();
            resetTrades();
        }
    }


    public CompoundNBT save(CompoundNBT tag) {
        ListNBT list = new ListNBT();
        this.trades.forEach((index, trade) -> {
            CompoundNBT tradeTag = new CompoundNBT();
            tradeTag.putInt("index", index.intValue());
            tradeTag.put("trade", (INBT) trade.serialize());
            list.add(tradeTag);
        });
        tag.put("trades", (INBT) list);
        tag.putLong("seed", this.seed);
        tag.putLong("nextReset", this.nextReset);
        return tag;
    }

    public static SoulShardTraderData get(ServerWorld world) {
        return get(world.getServer());
    }

    public static SoulShardTraderData get(MinecraftServer server) {
        return (SoulShardTraderData) server.overworld().getDataStorage().computeIfAbsent(SoulShardTraderData::new, "the_vault_SoulShardTrader");
    }

    public class TraderInventory
            implements IInventory {
        public int getContainerSize() {
            return 4;
        }


        public boolean isEmpty() {
            return false;
        }


        public ItemStack getItem(int index) {
            if (index == 0) {
                return new ItemStack((IItemProvider) ModItems.UNKNOWN_ITEM);
            }
            SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) SoulShardTraderData.this.trades.get(Integer.valueOf(index - 1));
            if (trade != null) {
                return trade.getStack();
            }

            return ItemStack.EMPTY;
        }


        public ItemStack removeItem(int index, int count) {
            if (count <= 0) {
                return ItemStack.EMPTY;
            }
            if (index == 0)
                return new ItemStack((IItemProvider) ModItems.UNKNOWN_ITEM);
            if (count > 0) {
                SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) SoulShardTraderData.this.trades.get(Integer.valueOf(index - 1));
                if (trade != null) {
                    return trade.getStack();
                }
            }
            return ItemStack.EMPTY;
        }


        public ItemStack removeItemNoUpdate(int index) {
            if (index == 0) {
                return new ItemStack((IItemProvider) ModItems.UNKNOWN_ITEM);
            }
            SoulShardTraderData.SelectedTrade trade = (SoulShardTraderData.SelectedTrade) SoulShardTraderData.this.trades.get(Integer.valueOf(index - 1));
            if (trade != null) {
                return trade.getStack();
            }

            return ItemStack.EMPTY;
        }


        public void setItem(int index, ItemStack stack) {
        }


        public void setChanged() {
            SoulShardTraderData.this.setDirty();
        }


        public boolean stillValid(PlayerEntity player) {
            return true;
        }


        public void clearContent() {
        }
    }


    public static class SelectedTrade {
        private final ItemStack stack;

        private final int shardCost;
        private boolean isInfinite = false;

        public SelectedTrade(SoulShardConfig.ShardTrade trade) {
            this.stack = trade.getItem();
            this.shardCost = MathUtilities.getRandomInt(trade.getMinPrice(), trade.getMaxPrice() + 1);
        }

        public SelectedTrade(ItemStack stack, int shardCost) {
            this.stack = stack;
            this.shardCost = shardCost;
        }

        public SelectedTrade(CompoundNBT tag) {
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
            CompoundNBT tag = new CompoundNBT();
            tag.put("stack", (INBT) this.stack.serializeNBT());
            tag.putInt("cost", this.shardCost);
            tag.putBoolean("infinite", this.isInfinite);
            return tag;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\SoulShardTraderData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */