package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LegendaryTreasureRareConfig extends Config {
    @Expose
    public List<SingleItemEntry> ITEMS = new ArrayList<>();


    public String getName() {
        return "legendary_treasure_rare";
    }


    protected void reset() {
        ItemStack fancyApple = new ItemStack((IItemProvider) Items.APPLE);
        fancyApple.setHoverName((ITextComponent) new StringTextComponent("Fancy Apple"));
        this.ITEMS.add(new SingleItemEntry(fancyApple));

        ItemStack sword = new ItemStack((IItemProvider) Items.GOLDEN_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 5);
        this.ITEMS.add(new SingleItemEntry(sword));
    }

    public ItemStack getRandom() {
        Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;

        SingleItemEntry singleItemEntry = this.ITEMS.get(rand.nextInt(this.ITEMS.size()));

        try {
            Item item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(singleItemEntry.ITEM));
            stack = new ItemStack((IItemProvider) item);
            CompoundNBT nbt = JsonToNBT.parseTag(singleItemEntry.NBT);
            stack.setTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return stack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\LegendaryTreasureRareConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */