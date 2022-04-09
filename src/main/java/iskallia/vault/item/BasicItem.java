// 
// Decompiled by Procyon v0.6.0
// 

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

public class BasicItem extends Item
{
    ITextComponent[] tooltip;
    
    public BasicItem(final ResourceLocation id, final Item.Properties properties) {
        super(properties);
        this.setRegistryName(id);
    }
    
    public BasicItem withTooltip(final ITextComponent... tooltip) {
        this.tooltip = tooltip;
        return this;
    }
    
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
        if (this.tooltip != null) {
            tooltip.addAll(Arrays.asList(this.tooltip));
        }
    }
}
