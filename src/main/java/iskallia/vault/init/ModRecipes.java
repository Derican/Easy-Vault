package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.recipe.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

public class ModRecipes {
    public static void initialize() {
//        TODO: check if the omit is acceptable
//        PotionBrewing.CONTAINER_MIXES.removeIf(o -> {
//            final Field f = ObfuscationReflectionHelper.findField(o.getClass(), "ingredient");
//            try {
//                final Ingredient i = (Ingredient)f.get(o);
//                if (i.test(new ItemStack((IItemProvider)Items.DRAGON_BREATH))) {
//                    return true;
//                }
//            }
//            catch (final Exception ex) {}
//            return false;
//        });
    }

    public static class Serializer {
        public static SpecialRecipeSerializer<RelicSetRecipe> CRAFTING_SPECIAL_RELIC_SET;
        public static SpecialRecipeSerializer<UnidentifiedRelicRecipe> CRAFTING_SPECIAL_UNIDENTIFIED_RELIC;
        public static NonRaffleCrystalShapedRecipe.Serializer NON_RAFFLE_CRYSTAL_SHAPED;
        public static ShapelessCopyNbtRecipe.Serializer COPY_NBT_SHAPELESS;
        public static SpecialRecipeSerializer<MysteryEggRecipe> MYSTERY_EGG_RECIPE;

        public static void register(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            Serializer.CRAFTING_SPECIAL_RELIC_SET = (SpecialRecipeSerializer<RelicSetRecipe>) register(event, "crafting_special_relic_set", new SpecialRecipeSerializer<>(RelicSetRecipe::new));
            Serializer.CRAFTING_SPECIAL_UNIDENTIFIED_RELIC = (SpecialRecipeSerializer<UnidentifiedRelicRecipe>) register(event, "crafting_special_unidentified_relic", new SpecialRecipeSerializer<>(UnidentifiedRelicRecipe::new));
            Serializer.NON_RAFFLE_CRYSTAL_SHAPED = register(event, "non_raffle_crystal_shaped", new NonRaffleCrystalShapedRecipe.Serializer());
            Serializer.COPY_NBT_SHAPELESS = register(event, "crafting_shapeless_copy_nbt", new ShapelessCopyNbtRecipe.Serializer());
            Serializer.MYSTERY_EGG_RECIPE = register(event, "mystery_egg", new SpecialRecipeSerializer<>(MysteryEggRecipe::new));
        }

        private static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> S register(final RegistryEvent.Register<IRecipeSerializer<?>> event, final String name, final S serializer) {
            serializer.setRegistryName(Vault.id(name));
            event.getRegistry().register(serializer);
            return serializer;
        }
    }
}
