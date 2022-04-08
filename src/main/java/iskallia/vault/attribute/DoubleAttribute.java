package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;


public class DoubleAttribute
        extends NumberAttribute<Double> {
    public DoubleAttribute() {
    }

    public DoubleAttribute(VAttribute.Modifier<Double> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putDouble("BaseValue", getBaseValue().doubleValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(Double.valueOf(nbt.getDouble("BaseValue")));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
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
                if (getType() == NumberAttribute.Type.SET)
                    return modifier;
                if (getType() == NumberAttribute.Type.ADD)
                    return Double.valueOf(value.doubleValue() + modifier.doubleValue());
                if (getType() == NumberAttribute.Type.MULTIPLY) {
                    return Double.valueOf(value.doubleValue() * modifier.doubleValue());
                }

                return value;
            }
        }
    }

    public static class Operator extends NumberAttribute.Generator.Operator<Double> {
        public Double apply(Double value, Double modifier) {
            if (getType() == NumberAttribute.Type.SET) return modifier;
            if (getType() == NumberAttribute.Type.ADD)
                return Double.valueOf(value.doubleValue() + modifier.doubleValue());
            if (getType() == NumberAttribute.Type.MULTIPLY)
                return Double.valueOf(value.doubleValue() * modifier.doubleValue());
            return value;
        }


        public Operator(NumberAttribute.Type type) {
            super(type);
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\DoubleAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */