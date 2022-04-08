package iskallia.vault.config.entry.vending;

import com.google.gson.annotations.Expose;
import iskallia.vault.vending.Trade;


public class TradeEntry {
    @Expose
    protected ProductEntry buy;
    @Expose
    protected ProductEntry sell;
    @Expose
    protected int max_trades;

    public TradeEntry() {
    }

    public TradeEntry(ProductEntry buy, ProductEntry sell, int max_trades) {
        this.buy = buy;
        this.sell = sell;
        this.max_trades = max_trades;
    }

    public Trade toTrade() {
        return new Trade(this.buy.toProduct(), null, this.sell.toProduct(), this.max_trades, 0);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\vending\TradeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */