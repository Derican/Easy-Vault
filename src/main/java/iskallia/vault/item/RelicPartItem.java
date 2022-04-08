package iskallia.vault.item;

import iskallia.vault.util.RelicSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

public class RelicPartItem
        extends Item {
    protected RelicSet relicSet;

    public RelicPartItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(64));

        setRegistryName(id);
    }

    public RelicSet getRelicSet() {
        return this.relicSet;
    }

    public void setRelicSet(RelicSet relicSet) {
        this.relicSet = relicSet;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        StringTextComponent line = new StringTextComponent("Vault Relic - " + this.relicSet.getName());
        line.setStyle(Style.EMPTY.withColor(Color.fromRgb(-3755746)));
        tooltip.add(new StringTextComponent(""));
        tooltip.add(line);

        super.appendHoverText(stack, world, tooltip, flag);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\RelicPartItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */