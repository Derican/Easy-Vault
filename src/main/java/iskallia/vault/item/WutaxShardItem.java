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

public class WutaxShardItem extends Item {
    public WutaxShardItem(final ResourceLocation id, final Item.Properties properties) {
        super(properties);
        this.setRegistryName(id);
    }

    public Rarity getRarity(final ItemStack stack) {
        return Rarity.RARE;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((ITextComponent) new StringTextComponent("Reduces the level requirement of").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("any vault gear by 1 when combined").withStyle(TextFormatting.GRAY));
        tooltip.add((ITextComponent) new StringTextComponent("in an anvil with a vault gear item.").withStyle(TextFormatting.GRAY));
        super.appendHoverText(stack, worldIn, (List) tooltip, flagIn);
    }
}
