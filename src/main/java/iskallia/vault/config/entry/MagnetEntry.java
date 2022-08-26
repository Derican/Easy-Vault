package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class MagnetEntry {
    @Expose
    private final float speed;
    @Expose
    private final float radius;
    @Expose
    private final boolean pullItems;
    @Expose
    private final boolean pullExperience;
    @Expose
    private final boolean pullInstantly;
    @Expose
    private final int maxDurability;

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
