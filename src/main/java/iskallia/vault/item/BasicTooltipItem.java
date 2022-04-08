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

public class BasicTooltipItem
        extends BasicItem {
    private final List<ITextComponent> components;

    public BasicTooltipItem(ResourceLocation id, Item.Properties properties, ITextComponent... components) {
        this(id, properties, Arrays.asList(components));
    }

    public BasicTooltipItem(ResourceLocation id, Item.Properties properties, List<ITextComponent> components) {
        super(id, properties);
        this.components = components;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.addAll(this.components);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\BasicTooltipItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */