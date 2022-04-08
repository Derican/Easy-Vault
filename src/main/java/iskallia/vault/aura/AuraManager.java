package iskallia.vault.aura;

import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class AuraManager {
    private static final AuraManager INSTANCE = new AuraManager();

    private final Map<RegistryKey<World>, Set<ActiveAura>> activeAuras = new HashMap<>();


    public static AuraManager getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.world.isClientSide() || event.phase != TickEvent.Phase.START) {
            return;
        }
        Set<ActiveAura> auras = INSTANCE.activeAuras.getOrDefault(event.world.dimension(), Collections.emptySet());
        if (auras.isEmpty()) {
            return;
        }
        auras.removeIf(aura -> !aura.canPersist());
        auras.forEach(ActiveAura::updateFromProvider);
        auras.forEach(aura -> aura.getAura().onTick(event.world, aura));
    }

    public void provideAura(AuraProvider provider) {
        ((Set<ActiveAura>) this.activeAuras.computeIfAbsent(provider.getWorld(), key -> new HashSet()))
                .add(new ActiveAura(provider));
    }

    @Nonnull
    public Collection<ActiveAura> getAurasAffecting(Entity entity) {
        Collection<ActiveAura> worldAuras = this.activeAuras.getOrDefault(entity.getCommandSenderWorld().dimension(), Collections.emptySet());
        if (worldAuras.isEmpty()) {
            return worldAuras;
        }
        return (Collection<ActiveAura>) worldAuras.stream().filter(aura -> aura.isAffected(entity)).collect(Collectors.toSet());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\AuraManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */