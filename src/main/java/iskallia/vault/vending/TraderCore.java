package iskallia.vault.vending;

import iskallia.vault.util.nbt.INBTSerializable;
import iskallia.vault.util.nbt.NBTSerialize;

public class TraderCore
        implements INBTSerializable {
    @NBTSerialize
    private String NAME;
    @NBTSerialize
    private Trade TRADE;

    public TraderCore(String name, Trade trade) {
        this.NAME = name;
        this.TRADE = trade;
    }

    public TraderCore() {
    }

    public String getName() {
        return (this.NAME == null) ? "Trader" : this.NAME;
    }

    public void setName(String name) {
        this.NAME = name;
    }

    public Trade getTrade() {
        return this.TRADE;
    }

    public void setTrade(Trade trade) {
        this.TRADE = trade;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\vending\TraderCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */