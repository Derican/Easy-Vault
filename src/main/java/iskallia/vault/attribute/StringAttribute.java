package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import iskallia.vault.util.gson.IgnoreEmpty;
import net.minecraft.nbt.CompoundNBT;

import java.util.Optional;
import java.util.Random;


public class StringAttribute
        extends PooledAttribute<String> {
    public StringAttribute() {
    }

    public StringAttribute(VAttribute.Modifier<String> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        nbt.putString("BaseValue", getBaseValue());
    }


    public void read(CompoundNBT nbt) {
        setBaseValue(nbt.getString("BaseValue"));
    }

    public static class Generator
            extends PooledAttribute.Generator<String, Generator.Operator> {
        public String getDefaultValue(Random random) {
            return "";
        }

        public static class Operator
                extends PooledAttribute.Generator.Operator<String> {
            @Expose
            protected String type;

            public Operator(StringAttribute.Type type) {
                this.type = type.name();
            }

            @Expose
            @JsonAdapter(IgnoreEmpty.StringAdapter.class)
            protected String delimiter;
            @Expose
            @JsonAdapter(IgnoreEmpty.StringAdapter.class)
            protected String regex;

            public StringAttribute.Type getType() throws Throwable {
                return StringAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }


            public String apply(String value, String modifier) {
                try {
                    if (getType() == StringAttribute.Type.SET)
                        return modifier;
                    if (getType() == StringAttribute.Type.APPEND)
                        return value + modifier;
                    if (getType() == StringAttribute.Type.JOIN)
                        return value + this.delimiter + modifier;
                    if (getType() == StringAttribute.Type.REPLACE_FIRST)
                        return value.replaceFirst(this.regex, modifier);
                    if (getType() == StringAttribute.Type.REPLACE_ALL) {
                        return value.replaceAll(this.regex, modifier);
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public enum Type {
        SET, APPEND, JOIN, REPLACE_FIRST, REPLACE_ALL;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\StringAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */