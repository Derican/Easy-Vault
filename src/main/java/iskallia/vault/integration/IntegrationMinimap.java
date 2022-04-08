package iskallia.vault.integration;

import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;


@OnlyIn(Dist.CLIENT)
@EventBusSubscriber({Dist.CLIENT})
public class IntegrationMinimap {
    private static boolean initialized = false;
    private static Consumer<Integer> setZoomSetting;
    private static Consumer<Boolean> setShowItems;

    public static void overrideItemCheck() {
    }

    private static void makeAccessible(Field f) throws Exception {
        if (Modifier.isFinal(f.getModifiers())) {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            if (!modifiersField.isAccessible()) {
                modifiersField.setAccessible(true);
            }
            modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
        }
    }

    private static void initialize() {
        if (initialized) {
            return;
        }
        try {
            setupConfigAccessors();
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
        initialized = true;
    }

    private static void setupConfigAccessors() throws Exception {
        Object minimapInstance = ModList.get().getModObjectById("xaerominimap").orElseThrow(IllegalStateException::new);
        Object minimapSettings = minimapInstance.getClass().getMethod("getSettings", new Class[0]).invoke(minimapInstance, new Object[0]);

        Class<?> modSettingsClass = Class.forName("xaero.common.settings.ModSettings");

        Field fModSettingsZoom = modSettingsClass.getDeclaredField("zoom");
        fModSettingsZoom.setAccessible(true);
        setZoomSetting = (val -> {
            try {
                fModSettingsZoom.setInt(minimapSettings, val.intValue());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        setZoomSetting.accept(Integer.valueOf(fModSettingsZoom.getInt(minimapSettings)));

        Field fModSettingsShowItems = modSettingsClass.getDeclaredField("showItems");
        fModSettingsShowItems.setAccessible(true);
        setShowItems = (val -> {
            try {
                fModSettingsShowItems.setBoolean(minimapSettings, val.booleanValue());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        setShowItems.accept(Boolean.valueOf(fModSettingsShowItems.getBoolean(minimapSettings)));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!ModList.get().isLoaded("xaerominimap")) {
            return;
        }
        if (!initialized) {
            initialize();
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            return;
        }
        ClientWorld clientWorld = (Minecraft.getInstance()).level;
        if (clientWorld == null || clientWorld.dimension() != Vault.VAULT_KEY) {
            return;
        }
        setZoomSetting.accept(Integer.valueOf(4));
        setShowItems.accept(Boolean.valueOf(false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\integration\IntegrationMinimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */