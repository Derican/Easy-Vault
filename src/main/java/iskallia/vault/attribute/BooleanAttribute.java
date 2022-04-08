package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;
import java.util.Random;


public class BooleanAttribute
        extends PooledAttribute<Boolean> {
    public BooleanAttribute() {
    }

    public BooleanAttribute(VAttribute.Modifier<Boolean> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putBoolean("BaseValue", getBaseValue().booleanValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(Boolean.valueOf(nbt.getBoolean("BaseValue")));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends PooledAttribute.Generator<Boolean, Generator.Operator> {
        public Boolean getDefaultValue(Random random) {
            return Boolean.valueOf(false);
        }

        public static class Operator extends PooledAttribute.Generator.Operator<Boolean> {
            @Expose
            protected String type;

            public Operator(BooleanAttribute.Type type) {
                this.type = type.name();
            }

            public BooleanAttribute.Type getType() throws Throwable {
                return BooleanAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            public Boolean apply(Boolean value, Boolean modifier) {
                try {
                    if (getType() == BooleanAttribute.Type.SET)
                        return modifier;
                    if (getType() == BooleanAttribute.Type.AND)
                        return Boolean.valueOf(value.booleanValue() & modifier.booleanValue());
                    if (getType() == BooleanAttribute.Type.OR)
                        return Boolean.valueOf(value.booleanValue() | modifier.booleanValue());
                    if (getType() == BooleanAttribute.Type.XOR)
                        return Boolean.valueOf(value.booleanValue() ^ modifier.booleanValue());
                    if (getType() == BooleanAttribute.Type.NAND)
                        return Boolean.valueOf(((value.booleanValue() & modifier.booleanValue()) == false));
                    if (getType() == BooleanAttribute.Type.NOR)
                        return Boolean.valueOf(((value.booleanValue() | modifier.booleanValue()) == false));
                    if (getType() == BooleanAttribute.Type.XNOR) {
                        return Boolean.valueOf((value == modifier));
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator extends PooledAttribute.Generator.Operator<Boolean> {
        public Boolean apply(Boolean value, Boolean modifier) {
            try {
                if (getType() == BooleanAttribute.Type.SET) return modifier;
                if (getType() == BooleanAttribute.Type.AND)
                    return Boolean.valueOf(value.booleanValue() & modifier.booleanValue());
                if (getType() == BooleanAttribute.Type.OR)
                    return Boolean.valueOf(value.booleanValue() | modifier.booleanValue());
                if (getType() == BooleanAttribute.Type.XOR)
                    return Boolean.valueOf(value.booleanValue() ^ modifier.booleanValue());
                if (getType() == BooleanAttribute.Type.NAND)
                    return Boolean.valueOf(((value.booleanValue() & modifier.booleanValue()) == false));
                if (getType() == BooleanAttribute.Type.NOR)
                    return Boolean.valueOf(((value.booleanValue() | modifier.booleanValue()) == false));
                if (getType() == BooleanAttribute.Type.XNOR) return Boolean.valueOf((value == modifier));
            } catch (Throwable e) {

            }
            return value;
        }

        @Expose
        protected String type;

        public Operator(BooleanAttribute.Type type) {
            this.type = type.name();
        }

        public BooleanAttribute.Type getType() throws Throwable {
            return BooleanAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
        }
    }

    public enum Type {
        SET, AND, OR, XOR, NAND, NOR, XNOR;

        public static Optional<Type> getByName(String name) {
            for (Type value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return Optional.of(value);
                }
            }

            return Optional.empty();
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\BooleanAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */