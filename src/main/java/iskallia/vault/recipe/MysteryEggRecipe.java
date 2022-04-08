package iskallia.vault.recipe;

import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MysteryEggRecipe extends SpecialRecipe {
    public MysteryEggRecipe(ResourceLocation id) {
        super(id);
    }


    public boolean matches(CraftingInventory inv, World world) {
        int foundEggs = 0;
        int foundAlex = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {


                if (stack.getItem() == ModItems.ALEXANDRITE_GEM) {
                    foundAlex++;
                }

                if (stack.getItem() instanceof net.minecraft.item.SpawnEggItem) {
                    foundEggs++;
                }
            }
        }
        return (foundEggs == 4 && foundAlex == 1);
    }


    public ItemStack assemble(CraftingInventory inv) {
        return new ItemStack((IItemProvider) ModItems.MYSTERY_EGG, 4);
    }


    public boolean canCraftInDimensions(int width, int height) {
        return (width * height >= 5);
    }


    public IRecipeSerializer<?> getSerializer() {
        return (IRecipeSerializer<?>) ModRecipes.Serializer.MYSTERY_EGG_RECIPE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\recipe\MysteryEggRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */