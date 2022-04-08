package iskallia.vault.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.time.extension.AccelerationExtension;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TimerAccelerationEffect extends Effect {
    public TimerAccelerationEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);
        setRegistryName(id);
    }


    public boolean isInstantenous() {
        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient())
            return;
        if (event.phase != TickEvent.Phase.END)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        EffectInstance effect = player.getEffect(ModEffects.TIMER_ACCELERATION);

        if (effect == null)
            return;
        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);

        if (vault == null)
            return;
        vault.getPlayers().forEach(vaultPlayer -> {
            int extraTicks = effect.getAmplifier() * 6;
            AccelerationExtension extension = new AccelerationExtension(-extraTicks);
            vaultPlayer.getTimer().addTime((TimeExtension) extension, 0);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\TimerAccelerationEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */