package iskallia.vault.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class WutaxShardItem
        extends Item {
    public WutaxShardItem(ResourceLocation id, Item.Properties properties) {
        super(properties);

        setRegistryName(id);
    }


    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((new StringTextComponent("Reduces the level requirement of")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("any vault gear by 1 when combined")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("in an anvil with a vault gear item.")).withStyle(TextFormatting.GRAY));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\WutaxShardItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */