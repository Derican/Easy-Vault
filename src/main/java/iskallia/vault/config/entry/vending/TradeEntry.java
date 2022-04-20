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

    public TradeEntry(final ProductEntry buy, final ProductEntry sell, final int max_trades) {
        this.buy = buy;
        this.sell = sell;
        this.max_trades = max_trades;
    }

    public Trade toTrade() {
        return new Trade(this.buy.toProduct(), null, this.sell.toProduct(), this.max_trades, 0);
    }
}
