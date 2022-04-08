package iskallia.vault.event;

import iskallia.vault.init.ModModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientInitEvents {
    @SubscribeEvent
    public static void onColorHandlerRegister(ColorHandlerEvent.Item event) {
        ModModels.registerItemColors(event.getItemColors());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\ClientInitEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */