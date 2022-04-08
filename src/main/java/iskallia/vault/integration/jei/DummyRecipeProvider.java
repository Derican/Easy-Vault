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
    public static List<Object> getAnvilRecipes(IVanillaRecipeFactory factory) {
        return Collections.singletonList(factory
                .createAnvilRecipe(new ItemStack((IItemProvider) ModItems.PERFECT_ECHO_GEM),

                        Collections.singletonList(new ItemStack((IItemProvider) ModItems.VAULT_CATALYST)),
                        Collections.singletonList(new ItemStack((IItemProvider) ModItems.VAULT_INHIBITOR))));
    }


    public static List<Object> getCustomCraftingRecipes(IVanillaRecipeFactory factory) {
        Ingredient spawnEggs = Ingredient.of(new IItemProvider[]{(IItemProvider) Items.BAT_SPAWN_EGG, (IItemProvider) Items.BEE_SPAWN_EGG, (IItemProvider) Items.BLAZE_SPAWN_EGG, (IItemProvider) Items.CAT_SPAWN_EGG, (IItemProvider) Items.CAVE_SPIDER_SPAWN_EGG, (IItemProvider) Items.CHICKEN_SPAWN_EGG, (IItemProvider) Items.COD_SPAWN_EGG, (IItemProvider) Items.COW_SPAWN_EGG, (IItemProvider) Items.CREEPER_SPAWN_EGG, (IItemProvider) Items.DOLPHIN_SPAWN_EGG, (IItemProvider) Items.DONKEY_SPAWN_EGG, (IItemProvider) Items.DROWNED_SPAWN_EGG, (IItemProvider) Items.ELDER_GUARDIAN_SPAWN_EGG, (IItemProvider) Items.ENDERMAN_SPAWN_EGG, (IItemProvider) Items.ENDERMITE_SPAWN_EGG, (IItemProvider) Items.EVOKER_SPAWN_EGG, (IItemProvider) Items.FOX_SPAWN_EGG, (IItemProvider) Items.GHAST_SPAWN_EGG, (IItemProvider) Items.GUARDIAN_SPAWN_EGG, (IItemProvider) Items.HOGLIN_SPAWN_EGG, (IItemProvider) Items.HORSE_SPAWN_EGG, (IItemProvider) Items.HUSK_SPAWN_EGG, (IItemProvider) Items.LLAMA_SPAWN_EGG, (IItemProvider) Items.MAGMA_CUBE_SPAWN_EGG, (IItemProvider) Items.MOOSHROOM_SPAWN_EGG, (IItemProvider) Items.MULE_SPAWN_EGG, (IItemProvider) Items.OCELOT_SPAWN_EGG, (IItemProvider) Items.PANDA_SPAWN_EGG, (IItemProvider) Items.PARROT_SPAWN_EGG, (IItemProvider) Items.PHANTOM_SPAWN_EGG, (IItemProvider) Items.PIG_SPAWN_EGG, (IItemProvider) Items.PIGLIN_SPAWN_EGG, (IItemProvider) Items.PIGLIN_BRUTE_SPAWN_EGG, (IItemProvider) Items.PILLAGER_SPAWN_EGG, (IItemProvider) Items.POLAR_BEAR_SPAWN_EGG, (IItemProvider) Items.PUFFERFISH_SPAWN_EGG, (IItemProvider) Items.RABBIT_SPAWN_EGG, (IItemProvider) Items.RAVAGER_SPAWN_EGG, (IItemProvider) Items.SALMON_SPAWN_EGG, (IItemProvider) Items.SHEEP_SPAWN_EGG, (IItemProvider) Items.SHULKER_SPAWN_EGG, (IItemProvider) Items.SILVERFISH_SPAWN_EGG, (IItemProvider) Items.SKELETON_SPAWN_EGG, (IItemProvider) Items.SKELETON_HORSE_SPAWN_EGG, (IItemProvider) Items.SLIME_SPAWN_EGG, (IItemProvider) Items.SPIDER_SPAWN_EGG, (IItemProvider) Items.SQUID_SPAWN_EGG, (IItemProvider) Items.STRAY_SPAWN_EGG, (IItemProvider) Items.STRIDER_SPAWN_EGG, (IItemProvider) Items.TRADER_LLAMA_SPAWN_EGG, (IItemProvider) Items.TROPICAL_FISH_SPAWN_EGG, (IItemProvider) Items.TURTLE_SPAWN_EGG, (IItemProvider) Items.VEX_SPAWN_EGG, (IItemProvider) Items.VILLAGER_SPAWN_EGG, (IItemProvider) Items.VINDICATOR_SPAWN_EGG, (IItemProvider) Items.WANDERING_TRADER_SPAWN_EGG, (IItemProvider) Items.WITCH_SPAWN_EGG, (IItemProvider) Items.WITHER_SKELETON_SPAWN_EGG, (IItemProvider) Items.WOLF_SPAWN_EGG, (IItemProvider) Items.ZOGLIN_SPAWN_EGG, (IItemProvider) Items.ZOMBIE_SPAWN_EGG, (IItemProvider) Items.ZOMBIE_HORSE_SPAWN_EGG, (IItemProvider) Items.ZOMBIE_VILLAGER_SPAWN_EGG, (IItemProvider) Items.ZOMBIFIED_PIGLIN_SPAWN_EGG});


        NonNullList<Ingredient> mysteryEggInputs = NonNullList.create();
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(spawnEggs);
        mysteryEggInputs.add(Ingredient.of(new IItemProvider[]{(IItemProvider) ModItems.ALEXANDRITE_GEM}));
        return Lists.newArrayList(new Object[]{new ShapelessRecipe(
                Vault.id("mystery_egg_recipe"), "", new ItemStack((IItemProvider) ModItems.MYSTERY_EGG, 4), mysteryEggInputs)});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\integration\jei\DummyRecipeProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */