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
    public static Map<String, KeyBinding> abilityQuickfireKey;

    public static void register(final FMLClientSetupEvent event) {
        ModKeybinds.openAbilityTree = createKeyBinding("open_ability_tree", 72);
        ModKeybinds.openShardTraderScreen = createKeyBinding("open_shard_trader_screen", 296);
        ModKeybinds.abilityKey = createKeyBinding("ability_key", 71);
        ModKeybinds.abilityWheelKey = createKeyBinding("ability_wheel_key", 342);
        for (final AbilityGroup<?, ?> group : ModConfigs.ABILITIES.getAll()) {
            final String abilityDescription = group.getParentName().toLowerCase().replace(' ', '_');
            ModKeybinds.abilityQuickfireKey.put(group.getParentName(), createKeyBinding("quickselect." + abilityDescription));
        }
    }

    private static KeyBinding createKeyBinding(final String name) {
        return createKeyBinding(name, InputMappings.UNKNOWN.getValue());
    }

    private static KeyBinding createKeyBinding(final String name, final int key) {
        final KeyBinding keyBind = new KeyBinding("key.the_vault." + name, key, "key.category.the_vault");
        ClientRegistry.registerKeyBinding(keyBind);
        return keyBind;
    }

    static {
        ModKeybinds.abilityQuickfireKey = new HashMap<String, KeyBinding>();
    }
}
