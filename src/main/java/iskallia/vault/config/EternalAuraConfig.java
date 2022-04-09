// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.type.*;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EternalAuraConfig extends Config
{
    @Expose
    private final List<EffectAuraConfig> EFFECT_AURAS;
    @Expose
    private final List<ParryAuraConfig> PARRY_AURAS;
    @Expose
    private final List<ResistanceAuraConfig> RESISTANCE_AURAS;
    @Expose
    private final List<TauntAuraConfig> TAUNT_AURAS;
    @Expose
    private final List<MobEffectAuraConfig> MOB_EFFECT_AURAS;
    @Expose
    private final WeightedList<String> availableAuras;
    
    public EternalAuraConfig() {
        this.EFFECT_AURAS = new ArrayList<EffectAuraConfig>();
        this.PARRY_AURAS = new ArrayList<ParryAuraConfig>();
        this.RESISTANCE_AURAS = new ArrayList<ResistanceAuraConfig>();
        this.TAUNT_AURAS = new ArrayList<TauntAuraConfig>();
        this.MOB_EFFECT_AURAS = new ArrayList<MobEffectAuraConfig>();
        this.availableAuras = new WeightedList<String>();
    }

    public List<AuraConfig> getAll() {
        return (List<AuraConfig>) Stream.<List>of(new List[]{this.EFFECT_AURAS, this.PARRY_AURAS, this.RESISTANCE_AURAS, this.TAUNT_AURAS, this.MOB_EFFECT_AURAS
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }
    
    @Override
    public String getName() {
        return "eternal_aura";
    }
    
    @Override
    protected void reset() {
        this.EFFECT_AURAS.clear();
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.REGENERATION, "Regeneration", "regeneration"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.LUCK, "Luck", "lucky"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.DIG_SPEED, "Haste", "haste"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.MOVEMENT_SPEED, "Speed", "speed"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.DAMAGE_BOOST, "Strength", "strength"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.SATURATION, "Saturation", "saturation"));
        this.PARRY_AURAS.clear();
        this.PARRY_AURAS.add(new ParryAuraConfig(0.1f));
        this.RESISTANCE_AURAS.clear();
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig(0.1f));
        this.TAUNT_AURAS.clear();
        this.TAUNT_AURAS.add(new TauntAuraConfig(60));
        this.MOB_EFFECT_AURAS.clear();
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig(Effects.MOVEMENT_SLOWDOWN, 2, "Slowness", "slowness"));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig(Effects.WEAKNESS, 2, "Weakness", "weakness"));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig(Effects.WITHER, 2, "Wither", "withering"));
        this.availableAuras.clear();
        this.availableAuras.add("Regeneration", 1);
        this.availableAuras.add("Luck", 1);
        this.availableAuras.add("Haste", 1);
        this.availableAuras.add("Speed", 1);
        this.availableAuras.add("Strength", 1);
        this.availableAuras.add("Saturation", 1);
        this.availableAuras.add("Parry", 1);
        this.availableAuras.add("Resistance", 1);
        this.availableAuras.add("Taunt", 1);
        this.availableAuras.add("Mob_Slowness", 1);
    }
    
    @Nonnull
    public List<AuraConfig> getRandom(final Random rand, final int count) {
        if (this.availableAuras.size() < count) {
            throw new IllegalStateException("Not enough unique eternal aura configurations available! Misconfigured?");
        }
        final List<AuraConfig> auraConfigurations = new ArrayList<AuraConfig>(count);
        for (int i = 0; i < count; ++i) {
            AuraConfig randomCfg;
            do {
                randomCfg = this.getByName(this.availableAuras.getRandom(rand));
            } while (auraConfigurations.contains(randomCfg));
            auraConfigurations.add(randomCfg);
        }
        return auraConfigurations;
    }
    
    @Nullable
    public AuraConfig getByName(final String name) {
        for (final AuraConfig cfg : this.getAll()) {
            if (cfg.getName().equals(name)) {
                return cfg;
            }
        }
        return null;
    }
    
    public static class AuraConfig
    {
        public static final DecimalFormat ROUNDING_FORMAT;
        @Expose
        private final String name;
        @Expose
        private final String displayName;
        @Expose
        private final String description;
        @Expose
        private final String iconPath;
        @Expose
        private final float radius;
        
        public AuraConfig(final String name, final String displayName, final String description, final String iconPath, final float radius) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.iconPath = Vault.sId("textures/entity/aura/aura_" + iconPath + ".png");
            this.radius = radius;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public String getIconPath() {
            return this.iconPath;
        }
        
        public float getRadius() {
            return this.radius;
        }
        
        public List<ITextComponent> getTooltip() {
            final List<ITextComponent> ttip = new ArrayList<ITextComponent>();
            ttip.add((ITextComponent)new StringTextComponent(this.getDisplayName()));
            ttip.add((ITextComponent)new StringTextComponent(this.getDescription()));
            return ttip;
        }
        
        public void onTick(final World world, final ActiveAura aura) {
        }
        
        static {
            ROUNDING_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        }
    }
}
