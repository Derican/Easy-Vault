package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class EffectConfig extends AbilityConfig {
    @Expose
    private final String effect;
    @Expose
    private final int amplifier;
    @Expose
    private final String type;

    public EffectConfig(int cost, Effect effect, int amplifier, Type type, AbilityConfig.Behavior behavior) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), behavior);
    }

    public EffectConfig(int cost, Effect effect, int amplifier, Type type, AbilityConfig.Behavior behavior, int cooldown) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), behavior, cooldown);
    }

    public EffectConfig(int cost, String effect, int amplifier, String type, AbilityConfig.Behavior behavior) {
        this(cost, effect, amplifier, type, behavior, 200);
    }

    public EffectConfig(int cost, String effect, int amplifier, String type, AbilityConfig.Behavior behavior, int cooldown) {
        super(cost, behavior, cooldown);
        this.effect = effect;
        this.amplifier = amplifier;
        this.type = type;
    }

    public Effect getEffect() {
        return (Effect) Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public EffectTalent.Type getType() {
        return EffectTalent.Type.fromString(this.type);
    }

    public enum Type {
        HIDDEN("hidden", false, false),
        PARTICLES_ONLY("particles_only", true, false),
        ICON_ONLY("icon_only", false, true),
        ALL("all", true, true);
        private static final Map<String, Type> STRING_TO_TYPE;

        static {
            STRING_TO_TYPE = (Map<String, Type>) Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::toString, o -> o));
        }

        private final String name;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\EffectConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */