// 
// Decompiled by Procyon v0.6.0
// 

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
import java.util.function.Function;
import java.util.stream.Collectors;

public class EffectModifier extends TexturedVaultModifier
{
    @Expose
    private final String effect;
    @Expose
    private final int value;
    @Expose
    private final String operator;
    @Expose
    private final String type;
    
    public EffectModifier(final String name, final ResourceLocation icon, final Effect effect, final int value, final String operator, final EffectConfig.Type type) {
        this(name, icon, Registry.MOB_EFFECT.getKey(effect).toString(), value, operator, type.toString());
    }
    
    public EffectModifier(final String name, final ResourceLocation icon, final String effect, final int value, final String operator, final String type) {
        super(name, icon);
        this.effect = effect;
        this.value = value;
        this.operator = operator;
        this.type = type;
        if (this.operator.equals("MULTIPLY")) {
            this.format(this.getColor(), "Multiples the current " + new ResourceLocation(this.effect).getPath() + " amplifier by " + this.value + ".");
        }
        else if (this.operator.equals("ADD")) {
            this.format(this.getColor(), "Adds " + this.value + " to the current " + new ResourceLocation(this.effect).getPath() + " amplifier.");
        }
        else if (this.operator.equals("SET")) {
            this.format(this.getColor(), "Gives " + new ResourceLocation(this.effect).getPath() + " " + RomanNumber.toRoman(this.value) + ".");
        }
        else {
            this.format(this.getColor(), "Does absolutely nothing. Whoever wrote this config made a mistake...");
        }
    }
    
    public Effect getEffect() {
        return (Effect)Registry.MOB_EFFECT.get(new ResourceLocation(this.effect));
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
        final EffectTalent.Operator operator = this.getOperator().equals("SET") ? EffectTalent.Operator.SET : EffectTalent.Operator.ADD;
        return new EffectTalent(0, this.getEffect(), this.getAmplifier(), this.getType(), operator);
    }
    
    public enum Type
    {
        HIDDEN("hidden", false, false), 
        PARTICLES_ONLY("particles_only", true, false), 
        ICON_ONLY("icon_only", false, true), 
        ALL("all", true, true);
        
        private static Map<String, Type> STRING_TO_TYPE;
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
            Type.STRING_TO_TYPE = Arrays.stream(values()).collect(Collectors.toMap((Function<? super Type, ? extends String>)Type::toString, o -> o));
        }
    }
}
