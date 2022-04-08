package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.EffectConfig;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.RomanNumber;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EffectModifier
        extends TexturedVaultModifier {
    @Expose
    private final String effect;
    @Expose
    private final int value;

    public EffectModifier(String name, ResourceLocation icon, Effect effect, int value, String operator, EffectConfig.Type type) {
        this(name, icon, Registry.MOB_EFFECT.getKey(effect).toString(), value, operator, type.toString());
    }

    @Expose
    private final String operator;
    @Expose
    private final String type;

    public EffectModifier(String name, ResourceLocation icon, String effect, int value, String operator, String type) {
        super(name, icon);
        this.effect = effect;
        this.value = value;
        this.operator = operator;
        this.type = type;

        if (this.operator.equals("MULTIPLY")) {
            format(getColor(), "Multiples the current " + (new ResourceLocation(this.effect)).getPath() + " amplifier by " + this.value + ".");
        } else if (this.operator.equals("ADD")) {
            format(getColor(), "Adds " + this.value + " to the current " + (new ResourceLocation(this.effect)).getPath() + " amplifier.");
        } else if (this.operator.equals("SET")) {
            format(getColor(), "Gives " + (new ResourceLocation(this.effect)).getPath() + " " + RomanNumber.toRoman(this.value) + ".");
        } else {
            format(getColor(), "Does absolutely nothing. Whoever wrote this config made a mistake...");
        }
    }

    public Effect getEffect() {
        return (Effect) Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
    }

    public int getAmplifier() {
        return this.value;
    }

    public String getOperator() {
        return this.operator;
    }

    public EffectTalent.Type getType() {
        return EffectTalent.Type.fromString(this.type);
    }

    public EffectTalent makeTalent() {
        EffectTalent.Operator operator = getOperator().equals("SET") ? EffectTalent.Operator.SET : EffectTalent.Operator.ADD;
        return new EffectTalent(0, getEffect(), getAmplifier(), getType(), operator);
    }

    public enum Type {
        HIDDEN("hidden", false, false),
        PARTICLES_ONLY("particles_only", true, false),
        ICON_ONLY("icon_only", false, true),
        ALL("all", true, true);
        private static Map<String, Type> STRING_TO_TYPE;
        private final String name;

        static {
            STRING_TO_TYPE = (Map<String, Type>) Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::toString, o -> o));
        }

        public final boolean showParticles;
        public final boolean showIcon;

        Type(String name, boolean showParticles, boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }

        public static Type fromString(String type) {
            return STRING_TO_TYPE.get(type);
        }


        public String toString() {
            return this.name;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\EffectModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */