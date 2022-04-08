package iskallia.vault.util;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RecipeUtil {
    @Nonnull
    public static Optional<Tuple<ItemStack, Float>> findSmeltingResult(World world, BlockState input) {
        ItemStack stack = new ItemStack((IItemProvider) input.getBlock());
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        return findSmeltingResult(world, stack);
    }

    @Nonnull
    public static Optional<Tuple<ItemStack, Float>> findSmeltingResult(World world, ItemStack input) {
        RecipeManager mgr = world.getRecipeManager();
        Inventory inventory = new Inventory(new ItemStack[]{input});
        Optional<IRecipe<IInventory>> optRecipe = (Optional<IRecipe<IInventory>>) ObjectUtils.firstNonNull((Object[]) new Optional[]{mgr
                .getRecipeFor(IRecipeType.SMELTING, (IInventory) inventory, world), mgr
                .getRecipeFor(IRecipeType.CAMPFIRE_COOKING, (IInventory) inventory, world), mgr
                .getRecipeFor(IRecipeType.SMOKING, (IInventory) inventory, world),
                Optional.empty()});
        return optRecipe.map(recipe -> {
            ItemStack smeltResult = recipe.assemble(inv).copy();
            float exp = 0.0F;
            if (recipe instanceof AbstractCookingRecipe)
                exp = ((AbstractCookingRecipe) recipe).getExperience();
            return new Tuple(smeltResult, Float.valueOf(exp));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\RecipeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */