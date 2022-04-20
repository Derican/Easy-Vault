package iskallia.vault.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GearAttributeDump extends JsonDump {
    @Override
    public String fileName() {
        return "gear_attributes.json";
    }

    @Override
    public JsonObject dumpToJSON() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject attributes = new JsonObject();
        this.addTooltip(attributes, ModAttributes.GEAR_CRAFTED_BY, PossibleValues.stringType(), TooltipFragment.of("Crafted by ${value}").color(16770048));
        this.addTooltip(attributes, ModAttributes.GEAR_TIER, PossibleValues.enumType(1, 2), TooltipFragment.of("Tier: "), TooltipFragment.of("${value}").color(9556190));
        this.addTooltip(attributes, ModAttributes.GEAR_RARITY, PossibleValues.enumType(VaultGear.Rarity.class), TooltipFragment.of("Rarity: "), TooltipFragment.of("${value}").color(9556190));
        this.addTooltip(attributes, ModAttributes.GEAR_SET, PossibleValues.enumType(VaultGear.Set.class), TooltipFragment.of("Etching: "), TooltipFragment.of("${value}").color(11184810));
        this.addTooltip(attributes, ModAttributes.MAX_REPAIRS, PossibleValues.integerType(), TooltipFragment.of("Repairs: "), TooltipFragment.of("0 / ${value}").color(16777045));
        this.addTooltip(attributes, ModAttributes.GEAR_MAX_LEVEL, PossibleValues.integerType(), TooltipFragment.of("Levels: "), TooltipFragment.of("0 / ${value}").color(16777045));
        final JsonObject modifiers = new JsonObject();
        this.addTooltip(modifiers, ModAttributes.ADD_ARMOR, PossibleValues.numberType(), TooltipFragment.of("+${value} Armor").color(4766456));
        this.addTooltip(modifiers, ModAttributes.ADD_ARMOR_TOUGHNESS, PossibleValues.numberType(), TooltipFragment.of("+${value} Armor Toughness").color(13302672));
        this.addTooltip(modifiers, ModAttributes.THORNS_CHANCE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Thorns Chance").color(7195648));
        this.addTooltip(modifiers, ModAttributes.THORNS_DAMAGE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Thorns Damage").color(3646976));
        this.addTooltip(modifiers, ModAttributes.ADD_KNOCKBACK_RESISTANCE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Knockback Resistance").color(16756751));
        this.addTooltip(modifiers, ModAttributes.ADD_ATTACK_DAMAGE, PossibleValues.numberType(), TooltipFragment.of("+${value} Attack Damage").color(13116966));
        this.addTooltip(modifiers, ModAttributes.ADD_ATTACK_SPEED, PossibleValues.numberType(), TooltipFragment.of("+${value} Attack Speed").color(16767592));
        this.addTooltip(modifiers, ModAttributes.ADD_DURABILITY, PossibleValues.numberType(), TooltipFragment.of("+${value} Durability").color(14668030));
        this.addTooltip(modifiers, ModAttributes.ADD_PLATING, PossibleValues.integerType(), TooltipFragment.of("+${value} Plating").color(14668030));
        this.addTooltip(modifiers, ModAttributes.ADD_PLATING, PossibleValues.numberType(), TooltipFragment.of("+${value} Reach").color(8706047));
        this.addTooltip(modifiers, ModAttributes.ADD_FEATHER_FEET, PossibleValues.numberType(), TooltipFragment.of("+${value}% Feather Feet").color(13499899));
        this.addTooltip(modifiers, ModAttributes.ADD_MIN_VAULT_LEVEL, PossibleValues.integerType(), TooltipFragment.of("+${value} Min Vault Level").color(15523772));
        this.addTooltip(modifiers, ModAttributes.ADD_COOLDOWN_REDUCTION, PossibleValues.numberType(), TooltipFragment.of("+${value}% Cooldown Reduction").color(63668));
        this.addTooltip(modifiers, ModAttributes.EXTRA_LEECH_RATIO, PossibleValues.numberType(), TooltipFragment.of("+${value}% Leech").color(16716820));
        this.addTooltip(modifiers, ModAttributes.FATAL_STRIKE_CHANCE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Fatal Strike Chance").color(16523264));
        this.addTooltip(modifiers, ModAttributes.FATAL_STRIKE_DAMAGE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Fatal Strike Damage").color(12520704));
        this.addTooltip(modifiers, ModAttributes.EXTRA_HEALTH, PossibleValues.numberType(), TooltipFragment.of("+${value} Health").color(2293541));
        this.addTooltip(modifiers, ModAttributes.EXTRA_PARRY_CHANCE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Parry").color(11534098));
        this.addTooltip(modifiers, ModAttributes.EXTRA_RESISTANCE, PossibleValues.numberType(), TooltipFragment.of("+${value}% Resistance").color(16702720));
        this.addTooltip(modifiers, ModAttributes.EFFECT_IMMUNITY, PossibleValues.enumType("Poison", "Wither", "Hunger", "Mining Fatigue", "Slowness", "Weakness"), TooltipFragment.of("+${value} Immunity").color(10801083));
        this.addTooltip(modifiers, ModAttributes.EFFECT_CLOUD, PossibleValues.enumType("Poison", "Wither", "Hunger", "Mining Fatigue", "Slowness", "Weakness"), TooltipFragment.of("+${value} Cloud").color(15007916));
        this.addTooltip(modifiers, ModAttributes.SOULBOUND, PossibleValues.noneType(), TooltipFragment.of("Soulbound").color(9856253));
        jsonObject.add("attributes", (JsonElement) attributes);
        jsonObject.add("modifiers", (JsonElement) modifiers);
        return jsonObject;
    }

    private void addTooltip(final JsonObject json, final VAttribute<?, ?> attribute, final PossibleValues possibleValues, final TooltipFragment... fragments) {
        final JsonObject tooltipJson = new JsonObject();
        final String attributeName = Arrays.stream(attribute.getId().getPath().replaceAll("_", " ").split("\\s+")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
        tooltipJson.addProperty("name", attributeName);
        final JsonArray format = new JsonArray();
        for (final TooltipFragment fragment : fragments) {
            final JsonObject fragmentJson = new JsonObject();
            fragmentJson.addProperty("text", fragment.text);
            fragmentJson.addProperty("color", (Number) fragment.color);
            if (fragment.bold) {
                fragmentJson.addProperty("bold", Boolean.valueOf(true));
            }
            if (fragment.italic) {
                fragmentJson.addProperty("italic", Boolean.valueOf(true));
            }
            if (fragment.underline) {
                fragmentJson.addProperty("underline", Boolean.valueOf(true));
            }
            format.add((JsonElement) fragmentJson);
        }
        tooltipJson.add("format", (JsonElement) format);
        final JsonObject possibleValuesJson = new JsonObject();
        possibleValuesJson.addProperty("type", possibleValues.type);
        if (possibleValues.values != null) {
            possibleValuesJson.add("values", (JsonElement) possibleValues.valuesAsJson());
        }
        tooltipJson.add("possibleValues", (JsonElement) possibleValuesJson);
        json.add(attribute.getId().getPath(), (JsonElement) tooltipJson);
    }

    public static class TooltipFragment {
        String text;
        int color;
        boolean bold;
        boolean italic;
        boolean underline;

        public static TooltipFragment of(final String text) {
            final TooltipFragment fragment = new TooltipFragment();
            fragment.text = text;
            fragment.color = 16777215;
            return fragment;
        }

        public TooltipFragment color(final int color) {
            this.color = color;
            return this;
        }

        public TooltipFragment bold() {
            this.bold = true;
            return this;
        }

        public TooltipFragment italic() {
            this.italic = true;
            return this;
        }

        public TooltipFragment underline() {
            this.underline = true;
            return this;
        }
    }

    public static class PossibleValues {
        String type;
        Object[] values;

        private static PossibleValues type(final String type) {
            final PossibleValues possibleValues = new PossibleValues();
            possibleValues.type = type;
            return possibleValues;
        }

        private static PossibleValues noneType() {
            return type("none");
        }

        private static PossibleValues stringType() {
            return type("string");
        }

        private static PossibleValues integerType() {
            return type("integer");
        }

        private static PossibleValues numberType() {
            return type("number");
        }

        private static PossibleValues booleanType() {
            return type("boolean");
        }

        public static <T extends Enum<?>> PossibleValues enumType(final Class<T> enumClass) {
            return enumType(enumNames(enumClass));
        }

        public static PossibleValues enumType(final Object... values) {
            final PossibleValues possibleValues = new PossibleValues();
            possibleValues.type = "enum";
            possibleValues.values = values;
            return possibleValues;
        }

        public PossibleValues values(final Object... values) {
            this.values = values;
            return this;
        }

        public JsonArray valuesAsJson() {
            final JsonArray valuesJson = new JsonArray();
            for (final Object value : this.values) {
                valuesJson.add(value.toString());
            }
            return valuesJson;
        }

        private static <T extends Enum<?>> Object[] enumNames(final Class<T> enumClass) {
            final Enum<?>[] enumConstants = enumClass.getEnumConstants();
            final List<String> names = new LinkedList<String>();
            for (final Enum<?> enumConstant : enumConstants) {
                final String enumName = enumConstant.name();
                final String normalizedName = Arrays.stream(enumName.replaceAll("_", " ").split("\\s+")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
                names.add(normalizedName);
            }
            return names.toArray();
        }
    }
}
