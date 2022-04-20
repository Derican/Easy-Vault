package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class GlobalTraderConfig extends Config {
    @Expose
    public Map<Integer, GlobalTradePool> POOLS;
    @Expose
    public int SKIN_UPDATE_RATE_SECONDS;

    public GlobalTraderConfig() {
        this.POOLS = new HashMap<Integer, GlobalTradePool>();
    }

    @Override
    public String getName() {
        return "global_trader";
    }

    @Override
    protected void reset() {
        this.SKIN_UPDATE_RATE_SECONDS = 60;
        for (int i = 0; i < 4; ++i) {
            this.POOLS.put(i, new GlobalTradePool());
        }
    }

    public GlobalTradePool getPool(final int id) {
        return this.POOLS.get(id);
    }

    public static class GlobalTradePool implements INBTSerializable<CompoundNBT> {
        @Expose
        public WeightedList<Trade> POOL;
        @Expose
        public int TOTAL_TRADE_COUNT;
        @Expose
        public int MAX_TRADES;
        @Expose
        public int RESET_INTERVAL_HOUR;
        @Expose
        public String skin;
        private int currentTick;
        private boolean isReset;
        private int resetCounter;

        public GlobalTradePool() {
            this.POOL = new WeightedList<Trade>();
            this.currentTick = 0;
            this.isReset = false;
            this.resetCounter = 0;
            this.TOTAL_TRADE_COUNT = 3;
            this.MAX_TRADES = 1;
            this.RESET_INTERVAL_HOUR = 24;
            this.POOL.add(new Trade(new Product(Items.APPLE, 8, null), null, new Product(Items.GOLDEN_APPLE, 1, null)), 20);
            this.POOL.add(new Trade(new Product(Items.GOLDEN_APPLE, 8, null), null, new Product(Items.ENCHANTED_GOLDEN_APPLE, 1, null)), 3);
            this.POOL.add(new Trade(new Product(Items.STONE, 64, null), null, new Product(Items.COBBLESTONE, 64, null)), 20);
            this.POOL.add(new Trade(new Product(Items.DIORITE, 64, null), null, new Product(Items.DIAMOND, 8, null)), 20);
            final CompoundNBT nbt = new CompoundNBT();
            final ListNBT enchantments = new ListNBT();
            final CompoundNBT knockback = new CompoundNBT();
            knockback.putString("id", "minecraft:knockback");
            knockback.putInt("lvl", 10);
            enchantments.add(knockback);
            nbt.put("Enchantments", (INBT) enchantments);
            nbt.put("ench", (INBT) enchantments);
            this.POOL.add(new Trade(new Product(Items.ENCHANTED_GOLDEN_APPLE, 8, null), null, new Product(Items.STICK, 1, nbt)), 1);
        }

        public void tick() {
            if (this.currentTick++ >= this.RESET_INTERVAL_HOUR * 60 * 60 * 20) {
                this.currentTick = 0;
                this.isReset = true;
            }
            if (this.isReset && this.resetCounter++ >= 600) {
                this.isReset = false;
                this.resetCounter = 0;
            }
        }

        public boolean ready() {
            return this.currentTick == 0 && this.isReset;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("CurrentTick", this.currentTick);
            nbt.putBoolean("IsReset", this.isReset);
            nbt.putInt("ResetCounter", this.resetCounter);
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.currentTick = nbt.getInt("CurrentTick");
            this.isReset = nbt.getBoolean("IsReset");
            this.resetCounter = nbt.getInt("ResetCounter");
        }
    }
}
