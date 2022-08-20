package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.ArchetypeTalentGroup;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.type.*;
import iskallia.vault.skill.talent.type.archetype.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ForgeMod;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TalentsConfig extends Config {
    @Expose
    public TalentGroup<EffectTalent> HASTE;
    @Expose
    public TalentGroup<ResistanceTalent> RESISTANCE;
    @Expose
    public TalentGroup<EffectTalent> STRENGTH;
    @Expose
    public TalentGroup<EffectTalent> FIRE_RESISTANCE;
    @Expose
    public TalentGroup<EffectTalent> SPEED;
    @Expose
    public TalentGroup<EffectTalent> WATER_BREATHING;
    @Expose
    public TalentGroup<AttributeTalent> WELL_FIT;
    @Expose
    public TalentGroup<AttributeTalent> REACH;
    @Expose
    public TalentGroup<TwerkerTalent> TWERKER;
    @Expose
    public TalentGroup<ElvishTalent> ELVISH;
    @Expose
    public TalentGroup<CarelessTalent> CARELESS;
    @Expose
    public TalentGroup<AngelTalent> ANGEL;
    @Expose
    public TalentGroup<ExperiencedTalent> EXPERIENCED;
    @Expose
    public TalentGroup<ParryTalent> PARRY;
    @Expose
    public TalentGroup<AttributeTalent> STONE_SKIN;
    @Expose
    public TalentGroup<UnbreakableTalent> UNBREAKABLE;
    @Expose
    public TalentGroup<CriticalStrikeTalent> CRITICAL_STRIKE;
    @Expose
    public TalentGroup<AttributeTalent> CHUNKY;
    @Expose
    public ArchetypeTalentGroup<FrenzyTalent> FRENZY;
    @Expose
    public TalentGroup<StepTalent> STEP;
    @Expose
    public TalentGroup<ArtisanTalent> ARTISAN;
    @Expose
    public TalentGroup<EffectTalent> LOOTER;
    @Expose
    public TalentGroup<EffectTalent> TREASURE_HUNTER;
    @Expose
    public TalentGroup<BreakableTalent> BREAKABLE;
    @Expose
    public TalentGroup<LuckyAltarTalent> LUCKY_ALTAR;
    @Expose
    public TalentGroup<FatalStrikeTalent> FATAL_STRIKE;
    @Expose
    public TalentGroup<FatalStrikeChanceTalent> FATAL_STRIKE_CHANCE;
    @Expose
    public TalentGroup<FatalStrikeDamageTalent> FATAL_STRIKE_DAMAGE;
    @Expose
    public TalentGroup<ThornsTalent> THORNS;
    @Expose
    public TalentGroup<ThornsChanceTalent> THORNS_CHANCE;
    @Expose
    public TalentGroup<ThornsDamageTalent> THORNS_DAMAGE;
    @Expose
    public ArchetypeTalentGroup<GlassCannonTalent> GLASS_CANNON;
    @Expose
    public ArchetypeTalentGroup<CommanderTalent> COMMANDER;
    @Expose
    public ArchetypeTalentGroup<WardTalent> WARD;
    @Expose
    public ArchetypeTalentGroup<BarbaricTalent> BARBARIC;
    @Expose
    public TalentGroup<SoulShardTalent> SOUL_HUNTER;
    @Expose
    public TalentGroup<AbsorptionTalent> BARRIER;

    @Override
    public String getName() {
        return "talents";
    }

    public List<TalentGroup<?>> getAll() {
        return (List<TalentGroup<?>>) Arrays.asList(this.HASTE, this.RESISTANCE, this.STRENGTH, this.FIRE_RESISTANCE, this.SPEED, this.WATER_BREATHING, this.WELL_FIT, this.TWERKER, this.ELVISH, this.CARELESS, this.ANGEL, this.REACH, this.EXPERIENCED, this.PARRY, this.STONE_SKIN, this.UNBREAKABLE, this.CRITICAL_STRIKE, this.LOOTER, this.CHUNKY, this.FRENZY, this.STEP, this.ARTISAN, this.TREASURE_HUNTER, this.BREAKABLE, this.LUCKY_ALTAR, this.FATAL_STRIKE, this.FATAL_STRIKE_CHANCE, this.FATAL_STRIKE_DAMAGE, this.THORNS, this.THORNS_CHANCE, this.THORNS_DAMAGE, this.GLASS_CANNON, this.COMMANDER, this.WARD, this.BARBARIC, this.SOUL_HUNTER, this.BARRIER);
    }

    public TalentGroup<?> getByName(final String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst().orElseThrow(() ->
                new IllegalStateException("Unknown talent with name " + name));
    }

    public Optional<TalentGroup<?>> getTalent(final String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst();
    }

    @Override
    protected void reset() {
        this.HASTE = TalentGroup.ofEffect("Haste", Effects.DIG_SPEED, EffectTalent.Type.ICON_ONLY, 6, i -> {
            if (i < 3) {
                return 2;
            } else if (i == 3) {
                return 3;
            } else {
                return 4;
            }
        }, EffectTalent.Operator.SET);
        this.RESISTANCE = new TalentGroup<ResistanceTalent>("Resistance", new ResistanceTalent[]{new ResistanceTalent(3, 0.01f), new ResistanceTalent(3, 0.02f), new ResistanceTalent(3, 0.03f), new ResistanceTalent(3, 0.04f), new ResistanceTalent(3, 0.05f), new ResistanceTalent(3, 0.06f), new ResistanceTalent(3, 0.07f), new ResistanceTalent(3, 0.08f), new ResistanceTalent(3, 0.09f), new ResistanceTalent(3, 0.1f)});
        this.STRENGTH = TalentGroup.ofEffect("Strength", Effects.DAMAGE_BOOST, EffectTalent.Type.ICON_ONLY, 2, i -> 3, EffectTalent.Operator.SET);
        this.FIRE_RESISTANCE = TalentGroup.ofEffect("Fire Resistance", Effects.FIRE_RESISTANCE, EffectTalent.Type.ICON_ONLY, 1, i -> 5, EffectTalent.Operator.SET);
        this.SPEED = TalentGroup.ofEffect("Speed", Effects.MOVEMENT_SPEED, EffectTalent.Type.ICON_ONLY, 5, i -> 2, EffectTalent.Operator.SET);
        this.WATER_BREATHING = TalentGroup.ofEffect("Water Breathing", Effects.WATER_BREATHING, EffectTalent.Type.ICON_ONLY, 1, i -> 5, EffectTalent.Operator.SET);
        this.WELL_FIT = TalentGroup.ofAttribute("Well Fit", Attributes.MAX_HEALTH, "Extra Health", 10, i -> 1, i -> i * 2.0, i -> AttributeModifier.Operation.ADDITION);
        this.REACH = TalentGroup.ofAttribute("Reach", (Attribute) ForgeMod.REACH_DISTANCE.get(), "Maximum Reach", 10, i -> 1, i -> i * 1.0, i -> AttributeModifier.Operation.ADDITION);
        this.TWERKER = new TalentGroup<TwerkerTalent>("Twerker", new TwerkerTalent(4, 1, false, false, false), new TwerkerTalent(4, 2, true, false, false), new TwerkerTalent(4, 3, true, true, false), new TwerkerTalent(4, 3, true, true, true));
        this.ELVISH = new TalentGroup<ElvishTalent>("Elvish", new ElvishTalent[]{new ElvishTalent(10)});
        this.CARELESS = new TalentGroup<CarelessTalent>("Careless", new CarelessTalent[]{new CarelessTalent(3)});
        this.ANGEL = new TalentGroup<AngelTalent>("Angel", new AngelTalent[]{new AngelTalent(200)});
        this.EXPERIENCED = new TalentGroup<ExperiencedTalent>("Experienced", new ExperiencedTalent[]{new ExperiencedTalent(2, 0.2f), new ExperiencedTalent(2, 0.4f), new ExperiencedTalent(2, 0.6f), new ExperiencedTalent(2, 0.8f), new ExperiencedTalent(2, 1.0f), new ExperiencedTalent(2, 1.2f), new ExperiencedTalent(2, 1.4f), new ExperiencedTalent(2, 1.6f), new ExperiencedTalent(2, 1.8f), new ExperiencedTalent(2, 2.0f)});
        this.PARRY = new TalentGroup<ParryTalent>("Parry", new ParryTalent[]{new ParryTalent(2, 0.02f), new ParryTalent(2, 0.04f), new ParryTalent(2, 0.06f), new ParryTalent(2, 0.08f), new ParryTalent(2, 0.1f), new ParryTalent(2, 0.12f), new ParryTalent(2, 0.14f), new ParryTalent(2, 0.16f), new ParryTalent(2, 0.18f), new ParryTalent(2, 0.2f)});
        this.STONE_SKIN = TalentGroup.ofAttribute("Stone Skin", Attributes.KNOCKBACK_RESISTANCE, "Extra Knockback Resistance", 10, i -> 2, i -> i * 0.1f, i -> AttributeModifier.Operation.ADDITION);
        this.UNBREAKABLE = TalentGroup.of("Unbreakable", 10, i -> new UnbreakableTalent(2, i + 1));
        this.CRITICAL_STRIKE = TalentGroup.of("Critical Strike", 5, i -> new CriticalStrikeTalent(3, (i + 1) * 0.2f));
        this.CHUNKY = TalentGroup.ofAttribute("Chunky", Attributes.MAX_HEALTH, "Extra Health 2", 10, i -> (i < 5) ? 2 : 3, i -> i * 2.0, i -> AttributeModifier.Operation.ADDITION);
        this.FRENZY = ArchetypeTalentGroup.of("Frenzy", 3, i -> new FrenzyTalent(i * 2 - 1, (i + 1) * 0.1f, 2.0f));
        this.STEP = TalentGroup.of("Step", 1, i -> new StepTalent(4, 1.0f));
        this.ARTISAN = TalentGroup.of("Artisan", 2, i -> new ArtisanTalent(3, "All"));
        this.LOOTER = TalentGroup.ofEffect("Looter", Effects.LUCK, EffectTalent.Type.ICON_ONLY, 2, i -> 3, EffectTalent.Operator.SET);
        this.TREASURE_HUNTER = TalentGroup.ofEffect("Treasure Hunter", Effects.LUCK, EffectTalent.Type.ICON_ONLY, 3, i -> 3, EffectTalent.Operator.SET);
        this.BREAKABLE = new TalentGroup<BreakableTalent>("Breakable", new BreakableTalent[]{new BreakableTalent(1, 0.1f, 0.0f), new BreakableTalent(1, 0.15f, -0.2f), new BreakableTalent(1, 0.2f, -0.3f), new BreakableTalent(1, 0.25f, -0.4f)});
        this.LUCKY_ALTAR = new TalentGroup<LuckyAltarTalent>("Lucky Altar", new LuckyAltarTalent[]{new LuckyAltarTalent(3, 0.1f), new LuckyAltarTalent(6, 0.2f), new LuckyAltarTalent(8, 0.25f)});
        this.FATAL_STRIKE = new TalentGroup<FatalStrikeTalent>("Fatal Strike", new FatalStrikeTalent[]{new FatalStrikeTalent(5, 0.05f, 0.5f)});
        this.FATAL_STRIKE_CHANCE = new TalentGroup<FatalStrikeChanceTalent>("Fatal Strike Chance", new FatalStrikeChanceTalent[]{new FatalStrikeChanceTalent(2, 0.05f), new FatalStrikeChanceTalent(2, 0.06f), new FatalStrikeChanceTalent(2, 0.07f), new FatalStrikeChanceTalent(2, 0.08f), new FatalStrikeChanceTalent(2, 0.09f), new FatalStrikeChanceTalent(2, 0.1f), new FatalStrikeChanceTalent(2, 0.11f), new FatalStrikeChanceTalent(2, 0.12f), new FatalStrikeChanceTalent(2, 0.13f), new FatalStrikeChanceTalent(2, 0.15f)});
        this.FATAL_STRIKE_DAMAGE = new TalentGroup<FatalStrikeDamageTalent>("Fatal Strike Damage", new FatalStrikeDamageTalent[]{new FatalStrikeDamageTalent(2, 1.0f), new FatalStrikeDamageTalent(2, 1.1f), new FatalStrikeDamageTalent(2, 1.2f), new FatalStrikeDamageTalent(2, 1.3f), new FatalStrikeDamageTalent(2, 1.4f), new FatalStrikeDamageTalent(2, 1.5f), new FatalStrikeDamageTalent(2, 1.6f), new FatalStrikeDamageTalent(2, 1.7f), new FatalStrikeDamageTalent(2, 1.8f), new FatalStrikeDamageTalent(2, 2.0f)});
        this.THORNS = new TalentGroup<ThornsTalent>("Thorns", new ThornsTalent[]{new ThornsTalent(5, 0.05f, 0.5f)});
        this.THORNS_CHANCE = new TalentGroup<ThornsChanceTalent>("Thorns Chance", new ThornsChanceTalent[]{new ThornsChanceTalent(2, 0.05f), new ThornsChanceTalent(2, 0.06f), new ThornsChanceTalent(2, 0.07f), new ThornsChanceTalent(2, 0.08f), new ThornsChanceTalent(2, 0.09f), new ThornsChanceTalent(2, 0.1f), new ThornsChanceTalent(2, 0.11f), new ThornsChanceTalent(2, 0.12f), new ThornsChanceTalent(2, 0.13f), new ThornsChanceTalent(2, 0.15f)});
        this.THORNS_DAMAGE = new TalentGroup<ThornsDamageTalent>("Thorns Damage", new ThornsDamageTalent[]{new ThornsDamageTalent(2, 1.0f), new ThornsDamageTalent(2, 1.1f), new ThornsDamageTalent(2, 1.2f), new ThornsDamageTalent(2, 1.3f), new ThornsDamageTalent(2, 1.4f), new ThornsDamageTalent(2, 1.5f), new ThornsDamageTalent(2, 1.6f), new ThornsDamageTalent(2, 1.7f), new ThornsDamageTalent(2, 1.8f), new ThornsDamageTalent(2, 2.0f)});
        this.GLASS_CANNON = new ArchetypeTalentGroup<GlassCannonTalent>("Glass Cannon", new GlassCannonTalent[]{new GlassCannonTalent(2, 1.5f, 1.5f)});
        this.COMMANDER = new ArchetypeTalentGroup<CommanderTalent>("Commander", new CommanderTalent[]{new CommanderTalent(2, 1.2f, 0.8f, 0.1f, 1.5f, true)});
        this.WARD = new ArchetypeTalentGroup<WardTalent>("Ward", new WardTalent[]{new WardTalent(2, 15, new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.HIDDEN, EffectTalent.Operator.ADD), 0.01f)});
        this.BARBARIC = new ArchetypeTalentGroup<BarbaricTalent>("Barbaric", new BarbaricTalent[]{new BarbaricTalent(2, 100, 0.015f, 1)});
        this.SOUL_HUNTER = new TalentGroup<SoulShardTalent>("Soul Hunter", new SoulShardTalent[]{new SoulShardTalent(2, 0.25f), new SoulShardTalent(2, 0.5f), new SoulShardTalent(2, 0.75f), new SoulShardTalent(2, 1.0f)});
        this.BARRIER = new TalentGroup<AbsorptionTalent>("Barrier", new AbsorptionTalent[]{new AbsorptionTalent(1, 0.1f)});
    }
}
