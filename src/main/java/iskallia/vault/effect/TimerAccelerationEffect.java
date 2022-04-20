package iskallia.vault.effect;

import iskallia.vault.init.ModEffects;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.time.extension.AccelerationExtension;
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
    public TimerAccelerationEffect(final EffectType typeIn, final int liquidColorIn, final ResourceLocation id) {
        super(typeIn, liquidColorIn);
        this.setRegistryName(id);
    }

    public boolean isInstantenous() {
        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        final EffectInstance effect = player.getEffect(ModEffects.TIMER_ACCELERATION);
        if (effect == null) {
            return;
        }
        final VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault == null) {
            return;
        }
        vault.getPlayers().forEach(vaultPlayer -> {
            final int extraTicks = effect.getAmplifier() * 6;
            final AccelerationExtension extension = new AccelerationExtension(-extraTicks);
            vaultPlayer.getTimer().addTime(extension, 0);
        });
    }
}
