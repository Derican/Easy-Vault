package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.attribute.*;
import iskallia.vault.entity.EffectCloudEntity;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class VaultGearConfig extends Config {
    @Expose
    public List<Tier> TIERS = new ArrayList<>();


    public static VaultGearConfig get(VaultGear.Rarity rarity) {
        switch (rarity) {
            case COMMON:
                return ModConfigs.VAULT_GEAR_COMMON;

            case RARE:
                return ModConfigs.VAULT_GEAR_RARE;

            case EPIC:
                return ModConfigs.VAULT_GEAR_EPIC;

            case UNIQUE:
            case OMEGA:
                return ModConfigs.VAULT_GEAR_OMEGA;
        }

        return ModConfigs.VAULT_GEAR_SCRAPPY;
    }


    protected void reset() {
        Tier tier1 = new Tier(), tier2 = new Tier(), tier3 = new Tier();

        tier1.NAME = "1";
        tier1.reset();

        tier2.NAME = "2";
        tier2.reset();

        tier3.NAME = "3";
        tier3.reset();

        this.TIERS.add(tier1);
        this.TIERS.add(tier2);
        this.TIERS.add(tier3);
    }

    public static class BaseAttributes {
        @Expose
        public DoubleAttribute.Generator ARMOR;
        @Expose
        public DoubleAttribute.Generator ARMOR_TOUGHNESS;
        @Expose
        public DoubleAttribute.Generator KNOCKBACK_RESISTANCE;
        @Expose
        public DoubleAttribute.Generator ATTACK_DAMAGE;
        @Expose
        public DoubleAttribute.Generator ATTACK_SPEED;
        @Expose
        public IntegerAttribute.Generator DURABILITY;
        @Expose
        public EnumAttribute.Generator<VaultGear.Set> GEAR_SET;
        @Expose
        public EnumAttribute.Generator<PlayerFavourData.VaultGodType> IDOL_TYPE;
        @Expose
        public FloatAttribute.Generator GEAR_LEVEL_CHANCE;
        @Expose
        public IntegerAttribute.Generator GEAR_MAX_LEVEL;
        @Expose
        public IntegerAttribute.Generator GEAR_MODIFIERS_TO_ROLL;
        @Expose
        public IntegerAttribute.Generator MAX_REPAIRS;
        @Expose
        public IntegerAttribute.Generator MIN_VAULT_LEVEL;
        @Expose
        public DoubleAttribute.Generator REACH;
        @Expose
        public FloatAttribute.Generator FEATHER_FEET;
        @Expose
        public EffectAttribute.Generator EFFECT_IMMUNITY;
        @Expose
        public EffectCloudAttribute.Generator EFFECT_CLOUD;
        @Expose
        public FloatAttribute.Generator COOLDOWN_REDUCTION;
        @Expose
        public BooleanAttribute.Generator SOULBOUND;
        @Expose
        public BooleanAttribute.Generator REFORGED;

        public void initialize(ItemStack stack, Random random) {
            if (this.ARMOR != null)
                ModAttributes.ARMOR.create(stack, random, (VAttribute.Instance.Generator) this.ARMOR);
            if (this.ARMOR_TOUGHNESS != null)
                ModAttributes.ARMOR_TOUGHNESS.create(stack, random, (VAttribute.Instance.Generator) this.ARMOR_TOUGHNESS);
            if (this.KNOCKBACK_RESISTANCE != null)
                ModAttributes.KNOCKBACK_RESISTANCE.create(stack, random, (VAttribute.Instance.Generator) this.KNOCKBACK_RESISTANCE);
            if (this.ATTACK_DAMAGE != null)
                ModAttributes.ATTACK_DAMAGE.create(stack, random, (VAttribute.Instance.Generator) this.ATTACK_DAMAGE);
            if (this.ATTACK_SPEED != null)
                ModAttributes.ATTACK_SPEED.create(stack, random, (VAttribute.Instance.Generator) this.ATTACK_SPEED);
            if (this.DURABILITY != null)
                ModAttributes.DURABILITY.create(stack, random, (VAttribute.Instance.Generator) this.DURABILITY);
            if (this.GEAR_SET != null)
                ModAttributes.GEAR_SET.create(stack, random, (VAttribute.Instance.Generator) this.GEAR_SET);
            if (this.IDOL_TYPE != null)
                ModAttributes.IDOL_TYPE.create(stack, random, (VAttribute.Instance.Generator) this.IDOL_TYPE);
            if (this.GEAR_LEVEL_CHANCE != null)
                ModAttributes.GEAR_LEVEL_CHANCE.create(stack, random, (VAttribute.Instance.Generator) this.GEAR_LEVEL_CHANCE);
            if (this.GEAR_MAX_LEVEL != null)
                ModAttributes.GEAR_MAX_LEVEL.create(stack, random, (VAttribute.Instance.Generator) this.GEAR_MAX_LEVEL);
            if (this.GEAR_MODIFIERS_TO_ROLL != null)
                ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, random, (VAttribute.Instance.Generator) this.GEAR_MODIFIERS_TO_ROLL);
            if (this.MAX_REPAIRS != null)
                ModAttributes.MAX_REPAIRS.create(stack, random, (VAttribute.Instance.Generator) this.MAX_REPAIRS);
            if (this.MIN_VAULT_LEVEL != null)
                ModAttributes.MIN_VAULT_LEVEL.create(stack, random, (VAttribute.Instance.Generator) this.MIN_VAULT_LEVEL);
            if (this.REACH != null)
                ModAttributes.REACH.create(stack, random, (VAttribute.Instance.Generator) this.REACH);
            if (this.FEATHER_FEET != null)
                ModAttributes.FEATHER_FEET.create(stack, random, (VAttribute.Instance.Generator) this.FEATHER_FEET);
            if (this.EFFECT_IMMUNITY != null)
                ModAttributes.EFFECT_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.EFFECT_IMMUNITY);
            if (this.EFFECT_CLOUD != null)
                ModAttributes.EFFECT_CLOUD.create(stack, random, (VAttribute.Instance.Generator) this.EFFECT_CLOUD);
            if (this.COOLDOWN_REDUCTION != null)
                ModAttributes.COOLDOWN_REDUCTION.create(stack, random, (VAttribute.Instance.Generator) this.COOLDOWN_REDUCTION);
            if (this.SOULBOUND != null)
                ModAttributes.SOULBOUND.create(stack, random, (VAttribute.Instance.Generator) this.SOULBOUND);
            if (this.REFORGED != null)
                ModAttributes.REFORGED.create(stack, random, (VAttribute.Instance.Generator) this.REFORGED);
        }

        private BaseAttributes copy() {
            BaseAttributes copy = new BaseAttributes();
            copy.ARMOR = this.ARMOR;
            copy.ARMOR_TOUGHNESS = this.ARMOR_TOUGHNESS;
            copy.KNOCKBACK_RESISTANCE = this.KNOCKBACK_RESISTANCE;
            copy.ATTACK_DAMAGE = this.ATTACK_DAMAGE;
            copy.ATTACK_SPEED = this.ATTACK_SPEED;
            copy.DURABILITY = this.DURABILITY;
            copy.GEAR_SET = this.GEAR_SET;
            copy.IDOL_TYPE = this.IDOL_TYPE;
            copy.GEAR_LEVEL_CHANCE = this.GEAR_LEVEL_CHANCE;
            copy.GEAR_MAX_LEVEL = this.GEAR_MAX_LEVEL;
            copy.GEAR_MODIFIERS_TO_ROLL = this.GEAR_MODIFIERS_TO_ROLL;
            copy.MAX_REPAIRS = this.MAX_REPAIRS;
            copy.MIN_VAULT_LEVEL = this.MIN_VAULT_LEVEL;
            copy.REACH = this.REACH;
            copy.FEATHER_FEET = this.FEATHER_FEET;
            copy.EFFECT_IMMUNITY = this.EFFECT_IMMUNITY;
            copy.EFFECT_CLOUD = this.EFFECT_CLOUD;
            copy.COOLDOWN_REDUCTION = this.COOLDOWN_REDUCTION;
            copy.SOULBOUND = this.SOULBOUND;
            return copy;
        }
    }

    public static class BaseModifiers {
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ARMOR;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ARMOR_2;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ARMOR_TOUGHNESS;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ARMOR_TOUGHNESS_2;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_KNOCKBACK_RESISTANCE;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_KNOCKBACK_RESISTANCE_2;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ATTACK_DAMAGE;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ATTACK_DAMAGE_2;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ATTACK_SPEED;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_ATTACK_SPEED_2;
        @Expose
        public WeightedList.Entry<IntegerAttribute.Generator> ADD_DURABILITY;
        @Expose
        public WeightedList.Entry<IntegerAttribute.Generator> ADD_DURABILITY_2;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> EXTRA_LEECH_RATIO;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_EXTRA_LEECH_RATIO;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> EXTRA_RESISTANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_EXTRA_RESISTANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> EXTRA_PARRY_CHANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_EXTRA_PARRY_CHANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> EXTRA_HEALTH;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_EXTRA_HEALTH;
        @Expose
        public WeightedList.Entry<EffectTalentAttribute.Generator> EXTRA_EFFECTS;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_REACH;
        @Expose
        public WeightedList.Entry<DoubleAttribute.Generator> ADD_REACH_2;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_COOLDOWN_REDUCTION;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_COOLDOWN_REDUCTION_2;

        public void initialize(ItemStack stack, Random random) {
            int rolls = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MODIFIERS_TO_ROLL.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
            if (rolls == 0) {
                return;
            }


            if (rolls < 0) {
                if (ModAttributes.GUARANTEED_MODIFIER_REMOVAL.exists(stack)) {
                    String attrName = (String) ModAttributes.GUARANTEED_MODIFIER_REMOVAL.getBase(stack).orElseThrow(RuntimeException::new);
                    VAttribute<?, ?> modifier = (VAttribute<?, ?>) ModAttributes.REGISTRY.get(new ResourceLocation(attrName));
                    if (modifier != null &&
                            VaultGearHelper.removeAttribute(stack, modifier)) {
                        ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, Integer.valueOf(rolls + 1));
                        VaultGear.decrementLevel(stack, 1);
                        if (Math.random() < ModConfigs.VAULT_GEAR_UTILITIES.getVoidOrbPredefinedRepairCostChance()) {
                            VaultGear.incrementRepairs(stack);
                        }
                    }

                    VaultGearHelper.removeAttribute(stack, ModAttributes.GUARANTEED_MODIFIER_REMOVAL);
                    return;
                }
                int removed = VaultGearHelper.removeRandomModifiers(stack, Math.abs(rolls));
                if (removed > 0) {
                    ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, Integer.valueOf(rolls + removed));
                    VaultGear.decrementLevel(stack, removed);
                    if (Math.random() < ModConfigs.VAULT_GEAR_UTILITIES.getVoidOrbRepairCostChance()) {
                        VaultGear.incrementRepairs(stack);

                    }

                } else {

                    ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, Integer.valueOf(0));
                }


                return;
            }


            List<WeightedList.Entry<? extends VAttribute.Instance.Generator<?>>> generators = getGenerators();

            List<Boolean> existing = Arrays.asList(new Boolean[]{
                    Boolean.valueOf(ModAttributes.ADD_ARMOR.exists(stack)), Boolean.valueOf(ModAttributes.ADD_ARMOR_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_ARMOR_TOUGHNESS.exists(stack)), Boolean.valueOf(ModAttributes.ADD_ARMOR_TOUGHNESS_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_KNOCKBACK_RESISTANCE.exists(stack)), Boolean.valueOf(ModAttributes.ADD_KNOCKBACK_RESISTANCE_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_ATTACK_DAMAGE.exists(stack)), Boolean.valueOf(ModAttributes.ADD_ATTACK_DAMAGE_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_ATTACK_SPEED.exists(stack)), Boolean.valueOf(ModAttributes.ADD_ATTACK_SPEED_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_DURABILITY.exists(stack)), Boolean.valueOf(ModAttributes.ADD_DURABILITY_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.EXTRA_LEECH_RATIO.exists(stack)), Boolean.valueOf(ModAttributes.ADD_EXTRA_LEECH_RATIO.exists(stack)),
                    Boolean.valueOf(ModAttributes.EXTRA_RESISTANCE.exists(stack)), Boolean.valueOf(ModAttributes.ADD_EXTRA_RESISTANCE.exists(stack)),
                    Boolean.valueOf(ModAttributes.EXTRA_PARRY_CHANCE.exists(stack)), Boolean.valueOf(ModAttributes.ADD_EXTRA_PARRY_CHANCE.exists(stack)),
                    Boolean.valueOf(ModAttributes.EXTRA_HEALTH.exists(stack)), Boolean.valueOf(ModAttributes.ADD_EXTRA_HEALTH.exists(stack)),
                    Boolean.valueOf(ModAttributes.EXTRA_EFFECTS.exists(stack)), Boolean.valueOf(ModAttributes.ADD_REACH.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_REACH_2.exists(stack)), Boolean.valueOf(ModAttributes.ADD_COOLDOWN_REDUCTION.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_COOLDOWN_REDUCTION_2.exists(stack)), Boolean.valueOf(ModAttributes.ADD_MIN_VAULT_LEVEL.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_REGEN_CLOUD.exists(stack)), Boolean.valueOf(ModAttributes.ADD_WEAKENING_CLOUD.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_WITHER_CLOUD.exists(stack)), Boolean.valueOf(ModAttributes.ADD_POISON_IMMUNITY.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_WITHER_IMMUNITY.exists(stack)), Boolean.valueOf(ModAttributes.ADD_HUNGER_IMMUNITY.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_MINING_FATIGUE_IMMUNITY.exists(stack)), Boolean.valueOf(ModAttributes.ADD_SLOWNESS_IMMUNITY.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_WEAKNESS_IMMUNITY.exists(stack)), Boolean.valueOf(ModAttributes.ADD_FEATHER_FEET.exists(stack)),
                    Boolean.valueOf(ModAttributes.ADD_SOULBOUND.exists(stack)), Boolean.valueOf(ModAttributes.FATAL_STRIKE_CHANCE.exists(stack)),
                    Boolean.valueOf(ModAttributes.FATAL_STRIKE_DAMAGE.exists(stack)), Boolean.valueOf(ModAttributes.THORNS_CHANCE.exists(stack)),
                    Boolean.valueOf(ModAttributes.THORNS_DAMAGE.exists(stack)), Boolean.valueOf(ModAttributes.CHEST_RARITY.exists(stack)),
                    Boolean.valueOf(ModAttributes.DAMAGE_INCREASE.exists(stack)), Boolean.valueOf(ModAttributes.DAMAGE_INCREASE_2.exists(stack)),
                    Boolean.valueOf(ModAttributes.DAMAGE_ILLAGERS.exists(stack)), Boolean.valueOf(ModAttributes.DAMAGE_SPIDERS.exists(stack)),
                    Boolean.valueOf(ModAttributes.DAMAGE_UNDEAD.exists(stack)), Boolean.valueOf(ModAttributes.ON_HIT_CHAIN.exists(stack)),
                    Boolean.valueOf(ModAttributes.ON_HIT_AOE.exists(stack)), Boolean.valueOf(ModAttributes.ON_HIT_STUN.exists(stack))});


            List<Integer> picked = (List<Integer>) IntStream.range(0, generators.size()).filter(i -> (generators.get(i) != null)).flatMap(i -> IntStream.range(0, ((WeightedList.Entry) generators.get(i)).weight).map((int integer) -> {
                return Integer.valueOf(0);
            })).filter(i -> !((Boolean) existing.get(i)).booleanValue()).boxed().collect((Collector) Collectors.toList());

            Collections.shuffle(picked, random);
            picked = (List<Integer>) picked.stream().distinct().collect(Collectors.toList());
            int added = Math.min(rolls, picked.size());

            for (int i = 0; i < added; i++) {
                if (this.ADD_ARMOR == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ARMOR.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ARMOR.value);
                if (this.ADD_ARMOR_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ARMOR_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ARMOR_2.value);
                if (this.ADD_ARMOR_TOUGHNESS == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ARMOR_TOUGHNESS.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ARMOR_TOUGHNESS.value);
                if (this.ADD_ARMOR_TOUGHNESS_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ARMOR_TOUGHNESS_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ARMOR_TOUGHNESS_2.value);
                if (this.ADD_KNOCKBACK_RESISTANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_KNOCKBACK_RESISTANCE.create(stack, random, (VAttribute.Instance.Generator) this.ADD_KNOCKBACK_RESISTANCE.value);
                if (this.ADD_KNOCKBACK_RESISTANCE_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_KNOCKBACK_RESISTANCE_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_KNOCKBACK_RESISTANCE_2.value);
                if (this.ADD_ATTACK_DAMAGE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ATTACK_DAMAGE.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ATTACK_DAMAGE.value);
                if (this.ADD_ATTACK_DAMAGE_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ATTACK_DAMAGE_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ATTACK_DAMAGE_2.value);
                if (this.ADD_ATTACK_SPEED == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ATTACK_SPEED.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ATTACK_SPEED.value);
                if (this.ADD_ATTACK_SPEED_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_ATTACK_SPEED_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_ATTACK_SPEED_2.value);
                if (this.ADD_DURABILITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_DURABILITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_DURABILITY.value);
                if (this.ADD_DURABILITY_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_DURABILITY_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_DURABILITY_2.value);
                if (this.EXTRA_LEECH_RATIO == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.EXTRA_LEECH_RATIO.create(stack, random, (VAttribute.Instance.Generator) this.EXTRA_LEECH_RATIO.value);
                if (this.ADD_EXTRA_LEECH_RATIO == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_EXTRA_LEECH_RATIO.create(stack, random, (VAttribute.Instance.Generator) this.ADD_EXTRA_LEECH_RATIO.value);
                if (this.EXTRA_RESISTANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.EXTRA_RESISTANCE.create(stack, random, (VAttribute.Instance.Generator) this.EXTRA_RESISTANCE.value);
                if (this.ADD_EXTRA_RESISTANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_EXTRA_RESISTANCE.create(stack, random, (VAttribute.Instance.Generator) this.ADD_EXTRA_RESISTANCE.value);
                if (this.EXTRA_PARRY_CHANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.EXTRA_PARRY_CHANCE.create(stack, random, (VAttribute.Instance.Generator) this.EXTRA_PARRY_CHANCE.value);
                if (this.ADD_EXTRA_PARRY_CHANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_EXTRA_PARRY_CHANCE.create(stack, random, (VAttribute.Instance.Generator) this.ADD_EXTRA_PARRY_CHANCE.value);
                if (this.EXTRA_HEALTH == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.EXTRA_HEALTH.create(stack, random, (VAttribute.Instance.Generator) this.EXTRA_HEALTH.value);
                if (this.ADD_EXTRA_HEALTH == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_EXTRA_HEALTH.create(stack, random, (VAttribute.Instance.Generator) this.ADD_EXTRA_HEALTH.value);
                if (this.EXTRA_EFFECTS == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.EXTRA_EFFECTS.create(stack, random, (VAttribute.Instance.Generator) this.EXTRA_EFFECTS.value);
                if (this.ADD_REACH == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_REACH.create(stack, random, (VAttribute.Instance.Generator) this.ADD_REACH.value);
                if (this.ADD_REACH_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_REACH_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_REACH_2.value);
                if (this.ADD_COOLDOWN_REDUCTION == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_COOLDOWN_REDUCTION.create(stack, random, (VAttribute.Instance.Generator) this.ADD_COOLDOWN_REDUCTION.value);
                if (this.ADD_MIN_VAULT_LEVEL == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_MIN_VAULT_LEVEL.create(stack, random, (VAttribute.Instance.Generator) this.ADD_MIN_VAULT_LEVEL.value);
                if (this.ADD_COOLDOWN_REDUCTION == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_COOLDOWN_REDUCTION.create(stack, random, (VAttribute.Instance.Generator) this.ADD_COOLDOWN_REDUCTION.value);
                if (this.ADD_COOLDOWN_REDUCTION_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_COOLDOWN_REDUCTION_2.create(stack, random, (VAttribute.Instance.Generator) this.ADD_COOLDOWN_REDUCTION_2.value);
                if (this.ADD_REGEN_CLOUD == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_REGEN_CLOUD.create(stack, random, (VAttribute.Instance.Generator) this.ADD_REGEN_CLOUD.value);
                if (this.ADD_WEAKENING_CLOUD == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_WEAKENING_CLOUD.create(stack, random, (VAttribute.Instance.Generator) this.ADD_WEAKENING_CLOUD.value);
                if (this.ADD_WITHER_CLOUD == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_WITHER_CLOUD.create(stack, random, (VAttribute.Instance.Generator) this.ADD_WITHER_CLOUD.value);
                if (this.ADD_POISON_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_POISON_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_POISON_IMMUNITY.value);
                if (this.ADD_WITHER_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_WITHER_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_WITHER_IMMUNITY.value);
                if (this.ADD_HUNGER_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_HUNGER_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_HUNGER_IMMUNITY.value);
                if (this.ADD_MINING_FATIGUE_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_MINING_FATIGUE_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_MINING_FATIGUE_IMMUNITY.value);
                if (this.ADD_SLOWNESS_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_SLOWNESS_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_SLOWNESS_IMMUNITY.value);
                if (this.ADD_WEAKNESS_IMMUNITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_WEAKNESS_IMMUNITY.create(stack, random, (VAttribute.Instance.Generator) this.ADD_WEAKNESS_IMMUNITY.value);
                if (this.ADD_FEATHER_FEET == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_FEATHER_FEET.create(stack, random, (VAttribute.Instance.Generator) this.ADD_FEATHER_FEET.value);
                if (this.ADD_SOULBOUND == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ADD_SOULBOUND.create(stack, random, (VAttribute.Instance.Generator) this.ADD_SOULBOUND.value);
                if (this.FATAL_STRIKE_CHANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.FATAL_STRIKE_CHANCE.create(stack, random, (VAttribute.Instance.Generator) this.FATAL_STRIKE_CHANCE.value);
                if (this.FATAL_STRIKE_DAMAGE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.FATAL_STRIKE_DAMAGE.create(stack, random, (VAttribute.Instance.Generator) this.FATAL_STRIKE_DAMAGE.value);
                if (this.THORNS_CHANCE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.THORNS_CHANCE.create(stack, random, (VAttribute.Instance.Generator) this.THORNS_CHANCE.value);
                if (this.THORNS_DAMAGE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.THORNS_DAMAGE.create(stack, random, (VAttribute.Instance.Generator) this.THORNS_DAMAGE.value);
                if (this.CHEST_RARITY == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.CHEST_RARITY.create(stack, random, (VAttribute.Instance.Generator) this.CHEST_RARITY.value);
                if (this.DAMAGE_INCREASE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.DAMAGE_INCREASE.create(stack, random, (VAttribute.Instance.Generator) this.DAMAGE_INCREASE.value);
                if (this.DAMAGE_INCREASE_2 == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.DAMAGE_INCREASE_2.create(stack, random, (VAttribute.Instance.Generator) this.DAMAGE_INCREASE_2.value);
                if (this.DAMAGE_ILLAGERS == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.DAMAGE_ILLAGERS.create(stack, random, (VAttribute.Instance.Generator) this.DAMAGE_ILLAGERS.value);
                if (this.DAMAGE_SPIDERS == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.DAMAGE_SPIDERS.create(stack, random, (VAttribute.Instance.Generator) this.DAMAGE_SPIDERS.value);
                if (this.DAMAGE_UNDEAD == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.DAMAGE_UNDEAD.create(stack, random, (VAttribute.Instance.Generator) this.DAMAGE_UNDEAD.value);
                if (this.ON_HIT_CHAIN == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ON_HIT_CHAIN.create(stack, random, (VAttribute.Instance.Generator) this.ON_HIT_CHAIN.value);
                if (this.ON_HIT_AOE == generators.get(((Integer) picked.get(i)).intValue()))
                    ModAttributes.ON_HIT_AOE.create(stack, random, (VAttribute.Instance.Generator) this.ON_HIT_AOE.value);
                if (this.ON_HIT_STUN == generators.get(((Integer) picked.get(i)).intValue())) {
                    ModAttributes.ON_HIT_STUN.create(stack, random, (VAttribute.Instance.Generator) this.ON_HIT_STUN.value);
                }
            }
            ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, Integer.valueOf(rolls - added));
        }

        @Expose
        public WeightedList.Entry<IntegerAttribute.Generator> ADD_MIN_VAULT_LEVEL;
        @Expose
        public WeightedList.Entry<EffectCloudAttribute.Generator> ADD_REGEN_CLOUD;
        @Expose
        public WeightedList.Entry<EffectCloudAttribute.Generator> ADD_WEAKENING_CLOUD;
        @Expose
        public WeightedList.Entry<EffectCloudAttribute.Generator> ADD_WITHER_CLOUD;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_POISON_IMMUNITY;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_WITHER_IMMUNITY;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_HUNGER_IMMUNITY;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_MINING_FATIGUE_IMMUNITY;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_SLOWNESS_IMMUNITY;
        @Expose
        public WeightedList.Entry<EffectAttribute.Generator> ADD_WEAKNESS_IMMUNITY;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ADD_FEATHER_FEET;
        @Expose
        public WeightedList.Entry<BooleanAttribute.Generator> ADD_SOULBOUND;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> FATAL_STRIKE_CHANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> FATAL_STRIKE_DAMAGE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> THORNS_CHANCE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> THORNS_DAMAGE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> CHEST_RARITY;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> DAMAGE_INCREASE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> DAMAGE_INCREASE_2;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> DAMAGE_ILLAGERS;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> DAMAGE_SPIDERS;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> DAMAGE_UNDEAD;
        @Expose
        public WeightedList.Entry<IntegerAttribute.Generator> ON_HIT_CHAIN;
        @Expose
        public WeightedList.Entry<IntegerAttribute.Generator> ON_HIT_AOE;
        @Expose
        public WeightedList.Entry<FloatAttribute.Generator> ON_HIT_STUN;

        public BaseModifiers copy() {
            BaseModifiers copy = new BaseModifiers();
            copy.ADD_ARMOR = this.ADD_ARMOR;
            copy.ADD_ARMOR_2 = this.ADD_ARMOR_2;
            copy.ADD_ARMOR_TOUGHNESS = this.ADD_ARMOR_TOUGHNESS;
            copy.ADD_ARMOR_TOUGHNESS_2 = this.ADD_ARMOR_TOUGHNESS_2;
            copy.ADD_KNOCKBACK_RESISTANCE = this.ADD_KNOCKBACK_RESISTANCE;
            copy.ADD_KNOCKBACK_RESISTANCE_2 = this.ADD_KNOCKBACK_RESISTANCE_2;
            copy.ADD_ATTACK_DAMAGE = this.ADD_ATTACK_DAMAGE;
            copy.ADD_ATTACK_DAMAGE_2 = this.ADD_ATTACK_DAMAGE_2;
            copy.ADD_ATTACK_SPEED = this.ADD_ATTACK_SPEED;
            copy.ADD_ATTACK_SPEED_2 = this.ADD_ATTACK_SPEED_2;
            copy.ADD_DURABILITY = this.ADD_DURABILITY;
            copy.ADD_DURABILITY_2 = this.ADD_DURABILITY_2;
            copy.EXTRA_LEECH_RATIO = this.EXTRA_LEECH_RATIO;
            copy.ADD_EXTRA_LEECH_RATIO = this.ADD_EXTRA_LEECH_RATIO;
            copy.EXTRA_RESISTANCE = this.EXTRA_RESISTANCE;
            copy.ADD_EXTRA_RESISTANCE = this.ADD_EXTRA_RESISTANCE;
            copy.EXTRA_PARRY_CHANCE = this.EXTRA_PARRY_CHANCE;
            copy.ADD_EXTRA_PARRY_CHANCE = this.ADD_EXTRA_PARRY_CHANCE;
            copy.EXTRA_HEALTH = this.EXTRA_HEALTH;
            copy.ADD_EXTRA_HEALTH = this.ADD_EXTRA_HEALTH;
            copy.EXTRA_EFFECTS = this.EXTRA_EFFECTS;
            copy.ADD_REACH = this.ADD_REACH;
            copy.ADD_REACH_2 = this.ADD_REACH_2;
            copy.ADD_COOLDOWN_REDUCTION = this.ADD_COOLDOWN_REDUCTION;
            copy.ADD_COOLDOWN_REDUCTION_2 = this.ADD_COOLDOWN_REDUCTION_2;
            copy.ADD_MIN_VAULT_LEVEL = this.ADD_MIN_VAULT_LEVEL;
            copy.ADD_REGEN_CLOUD = this.ADD_REGEN_CLOUD;
            copy.ADD_WEAKENING_CLOUD = this.ADD_WEAKENING_CLOUD;
            copy.ADD_WITHER_CLOUD = this.ADD_WITHER_CLOUD;
            copy.ADD_POISON_IMMUNITY = this.ADD_POISON_IMMUNITY;
            copy.ADD_WITHER_IMMUNITY = this.ADD_WITHER_IMMUNITY;
            copy.ADD_HUNGER_IMMUNITY = this.ADD_HUNGER_IMMUNITY;
            copy.ADD_MINING_FATIGUE_IMMUNITY = this.ADD_MINING_FATIGUE_IMMUNITY;
            copy.ADD_SLOWNESS_IMMUNITY = this.ADD_SLOWNESS_IMMUNITY;
            copy.ADD_WEAKNESS_IMMUNITY = this.ADD_WEAKNESS_IMMUNITY;
            copy.ADD_FEATHER_FEET = this.ADD_FEATHER_FEET;
            copy.ADD_SOULBOUND = this.ADD_SOULBOUND;
            copy.FATAL_STRIKE_CHANCE = this.FATAL_STRIKE_CHANCE;
            copy.FATAL_STRIKE_DAMAGE = this.FATAL_STRIKE_DAMAGE;
            copy.THORNS_CHANCE = this.THORNS_CHANCE;
            copy.THORNS_DAMAGE = this.THORNS_DAMAGE;
            copy.CHEST_RARITY = this.CHEST_RARITY;
            copy.DAMAGE_INCREASE = this.DAMAGE_INCREASE;
            copy.DAMAGE_INCREASE_2 = this.DAMAGE_INCREASE_2;
            copy.DAMAGE_ILLAGERS = this.DAMAGE_ILLAGERS;
            copy.DAMAGE_SPIDERS = this.DAMAGE_SPIDERS;
            copy.DAMAGE_UNDEAD = this.DAMAGE_UNDEAD;
            copy.ON_HIT_CHAIN = this.ON_HIT_CHAIN;
            copy.ON_HIT_AOE = this.ON_HIT_AOE;
            copy.ON_HIT_STUN = this.ON_HIT_STUN;
            return copy;
        }


        public List<WeightedList.Entry<? extends VAttribute.Instance.Generator<?>>> getGenerators() {
            return Arrays.asList((WeightedList.Entry<? extends VAttribute.Instance.Generator<?>>[]) new WeightedList.Entry[]{this.ADD_ARMOR, this.ADD_ARMOR_2, this.ADD_ARMOR_TOUGHNESS, this.ADD_ARMOR_TOUGHNESS_2, this.ADD_KNOCKBACK_RESISTANCE, this.ADD_KNOCKBACK_RESISTANCE_2, this.ADD_ATTACK_DAMAGE, this.ADD_ATTACK_DAMAGE_2, this.ADD_ATTACK_SPEED, this.ADD_ATTACK_SPEED_2, this.ADD_DURABILITY, this.ADD_DURABILITY_2, this.EXTRA_LEECH_RATIO, this.ADD_EXTRA_LEECH_RATIO, this.EXTRA_RESISTANCE, this.ADD_EXTRA_RESISTANCE, this.EXTRA_PARRY_CHANCE, this.ADD_EXTRA_PARRY_CHANCE, this.EXTRA_HEALTH, this.ADD_EXTRA_HEALTH, this.EXTRA_EFFECTS, this.ADD_REACH, this.ADD_REACH_2, this.ADD_COOLDOWN_REDUCTION, this.ADD_COOLDOWN_REDUCTION_2, this.ADD_MIN_VAULT_LEVEL, this.ADD_REGEN_CLOUD, this.ADD_WEAKENING_CLOUD, this.ADD_WITHER_CLOUD, this.ADD_POISON_IMMUNITY, this.ADD_WITHER_IMMUNITY, this.ADD_HUNGER_IMMUNITY, this.ADD_MINING_FATIGUE_IMMUNITY, this.ADD_SLOWNESS_IMMUNITY, this.ADD_WEAKNESS_IMMUNITY, this.ADD_FEATHER_FEET, this.ADD_SOULBOUND, this.FATAL_STRIKE_CHANCE, this.FATAL_STRIKE_DAMAGE, this.THORNS_CHANCE, this.THORNS_DAMAGE, this.CHEST_RARITY, this.DAMAGE_INCREASE, this.DAMAGE_INCREASE_2, this.DAMAGE_ILLAGERS, this.DAMAGE_SPIDERS, this.DAMAGE_UNDEAD, this.ON_HIT_CHAIN, this.ON_HIT_AOE, this.ON_HIT_STUN});
        }


        @Nullable
        public WeightedList.Entry<? extends VAttribute.Instance.Generator<?>> getGenerator(VAttribute<?, ?> attribute) {
            WeightedList.Entry<FloatAttribute.Generator> entry = null;
            WeightedList.Entry<? extends VAttribute.Instance.Generator<?>> generatorEntry = null;
            if (attribute == ModAttributes.ADD_ARMOR) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ARMOR;
            } else if (attribute == ModAttributes.ADD_ARMOR_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ARMOR_2;
            } else if (attribute == ModAttributes.ADD_ARMOR_TOUGHNESS) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ARMOR_TOUGHNESS;
            } else if (attribute == ModAttributes.ADD_ARMOR_TOUGHNESS_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ARMOR_TOUGHNESS_2;
            } else if (attribute == ModAttributes.ADD_KNOCKBACK_RESISTANCE) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_KNOCKBACK_RESISTANCE;
            } else if (attribute == ModAttributes.ADD_KNOCKBACK_RESISTANCE_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_KNOCKBACK_RESISTANCE_2;
            } else if (attribute == ModAttributes.ADD_ATTACK_DAMAGE) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ATTACK_DAMAGE;
            } else if (attribute == ModAttributes.ADD_ATTACK_DAMAGE_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ATTACK_DAMAGE_2;
            } else if (attribute == ModAttributes.ADD_ATTACK_SPEED) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ATTACK_SPEED;
            } else if (attribute == ModAttributes.ADD_ATTACK_SPEED_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_ATTACK_SPEED_2;
            } else if (attribute == ModAttributes.ADD_DURABILITY) {
                WeightedList.Entry<IntegerAttribute.Generator> entry1 = this.ADD_DURABILITY;
            } else if (attribute == ModAttributes.ADD_DURABILITY_2) {
                WeightedList.Entry<IntegerAttribute.Generator> entry1 = this.ADD_DURABILITY_2;
            } else if (attribute == ModAttributes.EXTRA_LEECH_RATIO) {
                entry = this.EXTRA_LEECH_RATIO;
            } else if (attribute == ModAttributes.ADD_EXTRA_LEECH_RATIO) {
                entry = this.ADD_EXTRA_LEECH_RATIO;
            } else if (attribute == ModAttributes.EXTRA_RESISTANCE) {
                entry = this.EXTRA_RESISTANCE;
            } else if (attribute == ModAttributes.ADD_EXTRA_RESISTANCE) {
                entry = this.ADD_EXTRA_RESISTANCE;
            } else if (attribute == ModAttributes.EXTRA_PARRY_CHANCE) {
                entry = this.EXTRA_PARRY_CHANCE;
            } else if (attribute == ModAttributes.ADD_EXTRA_PARRY_CHANCE) {
                entry = this.ADD_EXTRA_PARRY_CHANCE;
            } else if (attribute == ModAttributes.EXTRA_EFFECTS) {
                WeightedList.Entry<EffectTalentAttribute.Generator> entry1 = this.EXTRA_EFFECTS;
            } else if (attribute == ModAttributes.ADD_EXTRA_HEALTH) {
                entry = this.ADD_EXTRA_HEALTH;
            } else if (attribute == ModAttributes.EXTRA_HEALTH) {
                entry = this.EXTRA_HEALTH;
            } else if (attribute == ModAttributes.ADD_REACH) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_REACH;
            } else if (attribute == ModAttributes.ADD_REACH_2) {
                WeightedList.Entry<DoubleAttribute.Generator> entry1 = this.ADD_REACH_2;
            } else if (attribute == ModAttributes.ADD_COOLDOWN_REDUCTION) {
                entry = this.ADD_COOLDOWN_REDUCTION;
            } else if (attribute == ModAttributes.ADD_COOLDOWN_REDUCTION_2) {
                entry = this.ADD_COOLDOWN_REDUCTION_2;
            } else if (attribute == ModAttributes.ADD_MIN_VAULT_LEVEL) {
                WeightedList.Entry<IntegerAttribute.Generator> entry1 = this.ADD_MIN_VAULT_LEVEL;
            } else if (attribute == ModAttributes.ADD_REGEN_CLOUD) {
                WeightedList.Entry<EffectCloudAttribute.Generator> entry1 = this.ADD_REGEN_CLOUD;
            } else if (attribute == ModAttributes.ADD_WEAKENING_CLOUD) {
                WeightedList.Entry<EffectCloudAttribute.Generator> entry1 = this.ADD_WEAKENING_CLOUD;
            } else if (attribute == ModAttributes.ADD_WITHER_CLOUD) {
                WeightedList.Entry<EffectCloudAttribute.Generator> entry1 = this.ADD_WITHER_CLOUD;
            } else if (attribute == ModAttributes.ADD_POISON_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_POISON_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_WITHER_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_WITHER_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_HUNGER_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_HUNGER_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_MINING_FATIGUE_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_MINING_FATIGUE_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_SLOWNESS_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_SLOWNESS_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_WEAKNESS_IMMUNITY) {
                WeightedList.Entry<EffectAttribute.Generator> entry1 = this.ADD_WEAKNESS_IMMUNITY;
            } else if (attribute == ModAttributes.ADD_FEATHER_FEET) {
                entry = this.ADD_FEATHER_FEET;
            } else if (attribute == ModAttributes.ADD_SOULBOUND) {
                WeightedList.Entry<BooleanAttribute.Generator> entry1 = this.ADD_SOULBOUND;
            } else if (attribute == ModAttributes.FATAL_STRIKE_CHANCE) {
                entry = this.FATAL_STRIKE_CHANCE;
            } else if (attribute == ModAttributes.FATAL_STRIKE_DAMAGE) {
                entry = this.FATAL_STRIKE_DAMAGE;
            } else if (attribute == ModAttributes.THORNS_CHANCE) {
                entry = this.THORNS_CHANCE;
            } else if (attribute == ModAttributes.THORNS_DAMAGE) {
                entry = this.THORNS_DAMAGE;
            } else if (attribute == ModAttributes.CHEST_RARITY) {
                entry = this.CHEST_RARITY;
            } else if (attribute == ModAttributes.DAMAGE_INCREASE) {
                entry = this.DAMAGE_INCREASE;
            } else if (attribute == ModAttributes.DAMAGE_INCREASE_2) {
                entry = this.DAMAGE_INCREASE_2;
            } else if (attribute == ModAttributes.DAMAGE_ILLAGERS) {
                entry = this.DAMAGE_ILLAGERS;
            } else if (attribute == ModAttributes.DAMAGE_SPIDERS) {
                entry = this.DAMAGE_SPIDERS;
            } else if (attribute == ModAttributes.DAMAGE_UNDEAD) {
                entry = this.DAMAGE_UNDEAD;
            } else if (attribute == ModAttributes.ON_HIT_CHAIN) {
                WeightedList.Entry<IntegerAttribute.Generator> entry1 = this.ON_HIT_CHAIN;
            } else if (attribute == ModAttributes.ON_HIT_AOE) {
                WeightedList.Entry<IntegerAttribute.Generator> entry1 = this.ON_HIT_AOE;
            } else if (attribute == ModAttributes.ON_HIT_STUN) {
                entry = this.ON_HIT_STUN;
            }
            return (WeightedList.Entry) entry;
        }
    }

    public static class Tier {
        @Expose
        public String NAME;
        @Expose
        public Map<String, VaultGearConfig.BaseAttributes> BASE_ATTRIBUTES;
        @Expose
        public Map<String, VaultGearConfig.BaseModifiers> BASE_MODIFIERS;

        public String getName() {
            return this.NAME;
        }

        public Optional<VaultGearConfig.BaseAttributes> getAttributes(ItemStack stack) {
            if (stack.getItem() instanceof VaultGear) {
                return Optional.ofNullable(this.BASE_ATTRIBUTES.get(stack.getItem().getRegistryName().toString()));
            }
            return Optional.empty();
        }

        public Optional<VaultGearConfig.BaseModifiers> getModifiers(ItemStack stack) {
            if (stack.getItem() instanceof VaultGear) {
                return Optional.ofNullable(this.BASE_MODIFIERS.get(stack.getItem().getRegistryName().toString()));
            }
            return Optional.empty();
        }

        protected void reset() {
            resetAttributes();
            resetModifiers();
        }

        private void resetAttributes() {
            VaultGearConfig.BaseAttributes SWORD = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes AXE = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes DAGGER = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes HELMET = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes CHESTPLATE = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes LEGGINGS = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes BOOTS = new VaultGearConfig.BaseAttributes();
            VaultGearConfig.BaseAttributes ALL_IDOLS = new VaultGearConfig.BaseAttributes();


            SWORD


                    .ATTACK_DAMAGE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(6.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(7.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(20, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-0.5D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD));

            SWORD


                    .ATTACK_DAMAGE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(6.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(7.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(20, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-0.5D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD));

            SWORD


                    .ATTACK_SPEED = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(16, 0.1D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.MULTIPLY));

            SWORD


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            SWORD


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            SWORD


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            SWORD


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            AXE


                    .ATTACK_DAMAGE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(8.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(9.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(20, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(0.5D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD));

            AXE


                    .ATTACK_SPEED = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(10, 0.1D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.MULTIPLY));

            AXE


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            AXE


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            AXE


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            AXE


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            DAGGER


                    .ATTACK_DAMAGE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(3.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(4.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(20, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-0.5D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD));

            DAGGER


                    .ATTACK_SPEED = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(20, 0.1D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).add(Double.valueOf(-1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.MULTIPLY));

            DAGGER


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            DAGGER


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            DAGGER


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            DAGGER


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            HELMET


                    .ARMOR = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(3.0D), PooledAttribute.Rolls.ofBinomial(2, 0.5D), pool -> pool.add(Double.valueOf(1.0D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .ARMOR_TOUGHNESS = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(2.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(3.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .KNOCKBACK_RESISTANCE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(4, 0.4D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            HELMET


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            HELMET


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            HELMET


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            CHESTPLATE


                    .ARMOR = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(8.0D), PooledAttribute.Rolls.ofBinomial(2, 0.5D), pool -> pool.add(Double.valueOf(1.0D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .ARMOR_TOUGHNESS = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(2.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(3.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .KNOCKBACK_RESISTANCE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(4, 0.4D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            CHESTPLATE


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            CHESTPLATE


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            CHESTPLATE


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            LEGGINGS


                    .ARMOR = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(6.0D), PooledAttribute.Rolls.ofBinomial(2, 0.5D), pool -> pool.add(Double.valueOf(1.0D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .ARMOR_TOUGHNESS = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(2.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(3.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .KNOCKBACK_RESISTANCE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(4, 0.4D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            LEGGINGS


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            LEGGINGS


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            LEGGINGS


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            BOOTS


                    .ARMOR = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(3.0D), PooledAttribute.Rolls.ofBinomial(2, 0.5D), pool -> pool.add(Double.valueOf(1.0D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .ARMOR_TOUGHNESS = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Double.valueOf(2.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 2).add(Double.valueOf(3.0D), DoubleAttribute.of(NumberAttribute.Type.SET), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .KNOCKBACK_RESISTANCE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofBinomial(4, 0.4D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1561), IntegerAttribute.of(NumberAttribute.Type.SET), 2).add(Integer.valueOf(2031), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.5F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(15), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .MAX_REPAIRS = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(3), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .MIN_VAULT_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .FEATHER_FEET = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            BOOTS


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            BOOTS


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            BOOTS


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            ALL_IDOLS


                    .DURABILITY = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(0), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Integer.valueOf(1000), IntegerAttribute.of(NumberAttribute.Type.SET), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            ALL_IDOLS


                    .GEAR_LEVEL_CHANCE = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(1.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            ALL_IDOLS


                    .GEAR_MAX_LEVEL = (IntegerAttribute.Generator) IntegerAttribute.generator().add(Integer.valueOf(2), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET));

            ALL_IDOLS


                    .REACH = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET));

            ALL_IDOLS


                    .EFFECT_IMMUNITY = (EffectAttribute.Generator) EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectAttribute.of(EffectAttribute.Type.MERGE));

            ALL_IDOLS


                    .EFFECT_CLOUD = (EffectCloudAttribute.Generator) EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE));

            ALL_IDOLS


                    .COOLDOWN_REDUCTION = (FloatAttribute.Generator) FloatAttribute.generator().add(Float.valueOf(0.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET));

            ALL_IDOLS


                    .SOULBOUND = (BooleanAttribute.Generator) BooleanAttribute.generator().add(Boolean.valueOf(false), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET));


            VaultGearConfig.BaseAttributes IDOL_BENEVOLENT = ALL_IDOLS.copy();
            IDOL_BENEVOLENT


                    .IDOL_TYPE = (EnumAttribute.Generator<PlayerFavourData.VaultGodType>) EnumAttribute.generator(PlayerFavourData.VaultGodType.class).add(PlayerFavourData.VaultGodType.BENEVOLENT, PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EnumAttribute.of(EnumAttribute.Type.SET));

            VaultGearConfig.BaseAttributes IDOL_OMNISCIENT = ALL_IDOLS.copy();
            IDOL_OMNISCIENT


                    .IDOL_TYPE = (EnumAttribute.Generator<PlayerFavourData.VaultGodType>) EnumAttribute.generator(PlayerFavourData.VaultGodType.class).add(PlayerFavourData.VaultGodType.OMNISCIENT, PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EnumAttribute.of(EnumAttribute.Type.SET));

            VaultGearConfig.BaseAttributes IDOL_TIMEKEEPER = ALL_IDOLS.copy();
            IDOL_TIMEKEEPER


                    .IDOL_TYPE = (EnumAttribute.Generator<PlayerFavourData.VaultGodType>) EnumAttribute.generator(PlayerFavourData.VaultGodType.class).add(PlayerFavourData.VaultGodType.TIMEKEEPER, PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EnumAttribute.of(EnumAttribute.Type.SET));

            VaultGearConfig.BaseAttributes IDOL_MALEVOLENCE = ALL_IDOLS.copy();
            IDOL_MALEVOLENCE


                    .IDOL_TYPE = (EnumAttribute.Generator<PlayerFavourData.VaultGodType>) EnumAttribute.generator(PlayerFavourData.VaultGodType.class).add(PlayerFavourData.VaultGodType.MALEVOLENCE, PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(EnumAttribute.of(EnumAttribute.Type.SET));
            IDOL_MALEVOLENCE


                    .ATTACK_DAMAGE = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD));
            IDOL_MALEVOLENCE


                    .ATTACK_SPEED = (DoubleAttribute.Generator) DoubleAttribute.generator().add(Double.valueOf(0.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.MULTIPLY));


            this.BASE_ATTRIBUTES = new LinkedHashMap<>();
            this.BASE_ATTRIBUTES.put(ModItems.SWORD.getRegistryName().toString().toString(), SWORD);
            this.BASE_ATTRIBUTES.put(ModItems.AXE.getRegistryName().toString(), AXE);
            this.BASE_ATTRIBUTES.put(ModItems.HELMET.getRegistryName().toString(), HELMET);
            this.BASE_ATTRIBUTES.put(ModItems.CHESTPLATE.getRegistryName().toString(), CHESTPLATE);
            this.BASE_ATTRIBUTES.put(ModItems.LEGGINGS.getRegistryName().toString(), LEGGINGS);
            this.BASE_ATTRIBUTES.put(ModItems.BOOTS.getRegistryName().toString(), BOOTS);
            this.BASE_ATTRIBUTES.put(ModItems.IDOL_BENEVOLENT.getRegistryName().toString(), IDOL_BENEVOLENT);
            this.BASE_ATTRIBUTES.put(ModItems.IDOL_OMNISCIENT.getRegistryName().toString(), IDOL_OMNISCIENT);
            this.BASE_ATTRIBUTES.put(ModItems.IDOL_TIMEKEEPER.getRegistryName().toString(), IDOL_TIMEKEEPER);
            this.BASE_ATTRIBUTES.put(ModItems.IDOL_MALEVOLENCE.getRegistryName().toString(), IDOL_MALEVOLENCE);
        }

        private void resetModifiers() {
            VaultGearConfig.BaseModifiers SWORD = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers AXE = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers DAGGER = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers HELMET = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers CHESTPLATE = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers LEGGINGS = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers BOOTS = new VaultGearConfig.BaseModifiers();
            VaultGearConfig.BaseModifiers ALL_IDOLS = new VaultGearConfig.BaseModifiers();


            SWORD


                    .ADD_ATTACK_DAMAGE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_ATTACK_DAMAGE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_ATTACK_SPEED = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_ATTACK_SPEED_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_COOLDOWN_REDUCTION_2 = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            SWORD


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            SWORD


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            AXE


                    .ADD_ATTACK_DAMAGE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_ATTACK_DAMAGE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_ATTACK_SPEED = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_ATTACK_SPEED_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_COOLDOWN_REDUCTION_2 = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            AXE


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            AXE


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            DAGGER


                    .ADD_ATTACK_DAMAGE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_ATTACK_DAMAGE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_ATTACK_SPEED = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_ATTACK_SPEED_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.3D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_COOLDOWN_REDUCTION_2 = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            DAGGER


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            DAGGER


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            HELMET


                    .ADD_ARMOR = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_ARMOR_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_ARMOR_TOUGHNESS = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_ARMOR_TOUGHNESS_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_KNOCKBACK_RESISTANCE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_KNOCKBACK_RESISTANCE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            HELMET


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            HELMET


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            CHESTPLATE


                    .ADD_ARMOR = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_ARMOR_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_ARMOR_TOUGHNESS = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_ARMOR_TOUGHNESS_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_KNOCKBACK_RESISTANCE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_KNOCKBACK_RESISTANCE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            CHESTPLATE


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            CHESTPLATE


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            LEGGINGS


                    .ADD_ARMOR = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_ARMOR_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_ARMOR_TOUGHNESS = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_ARMOR_TOUGHNESS_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_KNOCKBACK_RESISTANCE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_KNOCKBACK_RESISTANCE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            LEGGINGS


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            LEGGINGS


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            BOOTS


                    .ADD_ARMOR = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_ARMOR_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_ARMOR_TOUGHNESS = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_ARMOR_TOUGHNESS_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(2.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_KNOCKBACK_RESISTANCE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_KNOCKBACK_RESISTANCE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.2D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(500), PooledAttribute.Rolls.ofConstant(1), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(2.0F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.DAMAGE_RESISTANCE, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 2).add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 2, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.2F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_MIN_VAULT_LEVEL = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(-5), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 0, false, true)}), 20, 2.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 1, false, true)}), 40, 3.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.REGENERATION, 20, 2, false, true)}), 60, 4.0F, -1, true, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 0, false, true)}), 20, 2.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 1, false, true)}), 40, 3.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 20, 2, false, true)}), 60, 4.0F, -1, false, 0.5F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 1);

            BOOTS


                    .ADD_FEATHER_FEET = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.05F), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 1);

            BOOTS


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 1);


            ALL_IDOLS


                    .ADD_DURABILITY = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(200), PooledAttribute.Rolls.ofBinomial(5, 0.5D), pool -> pool.add(Integer.valueOf(50), IntegerAttribute.of(NumberAttribute.Type.ADD), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 20);

            ALL_IDOLS


                    .ADD_DURABILITY_2 = new WeightedList.Entry(IntegerAttribute.generator().add(Integer.valueOf(200), PooledAttribute.Rolls.ofBinomial(5, 0.5D), pool -> pool.add(Integer.valueOf(50), IntegerAttribute.of(NumberAttribute.Type.ADD), 1)).collect(IntegerAttribute.of(NumberAttribute.Type.SET)), 20);

            ALL_IDOLS


                    .ADD_WEAKENING_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Weakening I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 40, 0, false, true)}), 40, 2.0F, 2824704, false, 0.04F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 6).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 40, 0, false, true)}), 40, 3.0F, 2824704, false, 0.05F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 3).add(Collections.singletonList(new EffectCloudEntity.Config("Weakening III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WEAKNESS, 60, 0, false, true)}), 40, 4.0F, 2824704, false, 0.06F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 20);

            ALL_IDOLS


                    .ADD_WITHER_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Withering I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 60, 0, false, true)}), 40, 2.0F, 0, false, 0.05F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 6).add(Collections.singletonList(new EffectCloudEntity.Config("Withering II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 60, 1, false, true)}), 60, 3.0F, 0, false, 0.075F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 3).add(Collections.singletonList(new EffectCloudEntity.Config("Withering III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.WITHER, 60, 2, false, true)}), 60, 4.0F, 0, false, 0.1F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 20);

            ALL_IDOLS


                    .ADD_WITHER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WITHER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 20);

            ALL_IDOLS


                    .ADD_POISON_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.POISON)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 20);

            ALL_IDOLS


                    .ADD_SOULBOUND = new WeightedList.Entry(BooleanAttribute.generator().add(Boolean.valueOf(true), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(BooleanAttribute.of(BooleanAttribute.Type.SET)), 20);


            VaultGearConfig.BaseModifiers IDOL_BENEVOLENT = ALL_IDOLS.copy();

            IDOL_BENEVOLENT


                    .EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(1.0F), PooledAttribute.Rolls.ofBinomial(3, 0.5D), pool -> pool.add(Float.valueOf(1.0F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_BENEVOLENT


                    .ADD_EXTRA_HEALTH = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(1.0F), PooledAttribute.Rolls.ofBinomial(3, 0.5D), pool -> pool.add(Float.valueOf(1.0F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_BENEVOLENT


                    .ADD_REGEN_CLOUD = new WeightedList.Entry(EffectCloudAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate I", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.HEAL, 60, 0, false, true)}), 60, 2.0F, 16711772, true, 0.04F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 6).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate II", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.HEAL, 60, 1, false, true)}), 40, 3.0F, -1, true, 0.04F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 3).add(Collections.singletonList(new EffectCloudEntity.Config("Rejuvenate III", Potions.EMPTY, Arrays.asList(new EffectCloudEntity.Config.CloudEffect[]{new EffectCloudEntity.Config.CloudEffect(Effects.HEAL, 60, 2, false, true)}), 60, 3.0F, -1, true, 0.04F)), EffectCloudAttribute.of(EffectCloudAttribute.Type.SET), 1)).collect(EffectCloudAttribute.of(EffectCloudAttribute.Type.MERGE)), 12);

            IDOL_BENEVOLENT


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.REGENERATION, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            IDOL_BENEVOLENT


                    .ADD_HUNGER_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.HUNGER)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 4);


            VaultGearConfig.BaseModifiers IDOL_OMNISCIENT = ALL_IDOLS.copy();

            IDOL_OMNISCIENT


                    .EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_OMNISCIENT


                    .ADD_EXTRA_RESISTANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_OMNISCIENT


                    .EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_OMNISCIENT


                    .ADD_EXTRA_PARRY_CHANCE = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 20);

            IDOL_OMNISCIENT


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.LUCK, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 1);

            IDOL_OMNISCIENT


                    .ADD_MINING_FATIGUE_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.DIG_SLOWDOWN)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 4);


            VaultGearConfig.BaseModifiers IDOL_TIMEKEEPER = ALL_IDOLS.copy();

            IDOL_TIMEKEEPER


                    .ADD_REACH = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD)), 20);

            IDOL_TIMEKEEPER


                    .ADD_REACH_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(1.0D), PooledAttribute.Rolls.ofEmpty(), pool -> {
            }).collect(DoubleAttribute.of(NumberAttribute.Type.ADD)), 20);

            IDOL_TIMEKEEPER


                    .ADD_COOLDOWN_REDUCTION = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.ADD)), 20);

            IDOL_TIMEKEEPER


                    .ADD_COOLDOWN_REDUCTION_2 = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(5, 0.2D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.ADD)), 20);

            IDOL_TIMEKEEPER


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.MOVEMENT_SPEED, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 2);

            IDOL_TIMEKEEPER


                    .ADD_SLOWNESS_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.MOVEMENT_SLOWDOWN)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 3);


            VaultGearConfig.BaseModifiers IDOL_MALEVOLENCE = ALL_IDOLS.copy();

            IDOL_MALEVOLENCE


                    .ADD_ATTACK_DAMAGE = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.1D), PooledAttribute.Rolls.ofBinomial(9, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 16);

            IDOL_MALEVOLENCE


                    .ADD_ATTACK_DAMAGE_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.1D), PooledAttribute.Rolls.ofBinomial(9, 0.5D), pool -> pool.add(Double.valueOf(0.1D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 16);

            IDOL_MALEVOLENCE


                    .ADD_ATTACK_SPEED = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.01D), PooledAttribute.Rolls.ofBinomial(15, 0.5D), pool -> pool.add(Double.valueOf(0.01D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 16);

            IDOL_MALEVOLENCE


                    .ADD_ATTACK_SPEED_2 = new WeightedList.Entry(DoubleAttribute.generator().add(Double.valueOf(0.01D), PooledAttribute.Rolls.ofBinomial(15, 0.5D), pool -> pool.add(Double.valueOf(0.01D), DoubleAttribute.of(NumberAttribute.Type.ADD), 1)).collect(DoubleAttribute.of(NumberAttribute.Type.SET)), 16);

            IDOL_MALEVOLENCE


                    .EXTRA_LEECH_RATIO = new WeightedList.Entry(FloatAttribute.generator().add(Float.valueOf(0.01F), PooledAttribute.Rolls.ofBinomial(2, 0.5D), pool -> pool.add(Float.valueOf(0.01F), FloatAttribute.of(NumberAttribute.Type.ADD), 1)).collect(FloatAttribute.of(NumberAttribute.Type.SET)), 4);

            IDOL_MALEVOLENCE


                    .EXTRA_EFFECTS = new WeightedList.Entry(EffectTalentAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectTalent(0, Effects.DIG_SPEED, 1, EffectTalent.Type.ICON_ONLY, EffectTalent.Operator.ADD)), EffectTalentAttribute.of(EffectTalentAttribute.Type.SET), 1)).collect(EffectTalentAttribute.of(EffectTalentAttribute.Type.MERGE)), 3);

            IDOL_MALEVOLENCE


                    .ADD_WEAKNESS_IMMUNITY = new WeightedList.Entry(EffectAttribute.generator().add(new ArrayList(), PooledAttribute.Rolls.ofConstant(1), pool -> pool.add(Collections.singletonList(new EffectAttribute.Instance(Effects.WEAKNESS)), EffectAttribute.of(EffectAttribute.Type.SET), 1)).collect(EffectAttribute.of(EffectAttribute.Type.MERGE)), 3);


            this.BASE_MODIFIERS = new LinkedHashMap<>();
            this.BASE_MODIFIERS.put(ModItems.SWORD.getRegistryName().toString(), SWORD);
            this.BASE_MODIFIERS.put(ModItems.AXE.getRegistryName().toString(), AXE);
            this.BASE_MODIFIERS.put(ModItems.HELMET.getRegistryName().toString(), HELMET);
            this.BASE_MODIFIERS.put(ModItems.CHESTPLATE.getRegistryName().toString(), CHESTPLATE);
            this.BASE_MODIFIERS.put(ModItems.LEGGINGS.getRegistryName().toString(), LEGGINGS);
            this.BASE_MODIFIERS.put(ModItems.BOOTS.getRegistryName().toString(), BOOTS);
            this.BASE_MODIFIERS.put(ModItems.IDOL_BENEVOLENT.getRegistryName().toString(), IDOL_BENEVOLENT);
            this.BASE_MODIFIERS.put(ModItems.IDOL_OMNISCIENT.getRegistryName().toString(), IDOL_OMNISCIENT);
            this.BASE_MODIFIERS.put(ModItems.IDOL_TIMEKEEPER.getRegistryName().toString(), IDOL_TIMEKEEPER);
            this.BASE_MODIFIERS.put(ModItems.IDOL_MALEVOLENCE.getRegistryName().toString(), IDOL_MALEVOLENCE);
        }
    }


    public static class Scrappy
            extends VaultGearConfig {
        public String getName() {
            return "vault_gear_" + VaultGear.Rarity.SCRAPPY.name().toLowerCase();
        }


        protected void reset() {
            super.reset();

            this.TIERS.forEach(tier -> tier.BASE_ATTRIBUTES.forEach((String string, BaseAttributes attributes) -> {
            }));
        }
    }


    public static class Common
            extends VaultGearConfig {
        public String getName() {
            return "vault_gear_" + VaultGear.Rarity.COMMON.name().toLowerCase();
        }


        protected void reset() {
            super.reset();

            this.TIERS.forEach(tier -> tier.BASE_ATTRIBUTES.forEach((String string, BaseAttributes attributes) -> {
            }));
        }
    }


    public static class Rare
            extends VaultGearConfig {
        public String getName() {
            return "vault_gear_" + VaultGear.Rarity.RARE.name().toLowerCase();
        }


        protected void reset() {
            super.reset();

            this.TIERS.forEach(tier -> tier.BASE_ATTRIBUTES.forEach((String string, BaseAttributes attributes) -> {
            }));
        }
    }


    public static class Epic
            extends VaultGearConfig {
        public String getName() {
            return "vault_gear_" + VaultGear.Rarity.EPIC.name().toLowerCase();
        }


        protected void reset() {
            super.reset();

            this.TIERS.forEach(tier -> tier.BASE_ATTRIBUTES.forEach((String string, BaseAttributes attributes) -> {
            }));
        }
    }


    public static class Omega
            extends VaultGearConfig {
        public String getName() {
            return "vault_gear_" + VaultGear.Rarity.OMEGA.name().toLowerCase();
        }


        protected void reset() {
            super.reset();

            this.TIERS.forEach(tier -> tier.BASE_ATTRIBUTES.forEach((String string, BaseAttributes attributes) -> {
            }));
        }
    }


    public static class General
            extends Config {
        @Expose
        public String DEFAULT_ROLL = "All";
        @Expose
        public Map<String, Roll> ROLLS;
        @Expose
        public List<TierConfig> TIER;

        public String getName() {
            return "vault_gear";
        }


        protected void reset() {
            this.TIER = new ArrayList<>();
            this.TIER.add(new TierConfig("", String.valueOf(65535), 0));
            this.TIER.add(new TierConfig("2", String.valueOf(65535), 100));
            this.TIER.add(new TierConfig("3", String.valueOf(65535), 200));

            this.ROLLS = new LinkedHashMap<>();

            this.ROLLS.put("Scrappy Only", new Roll((new WeightedList())
                    .add(VaultGear.Rarity.SCRAPPY, 1)));

            this.ROLLS.put("Treasure Only", new Roll((new WeightedList())
                    .add(VaultGear.Rarity.COMMON, 1)
                    .add(VaultGear.Rarity.RARE, 1)
                    .add(VaultGear.Rarity.EPIC, 1)
                    .add(VaultGear.Rarity.OMEGA, 1)));

            this.ROLLS.put("All", new Roll((new WeightedList())
                    .add(VaultGear.Rarity.SCRAPPY, 1)
                    .add(VaultGear.Rarity.COMMON, 1)
                    .add(VaultGear.Rarity.RARE, 1)
                    .add(VaultGear.Rarity.EPIC, 1)
                    .add(VaultGear.Rarity.OMEGA, 1)));
        }

        public Roll getDefaultRoll() {
            return getRoll(this.DEFAULT_ROLL).get();
        }

        public Optional<Roll> getRoll(String name) {
            Roll roll = this.ROLLS.get(name);

            if (roll != null) {
                roll.name = name;
            }

            return Optional.ofNullable(roll);
        }

        public TierConfig getTierConfig(int tier) {
            return this.TIER.get(MathHelper.clamp(tier, 0, this.TIER.size()));
        }

        public static class TierConfig {
            @Expose
            private final String name;
            @Expose
            private final String color;
            @Expose
            private final int minLevel;

            public TierConfig(String name, String color, int minLevel) {
                this.name = name;
                this.color = color;
                this.minLevel = minLevel;
            }

            public ITextComponent getDisplay() {
                return (ITextComponent) (new StringTextComponent(this.name)).setStyle(getDisplayColorStyle());
            }

            public Style getDisplayColorStyle() {
                return Style.EMPTY.withColor(Color.fromRgb(Integer.parseInt(this.color)));
            }

            public int getMinLevel() {
                return this.minLevel;
            }
        }

        public static class Roll {
            protected String name;
            @Expose
            protected WeightedList<VaultGear.Rarity> POOL;
            @Expose
            protected int COLOR;

            public Roll(WeightedList<VaultGear.Rarity> pool) {
                this.POOL = pool;
            }

            public String getName() {
                return this.name;
            }

            public int getColor() {
                return this.COLOR;
            }

            public VaultGear.Rarity getRandom(Random random) {
                return (VaultGear.Rarity) this.POOL.getRandom(random);
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultGearConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */