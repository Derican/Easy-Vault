package iskallia.vault.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OverlevelEnchantHelper {
    public static int getOverlevels(final ItemStack enchantedBookStack) {
        final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedBookStack);
        for (final Enchantment enchantment : enchantments.keySet()) {
            final int level = enchantments.get(enchantment);
            if (level > enchantment.getMaxLevel()) {
                return level - enchantment.getMaxLevel();
            }
        }
        return -1;
    }

    public static Map<Enchantment, Integer> getEnchantments(final ItemStack stack) {
        final CompoundNBT nbt = Optional.ofNullable(stack.getTag()).orElseGet(CompoundNBT::new);
        final ListNBT enchantmentsNBT = nbt.getList((stack.getItem() == Items.ENCHANTED_BOOK) ? "StoredEnchantments" : "Enchantments", 10);
        final HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
        for (int i = 0; i < enchantmentsNBT.size(); ++i) {
            final CompoundNBT enchantmentNBT = enchantmentsNBT.getCompound(i);
            final ResourceLocation id = new ResourceLocation(enchantmentNBT.getString("id"));
            final int level = enchantmentNBT.getInt("lvl");
            final Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
            if (enchantment != null) {
                enchantments.put(enchantment, level);
            }
        }
        return enchantments;
    }

    public static ItemStack increaseFortuneBy(final ItemStack itemStack, final int amount) {
        final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        final int level = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
        enchantments.put(Enchantments.BLOCK_FORTUNE, level + amount);
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
        return itemStack;
    }
}
