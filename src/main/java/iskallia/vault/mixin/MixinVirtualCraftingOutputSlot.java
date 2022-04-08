package iskallia.vault.mixin;

import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.StageManager;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.VirtualCraftingOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({VirtualCraftingOutputSlot.class})
public abstract class MixinVirtualCraftingOutputSlot {
    @Inject(method = {"canTakeStack"}, at = {@At("HEAD")}, cancellable = true)
    public void preventRestrictedOutput(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        InventoryContainerSlot thisSlot = (InventoryContainerSlot) this;
        if (!thisSlot.hasItem()) {
            return;
        }
        ItemStack resultStack = thisSlot.getItem();

        ResearchTree researchTree = StageManager.getResearchTree(player);
        String restrictedBy = researchTree.restrictedBy(resultStack.getItem(), Restrictions.Type.CRAFTABILITY);
        if (restrictedBy != null)
            cir.setReturnValue(Boolean.valueOf(false));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinVirtualCraftingOutputSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */