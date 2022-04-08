package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.client.util.ShaderUtil;
import iskallia.vault.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        Vault.LOGGER.info("setupClient()");
        ModScreens.register(event);
        ModScreens.registerOverlays();
        ModKeybinds.register(event);
        ModEntities.Renderers.register(event);
        MinecraftForge.EVENT_BUS.register(InputEvents.class);
        ModBlocks.registerTileEntityRenderers();
        event.enqueueWork(ShaderUtil::initShaders);
    }

    @SubscribeEvent
    public static void setupCommon(FMLCommonSetupEvent event) {
        Vault.LOGGER.info("setupCommon()");
        ModConfigs.register();
        ModNetwork.initialize();
        ModRecipes.initialize();
    }

    @SubscribeEvent
    public static void setupDedicatedServer(FMLDedicatedServerSetupEvent event) {
        Vault.LOGGER.info("setupDedicatedServer()");
        ModModels.SpecialGearModel.register();
        ModModels.GearModel.register();
        ModModels.SpecialSwordModel.register();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\SetupEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */