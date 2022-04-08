package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import iskallia.vault.entity.EffectCloudEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class EffectCloudAttribute
        extends PooledAttribute<List<EffectCloudEntity.Config>> {
    public EffectCloudAttribute() {
    }

    public EffectCloudAttribute(VAttribute.Modifier<List<EffectCloudEntity.Config>> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        if (getBaseValue() == null)
            return;
        CompoundNBT tag = new CompoundNBT();

        ListNBT effectsList = new ListNBT();
        getBaseValue().forEach(effect -> effectsList.add(effect.serializeNBT()));

        tag.put("EffectClouds", (INBT) effectsList);
        nbt.put("BaseValue", (INBT) tag);
    }


    public void read(CompoundNBT nbt) {
        if (!nbt.contains("BaseValue", 10)) {
            setBaseValue(new ArrayList<>());

            return;
        }
        CompoundNBT tag = nbt.getCompound("BaseValue");
        ListNBT effectsList = tag.getList("EffectClouds", 10);

        setBaseValue((List<EffectCloudEntity.Config>) effectsList.stream()
                .map(inbt -> EffectCloudEntity.Config.fromNBT((CompoundNBT) inbt))
                .collect(Collectors.toList()));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends PooledAttribute.Generator<List<EffectCloudEntity.Config>, Generator.Operator> {
        public List<EffectCloudEntity.Config> getDefaultValue(Random random) {
            return new ArrayList<>();
        }

        public static class Operator extends PooledAttribute.Generator.Operator<List<EffectCloudEntity.Config>> {
            @Expose
            protected String type;

            public Operator(EffectCloudAttribute.Type type) {
                this.type = type.name();
            }

            public EffectCloudAttribute.Type getType() throws Throwable {
                return EffectCloudAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            public List<EffectCloudEntity.Config> apply(List<EffectCloudEntity.Config> value, List<EffectCloudEntity.Config> modifier) {
                try {
                    if (getType() == EffectCloudAttribute.Type.SET)
                        return modifier;
                    if (getType() == EffectCloudAttribute.Type.MERGE) {
                        List<EffectCloudEntity.Config> res = new ArrayList<>(value);
                        res.addAll(modifier);
                        return res;
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator extends PooledAttribute.Generator.Operator<List<EffectCloudEntity.Config>> {
        public List<EffectCloudEntity.Config> apply(List<EffectCloudEntity.Config> value, List<EffectCloudEntity.Config> modifier) {
            try {
                if (getType() == EffectCloudAttribute.Type.SET) return modifier;
                if (getType() == EffectCloudAttribute.Type.MERGE) {
                    List<EffectCloudEntity.Config> res = new ArrayList<>(value);
                    res.addAll(modifier);
                    return res;
                }
            } catch (Throwable e) {

            }
            return value;
        }

        @Expose
        protected String type;

        public Operator(EffectCloudAttribute.Type type) {
            this.type = type.name();
        }

        public EffectCloudAttribute.Type getType() throws Throwable {
            return EffectCloudAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\EffectCloudAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */