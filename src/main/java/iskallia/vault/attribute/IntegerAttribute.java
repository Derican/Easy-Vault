package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;


public class IntegerAttribute
        extends NumberAttribute<Integer> {
    public IntegerAttribute() {
    }

    public IntegerAttribute(VAttribute.Modifier<Integer> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putInt("BaseValue", getBaseValue().intValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(Integer.valueOf(nbt.getInt("BaseValue")));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends NumberAttribute.Generator<Integer, Generator.Operator> {
        public Integer getDefaultValue(Random random) {
            return Integer.valueOf(0);
        }

        public static Operator of(NumberAttribute.Type type) {
            return new Operator(type);
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Integer> {
            public Operator(NumberAttribute.Type type) {
                super(type);
            }

            public Integer apply(Integer value, Integer modifier) {
                if (getType() == NumberAttribute.Type.SET)
                    return modifier;
                if (getType() == NumberAttribute.Type.ADD)
                    return Integer.valueOf(value.intValue() + modifier.intValue());
                if (getType() == NumberAttribute.Type.MULTIPLY) {
                    return Integer.valueOf(value.intValue() * modifier.intValue());
                }

                return value;
            }
        }
    }

    public static class Operator extends NumberAttribute.Generator.Operator<Integer> {
        public Integer apply(Integer value, Integer modifier) {
            if (getType() == NumberAttribute.Type.SET) return modifier;
            if (getType() == NumberAttribute.Type.ADD) return Integer.valueOf(value.intValue() + modifier.intValue());
            if (getType() == NumberAttribute.Type.MULTIPLY)
                return Integer.valueOf(value.intValue() * modifier.intValue());
            return value;
        }


        public Operator(NumberAttribute.Type type) {
            super(type);
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\IntegerAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */