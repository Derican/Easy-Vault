package iskallia.vault.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class NonRaffleCrystalShapedRecipe extends ShapedRecipe {
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;

    public NonRaffleCrystalShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }


    public boolean matches(CraftingInventory inv, World worldIn) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof VaultCrystalItem) {


                CrystalData data = VaultCrystalItem.getData(stack);
                if (data.getType() == CrystalData.Type.RAFFLE)
                    return false;
            }
        }
        return super.matches(inv, worldIn);
    }

    public static class Serializer
            extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ShapedRecipe> {
        public ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            Map<String, Ingredient> map = deserializeKey(JSONUtils.getAsJsonObject(json, "key"));
            String[] astring = shrink(patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new NonRaffleCrystalShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }


        @Nullable
        public ShapedRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf(32767);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < nonnulllist.size(); k++) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new NonRaffleCrystalShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }


        public void toNetwork(PacketBuffer buffer, ShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());
            buffer.writeUtf(recipe.getGroup());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }

        private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(keys.keySet());
            set.remove(" ");

            for (int i = 0; i < pattern.length; i++) {
                for (int j = 0; j < pattern[i].length(); j++) {
                    String s = pattern[i].substring(j, j + 1);
                    Ingredient ingredient = keys.get(s);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }

                    set.remove(s);
                    nonnulllist.set(j + patternWidth * i, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            }
            return nonnulllist;
        }


        private static String[] shrink(String... toShrink) {
            int i = Integer.MAX_VALUE;
            int j = 0;
            int k = 0;
            int l = 0;

            for (int i1 = 0; i1 < toShrink.length; i1++) {
                String s = toShrink[i1];
                i = Math.min(i, firstNonSpace(s));
                int j1 = lastNonSpace(s);
                j = Math.max(j, j1);
                if (j1 < 0) {
                    if (k == i1) {
                        k++;
                    }

                    l++;
                } else {
                    l = 0;
                }
            }

            if (toShrink.length == l) {
                return new String[0];
            }
            String[] astring = new String[toShrink.length - l - k];

            for (int k1 = 0; k1 < astring.length; k1++) {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }


        private static int firstNonSpace(String str) {
            int i = 0;
            while (i < str.length() && str.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        private static int lastNonSpace(String str) {
            int i = str.length() - 1;
            while (i >= 0 && str.charAt(i) == ' ') {
                i--;
            }
            return i;
        }

        private static String[] patternFromJson(JsonArray jsonArr) {
            String[] astring = new String[jsonArr.size()];
            if (astring.length > NonRaffleCrystalShapedRecipe.MAX_HEIGHT)
                throw new JsonSyntaxException("Invalid pattern: too many rows, " + NonRaffleCrystalShapedRecipe.MAX_HEIGHT + " is maximum");
            if (astring.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            }
            for (int i = 0; i < astring.length; i++) {
                String s = JSONUtils.convertToString(jsonArr.get(i), "pattern[" + i + "]");
                if (s.length() > NonRaffleCrystalShapedRecipe.MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + NonRaffleCrystalShapedRecipe.MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }


        private static Map<String, Ingredient> deserializeKey(JsonObject json) {
            Map<String, Ingredient> map = Maps.newHashMap();

            for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>) json.entrySet()) {
                if (((String) entry.getKey()).length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }

                if (" ".equals(entry.getKey())) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }

                map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }

            map.put(" ", Ingredient.EMPTY);
            return map;
        }

    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\recipe\NonRaffleCrystalShapedRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */