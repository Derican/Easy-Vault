package iskallia.vault.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.dankstorage.item.DankItem;
import tfar.dankstorage.utils.Utils;

@Mixin({DankItem.class})
public class MixinDankItem {
    @Inject(method = {"onItemRightClick"}, cancellable = true, at = {@At(value = "INVOKE", shift = At.Shift.BEFORE, target = "net/minecraft/item/ItemStack.copy()Lnet/minecraft/item/ItemStack;")})
    public void onItemRightClick(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> result) {
        ItemStack bag = player.getItemInHand(hand);
        if (Utils.isConstruction(bag)) {
            ItemStack selectedItem = Utils.getItemStackInSelectedSlot(bag);
            String registryName = selectedItem.getItem().getRegistryName().toString();

            if (registryName.equalsIgnoreCase("quark:pickarang") || registryName.equalsIgnoreCase("quark:flamerang")) {
                result.setReturnValue(new ActionResult(ActionResultType.FAIL, player.getItemInHand(hand)));
                result.cancel();
            }
        }
    }

    @Inject(method = {"onItemUse"}, cancellable = true, at = {@At(value = "INVOKE", target = "Ltfar/dankstorage/utils/Utils;getHandler(Lnet/minecraft/item/ItemStack;)Ltfar/dankstorage/inventory/PortableDankHandler;")})
    public void onItemUse(ItemUseContext ctx, CallbackInfoReturnable<ActionResultType> cir) {
        ItemStack bag = ctx.getItemInHand();
        if (Utils.isConstruction(bag)) {
            ItemStack selectedItem = Utils.getItemStackInSelectedSlot(bag);
            String registryName = selectedItem.getItem().getRegistryName().toString();

            if (registryName.equalsIgnoreCase("quark:pickarang") || registryName.equalsIgnoreCase("quark:flamerang")) {
                cir.setReturnValue(ActionResultType.FAIL);
                cir.cancel();
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinDankItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */