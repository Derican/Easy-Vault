// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.event;

import iskallia.vault.init.ModModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class ClientInitEvents
{
    @SubscribeEvent
    public static void onColorHandlerRegister(final ColorHandlerEvent.Item event) {
        ModModels.registerItemColors(event.getItemColors());
    }
}
