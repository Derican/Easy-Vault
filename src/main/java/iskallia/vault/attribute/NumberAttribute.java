package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;

import java.util.Optional;


public abstract class NumberAttribute<T>
        extends PooledAttribute<T> {
    protected NumberAttribute() {
    }

    protected NumberAttribute(VAttribute.Modifier<T> modifier) {
        super(modifier);
    }

    public static abstract class Generator<T, O extends Generator.Operator<T>> extends PooledAttribute.Generator<T, O> {
        public static abstract class Operator<T> extends PooledAttribute.Generator.Operator<T> {
            @Expose
            protected String type;

            public Operator(NumberAttribute.Type type) {
                this.type = type.name();
            }

            public NumberAttribute.Type getType() throws Throwable {
                return NumberAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }
        }
    }

    public enum Type {
        SET, ADD, MULTIPLY;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\NumberAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */