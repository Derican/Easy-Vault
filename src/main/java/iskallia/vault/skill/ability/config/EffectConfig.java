// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.type.EffectTalent;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectConfig extends AbilityConfig
{
    @Expose
    private final String effect;
    @Expose
    private final int amplifier;
    @Expose
    private final String type;
    
    public EffectConfig(final int cost, final Effect effect, final int amplifier, final Type type, final Behavior behavior) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), behavior);
    }
    
    public EffectConfig(final int cost, final Effect effect, final int amplifier, final Type type, final Behavior behavior, final int cooldown) {
        this(cost, Registry.MOB_EFFECT.getKey(effect).toString(), amplifier, type.toString(), behavior, cooldown);
    }
    
    public EffectConfig(final int cost, final String effect, final int amplifier, final String type, final Behavior behavior) {
        this(cost, effect, amplifier, type, behavior, 200);
    }
    
    public EffectConfig(final int cost, final String effect, final int amplifier, final String type, final Behavior behavior, final int cooldown) {
        super(cost, behavior, cooldown);
        this.effect = effect;
        this.amplifier = amplifier;
        this.type = type;
    }
    
    public Effect getEffect() {
        return (Effect)Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
    }
    
    public int getAmplifier() {
        return this.amplifier;
    }
    
    public EffectTalent.Type getType() {
        return EffectTalent.Type.fromString(this.type);
    }
    
    public enum Type
    {
        HIDDEN("hidden", false, false), 
        PARTICLES_ONLY("particles_only", true, false), 
        ICON_ONLY("icon_only", false, true), 
        ALL("all", true, true);
        
        private static final Map<String, Type> STRING_TO_TYPE;
        private final String name;
        public final boolean showParticles;
        public final boolean showIcon;
        
        private Type(final String name, final boolean showParticles, final boolean showIcon) {
            this.name = name;
            this.showParticles = showParticles;
            this.showIcon = showIcon;
        }
        
        public static Type fromString(final String type) {
            return Type.STRING_TO_TYPE.get(type);
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            STRING_TO_TYPE = Arrays.stream(values()).collect(Collectors.toMap((Function<? super Type, ? extends String>)Type::toString, o -> o));
        }
    }
}
