package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class MagnetEntry {
    @Expose
    private float speed;
    @Expose
    private float radius;
    @Expose
    private boolean pullItems;

    public MagnetEntry(float speed, float radius, boolean pullItems, boolean pullExperience, boolean pullInstantly, int maxDurability) {
        this.speed = speed;
        this.radius = radius;
        this.pullItems = pullItems;
        this.pullExperience = pullExperience;
        this.pullInstantly = pullInstantly;
        this.maxDurability = maxDurability;
    }

    @Expose
    private boolean pullExperience;
    @Expose
    private boolean pullInstantly;
    @Expose
    private int maxDurability;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\MagnetEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */