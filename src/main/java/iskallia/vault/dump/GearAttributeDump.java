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

public class GearAttributeDump
        extends JsonDump {
    public String fileName() {
        return "gear_attributes.json";
    }


    public JsonObject dumpToJSON() {
        JsonObject jsonObject = new JsonObject();

        JsonObject attributes = new JsonObject();
        addTooltip(attributes, ModAttributes.GEAR_CRAFTED_BY, PossibleValues.stringType(), new TooltipFragment[]{
                TooltipFragment.of("Crafted by ${value}").color(16770048)
        });
        addTooltip(attributes, ModAttributes.GEAR_TIER, PossibleValues.enumType(new Object[]{Integer.valueOf(1), Integer.valueOf(2)} ), new TooltipFragment[]{
                TooltipFragment.of("Tier: "),
                TooltipFragment.of("${value}").color(9556190)
        });
        addTooltip(attributes, ModAttributes.GEAR_RARITY, PossibleValues.enumType(VaultGear.Rarity.class), new TooltipFragment[]{
                TooltipFragment.of("Rarity: "),
                TooltipFragment.of("${value}").color(9556190)
        });
        addTooltip(attributes, ModAttributes.GEAR_SET, PossibleValues.enumType(VaultGear.Set.class), new TooltipFragment[]{
                TooltipFragment.of("Etching: "),
                TooltipFragment.of("${value}").color(11184810)
        });
        addTooltip(attributes, ModAttributes.MAX_REPAIRS, PossibleValues.integerType(), new TooltipFragment[]{
                TooltipFragment.of("Repairs: "),
                TooltipFragment.of("0 / ${value}").color(16777045)
        });
        addTooltip(attributes, ModAttributes.GEAR_MAX_LEVEL, PossibleValues.integerType(), new TooltipFragment[]{
                TooltipFragment.of("Levels: "),
                TooltipFragment.of("0 / ${value}").color(16777045)
        });
        JsonObject modifiers = new JsonObject();
        addTooltip(modifiers, ModAttributes.ADD_ARMOR, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Armor").color(4766456)
        });
        addTooltip(modifiers, ModAttributes.ADD_ARMOR_TOUGHNESS, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Armor Toughness").color(13302672)
        });
        addTooltip(modifiers, ModAttributes.THORNS_CHANCE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Thorns Chance").color(7195648)
        });
        addTooltip(modifiers, ModAttributes.THORNS_DAMAGE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Thorns Damage").color(3646976)
        });
        addTooltip(modifiers, ModAttributes.ADD_KNOCKBACK_RESISTANCE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Knockback Resistance").color(16756751)
        });
        addTooltip(modifiers, ModAttributes.ADD_ATTACK_DAMAGE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Attack Damage").color(13116966)
        });
        addTooltip(modifiers, ModAttributes.ADD_ATTACK_SPEED, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Attack Speed").color(16767592)
        });
        addTooltip(modifiers, ModAttributes.ADD_DURABILITY, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Durability").color(14668030)
        });
        addTooltip(modifiers, ModAttributes.ADD_PLATING, PossibleValues.integerType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Plating").color(14668030)
        });
        addTooltip(modifiers, ModAttributes.ADD_PLATING, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Reach").color(8706047)
        });
        addTooltip(modifiers, ModAttributes.ADD_FEATHER_FEET, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Feather Feet").color(13499899)
        });
        addTooltip(modifiers, ModAttributes.ADD_MIN_VAULT_LEVEL, PossibleValues.integerType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Min Vault Level").color(15523772)
        });
        addTooltip(modifiers, ModAttributes.ADD_COOLDOWN_REDUCTION, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Cooldown Reduction").color(63668)
        });
        addTooltip(modifiers, ModAttributes.EXTRA_LEECH_RATIO, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Leech").color(16716820)
        });
        addTooltip(modifiers, ModAttributes.FATAL_STRIKE_CHANCE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Fatal Strike Chance").color(16523264)
        });
        addTooltip(modifiers, ModAttributes.FATAL_STRIKE_DAMAGE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Fatal Strike Damage").color(12520704)
        });
        addTooltip(modifiers, ModAttributes.EXTRA_HEALTH, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value} Health").color(2293541)
        });
        addTooltip(modifiers, ModAttributes.EXTRA_PARRY_CHANCE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Parry").color(11534098)
        });
        addTooltip(modifiers, ModAttributes.EXTRA_RESISTANCE, PossibleValues.numberType(), new TooltipFragment[]{
                TooltipFragment.of("+${value}% Resistance").color(16702720)
        });
        addTooltip(modifiers, ModAttributes.EFFECT_IMMUNITY, PossibleValues.enumType(new Object[]{"Poison", "Wither", "Hunger", "Mining Fatigue", "Slowness", "Weakness"} ), new TooltipFragment[]{
                TooltipFragment.of("+${value} Immunity").color(10801083)
        });
        addTooltip(modifiers, ModAttributes.EFFECT_CLOUD, PossibleValues.enumType(new Object[]{"Poison", "Wither", "Hunger", "Mining Fatigue", "Slowness", "Weakness"} ), new TooltipFragment[]{
                TooltipFragment.of("+${value} Cloud").color(15007916)
        });
        addTooltip(modifiers, ModAttributes.SOULBOUND, PossibleValues.noneType(), new TooltipFragment[]{
                TooltipFragment.of("Soulbound").color(9856253)
        });
        jsonObject.add("attributes", (JsonElement) attributes);
        jsonObject.add("modifiers", (JsonElement) modifiers);
        return jsonObject;
    }

    private void addTooltip(JsonObject json, VAttribute<?, ?> attribute, PossibleValues possibleValues, TooltipFragment... fragments) {
        JsonObject tooltipJson = new JsonObject();


        String attributeName = Arrays.<String>stream(attribute.getId().getPath().replaceAll("_", " ").split("\\s+")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));

        tooltipJson.addProperty("name", attributeName);

        JsonArray format = new JsonArray();
        for (TooltipFragment fragment : fragments) {
            JsonObject fragmentJson = new JsonObject();
            fragmentJson.addProperty("text", fragment.text);
            fragmentJson.addProperty("color", Integer.valueOf(fragment.color));
            if (fragment.bold) fragmentJson.addProperty("bold", Boolean.valueOf(true));
            if (fragment.italic) fragmentJson.addProperty("italic", Boolean.valueOf(true));
            if (fragment.underline) fragmentJson.addProperty("underline", Boolean.valueOf(true));
            format.add((JsonElement) fragmentJson);
        }
        tooltipJson.add("format", (JsonElement) format);

        JsonObject possibleValuesJson = new JsonObject();
        possibleValuesJson.addProperty("type", possibleValues.type);
        if (possibleValues.values != null)
            possibleValuesJson.add("values", (JsonElement) possibleValues.valuesAsJson());
        tooltipJson.add("possibleValues", (JsonElement) possibleValuesJson);

        json.add(attribute.getId().getPath(), (JsonElement) tooltipJson);
    }

    public static class TooltipFragment {
        String text;
        int color;
        boolean bold;
        boolean italic;
        boolean underline;

        public static TooltipFragment of(String text) {
            TooltipFragment fragment = new TooltipFragment();
            fragment.text = text;
            fragment.color = 16777215;
            return fragment;
        }

        public TooltipFragment color(int color) {
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

        private static PossibleValues type(String type) {
            PossibleValues possibleValues = new PossibleValues();
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

        public static <T extends Enum<?>> PossibleValues enumType(Class<T> enumClass) {
            return enumType(enumNames(enumClass));
        }

        public static PossibleValues enumType(Object... values) {
            PossibleValues possibleValues = new PossibleValues();
            possibleValues.type = "enum";
            possibleValues.values = values;
            return possibleValues;
        }

        public PossibleValues values(Object... values) {
            this.values = values;
            return this;
        }

        public JsonArray valuesAsJson() {
            JsonArray valuesJson = new JsonArray();
            for (Object value : this.values) {
                valuesJson.add(value.toString());
            }
            return valuesJson;
        }

        private static <T extends Enum<?>> Object[] enumNames(Class<T> enumClass) {
            Enum[] arrayOfEnum = (Enum[]) enumClass.getEnumConstants();
            List<String> names = new LinkedList<>();
            for (Enum<?> enumConstant : arrayOfEnum) {
                String enumName = enumConstant.name();


                String normalizedName = Arrays.<String>stream(enumName.replaceAll("_", " ").split("\\s+")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
                names.add(normalizedName);
            }
            return names.toArray();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\dump\GearAttributeDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */