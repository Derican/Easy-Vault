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

public class AdvancedVendingMachineBlockItem
        extends BlockItem {
    public AdvancedVendingMachineBlockItem(Block block) {
        super(block, (new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(64));
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();

        if (nbt != null) {
            CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
            ListNBT cores = blockEntityTag.getList("coresList", 10);
            for (INBT tag : cores) {

                try {
                    TraderCore core = (TraderCore) NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) tag);
                    StringTextComponent text = new StringTextComponent(" Vendor: " + core.getName());
                    text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-26266)));
                    tooltip.add(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\item\AdvancedVendingMachineBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */