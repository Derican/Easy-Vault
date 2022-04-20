package iskallia.vault.mixin;

import iskallia.vault.block.property.HiddenIntegerProperty;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.state.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin({DebugOverlayGui.class})
public class MixinDebugOverlayGui {
    @Inject(method = {"getPropertyValueString"}, at = {@At("RETURN")}, cancellable = true)
    public void hidePropertyString(final Map.Entry<Property<?>, Comparable<?>> entryIn, final CallbackInfoReturnable<String> cir) {
        if (entryIn.getKey() instanceof HiddenIntegerProperty) {
            cir.setReturnValue((entryIn.getKey().getName() + ": unknown"));
        }
    }
}
