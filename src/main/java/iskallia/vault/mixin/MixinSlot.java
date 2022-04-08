package iskallia.vault.mixin;

import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.StageManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Slot.class})
public abstract class MixinSlot {
    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract boolean hasItem();

    @Inject(method = {"canTakeStack"}, at = {@At("HEAD")}, cancellable = true)
    public void preventRestrictedTake(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        Slot thisSlot = (Slot) this;
        if (!(thisSlot instanceof net.minecraft.inventory.container.CraftingResultSlot)) {
            return;
        }
        if (!hasItem()) {
            return;
        }
        ItemStack resultStack = getItem();

        ResearchTree researchTree = StageManager.getResearchTree(player);
        String restrictedBy = researchTree.restrictedBy(resultStack.getItem(), Restrictions.Type.CRAFTABILITY);
        if (restrictedBy != null)
            cir.setReturnValue(Boolean.valueOf(false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */