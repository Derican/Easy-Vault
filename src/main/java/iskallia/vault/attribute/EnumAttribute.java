package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;
import java.util.Random;

public class EnumAttribute<E extends Enum<E>>
        extends PooledAttribute<E> {
    private final Class<E> enumClass;

    public EnumAttribute(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public EnumAttribute(Class<E> enumClass, VAttribute.Modifier<E> modifier) {
        super(modifier);
        this.enumClass = enumClass;
    }

    public Class<E> getEnumClass() {
        return this.enumClass;
    }


    public void write(CompoundNBT nbt) {
        nbt.putString("BaseValue", ((Enum) getBaseValue()).name());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(getEnumConstant(nbt.getString("BaseValue")));
    }

    public E getEnumConstant(String value) {
        try {
            return Enum.valueOf(getEnumClass(), value);
        } catch (Exception e) {
            Enum[] arrayOfEnum = (Enum[]) getEnumClass().getEnumConstants();
            return (arrayOfEnum.length == 0) ? null : (E) arrayOfEnum[0];
        }
    }

    public static <E extends Enum<E>> Generator<E> generator(Class<E> enumClass) {
        return new Generator<>();
    }

    public static <E extends Enum<E>> Generator.Operator<E> of(Type type) {
        return new Generator.Operator<>(type);
    }

    public static class Generator<E extends Enum<E>>
            extends PooledAttribute.Generator<E, Generator.Operator<E>> {
        public E getDefaultValue(Random random) {
            return null;
        }

        public static class Operator<E extends Enum<E>> extends PooledAttribute.Generator.Operator<E> {
            @Expose
            protected String type;

            public Operator(EnumAttribute.Type type) {
                this.type = type.name();
            }

            public EnumAttribute.Type getType() throws Throwable {
                return EnumAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            public E apply(E value, E modifier) {
                try {
                    if (getType() == EnumAttribute.Type.SET) {
                        return modifier;
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator<E extends Enum<E>> extends PooledAttribute.Generator.Operator<E> {
        public E apply(E value, E modifier) {
            try {
                if (getType() == EnumAttribute.Type.SET) return modifier;
            } catch (Throwable e) {

            }
            return value;
        }

        @Expose
        protected String type;

        public Operator(EnumAttribute.Type type) {
            this.type = type.name();
        }

        public EnumAttribute.Type getType() throws Throwable {
            return EnumAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
        }
    }

    public enum Type {
        SET;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\EnumAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */