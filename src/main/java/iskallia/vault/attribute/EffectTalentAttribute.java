package iskallia.vault.attribute;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class EffectTalentAttribute
        extends PooledAttribute<List<EffectTalent>> {
    public EffectTalentAttribute() {
    }

    public EffectTalentAttribute(VAttribute.Modifier<List<EffectTalent>> modifier) {
        super(modifier);
    }


    public void write(CompoundNBT nbt) {
        if (getBaseValue() == null)
            return;
        CompoundNBT tag = new CompoundNBT();
        ListNBT effectsList = new ListNBT();

        getBaseValue().forEach(effect -> {
            CompoundNBT effectTag = new CompoundNBT();

            tag.putString("Id", effect.getEffect().getRegistryName().toString());
            tag.putInt("Amplifier", effect.getAmplifier());
            tag.putString("Type", (effect.getType()).name);
            tag.putString("Operator", (effect.getOperator()).name);
            effectsList.add(effectTag);
        });
        tag.put("EffectTalents", (INBT) effectsList);
        nbt.put("BaseValue", (INBT) tag);
    }


    public void read(CompoundNBT nbt) {
        if (!nbt.contains("BaseValue", 10)) {
            setBaseValue(new ArrayList<>());

            return;
        }
        CompoundNBT tag = nbt.getCompound("BaseValue");
        ListNBT effectsList = tag.getList("EffectTalents", 10);

        setBaseValue((List<EffectTalent>) effectsList.stream()
                .map(inbt -> (CompoundNBT) inbt)
                .map(compoundNBT -> new EffectTalent(0, tag.getString("Id"), tag.getInt("Amplifier"), tag.getString("Type"), tag.getString("Operator")))

                .collect(Collectors.toList()));
    }

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator
            extends PooledAttribute.Generator<List<EffectTalent>, Generator.Operator> {
        public List<EffectTalent> getDefaultValue(Random random) {
            return new ArrayList<>();
        }

        public static class Operator extends PooledAttribute.Generator.Operator<List<EffectTalent>> {
            @Expose
            protected String type;

            public Operator(EffectTalentAttribute.Type type) {
                this.type = type.name();
            }

            public EffectTalentAttribute.Type getType() throws Throwable {
                return EffectTalentAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            public List<EffectTalent> apply(List<EffectTalent> value, List<EffectTalent> modifier) {
                try {
                    if (getType() == EffectTalentAttribute.Type.SET)
                        return modifier;
                    if (getType() == EffectTalentAttribute.Type.MERGE) {
                        List<EffectTalent> res = new ArrayList<>(value);
                        res.addAll(modifier);
                        return res;
                    }
                } catch (Throwable e) {

                }

                return value;
            }
        }
    }

    public static class Operator extends PooledAttribute.Generator.Operator<List<EffectTalent>> {
        public List<EffectTalent> apply(List<EffectTalent> value, List<EffectTalent> modifier) {
            try {
                if (getType() == EffectTalentAttribute.Type.SET) return modifier;
                if (getType() == EffectTalentAttribute.Type.MERGE) {
                    List<EffectTalent> res = new ArrayList<>(value);
                    res.addAll(modifier);
                    return res;
                }
            } catch (Throwable e) {

            }
            return value;
        }

        @Expose
        protected String type;

        public Operator(EffectTalentAttribute.Type type) {
            this.type = type.name();
        }

        public EffectTalentAttribute.Type getType() throws Throwable {
            return EffectTalentAttribute.Type.getByName(this.type).<Throwable>orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\EffectTalentAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */