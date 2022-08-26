package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModItems;
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

public class LegendaryTreasureOmegaConfig extends Config {
    @Expose
    public List<SingleItemEntry> ITEMS;

    public LegendaryTreasureOmegaConfig() {
        this.ITEMS = new ArrayList<SingleItemEntry>();
    }

    @Override
    public String getName() {
        return "legendary_treasure_omega";
    }

    @Override
    protected void reset() {
        try {
            {
                ItemStack itemStack = new ItemStack(ModItems.HELMET);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.CHESTPLATE);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.LEGGINGS);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.BOOTS);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.SWORD);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.AXE);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.IDOL_BENEVOLENT);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.IDOL_OMNISCIENT);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.IDOL_TIMEKEEPER);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
            {
                ItemStack itemStack = new ItemStack(ModItems.IDOL_MALEVOLENCE);
                itemStack.setTag(JsonToNBT.parseTag("{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_type\\\",BaseValue:\\\"Omega\\\"}]}}"));
                this.ITEMS.add(new SingleItemEntry(itemStack));
            }
        } catch (CommandSyntaxException e) {

        }
    }

    public ItemStack getRandom() {
        final Random rand = new Random();
        ItemStack stack = ItemStack.EMPTY;
        final SingleItemEntry singleItemEntry = this.ITEMS.get(rand.nextInt(this.ITEMS.size()));
        try {
            final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(singleItemEntry.ITEM));
            stack = new ItemStack(item);
            final CompoundNBT nbt = JsonToNBT.parseTag(singleItemEntry.NBT);
            stack.setTag(nbt);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return stack;
    }
}
