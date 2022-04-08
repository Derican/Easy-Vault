package iskallia.vault.mixin;

import iskallia.vault.Vault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;
import java.util.stream.Collectors;

@Mixin({BossOverlayGui.class})
public abstract class MixinBossOverlayGui {
    @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private Collection<ClientBossInfo> thing(Map<UUID, ClientBossInfo> map) {
        if ((Minecraft.getInstance()).level.dimension() != Vault.VAULT_KEY) {
            return this.events.values();
        }

        Map<UUID, Entity> entities = new HashMap<>();

        (Minecraft.getInstance()).level.entitiesForRendering().forEach(entity -> entities.put(entity.getUUID(), entity));


        return (Collection<ClientBossInfo>) this.events.entrySet().stream()
                .sorted(Comparator.comparingDouble(o -> {
                    ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;


                    Entity entity = (Entity) entities.get(o.getKey());


                    return (entity == null) ? 2.147483647E9D : (clientPlayerEntity.getName().getString().equals(entity.getCustomName().getString()) ? -2.147483648E9D : clientPlayerEntity.distanceTo(entity));
                })).map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Shadow
    @Final
    private Map<UUID, ClientBossInfo> events;
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinBossOverlayGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */