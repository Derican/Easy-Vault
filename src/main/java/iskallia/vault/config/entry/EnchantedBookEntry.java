package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class EnchantedBookEntry {
    @Expose
    private final int extraLevel;
    @Expose
    private final int levelNeeded;
    @Expose
    private final String prefix;
    @Expose
    private final String colorHex;

    public EnchantedBookEntry(final int extraLevel, final int levelNeeded, final String prefix, final String colorHex) {
        this.extraLevel = extraLevel;
        this.levelNeeded = levelNeeded;
        this.prefix = prefix;
        this.colorHex = colorHex;
    }

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
