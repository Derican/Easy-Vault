// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear;

public class GearModelProperties
{
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
