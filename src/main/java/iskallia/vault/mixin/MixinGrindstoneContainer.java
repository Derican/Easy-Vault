// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.item.ItemStack;
import iskallia.vault.item.gear.VaultGear;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.GrindstoneContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({GrindstoneContainer.class})
public class MixinGrindstoneContainer {
    @Shadow
    @Final
    private IInventory resultSlots;
    @Shadow
    @Final
    private IInventory repairSlots;

    @Inject(method = {"createResult"}, at = {@At("HEAD")}, cancellable = true)
    public void outputEmptyOnVaultGears(final CallbackInfo ci) {
        final GrindstoneContainer container = (GrindstoneContainer) (Object) this;
        final ItemStack topStack = this.repairSlots.getItem(0);
        final ItemStack bottomStack = this.repairSlots.getItem(1);
        if (topStack.getItem() instanceof VaultGear || bottomStack.getItem() instanceof VaultGear) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            container.broadcastChanges();
            ci.cancel();
        }
    }
}
