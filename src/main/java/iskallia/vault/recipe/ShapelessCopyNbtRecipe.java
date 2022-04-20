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
    public ShapelessCopyNbtRecipe(final ResourceLocation idIn, final String groupIn, final ItemStack recipeOutputIn, final NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, (NonNullList) recipeItemsIn);
    }

    public ItemStack assemble(final CraftingInventory inv) {
        ItemStack input = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            final ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                input = stack;
                break;
            }
        }
        if (input.isEmpty()) {
            return this.getResultItem();
        }
        final ItemStack out = this.getResultItem();
        out.setTag(input.getTag());
        return out;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessCopyNbtRecipe> {
        public ShapelessCopyNbtRecipe fromJson(final ResourceLocation recipeId, final JsonObject json) {
            final String s = JSONUtils.getAsString(json, "group", "");
            final NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (nonnulllist.size() > 1) {
                throw new JsonParseException("Too many ingredients for blank nbt copy recipe. The max is 1");
            }
            final ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new ShapelessCopyNbtRecipe(recipeId, s, itemstack, nonnulllist);
        }

        private static NonNullList<Ingredient> readIngredients(final JsonArray ingredientArray) {
            final NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < ingredientArray.size(); ++i) {
                final Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        public ShapelessCopyNbtRecipe fromNetwork(final ResourceLocation recipeId, final PacketBuffer buffer) {
            final String s = buffer.readUtf(32767);
            final int i = buffer.readVarInt();
            final NonNullList<Ingredient> nonnulllist = (NonNullList<Ingredient>) NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }
            final ItemStack itemstack = buffer.readItem();
            return new ShapelessCopyNbtRecipe(recipeId, s, itemstack, nonnulllist);
        }

        public void toNetwork(final PacketBuffer buffer, final ShapelessCopyNbtRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());
            for (final Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.getResultItem());
        }
    }
}
