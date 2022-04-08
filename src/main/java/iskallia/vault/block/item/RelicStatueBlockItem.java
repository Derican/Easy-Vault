package iskallia.vault.block.item;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.RelicSet;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RelicStatueBlockItem extends BlockItem {
    public RelicStatueBlockItem() {
        super((Block) ModBlocks.RELIC_STATUE, (new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(1));
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();

        if (nbt != null) {
            CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
            String relicSet = blockEntityTag.getString("RelicSet");

            RelicSet set = (RelicSet) RelicSet.REGISTRY.get(new ResourceLocation(relicSet));

            if (set != null) {
                StringTextComponent titleText = new StringTextComponent(" Relic Set: " + set.getName());
                titleText.setStyle(Style.EMPTY.withColor(Color.fromRgb(-26266)));
                tooltip.add(titleText);
            }
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static ItemStack withRelicSet(RelicSet relicSet) {
        ItemStack itemStack = new ItemStack((IItemProvider) ModBlocks.RELIC_STATUE);

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("RelicSet", relicSet.getId().toString());

        CompoundNBT stackNBT = new CompoundNBT();
        stackNBT.put("BlockEntityTag", (INBT) nbt);
        itemStack.setTag(stackNBT);

        return itemStack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\item\RelicStatueBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */