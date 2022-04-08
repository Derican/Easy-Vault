package iskallia.vault.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ShapelessCopyNbtRecipe extends ShapelessRecipe {
    public ShapelessCopyNbtRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }


    public ItemStack assemble(CraftingInventory inv) {
        ItemStack input = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                input = stack;
                break;
            }
        }
        if (input.isEmpty()) {
            return getResultItem();
        }
        ItemStack out = getResultItem();
        out.setTag(input.getTag());
        return out;
    }

    public static class Serializer
            extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessCopyNbtRecipe> {
        public ShapelessCopyNbtRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty())
                throw new JsonParseException("No ingredients for shapeless recipe");
            if (nonnulllist.size() > 1) {
                throw new JsonParseException("Too many ingredients for blank nbt copy recipe. The max is 1");
            }
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new ShapelessCopyNbtRecipe(recipeId, s, itemstack, nonnulllist);
        }


        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); i++) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        public ShapelessCopyNbtRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < nonnulllist.size(); j++) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new ShapelessCopyNbtRecipe(recipeId, s, itemstack, nonnulllist);
        }

        public void toNetwork(PacketBuffer buffer, ShapelessCopyNbtRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\recipe\ShapelessCopyNbtRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */