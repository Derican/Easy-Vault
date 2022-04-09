// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;

public class FloatRangeEntry
{
    @Expose
    private final float min;
    @Expose
    private final float max;
    
    public FloatRangeEntry(final float min, final float max) {
        this.min = min;
        this.max = max;
    }
    
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
