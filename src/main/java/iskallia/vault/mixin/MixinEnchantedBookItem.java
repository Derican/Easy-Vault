package iskallia.vault.mixin;

import iskallia.vault.util.OverlevelEnchantHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin({EnchantedBookItem.class})
public class MixinEnchantedBookItem {
    @Inject(method = {"addInformation"}, at = {@At("TAIL")})
    public void appendOverlevelBookExplanation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, CallbackInfo ci) {
        if (stack.getItem() == Items.ENCHANTED_BOOK &&
                OverlevelEnchantHelper.getOverlevels(stack) != -1) {
            tooltip.add(new StringTextComponent(""));
            tooltip.add((new StringTextComponent("Upgrades an equipment's EXISTING"))
                    .setStyle(Style.EMPTY.withColor(Color.parseColor("#FFFFFF")).withItalic(Boolean.valueOf(true))));
            tooltip.add((new StringTextComponent("enchantment level when used on Anvil."))
                    .setStyle(Style.EMPTY.withColor(Color.parseColor("#FFFFFF")).withItalic(Boolean.valueOf(true))));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinEnchantedBookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */