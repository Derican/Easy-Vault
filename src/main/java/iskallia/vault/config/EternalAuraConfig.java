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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EternalAuraConfig extends Config {
    @Expose
    private final List<EffectAuraConfig> EFFECT_AURAS = new ArrayList<>();
    @Expose
    private final List<ParryAuraConfig> PARRY_AURAS = new ArrayList<>();
    @Expose
    private final List<ResistanceAuraConfig> RESISTANCE_AURAS = new ArrayList<>();
    @Expose
    private final List<TauntAuraConfig> TAUNT_AURAS = new ArrayList<>();
    @Expose
    private final List<MobEffectAuraConfig> MOB_EFFECT_AURAS = new ArrayList<>();
    @Expose
    private final WeightedList<String> availableAuras = new WeightedList();

    public List<AuraConfig> getAll() {
        return (List<AuraConfig>) Stream.<List>of(new List[]{this.EFFECT_AURAS, this.PARRY_AURAS, this.RESISTANCE_AURAS, this.TAUNT_AURAS, this.MOB_EFFECT_AURAS
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }


    public String getName() {
        return "eternal_aura";
    }


    protected void reset() {
        this.EFFECT_AURAS.clear();
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.REGENERATION, "Regeneration", "regeneration"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.LUCK, "Luck", "lucky"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.DIG_SPEED, "Haste", "haste"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.MOVEMENT_SPEED, "Speed", "speed"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.DAMAGE_BOOST, "Strength", "strength"));
        this.EFFECT_AURAS.add(new EffectAuraConfig(Effects.SATURATION, "Saturation", "saturation"));

        this.PARRY_AURAS.clear();
        this.PARRY_AURAS.add(new ParryAuraConfig(0.1F));

        this.RESISTANCE_AURAS.clear();
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig(0.1F));

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
    public List<AuraConfig> getRandom(Random rand, int count) {
        if (this.availableAuras.size() < count) {
            throw new IllegalStateException("Not enough unique eternal aura configurations available! Misconfigured?");
        }
        List<AuraConfig> auraConfigurations = new ArrayList<>(count);
        for (int i = 0; i < count; ) {

            while (true) {
                AuraConfig randomCfg = getByName((String) this.availableAuras.getRandom(rand));
                if (!auraConfigurations.contains(randomCfg)) {
                    auraConfigurations.add(randomCfg);
                    break;
                }
            }
            i++;
        }
        return auraConfigurations;
    }

    @Nullable
    public AuraConfig getByName(String name) {
        for (AuraConfig cfg : getAll()) {
            if (cfg.getName().equals(name)) {
                return cfg;
            }
        }
        return null;
    }

    public static class AuraConfig {
        public static final DecimalFormat ROUNDING_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
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

        public AuraConfig(String name, String displayName, String description, String iconPath, float radius) {
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
            List<ITextComponent> ttip = new ArrayList<>();
            ttip.add(new StringTextComponent(getDisplayName()));
            ttip.add(new StringTextComponent(getDescription()));
            return ttip;
        }

        public void onTick(World world, ActiveAura aura) {
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\EternalAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */