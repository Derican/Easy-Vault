package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.Vault;
import iskallia.vault.config.entry.ItemEntry;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class KeyPressRecipesConfig
        extends Config {
    @Expose
    private List<Recipe> RECIPES;

    public String getName() {
        return "key_press_recipes";
    }

    public List<Recipe> getRecipes() {
        return this.RECIPES;
    }

    public Recipe getRecipeFor(Item keyItem, Item clusterItem) {
        ResourceLocation keyID = keyItem.getRegistryName();
        ResourceLocation clusterID = clusterItem.getRegistryName();

        if (keyID == null || clusterID == null) {
            return null;
        }
        for (Recipe recipe : getRecipes()) {
            if (recipe.KEY_ITEM.ITEM.equals(keyID.toString()) &&
                    recipe.CLUSTER_ITEM.ITEM.equals(clusterID.toString())) {
                return recipe;
            }
        }


        return null;
    }

    public ItemStack getResultFor(Item keyItem, Item clusterItem) {
        Recipe recipe = getRecipeFor(keyItem, clusterItem);

        if (recipe == null) return ItemStack.EMPTY;

        ResourceLocation resultID = new ResourceLocation(recipe.RESULT_ITEM.ITEM);
        Item item = (Item) ForgeRegistries.ITEMS.getValue(resultID);

        if (item == null) {
            Vault.LOGGER.warn("Invalid Key Press recipe result -> {}", recipe.RESULT_ITEM.ITEM);
            return ItemStack.EMPTY;
        }

        try {
            ItemStack resultStack = new ItemStack((IItemProvider) item, recipe.RESULT_ITEM.AMOUNT);

            if (recipe.RESULT_ITEM.NBT != null && !recipe.RESULT_ITEM.NBT.isEmpty()) {
                resultStack.setTag(JsonToNBT.parseTag(recipe.RESULT_ITEM.NBT));
            }

            return resultStack;
        } catch (CommandSyntaxException e) {
            Vault.LOGGER.warn("Malformed NBT at Key Press recipe result -> {} NBT: {}",
                    recipe.RESULT_ITEM.ITEM, recipe.RESULT_ITEM.NBT);
            return ItemStack.EMPTY;
        }
    }


    protected void reset() {
        this.RECIPES = new ArrayList<>();


        Recipe recipe = new Recipe();
        recipe.KEY_ITEM = new SingleItemEntry((IItemProvider) ModItems.BLANK_KEY);
        recipe.CLUSTER_ITEM = new SingleItemEntry((IItemProvider) ModItems.SPARKLETINE_CLUSTER);
        recipe.RESULT_ITEM = new ItemEntry(new ItemStack((IItemProvider) ModItems.SPARKLETINE_KEY));
        this.RECIPES.add(recipe);
    }

    public static class Recipe {
        @Expose
        private SingleItemEntry KEY_ITEM;
        @Expose
        private SingleItemEntry CLUSTER_ITEM;
        @Expose
        private ItemEntry RESULT_ITEM;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\KeyPressRecipesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */