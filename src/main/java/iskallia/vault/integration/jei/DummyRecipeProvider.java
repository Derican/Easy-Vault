package iskallia.vault.integration.jei;

import com.google.common.collect.Lists;
import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.Collections;
import java.util.List;

public class DummyRecipeProvider {
    public static List<Object> getAnvilRecipes(final IVanillaRecipeFactory factory) {
        return Collections.singletonList(factory.createAnvilRecipe(new ItemStack(ModItems.PERFECT_ECHO_GEM), Collections.singletonList(new ItemStack(ModItems.VAULT_CATALYST)), Collections.singletonList(new ItemStack(ModItems.VAULT_INHIBITOR))));
    }

    public static List<Object> getCustomCraftingRecipes(final IVanillaRecipeFactory factory) {
        final Ingredient spawnEggs = Ingredient.of(Items.BAT_SPAWN_EGG, Items.BEE_SPAWN_EGG, Items.BLAZE_SPAWN_EGG, Items.CAT_SPAWN_EGG, Items.CAVE_SPIDER_SPAWN_EGG, Items.CHICKEN_SPAWN_EGG, Items.COD_SPAWN_EGG, Items.COW_SPAWN_EGG, Items.CREEPER_SPAWN_EGG, Items.DOLPHIN_SPAWN_EGG, Items.DONKEY_SPAWN_EGG, Items.DROWNED_SPAWN_EGG, Items.ELDER_GUARDIAN_SPAWN_EGG, Items.ENDERMAN_SPAWN_EGG, Items.ENDERMITE_SPAWN_EGG, Items.EVOKER_SPAWN_EGG, Items.FOX_SPAWN_EGG, Items.GHAST_SPAWN_EGG, Items.GUARDIAN_SPAWN_EGG, Items.HOGLIN_SPAWN_EGG, Items.HORSE_SPAWN_EGG, Items.HUSK_SPAWN_EGG, Items.LLAMA_SPAWN_EGG, Items.MAGMA_CUBE_SPAWN_EGG, Items.MOOSHROOM_SPAWN_EGG, Items.MULE_SPAWN_EGG, Items.OCELOT_SPAWN_EGG, Items.PANDA_SPAWN_EGG, Items.PARROT_SPAWN_EGG, Items.PHANTOM_SPAWN_EGG, Items.PIG_SPAWN_EGG, Items.PIGLIN_SPAWN_EGG, Items.PIGLIN_BRUTE_SPAWN_EGG, Items.PILLAGER_SPAWN_EGG, Items.POLAR_BEAR_SPAWN_EGG, Items.PUFFERFISH_SPAWN_EGG, Items.RABBIT_SPAWN_EGG, Items.RAVAGER_SPAWN_EGG, Items.SALMON_SPAWN_EGG, Items.SHEEP_SPAWN_EGG, Items.SHULKER_SPAWN_EGG, Items.SILVERFISH_SPAWN_EGG, Items.SKELETON_SPAWN_EGG, Items.SKELETON_HORSE_SPAWN_EGG, Items.SLIME_SPAWN_EGG, Items.SPIDER_SPAWN_EGG, Items.SQUID_SPAWN_EGG, Items.STRAY_SPAWN_EGG, Items.STRIDER_SPAWN_EGG, Items.TRADER_LLAMA_SPAWN_EGG, Items.TROPICAL_FISH_SPAWN_EGG, Items.TURTLE_SPAWN_EGG, Items.VEX_SPAWN_EGG, Items.VILLAGER_SPAWN_EGG, Items.VINDICATOR_SPAWN_EGG, Items.WANDERING_TRADER_SPAWN_EGG, Items.WITCH_SPAWN_EGG, Items.WITHER_SKELETON_SPAWN_EGG, Items.WOLF_SPAWN_EGG, Items.ZOGLIN_SPAWN_EGG, Items.ZOMBIE_SPAWN_EGG, Items.ZOMBIE_HORSE_SPAWN_EGG, Items.ZOMBIE_VILLAGER_SPAWN_EGG, Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
        final NonNullList<Ingredient> mysteryEggInputs = NonNullList.create();
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(Ingredient.of(ModItems.ALEXANDRITE_GEM));
        return Lists.newArrayList(new Object[]{new ShapelessRecipe(Vault.id("mystery_egg_recipe"), "", new ItemStack(ModItems.MYSTERY_EGG, 4), mysteryEggInputs)});
    }
}
