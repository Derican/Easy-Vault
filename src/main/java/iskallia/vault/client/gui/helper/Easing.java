package iskallia.vault.client.gui.helper;

import java.util.function.Function;

public enum Easing {
    CONSTANT_ONE(x -> 1.0F),
    LINEAR_IN(x -> x),
    LINEAR_OUT(x -> Float.valueOf(1.0F - x.floatValue())),
    EASE_IN_OUT_SINE(x -> Float.valueOf(-((float) Math.cos(Math.PI * x.floatValue()) - 1.0F) / 2.0F)),
    EXPO_OUT(x -> Float
            .valueOf((x.floatValue() == 1.0F) ? 1.0F : (1.0F - (float) Math.pow(2.0D, (-10.0F * x.floatValue()))))),
    EASE_OUT_BOUNCE(x -> {
        float n1 = 7.5625F;
        float d1 = 2.75F;
        return (x.floatValue() < 1.0F / d1) ? Float.valueOf(n1 * x.floatValue() * x.floatValue())
                : ((x.floatValue() < 2.0F / d1) ? Float.valueOf(
                n1 * (x = Float.valueOf(x.floatValue() - 1.5F / d1)).floatValue() * x.floatValue() + 0.75F)
                : ((x.floatValue() < 2.5D / d1)
                ? Float.valueOf(n1 * (x = Float.valueOf(x.floatValue() - 2.25F / d1)).floatValue()
                * x.floatValue() + 0.9375F)
                : Float.valueOf(n1 * (x = Float.valueOf(x.floatValue() - 2.625F / d1)).floatValue()
                * x.floatValue() + 0.984375F)));
    });

    Function<Float, Float> function;

    Easing(Function<Float, Float> function) {
        this.function = function;
    }

    public Function<Float, Float> getFunction() {
        return this.function;
    }

    public float calc(float time) {
        return ((Float) this.function.apply(Float.valueOf(time))).floatValue();
    }
}

/*
 * Location:
 * C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\
 * helper\Easing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version: 1.1.3
 */