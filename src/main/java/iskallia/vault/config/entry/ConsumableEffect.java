package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class ConsumableEffect {
    @Expose
    private String effectId;
    @Expose
    private final int amplifier;
    @Expose
    private final int duration;

    public ConsumableEffect(String effectId, int amplifier, int duration) {
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.duration = duration;
        this.ambient = false;
        this.showParticles = false;
        this.showIcon = false;
    }

    @Expose
    private boolean ambient;
    @Expose
    private boolean showParticles;
    @Expose
    private boolean showIcon;

    public String getEffectId() {
        return this.effectId;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean shouldShowParticles() {
        return this.showParticles;
    }

    public boolean shouldShowIcon() {
        return this.showIcon;
    }

    public ConsumableEffect ambient() {
        this.ambient = true;
        return this;
    }

    public ConsumableEffect showParticles() {
        this.showParticles = true;
        return this;
    }

    public ConsumableEffect showIcon() {
        this.showIcon = true;
        return this;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\ConsumableEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */