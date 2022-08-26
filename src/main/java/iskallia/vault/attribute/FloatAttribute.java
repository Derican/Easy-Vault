package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class FloatAttribute extends NumberAttribute<Float> {
    public FloatAttribute() {
    }

    public FloatAttribute(final VAttribute.Modifier<Float> modifier) {
        super(modifier);
    }

    @Override
    public void write(final CompoundNBT nbt) {
        nbt.putFloat("BaseValue", this.getBaseValue());
    }

    @Override
    public void read(final CompoundNBT nbt) {
        this.setBaseValue(nbt.getFloat("BaseValue"));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(final Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator extends NumberAttribute.Generator<Float, Generator.Operator> {
        @Override
        public Float getDefaultValue(final Random random) {
            return 0.0f;
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Float> {
            public Operator(final Type type) {
                super(type);
            }

            @Override
            public Float apply(final Float value, final Float modifier) {
                try {
                    if (this.getType() == Type.SET) {
                        return modifier;
                    }
                    if (this.getType() == Type.ADD) {
                        return value + modifier;
                    }
                    if (this.getType() == Type.MULTIPLY) {
                        return value * modifier;
                    }
                } catch (Throwable e) {

                }
                return value;
            }
        }
    }
}
