package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class EnchantedBookEntry {
    @Expose
    private int extraLevel;
    @Expose
    private int levelNeeded;

    public EnchantedBookEntry(int extraLevel, int levelNeeded, String prefix, String colorHex) {
        this.extraLevel = extraLevel;
        this.levelNeeded = levelNeeded;
        this.prefix = prefix;
        this.colorHex = colorHex;
    }

    @Expose
    private String prefix;
    @Expose
    private String colorHex;

    public int getExtraLevel() {
        return this.extraLevel;
    }

    public int getLevelNeeded() {
        return this.levelNeeded;
    }

    public String getColorHex() {
        return this.colorHex;
    }

    public String getPrefix() {
        return this.prefix;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\EnchantedBookEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */