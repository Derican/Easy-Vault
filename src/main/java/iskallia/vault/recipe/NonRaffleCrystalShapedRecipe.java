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
    static int MAX_WIDTH;
    static int MAX_HEIGHT;

    public NonRaffleCrystalShapedRecipe(final ResourceLocation idIn, final String groupIn, final int recipeWidthIn, final int recipeHeightIn, final NonNullList<Ingredient> recipeItemsIn, final ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    public boolean matches(final CraftingInventory inv, final World worldIn) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            final ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof VaultCrystalItem) {
                final CrystalData data = VaultCrystalItem.getData(stack);
                if (data.getType() == CrystalData.Type.RAFFLE) {
                    return false;
                }
            }
        }
        return super.matches(inv, worldIn);
    }

    static {
        NonRaffleCrystalShapedRecipe.MAX_WIDTH = 3;
        NonRaffleCrystalShapedRecipe.MAX_HEIGHT = 3;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapedRecipe> {
        public ShapedRecipe fromJson(final ResourceLocation recipeId, final JsonObject json) {
            final String s = JSONUtils.getAsString(json, "group", "");
            final Map<String, Ingredient> map = deserializeKey(JSONUtils.getAsJsonObject(json, "key"));
            final String[] astring = shrink(patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
            final int i = astring[0].length();
            final int j = astring.length;
            final NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
            final ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new NonRaffleCrystalShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        @Nullable
        public ShapedRecipe fromNetwork(final ResourceLocation recipeId, final PacketBuffer buffer) {
            final int i = buffer.readVarInt();
            final int j = buffer.readVarInt();
            final String s = buffer.readUtf(32767);
            final NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
            for (int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }
            final ItemStack itemstack = buffer.readItem();
            return new NonRaffleCrystalShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        public void toNetwork(final PacketBuffer buffer, final ShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());
            buffer.writeUtf(recipe.getGroup());
            for (final Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.getResultItem());
        }

        private static NonNullList<Ingredient> deserializeIngredients(final String[] pattern, final Map<String, Ingredient> keys, final int patternWidth, final int patternHeight) {
            final NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
            final Set<String> set = Sets.newHashSet((Iterable) keys.keySet());
            set.remove(" ");
            for (int i = 0; i < pattern.length; ++i) {
                for (int j = 0; j < pattern[i].length(); ++j) {
                    final String s = pattern[i].substring(j, j + 1);
                    final Ingredient ingredient = keys.get(s);
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

        private static String[] shrink(final String... toShrink) {
            int i = Integer.MAX_VALUE;
            int j = 0;
            int k = 0;
            int l = 0;
            for (int i2 = 0; i2 < toShrink.length; ++i2) {
                final String s = toShrink[i2];
                i = Math.min(i, firstNonSpace(s));
                final int j2 = lastNonSpace(s);
                j = Math.max(j, j2);
                if (j2 < 0) {
                    if (k == i2) {
                        ++k;
                    }
                    ++l;
                } else {
                    l = 0;
                }
            }
            if (toShrink.length == l) {
                return new String[0];
            }
            final String[] astring = new String[toShrink.length - l - k];
            for (int k2 = 0; k2 < astring.length; ++k2) {
                astring[k2] = toShrink[k2 + k].substring(i, j + 1);
            }
            return astring;
        }

        private static int firstNonSpace(final String str) {
            int i;
            for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
            }
            return i;
        }

        private static int lastNonSpace(final String str) {
            int i;
            for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
            }
            return i;
        }

        private static String[] patternFromJson(final JsonArray jsonArr) {
            final String[] astring = new String[jsonArr.size()];
            if (astring.length > NonRaffleCrystalShapedRecipe.MAX_HEIGHT) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, " + NonRaffleCrystalShapedRecipe.MAX_HEIGHT + " is maximum");
            }
            if (astring.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            }
            for (int i = 0; i < astring.length; ++i) {
                final String s = JSONUtils.convertToString(jsonArr.get(i), "pattern[" + i + "]");
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

        private static Map<String, Ingredient> deserializeKey(final JsonObject json) {
            final Map<String, Ingredient> map = Maps.newHashMap();
            for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
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
