package iskallia.vault.vending;

import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;

public class TraderCore implements INBTSerializable {
    @NBTSerialize
    private String NAME;
    @NBTSerialize
    private Trade TRADE;

    public TraderCore(final String name, final Trade trade) {
        this.NAME = name;
        this.TRADE = trade;
    }

    public TraderCore() {
    }

    public String getName() {
        return (this.NAME == null) ? "Trader" : this.NAME;
    }

    public void setName(final String name) {
        this.NAME = name;
    }

    public Trade getTrade() {
        return this.TRADE;
    }

    public void setTrade(final Trade trade) {
        this.TRADE = trade;
    }
}
