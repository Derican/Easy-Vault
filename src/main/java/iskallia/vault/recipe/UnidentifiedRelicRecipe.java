package iskallia.vault.recipe;

import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModRecipes;
import iskallia.vault.item.RelicPartItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class UnidentifiedRelicRecipe extends SpecialRecipe {
    public UnidentifiedRelicRecipe(final ResourceLocation id) {
        super(id);
    }

    public boolean matches(final CraftingInventory inv, final World world) {
        RelicPartItem relic = null;
        int diamondCount = 0;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            final ItemStack stack = inv.getItem(i);
            if (stack.getItem() == ModItems.VAULT_DIAMOND) {
                if (diamondCount++ == 8) {
                    return false;
                }
            } else {
                if (!(stack.getItem() instanceof RelicPartItem)) {
                    return false;
                }
                if (relic != null) {
                    return false;
                }
                relic = (RelicPartItem) stack.getItem();
            }
        }
        return true;
    }

    public ItemStack assemble(final CraftingInventory inv) {
        return new ItemStack(ModItems.UNIDENTIFIED_RELIC);
    }

    public boolean canCraftInDimensions(final int width, final int height) {
        return width * height >= 9;
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.Serializer.CRAFTING_SPECIAL_UNIDENTIFIED_RELIC;
    }
}
