package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.consumable.ConsumableType;

import java.util.ArrayList;
import java.util.List;

public class ConsumableEntry {
    @Expose
    private String itemId;
    @Expose
    private List<ConsumableEffect> effects = new ArrayList<>();
    @Expose
    private boolean absorption;
    @Expose
    private float absorptionAmount;
    @Expose
    private String type = "";

    public ConsumableEntry(String itemId, boolean absorption, float absorptionAmount, ConsumableType type) {
        this.itemId = itemId;
        this.absorption = absorption;
        this.absorptionAmount = absorptionAmount;
        this.type = type.toString();
    }

    public ConsumableEntry addEffect(ConsumableEffect entry) {
        this.effects.add(entry);
        return this;
    }

    public String getItemId() {
        return this.itemId;
    }

    public boolean shouldAddAbsorption() {
        return this.absorption;
    }

    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }

    public List<ConsumableEffect> getEffects() {
        return this.effects;
    }

    public ConsumableType getType() {
        return ConsumableType.fromString(this.type);
    }

    public boolean isPowerup() {
        return (ConsumableType.fromString(this.type) == ConsumableType.POWERUP);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\ConsumableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */