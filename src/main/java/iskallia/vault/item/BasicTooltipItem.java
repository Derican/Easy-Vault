package iskallia.vault.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BasicTooltipItem extends BasicItem {
    private final List<ITextComponent> components;

    public BasicTooltipItem(final ResourceLocation id, final Item.Properties properties, final ITextComponent... components) {
        this(id, properties, Arrays.asList(components));
    }

    public BasicTooltipItem(final ResourceLocation id, final Item.Properties properties, final List<ITextComponent> components) {
        super(id, properties);
        this.components = components;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.addAll(this.components);
    }
}
