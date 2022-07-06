// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.item;

import net.minecraft.nbt.INBT;
import java.util.UUID;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class HourglassBlockItem extends BlockItem
{
    public HourglassBlockItem(final Block blockIn) {
        super(blockIn, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final CompoundNBT tag = stack.getOrCreateTag().getCompound("BlockEntityTag");
        if (tag.contains("ownerPlayerName")) {
            tooltip.add((ITextComponent)new StringTextComponent(tag.getString("ownerPlayerName")).withStyle(TextFormatting.GOLD));
        }
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
    
    public static void addHourglassOwner(final ItemStack stack, final UUID playerUUID, final String playerName) {
        if (!(stack.getItem() instanceof HourglassBlockItem)) {
            return;
        }
        final CompoundNBT tileTag = new CompoundNBT();
        tileTag.putUUID("ownerUUID", playerUUID);
        tileTag.putString("ownerPlayerName", playerName);
        stack.getOrCreateTag().put("BlockEntityTag", (INBT)tileTag);
    }
}
