// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Slot.class })
public abstract class MixinSlot
{
    @Shadow
    public abstract ItemStack getItem();
    
    @Shadow
    public abstract boolean hasItem();
    
    @Inject(method = { "mayPickup" }, at = { @At("HEAD") }, cancellable = true)
    public void preventRestrictedTake(final PlayerEntity player, final CallbackInfoReturnable<Boolean> cir) {
        final Slot thisSlot = (Slot)(Object)this;
        if (!(thisSlot instanceof CraftingResultSlot)) {
            return;
        }
        this.hasItem();
    }
}
