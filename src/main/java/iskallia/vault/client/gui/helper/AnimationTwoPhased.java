package iskallia.vault.client.gui.helper;

public class AnimationTwoPhased {
    protected Easing initEasing = Easing.LINEAR_IN;
    protected Easing endEasing = Easing.LINEAR_OUT;

    protected boolean paused;
    protected float value;
    protected float initValue;
    protected float midValue;
    protected float endValue;
    protected int elapsedTime;
    protected int animationTime;

    public AnimationTwoPhased(float initValue, float midValue, float endValue, int animationTime) {
        this.initValue = initValue;
        this.midValue = midValue;
        this.endValue = endValue;
        this.elapsedTime = 0;
        this.animationTime = animationTime;
        this.value = initValue;
        this.paused = true;
    }

    public AnimationTwoPhased withEasing(Easing initEasing, Easing endEasing) {
        this.initEasing = initEasing;
        this.endEasing = endEasing;
        return this;
    }

    public float getValue() {
        return this.value;
    }

    public void tick(int deltaTime) {
        if (this.paused)
            return;
        this.elapsedTime = Math.min(this.elapsedTime + deltaTime, this.animationTime);

        float elapsedPercent = getElapsedPercentage();

        if (this.elapsedTime < 0.5F * this.animationTime) {
            float value = this.initEasing.calc(2.0F * elapsedPercent);
            this.value = value * (this.midValue - this.initValue) + this.initValue;
        } else {

            float value = this.initEasing.calc(2.0F * elapsedPercent - 1.0F);
            this.value = value * (this.endValue - this.midValue) + this.midValue;
        }

        if (this.elapsedTime >= this.animationTime) {
            pause();
        }
    }

    public void changeValues(float initValue, float midValue, float endValue) {
        this.initValue = initValue;
        this.midValue = midValue;
        this.endValue = endValue;
        float elapsedPercent = getElapsedPercentage();
        if (this.elapsedTime < 0.5F * this.animationTime) {
            float value = this.initEasing.calc(2.0F * elapsedPercent);
            this.value = value * (midValue - initValue) + initValue;
        } else {
            float value = this.initEasing.calc(2.0F * elapsedPercent - 1.0F);
            this.value = value * (endValue - midValue) + midValue;
        }
    }

    public float getElapsedPercentage() {
        return this.elapsedTime / this.animationTime;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\AnimationTwoPhased.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */