package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class EffectAttribute
        extends PooledAttribute<List<EffectAttribute.Instance>> {
    public EffectAttribute() {
    }

    public EffectAttribute(VAttribute.Modifier<List<Instance>> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        if (getBaseValue() == null)
            return;
        CompoundNBT tag = new CompoundNBT();
        ListNBT effectsList = new ListNBT();

        getBaseValue().forEach(effect -> {
            CompoundNBT effectTag = new CompoundNBT();

            tag.putString("Id", effect.effect);
            effectsList.add(effectTag);
        });
        tag.put("Effects", (INBT) effectsList);
        nbt.put("BaseValue", (INBT) tag);
    }


    public void read(CompoundNBT nbt) {
        if (!nbt.contains("BaseValue", 10)) {
            setBaseValue(new ArrayList<>());

            return;
        }
        CompoundNBT tag = nbt.getCompound("BaseValue");
        ListNBT effectsList = tag.getList("Effects", 10);

        setBaseValue((List<Instance>) effectsList.stream()
                .map(inbt -> (CompoundNBT) inbt)
                .map(effect -> new Instance(tag.getString("Id")))
                .collect(Collectors.toList()));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(Type type) {
        return new Generator.Operator(type);
    }

    public static class Instance {
        @Expose
        protected String effect;

        public Instance(String effect) {
            this.effect = effect;
        }

        public Instance(Effect effect) {
            this(effect.getRegistryName().toString());
        }

        public String getId() {
            return this.effect;
        }

        public Effect toEffect() {
            return Registry.MOB_EFFECT.getOptional(new ResourceLocation(this.effect)).orElse(null);
        }


        public String toString() {
            return "Instance{effect='" + this.effect + '\'' + '}';
        }
    }


    public static class Generator
            extends PooledAttribute.Generator<List<Instance>, Generator.Operator> {
        public List<EffectAttribute.Instance> getDefaultValue(Random random) {
            return new ArrayList<>();
        }

        public static class Operator extends PooledAttribute.Generator.Operator<List<EffectAttribute.Instance>> {
            @Expose
            protected String type;

            public Operator(EffectAttribute.Type type) {
                this.type = type.name();
            }

            public EffectAttribute.Type getType() throws Throwable {
                return EffectAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            public List<EffectAttribute.Instance> apply(List<EffectAttribute.Instance> value, List<EffectAttribute.Instance> modifier) {
                try {
                    if (getType() == EffectAttribute.Type.SET)
                        return modifier;
                    if (getType() == EffectAttribute.Type.MERGE) {
                        List<EffectAttribute.Instance> res = new ArrayList<>(value);
                        res.addAll(modifier);
                        return res;
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator extends PooledAttribute.Generator.Operator<List<Instance>> {
        public List<EffectAttribute.Instance> apply(List<EffectAttribute.Instance> value, List<EffectAttribute.Instance> modifier) {
            try {
                if (getType() == EffectAttribute.Type.SET) return modifier;
                if (getType() == EffectAttribute.Type.MERGE) {
                    List<EffectAttribute.Instance> res = new ArrayList<>(value);
                    res.addAll(modifier);
                    return res;
                }
            } catch (Throwable e) {
            }
            return value;
        }

        @Expose
        protected String type;

        public Operator(EffectAttribute.Type type) {
            this.type = type.name();
        }

        public EffectAttribute.Type getType() throws Throwable {
            return EffectAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
        }
    }

    public enum Type {
        SET, MERGE;

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\EffectAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */