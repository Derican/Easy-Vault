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
    public static int getOverlevels(ItemStack enchantedBookStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedBookStack);
        for (Enchantment enchantment : enchantments.keySet()) {
            int level = ((Integer) enchantments.get(enchantment)).intValue();
            if (level > enchantment.getMaxLevel()) {
                return level - enchantment.getMaxLevel();
            }
        }
        return -1;
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        CompoundNBT nbt = Optional.<CompoundNBT>ofNullable(stack.getTag()).orElseGet(CompoundNBT::new);
        ListNBT enchantmentsNBT = nbt.getList((stack.getItem() == Items.ENCHANTED_BOOK) ? "StoredEnchantments" : "Enchantments", 10);


        HashMap<Enchantment, Integer> enchantments = new HashMap<>();

        for (int i = 0; i < enchantmentsNBT.size(); i++) {
            CompoundNBT enchantmentNBT = enchantmentsNBT.getCompound(i);
            ResourceLocation id = new ResourceLocation(enchantmentNBT.getString("id"));
            int level = enchantmentNBT.getInt("lvl");

            Enchantment enchantment = (Enchantment) ForgeRegistries.ENCHANTMENTS.getValue(id);
            if (enchantment != null) enchantments.put(enchantment, Integer.valueOf(level));

        }
        return enchantments;
    }

    public static ItemStack increaseFortuneBy(ItemStack itemStack, int amount) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        int level = ((Integer) enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, Integer.valueOf(0))).intValue();
        enchantments.put(Enchantments.BLOCK_FORTUNE, Integer.valueOf(level + amount));
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
        return itemStack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\OverlevelEnchantHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */