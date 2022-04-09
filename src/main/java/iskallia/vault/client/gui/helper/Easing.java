// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.gui.helper;

import java.util.function.Function;

public enum Easing
{
    CONSTANT_ONE(x -> 1.0f), 
    LINEAR_IN(x -> x), 
    LINEAR_OUT(x -> 1.0f - x), 
    EASE_IN_OUT_SINE(x -> -((float)Math.cos(3.141592653589793 * x) - 1.0f) / 2.0f), 
    EXPO_OUT(x -> (x == 1.0f) ? 1.0f : (1.0f - (float)Math.pow(2.0, -10.0f * x))),
    EASE_OUT_BOUNCE(x -> {
        float n1 = 7.5625F;
        float d1 = 2.75F;
        return (x < 1.0F / d1) ? Float.valueOf(n1 * x * x)
                : ((x < 2.0F / d1) ? Float.valueOf(
                n1 * (x = x - 1.5F / d1) * x + 0.75F)
                : ((x < 2.5D / d1)
                ? Float.valueOf(n1 * (x = x - 2.25F / d1)
                * x + 0.9375F)
                : Float.valueOf(n1 * (x = x - 2.625F / d1)
                * x + 0.984375F)));
    });
    
    Function<Float, Float> function;
    
    private Easing(final Function<Float, Float> function) {
        this.function = function;
    }
    
    public Function<Float, Float> getFunction() {
        return this.function;
    }
    
    public float calc(final float time) {
        return this.function.apply(time);
    }
}
