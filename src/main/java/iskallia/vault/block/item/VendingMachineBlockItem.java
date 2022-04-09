// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.item;

import iskallia.vault.init.ModItems;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VendingMachineBlockItem extends BlockItem
{
    public VendingMachineBlockItem(final Block block) {
        super(block, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(64));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            final CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
            final ListNBT cores = blockEntityTag.getList("coresList", 10);
            for (final INBT tag : cores) {
                try {
                    final TraderCore core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT)tag);
                    final StringTextComponent text = new StringTextComponent(" Vendor: " + core.getName());
                    text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-26266)));
                    tooltip.add((ITextComponent)text);
                    return;
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
}
