// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class DoubleAttribute extends NumberAttribute<Double> {
    public DoubleAttribute() {
    }

    public DoubleAttribute(final VAttribute.Modifier<Double> modifier) {
        super(modifier);
    }

    @Override
    public void write(final CompoundNBT nbt) {
        nbt.putDouble("BaseValue", (double) this.getBaseValue());
    }

    @Override
    public void read(final CompoundNBT nbt) {
        this.setBaseValue(nbt.getDouble("BaseValue"));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(final Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends NumberAttribute.Generator<Double, Generator.Operator> {
        public Double getDefaultValue(Random random) {
            return Double.valueOf(0.0D);
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Double> {
            public Operator(NumberAttribute.Type type) {
                super(type);
            }

            public Double apply(Double value, Double modifier) {
                try {
                    if (getType() == NumberAttribute.Type.SET)
                        return modifier;
                    if (getType() == NumberAttribute.Type.ADD)
                        return Double.valueOf(value.doubleValue() + modifier.doubleValue());
                    if (getType() == NumberAttribute.Type.MULTIPLY) {
                        return Double.valueOf(value.doubleValue() * modifier.doubleValue());
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator extends NumberAttribute.Generator.Operator<Double> {
        public Double apply(Double value, Double modifier) {
            try {
                if (getType() == NumberAttribute.Type.SET) return modifier;
                if (getType() == NumberAttribute.Type.ADD)
                    return Double.valueOf(value.doubleValue() + modifier.doubleValue());
                if (getType() == NumberAttribute.Type.MULTIPLY)
                    return Double.valueOf(value.doubleValue() * modifier.doubleValue());
            } catch (Throwable e) {

            }
            return value;
        }


        public Operator(NumberAttribute.Type type) {
            super(type);
        }
    }
}
