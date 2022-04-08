package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;


public class FloatAttribute
        extends NumberAttribute<Float> {
    public FloatAttribute() {
    }

    public FloatAttribute(VAttribute.Modifier<Float> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putFloat("BaseValue", getBaseValue().floatValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(Float.valueOf(nbt.getFloat("BaseValue")));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends NumberAttribute.Generator<Float, Generator.Operator> {
        public Float getDefaultValue(Random random) {
            return Float.valueOf(0.0F);
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Float> {
            public Operator(NumberAttribute.Type type) {
                super(type);
            }

            public Float apply(Float value, Float modifier) {
                if (getType() == NumberAttribute.Type.SET)
                    return modifier;
                if (getType() == NumberAttribute.Type.ADD)
                    return Float.valueOf(value.floatValue() + modifier.floatValue());
                if (getType() == NumberAttribute.Type.MULTIPLY) {
                    return Float.valueOf(value.floatValue() * modifier.floatValue());
                }

                return value;
            }
        }
    }

    public static class Operator extends NumberAttribute.Generator.Operator<Float> {
        public Float apply(Float value, Float modifier) {
            if (getType() == NumberAttribute.Type.SET) return modifier;
            if (getType() == NumberAttribute.Type.ADD) return Float.valueOf(value.floatValue() + modifier.floatValue());
            if (getType() == NumberAttribute.Type.MULTIPLY)
                return Float.valueOf(value.floatValue() * modifier.floatValue());
            return value;
        }


        public Operator(NumberAttribute.Type type) {
            super(type);
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\FloatAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */