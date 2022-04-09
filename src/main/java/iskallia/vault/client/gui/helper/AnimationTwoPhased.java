// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.helper;

public class AnimationTwoPhased
{
    protected Easing initEasing;
    protected Easing endEasing;
    protected boolean paused;
    protected float value;
    protected float initValue;
    protected float midValue;
    protected float endValue;
    protected int elapsedTime;
    protected int animationTime;
    
    public AnimationTwoPhased(final float initValue, final float midValue, final float endValue, final int animationTime) {
        this.initEasing = Easing.LINEAR_IN;
        this.endEasing = Easing.LINEAR_OUT;
        this.initValue = initValue;
        this.midValue = midValue;
        this.endValue = endValue;
        this.elapsedTime = 0;
        this.animationTime = animationTime;
        this.value = initValue;
        this.paused = true;
    }
    
    public AnimationTwoPhased withEasing(final Easing initEasing, final Easing endEasing) {
        this.initEasing = initEasing;
        this.endEasing = endEasing;
        return this;
    }
    
    public float getValue() {
        return this.value;
    }
    
    public void tick(final int deltaTime) {
        if (this.paused) {
            return;
        }
        this.elapsedTime = Math.min(this.elapsedTime + deltaTime, this.animationTime);
        final float elapsedPercent = this.getElapsedPercentage();
        if (this.elapsedTime < 0.5f * this.animationTime) {
            final float value = this.initEasing.calc(2.0f * elapsedPercent);
            this.value = value * (this.midValue - this.initValue) + this.initValue;
        }
        else {
            final float value = this.initEasing.calc(2.0f * elapsedPercent - 1.0f);
            this.value = value * (this.endValue - this.midValue) + this.midValue;
        }
        if (this.elapsedTime >= this.animationTime) {
            this.pause();
        }
    }
    
    public void changeValues(final float initValue, final float midValue, final float endValue) {
        this.initValue = initValue;
        this.midValue = midValue;
        this.endValue = endValue;
        final float elapsedPercent = this.getElapsedPercentage();
        if (this.elapsedTime < 0.5f * this.animationTime) {
            final float value = this.initEasing.calc(2.0f * elapsedPercent);
            this.value = value * (midValue - initValue) + initValue;
        }
        else {
            final float value = this.initEasing.calc(2.0f * elapsedPercent - 1.0f);
            this.value = value * (endValue - midValue) + midValue;
        }
    }
    
    public float getElapsedPercentage() {
        return this.elapsedTime / (float)this.animationTime;
    }
    
    public void pause() {
        this.paused = true;
    }
    
    public void play() {
        this.paused = false;
    }
    
    public void reset() {
        this.value = this.initValue;
        this.elapsedTime = 0;
    }
}
