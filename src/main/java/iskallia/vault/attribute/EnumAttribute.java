package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;
import java.util.Random;

public class EnumAttribute<E extends Enum<E>> extends PooledAttribute<E> {
    private final Class<E> enumClass;

    public EnumAttribute(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public EnumAttribute(final Class<E> enumClass, final VAttribute.Modifier<E> modifier) {
        super(modifier);
        this.enumClass = enumClass;
    }

    public Class<E> getEnumClass() {
        return this.enumClass;
    }

    @Override
    public void write(final CompoundNBT nbt) {
        nbt.putString("BaseValue", this.getBaseValue().name());
    }

    @Override
    public void read(final CompoundNBT nbt) {
        this.setBaseValue(this.getEnumConstant(nbt.getString("BaseValue")));
    }

    public E getEnumConstant(final String value) {
        try {
            return Enum.valueOf(this.getEnumClass(), value);
        } catch (final Exception e) {
            final E[] enumConstants = this.getEnumClass().getEnumConstants();
            return (E) ((enumConstants.length == 0) ? null : enumConstants[0]);
        }
    }

    public static <E extends Enum<E>> Generator<E> generator(final Class<E> enumClass) {
        return new Generator<E>();
    }

    public static <E extends Enum<E>> Generator.Operator<E> of(final Type type) {
        return new Generator.Operator<E>(type);
    }

    public static class Generator<E extends Enum<E>> extends PooledAttribute.Generator<E, Generator.Operator<E>> {
        @Override
        public E getDefaultValue(final Random random) {
            return null;
        }

        public static class Operator<E extends Enum<E>> extends PooledAttribute.Generator.Operator<E> {
            @Expose
            protected String type;

            public Operator(final Type type) {
                this.type = type.name();
            }

            public EnumAttribute.Type getType() throws Throwable {
                return EnumAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            @Override
            public E apply(final E value, final E modifier) {
                try {
                    if (this.getType() == Type.SET) {
                        return modifier;
                    }
                } catch (Throwable e) {

                }
                return value;
            }
        }
    }

    public enum Type {
        SET;

        public static Optional<Type> getByName(final String name) {
            for (final Type value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
}
