package iskallia.vault.item.gear;

import iskallia.vault.attribute.EffectTalentAttribute;
import iskallia.vault.attribute.FloatAttribute;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.config.VaultGearConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class VaultGearHelper {
    private static final Random rand = new Random();

    public static boolean removeRandomModifier(ItemStack stack) {
        return (removeRandomModifiers(stack, 1) == 1);
    }

    public static int removeRandomModifiers(ItemStack stack, int toRemove) {
        if (!hasModifier(stack)) {
            return 0;
        }

        List<VAttribute<?, ?>> modifiers = getRollableGearModifiers();
        Collections.shuffle(modifiers);

        int removed = 0;
        for (VAttribute<?, ?> attribute : modifiers) {
            if (removed >= toRemove) {
                break;
            }

            if (attribute.exists(stack) && attribute == ModAttributes.EXTRA_EFFECTS) {
                Optional<List<EffectTalent>> possibleEffects = ModAttributes.EXTRA_EFFECTS.getValue(stack).filter(effects -> !effects.isEmpty());
                if (possibleEffects.isPresent()) {
                    List<EffectTalent> effects = possibleEffects.get();
                    Collections.shuffle(effects);

                    EffectTalent removedEffect = effects.remove(0);
                    EffectTalentAttribute created = (EffectTalentAttribute) ModAttributes.EXTRA_EFFECTS.create(stack, effects);
                    if (!((List) created.getValue(stack)).contains(removedEffect)) {
                        removed++;

                        continue;
                    }
                }
            }
            if (attribute.exists(stack) && attribute != ModAttributes.EXTRA_EFFECTS) {
                ListNBT existingAttributes = stack.getOrCreateTag().getCompound("Vault").getList("Attributes", 10);
                if (removeAttribute(attribute, existingAttributes)) {
                    removed++;
                }
            }
        }
        return removed;
    }

    public static void removeAllAttributes(ItemStack stack) {
        List<VAttribute<?, ?>> removeable = new ArrayList<>(ModAttributes.REGISTRY.values());
        List<VAttribute<?, ?>> baseAttributes = getBaseAttributes();

        for (VAttribute<?, ?> attribute : removeable) {
            if (baseAttributes.contains(attribute)) {
                continue;
            }
            removeAttribute(stack, attribute);
        }
    }

    public static boolean removeAttribute(ItemStack stack, VAttribute<?, ?> attribute) {
        if (attribute.exists(stack) && attribute == ModAttributes.EXTRA_EFFECTS) {
            Optional<List<EffectTalent>> possibleEffects = ModAttributes.EXTRA_EFFECTS.getValue(stack).filter(effects -> !effects.isEmpty());
            if (possibleEffects.isPresent()) {
                List<EffectTalent> effects = possibleEffects.get();
                effects.clear();
                ModAttributes.EXTRA_EFFECTS.create(stack, effects);
                return true;
            }
        }

        if (attribute.exists(stack) && attribute != ModAttributes.EXTRA_EFFECTS) {
            ListNBT existingAttributes = stack.getOrCreateTag().getCompound("Vault").getList("Attributes", 10);
            return removeAttribute(attribute, existingAttributes);
        }
        return false;
    }

    private static boolean removeAttribute(VAttribute<?, ?> attribute, ListNBT attributes) {
        return ((Boolean) attributes.stream()
                .map(nbt -> (CompoundNBT) nbt)
                .filter(compoundNBT -> attribute.getId().equals(new ResourceLocation(compoundNBT.getString("Id"))))
                .findFirst()
                .map(attributes::remove)
                .orElse(Boolean.valueOf(false))).booleanValue();
    }

    @Nullable
    public static VAttribute<?, ?> getRandomModifier(ItemStack stack, Random rand) {
        List<VAttribute<?, ?>> modifiers = getRollableGearModifiers();
        Collections.shuffle(modifiers, rand);
        for (VAttribute<?, ?> attribute : modifiers) {
            if (attribute.exists(stack) && attribute == ModAttributes.EXTRA_EFFECTS && (
                    (Boolean) ModAttributes.EXTRA_EFFECTS.getValue(stack).map(effects -> Boolean.valueOf(!effects.isEmpty())).orElse(Boolean.valueOf(false))).booleanValue()) {
                return attribute;
            }


            if (attribute.exists(stack) && attribute != ModAttributes.EXTRA_EFFECTS) {
                return attribute;
            }
        }
        return null;
    }

    public static boolean applyGearModifier(ItemStack stack, VaultGear.Rarity rarity, int tier, VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return false;
        }

        VaultGearConfig.Tier gearTier = (VaultGearConfig.get(rarity)).TIERS.get(tier);
        if (gearTier == null) {
            return false;
        }
        return applyGearModifier(stack, gearTier, attribute);
    }

    public static boolean applyGearModifier(ItemStack stack, VaultGearConfig.Tier tierConfig, VAttribute<?, ?> attribute) {
        if (hasModifier(stack, attribute)) {
            return false;
        }
        VaultGearConfig.BaseModifiers modifiers = (VaultGearConfig.BaseModifiers) tierConfig.BASE_MODIFIERS.get(stack.getItem().getRegistryName().toString());
        if (modifiers == null) {
            return false;
        }
        WeightedList.Entry<? extends VAttribute.Instance.Generator<?>> generatorEntry = modifiers.getGenerator(attribute);
        if (generatorEntry == null) {
            return false;
        }
        VAttribute.Instance.Generator generator = (VAttribute.Instance.Generator) generatorEntry.value;
        attribute.create(stack, rand, generator);
        return true;
    }

    public static boolean hasUsedLevels(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return false;
        }
        float currentLevel = ((Float) ModAttributes.GEAR_LEVEL.get(stack).map(level -> (Float) level.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();
        return (currentLevel > 1.0E-4D);
    }

    public static boolean hasModifier(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return false;
        }

        List<VAttribute<?, ?>> modifiers = getRollableGearModifiers();
        Optional<VAttribute<?, ?>> extraEffects = modifiers.stream().filter(vAttribute -> (vAttribute == ModAttributes.EXTRA_EFFECTS)).findFirst();
        if (extraEffects.isPresent() && (
                (Boolean) ModAttributes.EXTRA_EFFECTS.getValue(stack).map(effects -> Boolean.valueOf(!effects.isEmpty())).orElse(Boolean.valueOf(false))).booleanValue()) {
            return true;
        }

        return modifiers.stream().anyMatch(vAttribute -> (vAttribute != ModAttributes.EXTRA_EFFECTS && vAttribute.exists(stack)));
    }

    public static boolean hasModifier(ItemStack stack, VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return false;
        }

        if (attribute.exists(stack)) {
            if (attribute == ModAttributes.EXTRA_EFFECTS && ((Boolean) ModAttributes.EXTRA_EFFECTS.getValue(stack).map(List::isEmpty).orElse(Boolean.valueOf(false))).booleanValue()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static List<VAttribute<?, ?>> getModifiers(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return Collections.emptyList();
        }

        return (List<VAttribute<?, ?>>) getRollableGearModifiers().stream()
                .filter(modifier -> hasModifier(stack, modifier))
                .collect(Collectors.toList());
    }

    public static boolean canRollModifier(ItemStack stack, VaultGear.Rarity rarity, int tier, VAttribute<?, ?> attribute) {
        if (stack.isEmpty() || !(stack.getItem() instanceof VaultGear)) {
            return false;
        }

        VaultGearConfig.Tier tierConfig = (VaultGearConfig.get(rarity)).TIERS.get(tier);
        return ((Boolean) tierConfig.getModifiers(stack).map(modifiers -> Boolean.valueOf((modifiers.getGenerator(attribute) != null))).orElse(Boolean.valueOf(false))).booleanValue();
    }

    public static int getAttributeValueOnGearSumInt(LivingEntity le, VAttribute<Integer, IntegerAttribute>... attributes) {
        int sum = 0;
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            ItemStack stack = le.getItemBySlot(slotType);
            if (!stack.isEmpty() && stack.getItem() instanceof VaultGear) {
                VaultGear<?> gear = (VaultGear) stack.getItem();
                if (gear.isIntendedForSlot(slotType)) {

                    for (VAttribute<Integer, IntegerAttribute> attribute : attributes)
                        sum += ((Integer) attribute.getBase(stack).orElse(Integer.valueOf(0))).intValue();
                }
            }
        }
        return sum;
    }

    public static float getAttributeValueOnGearSumFloat(LivingEntity le, VAttribute<Float, FloatAttribute>... attributes) {
        float sum = 0.0F;
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            ItemStack stack = le.getItemBySlot(slotType);
            if (!stack.isEmpty() && stack.getItem() instanceof VaultGear) {
                VaultGear<?> gear = (VaultGear) stack.getItem();
                if (gear.isIntendedForSlot(slotType)) {

                    for (VAttribute<Float, FloatAttribute> attribute : attributes)
                        sum += ((Float) attribute.getBase(stack).orElse(Float.valueOf(0.0F))).floatValue();
                }
            }
        }
        return sum;
    }

    public static List<VAttribute<?, ?>> getAliasAttributes(ResourceLocation key) {
        List<VAttribute<?, ?>> attributes = new ArrayList<>();
        switch (key.getPath()) {
            case "add_armor":
            case "add_armor_2":
                attributes.add(ModAttributes.ADD_ARMOR);
                attributes.add(ModAttributes.ADD_ARMOR_2);


                return attributes;
            case "add_armor_toughness":
            case "add_armor_toughness_2":
                attributes.add(ModAttributes.ADD_ARMOR_TOUGHNESS);
                attributes.add(ModAttributes.ADD_ARMOR_TOUGHNESS_2);
                return attributes;
            case "add_knockback_resistance":
            case "add_knockback_resistance_2":
                attributes.add(ModAttributes.ADD_KNOCKBACK_RESISTANCE);
                attributes.add(ModAttributes.ADD_KNOCKBACK_RESISTANCE_2);
                return attributes;
            case "add_attack_damage":
            case "add_attack_damage_2":
                attributes.add(ModAttributes.ADD_ATTACK_DAMAGE);
                attributes.add(ModAttributes.ADD_ATTACK_DAMAGE_2);
                return attributes;
            case "add_attack_speed":
            case "add_attack_speed_2":
                attributes.add(ModAttributes.ADD_ATTACK_SPEED);
                attributes.add(ModAttributes.ADD_ATTACK_SPEED_2);
                return attributes;
            case "add_durability":
            case "add_durability_2":
                attributes.add(ModAttributes.ADD_DURABILITY);
                attributes.add(ModAttributes.ADD_DURABILITY_2);
                return attributes;
            case "add_reach":
            case "add_reach_2":
                attributes.add(ModAttributes.ADD_REACH);
                attributes.add(ModAttributes.ADD_REACH_2);
                return attributes;
            case "add_cooldown_reduction":
            case "add_cooldown_reduction_2":
                attributes.add(ModAttributes.ADD_COOLDOWN_REDUCTION);
                attributes.add(ModAttributes.ADD_COOLDOWN_REDUCTION_2);
                return attributes;
            case "add_extra_leech_ratio":
            case "extra_leech_ratio":
                attributes.add(ModAttributes.ADD_EXTRA_LEECH_RATIO);
                attributes.add(ModAttributes.EXTRA_LEECH_RATIO);
                return attributes;
            case "add_extra_resistance":
            case "extra_resistance":
                attributes.add(ModAttributes.ADD_EXTRA_RESISTANCE);
                attributes.add(ModAttributes.EXTRA_RESISTANCE);
                return attributes;
            case "add_extra_parry_chance":
            case "extra_parry_chance":
                attributes.add(ModAttributes.ADD_EXTRA_PARRY_CHANCE);
                attributes.add(ModAttributes.EXTRA_PARRY_CHANCE);
                return attributes;
            case "add_extra_health":
            case "extra_health":
                attributes.add(ModAttributes.ADD_EXTRA_HEALTH);
                attributes.add(ModAttributes.EXTRA_HEALTH);
                return attributes;
            case "damage_increase":
            case "damage_increase_2":
                attributes.add(ModAttributes.DAMAGE_INCREASE);
                attributes.add(ModAttributes.DAMAGE_INCREASE_2);
                return attributes;
        }
        VAttribute<?, ?> attribute = (VAttribute<?, ?>) ModAttributes.REGISTRY.get(key);
        if (attribute != null) attributes.add(attribute);
        return attributes;
    }


    public static List<VAttribute<?, ?>> getBaseAttributes() {
        return Arrays.asList((VAttribute<?, ?>[]) new VAttribute[]{ModAttributes.GEAR_CRAFTED_BY, ModAttributes.GEAR_SPECIAL_MODEL, ModAttributes.GEAR_COLOR, ModAttributes.GEAR_NAME, ModAttributes.IDOL_TYPE, ModAttributes.GEAR_LEVEL_CHANCE, ModAttributes.GEAR_TIER, ModAttributes.GEAR_ROLL_POOL, ModAttributes.GEAR_ROLL_TYPE});
    }


    public static List<VAttribute<?, ?>> getRollableGearModifiers() {
        return Arrays.asList((VAttribute<?, ?>[]) new VAttribute[]{ModAttributes.ADD_ARMOR, ModAttributes.ADD_ARMOR_2, ModAttributes.ADD_ARMOR_TOUGHNESS, ModAttributes.ADD_ARMOR_TOUGHNESS_2, ModAttributes.ADD_KNOCKBACK_RESISTANCE, ModAttributes.ADD_KNOCKBACK_RESISTANCE_2, ModAttributes.ADD_ATTACK_DAMAGE, ModAttributes.ADD_ATTACK_DAMAGE_2, ModAttributes.ADD_ATTACK_SPEED, ModAttributes.ADD_ATTACK_SPEED_2, ModAttributes.ADD_DURABILITY, ModAttributes.ADD_DURABILITY_2, ModAttributes.ADD_REACH, ModAttributes.ADD_REACH_2, ModAttributes.ADD_COOLDOWN_REDUCTION, ModAttributes.ADD_COOLDOWN_REDUCTION_2, ModAttributes.ADD_MIN_VAULT_LEVEL, ModAttributes.ADD_REGEN_CLOUD, ModAttributes.ADD_WEAKENING_CLOUD, ModAttributes.ADD_WITHER_CLOUD, ModAttributes.ADD_POISON_IMMUNITY, ModAttributes.ADD_WITHER_IMMUNITY, ModAttributes.ADD_HUNGER_IMMUNITY, ModAttributes.ADD_MINING_FATIGUE_IMMUNITY, ModAttributes.ADD_SLOWNESS_IMMUNITY, ModAttributes.ADD_WEAKNESS_IMMUNITY, ModAttributes.ADD_FEATHER_FEET, ModAttributes.ADD_SOULBOUND, ModAttributes.ADD_EXTRA_LEECH_RATIO, ModAttributes.ADD_EXTRA_RESISTANCE, ModAttributes.ADD_EXTRA_PARRY_CHANCE, ModAttributes.ADD_EXTRA_HEALTH, ModAttributes.FATAL_STRIKE_CHANCE, ModAttributes.FATAL_STRIKE_DAMAGE, ModAttributes.THORNS_CHANCE, ModAttributes.THORNS_DAMAGE, ModAttributes.EXTRA_LEECH_RATIO, ModAttributes.EXTRA_RESISTANCE, ModAttributes.EXTRA_PARRY_CHANCE, ModAttributes.EXTRA_HEALTH, ModAttributes.EXTRA_EFFECTS, ModAttributes.CHEST_RARITY, ModAttributes.DAMAGE_INCREASE, ModAttributes.DAMAGE_INCREASE_2, ModAttributes.DAMAGE_ILLAGERS, ModAttributes.DAMAGE_SPIDERS, ModAttributes.DAMAGE_UNDEAD, ModAttributes.ON_HIT_CHAIN, ModAttributes.ON_HIT_AOE, ModAttributes.ON_HIT_STUN});
    }


    public static ITextComponent getDisplayName(VAttribute<?, ?> attribute) {
        if (attribute == ModAttributes.ADD_ARMOR || attribute == ModAttributes.ADD_ARMOR_2)
            return text("Armor", 4766456);
        if (attribute == ModAttributes.ADD_ARMOR_TOUGHNESS || attribute == ModAttributes.ADD_ARMOR_TOUGHNESS_2)
            return text("Armor Toughness", 13302672);
        if (attribute == ModAttributes.THORNS_CHANCE)
            return text("Thorns Chance", 7195648);
        if (attribute == ModAttributes.THORNS_DAMAGE)
            return text("Thorns Damage", 3646976);
        if (attribute == ModAttributes.ADD_KNOCKBACK_RESISTANCE || attribute == ModAttributes.ADD_KNOCKBACK_RESISTANCE_2)
            return text("Knockback Resistance", 16756751);
        if (attribute == ModAttributes.ADD_ATTACK_DAMAGE || attribute == ModAttributes.ADD_ATTACK_DAMAGE_2)
            return text("Attack Damage", 13116966);
        if (attribute == ModAttributes.ADD_ATTACK_SPEED || attribute == ModAttributes.ADD_ATTACK_SPEED_2)
            return text("Attack Speed", 16767592);
        if (attribute == ModAttributes.ADD_DURABILITY || attribute == ModAttributes.ADD_DURABILITY_2)
            return text("Durability", 14668030);
        if (attribute == ModAttributes.ADD_REACH || attribute == ModAttributes.ADD_REACH_2)
            return text("Reach", 8706047);
        if (attribute == ModAttributes.ADD_FEATHER_FEET)
            return text("Feather Feet", 13499899);
        if (attribute == ModAttributes.ADD_COOLDOWN_REDUCTION || attribute == ModAttributes.ADD_COOLDOWN_REDUCTION_2)
            return text("Cooldown Reduction", 63668);
        if (attribute == ModAttributes.EXTRA_LEECH_RATIO || attribute == ModAttributes.ADD_EXTRA_LEECH_RATIO)
            return text("Leech", 16716820);
        if (attribute == ModAttributes.FATAL_STRIKE_CHANCE)
            return text("Fatal Strike Chance", 16523264);
        if (attribute == ModAttributes.FATAL_STRIKE_DAMAGE)
            return text("Fatal Strike Damage", 12520704);
        if (attribute == ModAttributes.EXTRA_HEALTH || attribute == ModAttributes.ADD_EXTRA_HEALTH)
            return text("Health", 2293541);
        if (attribute == ModAttributes.EXTRA_PARRY_CHANCE || attribute == ModAttributes.ADD_EXTRA_PARRY_CHANCE)
            return text("Parry", 11534098);
        if (attribute == ModAttributes.EXTRA_RESISTANCE || attribute == ModAttributes.ADD_EXTRA_RESISTANCE)
            return text("Resistance", 16702720);
        if (attribute == ModAttributes.ADD_HUNGER_IMMUNITY)
            return text("Hunger Immunity", 10801083);
        if (attribute == ModAttributes.ADD_MINING_FATIGUE_IMMUNITY)
            return text("Mining Fatigue Immunity", 10801083);
        if (attribute == ModAttributes.ADD_POISON_IMMUNITY)
            return text("Poison Immunity", 10801083);
        if (attribute == ModAttributes.ADD_SLOWNESS_IMMUNITY)
            return text("Slowness Immunity", 10801083);
        if (attribute == ModAttributes.ADD_WEAKNESS_IMMUNITY)
            return text("Weakness Immunity", 10801083);
        if (attribute == ModAttributes.ADD_WITHER_IMMUNITY)
            return text("Wither Immunity", 10801083);
        if (attribute == ModAttributes.ADD_REGEN_CLOUD)
            return text("Rejuvenate Cloud", 15007916);
        if (attribute == ModAttributes.ADD_WEAKENING_CLOUD)
            return text("Weakening Cloud", 15007916);
        if (attribute == ModAttributes.ADD_WITHER_CLOUD)
            return text("Withering Cloud", 15007916);
        if (attribute == ModAttributes.EXTRA_EFFECTS)
            return text("Potion Effect", 14111487);
        if (attribute == ModAttributes.ADD_SOULBOUND)
            return text("Soulbound", 9856253);
        if (attribute == ModAttributes.CHEST_RARITY)
            return text("Chest Rarity", 11073085);
        if (attribute == ModAttributes.DAMAGE_INCREASE || attribute == ModAttributes.DAMAGE_INCREASE_2)
            return text("Increased Damage", 16739072);
        if (attribute == ModAttributes.DAMAGE_ILLAGERS)
            return text("Spiteful", 40882);
        if (attribute == ModAttributes.DAMAGE_SPIDERS)
            return text("Baneful", 8281694);
        if (attribute == ModAttributes.DAMAGE_UNDEAD)
            return text("Holy", 16382128);
        if (attribute == ModAttributes.ON_HIT_CHAIN)
            return text("Chaining Attacks", 6119096);
        if (attribute == ModAttributes.ON_HIT_AOE)
            return text("Attack AoE", 12085504);
        if (attribute == ModAttributes.ON_HIT_STUN) {
            return text("Stun Attack Chance", 1681124);
        }
        return (ITextComponent) (new StringTextComponent(attribute.getId().toString())).withStyle(TextFormatting.GRAY);
    }

    private static ITextComponent text(String txt, int color) {
        return (ITextComponent) (new StringTextComponent(txt)).setStyle(Style.EMPTY.withColor(Color.fromRgb(color)));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\VaultGearHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */