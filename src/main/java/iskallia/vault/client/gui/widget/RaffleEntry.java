package iskallia.vault.client.gui.widget;

import java.awt.*;

public class RaffleEntry {
    protected Rectangle bounds;
    protected String occupantName;
    protected int typeIndex;

    public RaffleEntry(String occupantName, int typeIndex) {
        this.occupantName = occupantName;
        this.typeIndex = typeIndex;
    }

    public void setBounds(Rectangle bounds) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\RaffleEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */