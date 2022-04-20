package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.util.gson.IgnoreEmpty;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;
import java.util.Random;

public class StringAttribute extends PooledAttribute<String> {
    public StringAttribute() {
    }

    public StringAttribute(final VAttribute.Modifier<String> modifier) {
        super(modifier);
    }

    @Override
    public void write(final CompoundNBT nbt) {
        nbt.putString("BaseValue", (String) this.getBaseValue());
    }

    @Override
    public void read(final CompoundNBT nbt) {
        this.setBaseValue(nbt.getString("BaseValue"));
    }

    public static class Generator extends PooledAttribute.Generator<String, Generator.Operator> {
        @Override
        public String getDefaultValue(final Random random) {
            return "";
        }

        public static class Operator extends PooledAttribute.Generator.Operator<String> {
            @Expose
            protected String type;
            @Expose
            @JsonAdapter(IgnoreEmpty.StringAdapter.class)
            protected String delimiter;
            @Expose
            @JsonAdapter(IgnoreEmpty.StringAdapter.class)
            protected String regex;

            public Operator(final Type type) {
                this.type = type.name();
            }

            public StringAttribute.Type getType() throws Throwable {
                return StringAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            @Override
            public String apply(final String value, final String modifier) {
                try {
                    if (this.getType() == Type.SET) {
                        return modifier;
                    }
                    if (this.getType() == Type.APPEND) {
                        return value + modifier;
                    }
                    if (this.getType() == Type.JOIN) {
                        return value + this.delimiter + modifier;
                    }
                    if (this.getType() == Type.REPLACE_FIRST) {
                        return value.replaceFirst(this.regex, modifier);
                    }
                    if (this.getType() == Type.REPLACE_ALL) {
                        return value.replaceAll(this.regex, modifier);
                    }
                } catch (Throwable e) {

                }
                return value;
            }
        }
    }

    public enum Type {
        SET,
        APPEND,
        JOIN,
        REPLACE_FIRST,
        REPLACE_ALL;

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
