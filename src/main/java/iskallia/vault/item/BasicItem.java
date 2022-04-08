package iskallia.vault.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BasicItem
        extends Item {
    ITextComponent[] tooltip;

    public BasicItem(ResourceLocation id, Item.Properties properties) {
        super(properties);
        setRegistryName(id);
    }

    public BasicItem withTooltip(ITextComponent... tooltip) {
        this.tooltip = tooltip;
        return this;
    }


    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (this.tooltip != null)
            tooltip.addAll(Arrays.asList(this.tooltip));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\BasicItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */