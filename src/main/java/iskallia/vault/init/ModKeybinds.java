package iskallia.vault.init;

import iskallia.vault.skill.ability.AbilityGroup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;


@OnlyIn(Dist.CLIENT)
public class ModKeybinds {
    public static KeyBinding openAbilityTree;
    public static KeyBinding openShardTraderScreen;
    public static KeyBinding abilityKey;
    public static KeyBinding abilityWheelKey;
    public static Map<String, KeyBinding> abilityQuickfireKey = new HashMap<>();

    public static void register(FMLClientSetupEvent event) {
        openAbilityTree = createKeyBinding("open_ability_tree", 72);
        openShardTraderScreen = createKeyBinding("open_shard_trader_screen", 296);
        abilityKey = createKeyBinding("ability_key", 71);
        abilityWheelKey = createKeyBinding("ability_wheel_key", 342);

        for (AbilityGroup<?, ?> group : (Iterable<AbilityGroup<?, ?>>) ModConfigs.ABILITIES.getAll()) {
            String abilityDescription = group.getParentName().toLowerCase().replace(' ', '_');
            abilityQuickfireKey.put(group.getParentName(), createKeyBinding("quickselect." + abilityDescription));
        }
    }

    private static KeyBinding createKeyBinding(String name) {
        return createKeyBinding(name, InputMappings.UNKNOWN.getValue());
    }

    private static KeyBinding createKeyBinding(String name, int key) {
        KeyBinding keyBind = new KeyBinding("key.the_vault." + name, key, "key.category.the_vault");


        ClientRegistry.registerKeyBinding(keyBind);
        return keyBind;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModKeybinds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */