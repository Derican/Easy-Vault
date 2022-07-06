package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.type.*;
import iskallia.vault.skill.talent.type.EffectTalent;
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
        Stream<List<AuraConfig>> stream = Stream.<List<AuraConfig>>of(new List[]{this.EFFECT_AURAS, this.PARRY_AURAS, this.RESISTANCE_AURAS, this.TAUNT_AURAS, this.MOB_EFFECT_AURAS
        });
        Stream<AuraConfig> stream1 = stream.flatMap(Collection::stream);
        List<AuraConfig> list = stream1.collect(Collectors.toList());
        return list;
//        return (List<AuraConfig>) Stream.<List<AuraConfig>>of(new List[]{this.EFFECT_AURAS, this.PARRY_AURAS, this.RESISTANCE_AURAS, this.TAUNT_AURAS, this.MOB_EFFECT_AURAS
//        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "eternal_aura";
    }

    @Override
    protected void reset() {
        this.EFFECT_AURAS.clear();
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.REGENERATION, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Regeneration1", "Regeneration 1", "Grants an aura of Regeneration +1", "regeneration", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.REGENERATION, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Regeneration2", "Regeneration 2", "Grants an aura of Regeneration +2", "regeneration", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Luck1", "Luck", "Grants an aura of Luck +1", "lucky", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Luck2", "Luck 2", "Grants an aura of Luck +2", "lucky_2", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.DIG_SPEED, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Haste1", "Haste 1", "Grants an aura of Haste +1", "haste", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.DIG_SPEED, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Haste2", "Haste 2", "Grants an aura of Haste +2", "haste_2", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.MOVEMENT_SPEED, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Speed1", "Speed 1", "Grants an aura of Speed +1", "speed", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.MOVEMENT_SPEED, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Speed2", "Speed 2", "Grants an aura of Speed +2", "speed_2", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.DAMAGE_BOOST, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Strength1", "Strength 1", "Grants an aura of Strength", "strength", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.DAMAGE_BOOST, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Strength2", "Strength 2", "Grants an aura of Strength +2", "strength_2", 5.0f));
        this.EFFECT_AURAS.add(new EffectAuraConfig(new EffectTalent(0, Effects.SATURATION, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD), "Saturation", "Saturation", "Grants an aura of Saturation", "saturation", 5.0f));
        this.PARRY_AURAS.clear();
        this.PARRY_AURAS.add(new ParryAuraConfig("Parry2", "Parry 2%", "Players in aura have +2% Parry", "parry", 5.0f, 0.02f));
        this.PARRY_AURAS.add(new ParryAuraConfig("Parry4", "Parry 4%", "Players in aura have +4% Parry", "parry_4", 5.0f, 0.04f));
        this.PARRY_AURAS.add(new ParryAuraConfig("Parry6", "Parry 6%", "Players in aura have +6% Parry", "parry_6", 5.0f, 0.06f));
        this.PARRY_AURAS.add(new ParryAuraConfig("Parry8", "Parry 8%", "Players in aura have +8% Parry", "parry_8", 5.0f, 0.08f));
        this.PARRY_AURAS.add(new ParryAuraConfig("Parry10", "Parry 10%", "Players in aura have +10% Parry", "parry_10", 5.0f, 0.1f));
        this.RESISTANCE_AURAS.clear();
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig("Resistance2", "Resistance 2%", "Players in aura have +2% Resistance", "resistance", 5.0f, 0.02f));
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig("Resistance4", "Resistance 4%", "Players in aura have +4% Resistance", "resistance_4", 5.0f, 0.04f));
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig("Resistance6", "Resistance 6%", "Players in aura have +6% Resistance", "resistance_6", 5.0f, 0.06f));
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig("Resistance8", "Resistance 8%", "Players in aura have +8% Resistance", "resistance_8", 5.0f, 0.08f));
        this.RESISTANCE_AURAS.add(new ResistanceAuraConfig("Resistance10", "Resistance 10%", "Players in aura have +10% Resistance", "resistance_10", 5.0f, 0.1f));
        this.TAUNT_AURAS.clear();
        this.TAUNT_AURAS.add(new TauntAuraConfig("Taunt1", "Taunt 1", "Periodically taunts enemies nearby", "taunt", 8.0f, 100));
        this.TAUNT_AURAS.add(new TauntAuraConfig("Taunt2", "Taunt 2", "Periodically taunts enemies nearby", "taunt_2", 10.0f, 60));
        this.TAUNT_AURAS.add(new TauntAuraConfig("Taunt3", "Taunt 3", "Periodically taunts enemies nearby", "taunt_3", 12.0f, 40));
        this.MOB_EFFECT_AURAS.clear();
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Slowness1", "Slowness 1", "Applies Slowness 1 to enemies in its radius", "slowness", 8.0f, Effects.MOVEMENT_SLOWDOWN, 1));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Slowness2", "Slowness 2", "Applies Slowness 2 to enemies in its radius", "slowness_2", 8.0f, Effects.MOVEMENT_SLOWDOWN, 2));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Slowness3", "Slowness 3", "Applies Slowness 3 to enemies in its radius", "slowness_3", 8.0f, Effects.MOVEMENT_SLOWDOWN, 3));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Weakness1", "Weakness 1", "Applies Weakness 1 to enemies in its radius", "weakness", 8.0f, Effects.WEAKNESS, 1));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Weakness2", "Weakness 2", "Applies Weakness 2 to enemies in its radius", "weakness_2", 8.0f, Effects.WEAKNESS, 2));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Wither1", "Wither 1", "Applies Wither 1 to enemies in its radius", "withering", 8.0f, Effects.WITHER, 1));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Wither2", "Wither 2", "Applies Wither 2 to enemies in its radius", "withering_2", 8.0f, Effects.WITHER, 2));
        this.MOB_EFFECT_AURAS.add(new MobEffectAuraConfig("Mob_Wither3", "Wither 3", "Applies Wither 3 to enemies in its radius", "withering_3", 8.0f, Effects.WITHER, 3));
        this.availableAuras.clear();
        this.availableAuras.add("Regeneration1", 4);
        this.availableAuras.add("Regeneration2", 2);
        this.availableAuras.add("Luck1", 2);
        this.availableAuras.add("Luck2", 1);
        this.availableAuras.add("Haste1", 12);
        this.availableAuras.add("Haste2", 8);
        this.availableAuras.add("Speed1", 16);
        this.availableAuras.add("Speed2", 8);
        this.availableAuras.add("Strength1", 5);
        this.availableAuras.add("Strength2", 3);
        this.availableAuras.add("Saturation", 32);
        this.availableAuras.add("Parry2", 48);
        this.availableAuras.add("Parry4", 32);
        this.availableAuras.add("Parry6", 16);
        this.availableAuras.add("Parry8", 8);
        this.availableAuras.add("Parry10", 4);
        this.availableAuras.add("Resistance2", 48);
        this.availableAuras.add("Resistance4", 40);
        this.availableAuras.add("Resistance6", 12);
        this.availableAuras.add("Resistance8", 8);
        this.availableAuras.add("Resistance10", 4);
        this.availableAuras.add("Taunt1", 24);
        this.availableAuras.add("Taunt2", 12);
        this.availableAuras.add("Taunt3", 8);
        this.availableAuras.add("Mob_Slowness1", 36);
        this.availableAuras.add("Mob_Slowness2", 24);
        this.availableAuras.add("Mob_Slowness3", 12);
        this.availableAuras.add("Mob_Weakness1", 48);
        this.availableAuras.add("Mob_Weakness2", 32);
        this.availableAuras.add("Mob_Wither1", 36);
        this.availableAuras.add("Mob_Wither2", 16);
        this.availableAuras.add("Mob_Wither3", 8);
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

    public static class AuraConfig {
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
            ttip.add((ITextComponent) new StringTextComponent(this.getDisplayName()));
            ttip.add((ITextComponent) new StringTextComponent(this.getDescription()));
            return ttip;
        }

        public void onTick(final World world, final ActiveAura aura) {
        }

        static {
            ROUNDING_FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        }
    }
}
