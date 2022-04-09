// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

public class ConsumableEffect
{
    @Expose
    private String effectId;
    @Expose
    private final int amplifier;
    @Expose
    private final int duration;
    @Expose
    private boolean ambient;
    @Expose
    private boolean showParticles;
    @Expose
    private boolean showIcon;
    
    public ConsumableEffect(final String effectId, final int amplifier, final int duration) {
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.duration = duration;
        this.ambient = false;
        this.showParticles = false;
        this.showIcon = false;
    }
    
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
