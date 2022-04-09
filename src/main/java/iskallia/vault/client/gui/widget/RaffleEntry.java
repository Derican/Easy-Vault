// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.widget;

import java.awt.*;

public class RaffleEntry
{
    protected Rectangle bounds;
    protected String occupantName;
    protected int typeIndex;
    
    public RaffleEntry(final String occupantName, final int typeIndex) {
        this.occupantName = occupantName;
        this.typeIndex = typeIndex;
    }
    
    public void setBounds(final Rectangle bounds) {
        this.bounds = bounds;
    }
    
    public Rectangle getBounds() {
        return this.bounds;
    }
    
    public String getOccupantName() {
        return this.occupantName;
    }
    
    public int getTypeIndex() {
        return this.typeIndex;
    }
}
