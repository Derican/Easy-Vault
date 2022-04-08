package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;

public class RangeEntry {
    @Expose
    private final int min;
    @Expose
    private final int max;

    public RangeEntry(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getRandom() {
        return MathUtilities.getRandomInt(this.min, this.max);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\RangeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */