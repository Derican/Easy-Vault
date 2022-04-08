package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;

public class FloatRangeEntry {
    @Expose
    private final float min;

    public FloatRangeEntry(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Expose
    private final float max;

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getRandom() {
        return MathUtilities.randomFloat(this.min, this.max);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\FloatRangeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */