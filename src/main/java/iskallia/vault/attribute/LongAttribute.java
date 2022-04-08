package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;


public class LongAttribute
        extends NumberAttribute<Long> {
    public LongAttribute() {
    }

    public LongAttribute(VAttribute.Modifier<Long> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putLong("BaseValue", getBaseValue().longValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(Long.valueOf(nbt.getLong("BaseValue")));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends NumberAttribute.Generator<Long, Generator.Operator> {
        public Long getDefaultValue(Random random) {
            return Long.valueOf(0L);
        }

        public static Operator of(NumberAttribute.Type type) {
            return new Operator(type);
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Long> {
            public Operator(NumberAttribute.Type type) {
                super(type);
            }

            public Long apply(Long value, Long modifier) {
                if (getType() == NumberAttribute.Type.SET)
                    return modifier;
                if (getType() == NumberAttribute.Type.ADD)
                    return Long.valueOf(value.longValue() + modifier.longValue());
                if (getType() == NumberAttribute.Type.MULTIPLY) {
                    return Long.valueOf(value.longValue() * modifier.longValue());
                }

                return value;
            }
        }
    }

    public static class Operator extends NumberAttribute.Generator.Operator<Long> {
        public Long apply(Long value, Long modifier) {
            if (getType() == NumberAttribute.Type.SET) return modifier;
            if (getType() == NumberAttribute.Type.ADD) return Long.valueOf(value.longValue() + modifier.longValue());
            if (getType() == NumberAttribute.Type.MULTIPLY)
                return Long.valueOf(value.longValue() * modifier.longValue());
            return value;
        }


        public Operator(NumberAttribute.Type type) {
            super(type);
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\LongAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */