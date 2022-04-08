package iskallia.vault.mixin;

import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.state.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;


@Mixin({DebugOverlayGui.class})
public class MixinDebugOverlayGui {
    @Inject(method = {"getPropertyString"}, at = {@At("RETURN")}, cancellable = true)
    public void hidePropertyString(Map.Entry<Property<?>, Comparable<?>> entryIn, CallbackInfoReturnable<String> cir) {
        if (entryIn.getKey() instanceof iskallia.vault.block.property.HiddenIntegerProperty)
            cir.setReturnValue(((Property) entryIn.getKey()).getName() + ": unknown");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinDebugOverlayGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */