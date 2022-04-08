package iskallia.vault.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({SlotItemHandler.class})
public class MixinSlotItemHandler {
    @Inject(method = {"isItemValid"}, at = {@At("HEAD")}, cancellable = true)
    public void itemValid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        SlotItemHandler itemHandler = (SlotItemHandler) this;
        if (itemHandler instanceof tfar.dankstorage.inventory.DankSlot && stack.getItem() instanceof tfar.dankstorage.item.DankItem)
            cir.setReturnValue(Boolean.valueOf(false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinSlotItemHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */