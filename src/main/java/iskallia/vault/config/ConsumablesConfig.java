package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.config.entry.ConsumableEffect;
import iskallia.vault.config.entry.ConsumableEntry;
import iskallia.vault.item.consumable.ConsumableType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConsumablesConfig
        extends Config {
    @Expose
    private List<ConsumableEntry> itemEffects;
    @Expose
    private HashMap<String, List<String>> descriptions = new HashMap<>();


    public String getName() {
        return "consumables";
    }


    protected void reset() {
        final ConsumableEntry jadeAppleEntry = (new ConsumableEntry("the_vault:jade_apple", true, 4.0F, ConsumableType.BASIC)).addEffect((new ConsumableEffect("minecraft:haste", 3, 6000)).showIcon());


        final ConsumableEntry cobaltAppleEntry = (new ConsumableEntry("the_vault:cobalt_apple", true, 0.0F, ConsumableType.BASIC)).addEffect((new ConsumableEffect("minecraft:fire_resistance", 1, 6000)).showIcon()).addEffect((new ConsumableEffect("botania:feather_feet", 1, 6000)).showIcon());


        final ConsumableEntry pixieAppleEntry = (new ConsumableEntry("the_vault:pixie_apple", true, 8.0F, ConsumableType.BASIC)).addEffect((new ConsumableEffect("minecraft:speed", 5, 600)).showIcon()).addEffect((new ConsumableEffect("minecraft:slow_falling", 2, 600)).showIcon()).addEffect((new ConsumableEffect("minecraft:jump_boost", 2, 600)).showIcon());


        final ConsumableEntry candyBarEntry = (new ConsumableEntry("the_vault:candy_bar", false, 0.0F, ConsumableType.BASIC)).addEffect((new ConsumableEffect("minecraft:speed", 5, 1200)).showIcon());


        final ConsumableEntry powerBarEntry = (new ConsumableEntry("the_vault:power_bar", false, 0.0F, ConsumableType.BASIC)).addEffect((new ConsumableEffect("minecraft:strength", 4, 600)).showIcon());


        final ConsumableEntry luckyAppleEntry = (new ConsumableEntry("the_vault:lucky_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("minecraft:luck", 1, 2400)).showIcon());


        final ConsumableEntry treasureAppleEntry = (new ConsumableEntry("the_vault:treasure_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("minecraft:luck", 5, 2400)).showIcon());


        final ConsumableEntry powerAppleEntry = (new ConsumableEntry("the_vault:power_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("minecraft:strength", 2, 2400)).showIcon());


        final ConsumableEntry ghostAppleEntry = (new ConsumableEntry("the_vault:ghost_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("the_vault:parry", 15, 2400)).showIcon());


        final ConsumableEntry golemAppleEntry = (new ConsumableEntry("the_vault:golem_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("the_vault:resistance", 10, 2400)).showIcon());


        final ConsumableEntry sweetAppleEntry = (new ConsumableEntry("the_vault:sweet_apple", false, 0.0F, ConsumableType.POWERUP)).addEffect((new ConsumableEffect("minecraft:speed", 2, 2400)).showIcon());

        final ConsumableEntry heartyAppleEntry = new ConsumableEntry("the_vault:hearty_apple", true, 2.0F, ConsumableType.POWERUP);

        this.itemEffects = new ArrayList<ConsumableEntry>() {

        };


        this.descriptions.put("the_vault:jade_apple", createStringList(new String[]{(new StringTextComponent(TextFormatting.GREEN + "Mine Pro!"))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Gives you " + TextFormatting.YELLOW + "2 extra hearts "))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "temporarily.."))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RED + "Haste 3 " + TextFormatting.RESET + "for 5 minutes."))
                .getText()}));


        this.descriptions.put("the_vault:cobalt_apple", createStringList(new String[]{(new StringTextComponent(TextFormatting.GREEN + "No Fear!"))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RED + "Fire Resistance 1 " + TextFormatting.RESET + "and"))
                .getText(), (new StringTextComponent(TextFormatting.RED + "negates fall damage " + TextFormatting.RESET + "for 5 minutes."))
                .getText()}));


        this.descriptions.put("the_vault:pixie_apple", createStringList(new String[]{(new StringTextComponent(TextFormatting.GREEN + "Wannabe Flier!"))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Gives you " + TextFormatting.YELLOW + "2 extra hearts "))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "temporarily.."))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RED + "Speed 5 " + TextFormatting.RESET + "and"))
                .getText(), (new StringTextComponent(TextFormatting.RED + "Jump Boost 2 " + TextFormatting.RESET + "for 30 seconds."))
                .getText()}));


        this.descriptions.put("the_vault:candy_bar", createStringList(new String[]{(new StringTextComponent(TextFormatting.GREEN + "Sugar Rush!"))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Gives you " + TextFormatting.RED + "Speed 5"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 60 seconds."))
                .getText()}));


        this.descriptions.put("the_vault:power_bar", createStringList(new String[]{(new StringTextComponent(TextFormatting.GREEN + "Pumping Iron!"))
                .getText(),
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Gives you " + TextFormatting.RED + "Strength 4"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 30 seconds."))
                .getText()}));


        this.descriptions.put("the_vault:lucky_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+1 Luck"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:treasure_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+5 Luck"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:power_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+2 Strength"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:ghost_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+25% Parry"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:golem_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+15 Resistance"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:sweet_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "+2 Speed"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "for 2 minutes."))
                .getText()
        }));

        this.descriptions.put("the_vault:hearty_apple", createStringList(new String[]{
                newLine(), (new StringTextComponent(TextFormatting.RESET + "Add " + TextFormatting.RED + "1 Extra Heart"))
                .getText(), (new StringTextComponent(TextFormatting.RESET + "temporarily..."))
                .getText()
        }));
    }

    private String newLine() {
        return " ";
    }

    private List<String> createStringList(String... lines) {
        return new ArrayList<>(Arrays.asList(lines));
    }

    public ConsumableEntry get(String id) {
        for (ConsumableEntry setting : this.itemEffects) {
            if (setting.getItemId().equalsIgnoreCase(id)) return setting;
        }
        return null;
    }

    public List<String> getDescriptionFor(String item) {
        return this.descriptions.get(item);
    }

    public float getParryChance() {
        ConsumableEntry entry = get("the_vault:ghost_apple");
        if (entry != null) {
            for (ConsumableEffect effect : entry.getEffects()) {
                if (effect.getEffectId().equalsIgnoreCase(Vault.id("parry").toString())) {
                    return effect.getAmplifier() * 0.01F;
                }
            }
        }
        return 0.0F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ConsumablesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */