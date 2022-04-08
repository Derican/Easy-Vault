package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

public class ModRecipes {
    public static class Serializer {
        public static SpecialRecipeSerializer<RelicSetRecipe> CRAFTING_SPECIAL_RELIC_SET;
        public static SpecialRecipeSerializer<UnidentifiedRelicRecipe> CRAFTING_SPECIAL_UNIDENTIFIED_RELIC;

        public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
            CRAFTING_SPECIAL_RELIC_SET = register(event, "crafting_special_relic_set", new SpecialRecipeSerializer(RelicSetRecipe::new));
            CRAFTING_SPECIAL_UNIDENTIFIED_RELIC = register(event, "crafting_special_unidentified_relic", new SpecialRecipeSerializer(UnidentifiedRelicRecipe::new));
            NON_RAFFLE_CRYSTAL_SHAPED = register(event, "non_raffle_crystal_shaped", new NonRaffleCrystalShapedRecipe.Serializer());
            COPY_NBT_SHAPELESS = register(event, "crafting_shapeless_copy_nbt", new ShapelessCopyNbtRecipe.Serializer());
            MYSTERY_EGG_RECIPE = register(event, "mystery_egg", new SpecialRecipeSerializer(MysteryEggRecipe::new));
        }

        public static NonRaffleCrystalShapedRecipe.Serializer NON_RAFFLE_CRYSTAL_SHAPED;
        public static ShapelessCopyNbtRecipe.Serializer COPY_NBT_SHAPELESS;
        public static SpecialRecipeSerializer<MysteryEggRecipe> MYSTERY_EGG_RECIPE;

        private static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> S register(RegistryEvent.Register<IRecipeSerializer<?>> event, String name, S serializer) {
            serializer.setRegistryName(Vault.id(name));
            event.getRegistry().register((IForgeRegistryEntry) serializer);
            return serializer;
        }
    }

    public static void initialize() {
        PotionBrewing.CONTAINER_MIXES.removeIf(o -> {
            Field f = ObfuscationReflectionHelper.findField(o.getClass(), "ingredient");
            try {
                Ingredient i = (Ingredient) f.get(o);
                if (i.test(new ItemStack((IItemProvider) Items.DRAGON_BREATH))) {
                    return true;
                }
            } catch (Exception exception) {
            }
            return false;
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModRecipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */