package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;

import java.util.Optional;

public abstract class NumberAttribute<T> extends PooledAttribute<T> {
    protected NumberAttribute() {
    }

    protected NumberAttribute(final VAttribute.Modifier<T> modifier) {
        super(modifier);
    }

    public abstract static class Generator<T, O extends Generator.Operator<T>> extends PooledAttribute.Generator<T, O> {
        public abstract static class Operator<T> extends PooledAttribute.Generator.Operator<T> {
            @Expose
            protected String type;

            public Operator(final Type type) {
                this.type = type.name();
            }

            public NumberAttribute.Type getType() throws Throwable {
                return NumberAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }
        }
    }

    public enum Type {
        SET,
        ADD,
        MULTIPLY;

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
