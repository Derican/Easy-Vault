package iskallia.vault.item.gear;


public class GearModelProperties {
    boolean pieceOfSet;
    boolean allowTransmogrification;

    public GearModelProperties makePieceOfSet() {
        this.pieceOfSet = true;
        return this;
    }

    public GearModelProperties allowTransmogrification() {
        this.allowTransmogrification = true;
        return this;
    }


    public boolean isPieceOfSet() {
        return this.pieceOfSet;
    }

    public boolean doesAllowTransmogrification() {
        return this.allowTransmogrification;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\GearModelProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */