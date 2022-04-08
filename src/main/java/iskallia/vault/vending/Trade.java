package iskallia.vault.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;


public class Trade
        implements INBTSerializable {
    @Expose
    @NBTSerialize
    protected Product buy;
    @Expose
    @NBTSerialize
    protected Product extra;
    @Expose
    @NBTSerialize
    protected Product sell;
    @Expose
    @NBTSerialize
    protected int max_trades;
    @Expose
    @NBTSerialize
    protected int times_traded;

    public Trade() {
        this.max_trades = -1;
    }

    public Trade(Product buy, Product extra, Product sell) {
        this.buy = buy;
        this.extra = extra;
        this.sell = sell;
    }

    public Trade(Product buy, Product extra, Product sell, int max_trades, int times_traded) {
        this(buy, extra, sell);
        this.max_trades = max_trades;
        this.times_traded = times_traded;
    }

    public Product getBuy() {
        return this.buy;
    }

    public Product getExtra() {
        return this.extra;
    }

    public Product getSell() {
        return this.sell;
    }

    public int getMaxTrades() {
        return this.max_trades;
    }

    public int getTimesTraded() {
        return this.times_traded;
    }

    public void setTimesTraded(int amount) {
        this.times_traded = amount;
    }

    public int getTradesLeft() {
        if (this.max_trades == -1) return -1;
        return Math.max(0, this.max_trades - this.times_traded);
    }

    public void onTraded() {
        this.times_traded++;
    }

    public boolean wasTradeUsed() {
        if (this.max_trades == -1) {
            return true;
        }
        return (this.times_traded > 0);
    }

    public boolean isValid() {
        if (this.buy == null || !this.buy.isValid())
            return false;
        if (this.sell == null || !this.sell.isValid())
            return false;
        if (this.extra != null && !this.extra.isValid())
            return false;
        return true;
    }


    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (getClass() != obj.getClass()) {
            return false;
        }
        Trade trade = (Trade) obj;
        return (trade.sell.equals(this.sell) && trade.buy.equals(this.buy));
    }

    public Trade setMaxTrades(int amount) {
        this.max_trades = amount;
        return this;
    }

    public Trade copy() {
        return new Trade(getBuy(), getExtra(), getSell(), this.max_trades, this.times_traded);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\vending\Trade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */