// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LegendaryTreasureNormalConfig extends Config
{
    @Expose
    public List<SingleItemEntry> ITEMS;
    
    public LegendaryTreasureNormalConfig() {
        this.ITEMS = new ArrayList<SingleItemEntry>();
    }
    
    @Override
    public String getName() {
        return "legendary_treasure_normal";
    }
    
    @Override
    protected void reset() {
        this.ITEMS.add(new SingleItemEntry(new ItemStack((IItemProvider)Items.APPLE)));
        final ItemStack sword = new ItemStack((IItemProvider)Items.WOODEN_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 1);
        this.ITEMS.add(new SingleItemEntry(sword));
    }
    
    public ItemStack getRandom() {
        final Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;
        final SingleItemEntry singleItemEntry = this.ITEMS.get(rand.nextInt(this.ITEMS.size()));
        try {
            final Item item = (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(singleItemEntry.ITEM));
            stack = new ItemStack((IItemProvider)item);
            final CompoundNBT nbt = JsonToNBT.parseTag(singleItemEntry.NBT);
            stack.setTag(nbt);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return stack;
    }
}
