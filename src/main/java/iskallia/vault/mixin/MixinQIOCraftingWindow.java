package iskallia.vault.mixin;

import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.StageManager;
import mekanism.common.content.qio.QIOCraftingWindow;
import mekanism.common.inventory.container.slot.HotBarSlot;
import mekanism.common.inventory.container.slot.MainInventorySlot;
import mekanism.common.inventory.slot.CraftingWindowOutputInventorySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin({QIOCraftingWindow.class})
public class MixinQIOCraftingWindow {
    @Shadow
    @Final
    private CraftingWindowOutputInventorySlot outputSlot;

    @Inject(method = {"performCraft(Lnet/minecraft/entity/player/PlayerEntity;Ljava/util/List;Ljava/util/List;)V"}, at = {@At(value = "INVOKE", target = "Lmekanism/common/content/qio/IQIOCraftingWindowHolder;getHolderWorld()Lnet/minecraft/world/World;")}, cancellable = true, remap = false)
    public void preventShiftCrafting(PlayerEntity player, List<HotBarSlot> hotBarSlots, List<MainInventorySlot> mainInventorySlots, CallbackInfo ci) {
        ItemStack resultStack = this.outputSlot.getStack().copy();

        ResearchTree researchTree = StageManager.getResearchTree(player);
        String restrictedBy = researchTree.restrictedBy(resultStack.getItem(), Restrictions.Type.CRAFTABILITY);
        if (restrictedBy != null) {
            ci.cancel();
        }
    }


    @Inject(method = {"performCraft(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;"}, at = {@At("HEAD")}, cancellable = true, remap = false)
    public void preventCrafting(PlayerEntity player, ItemStack result, int amountCrafted, CallbackInfoReturnable<ItemStack> cir) {
        if (result.isEmpty()) {
            return;
        }

        ResearchTree researchTree = StageManager.getResearchTree(player);
        String restrictedBy = researchTree.restrictedBy(result.getItem(), Restrictions.Type.CRAFTABILITY);
        if (restrictedBy != null)
            cir.setReturnValue(ItemStack.EMPTY);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinQIOCraftingWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */