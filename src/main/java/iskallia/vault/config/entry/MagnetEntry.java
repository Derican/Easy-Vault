// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class MagnetEntry
{
    @Expose
    private float speed;
    @Expose
    private float radius;
    @Expose
    private boolean pullItems;
    @Expose
    private boolean pullExperience;
    @Expose
    private boolean pullInstantly;
    @Expose
    private int maxDurability;
    
    public MagnetEntry(final float speed, final float radius, final boolean pullItems, final boolean pullExperience, final boolean pullInstantly, final int maxDurability) {
        this.speed = speed;
        this.radius = radius;
        this.pullItems = pullItems;
        this.pullExperience = pullExperience;
        this.pullInstantly = pullInstantly;
        this.maxDurability = maxDurability;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public float getRadius() {
        return this.radius;
    }
    
    public boolean shouldPullItems() {
        return this.pullItems;
    }
    
    public boolean shouldPullExperience() {
        return this.pullExperience;
    }
    
    public boolean shouldPullInstantly() {
        return this.pullInstantly;
    }
    
    public int getMaxDurability() {
        return this.maxDurability;
    }
}
