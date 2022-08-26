package iskallia.vault.event;

import iskallia.vault.client.gui.screen.AbilitySelectionScreen;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityKeyMessage;
import iskallia.vault.network.message.AbilityQuickselectMessage;
import iskallia.vault.network.message.OpenSkillTreeMessage;
import iskallia.vault.network.message.ShardTraderScreenMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class InputEvents {
    private static boolean isShiftDown;

    public static boolean isShiftDown() {
        return InputEvents.isShiftDown;
    }

    @SubscribeEvent
    public static void onShiftKey(final InputEvent.KeyInputEvent event) {
        if (event.getKey() == 340) {
            if (event.getAction() == 1) {
                InputEvents.isShiftDown = true;
            } else if (event.getAction() == 0) {
                InputEvents.isShiftDown = false;
            }
        }
    }

    @SubscribeEvent
    public static void onKey(final InputEvent.KeyInputEvent event) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        onInput(minecraft, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouse(final InputEvent.MouseInputEvent event) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        onInput(minecraft, event.getButton(), event.getAction());
    }

    private static void onInput(final Minecraft minecraft, final int key, final int action) {
        if (minecraft.screen != null || key == -1) {
            return;
        }
        for (final Map.Entry<String, KeyBinding> quickSelectKeybind : ModKeybinds.abilityQuickfireKey.entrySet()) {
            if (quickSelectKeybind.getValue().getKey().getValue() == key) {
                if (action != 1) {
                    return;
                }
                ModNetwork.CHANNEL.sendToServer(new AbilityQuickselectMessage(quickSelectKeybind.getKey()));
            }
        }
        if (ModKeybinds.abilityWheelKey.getKey().getValue() == key) {
            if (action != 1) {
                return;
            }
            minecraft.setScreen(new AbilitySelectionScreen());
            ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(true));
        } else if (ModKeybinds.openShardTraderScreen.consumeClick()) {
            ModNetwork.CHANNEL.sendToServer(new ShardTraderScreenMessage());
        } else if (ModKeybinds.openAbilityTree.consumeClick()) {
            ModNetwork.CHANNEL.sendToServer(new OpenSkillTreeMessage());
        } else if (ModKeybinds.abilityKey.getKey().getValue() == key) {
            if (action == 0) {
                ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(true, false, false, false));
            } else if (action == 1) {
                ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, true, false, false));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(final InputEvent.MouseScrollEvent event) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        final double scrollDelta = event.getScrollDelta();
        if (ModKeybinds.abilityKey.isDown()) {
            if (minecraft.screen == null) {
                if (scrollDelta < 0.0) {
                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, false, false, true));
                } else {
                    ModNetwork.CHANNEL.sendToServer(new AbilityKeyMessage(false, false, true, false));
                }
            }
            event.setCanceled(true);
        }
    }
}
