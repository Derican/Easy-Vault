package iskallia.vault.recipe;

import iskallia.vault.block.item.RelicStatueBlockItem;
import iskallia.vault.init.ModRecipes;
import iskallia.vault.item.RelicPartItem;
import iskallia.vault.util.RelicSet;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RelicSetRecipe
        extends SpecialRecipe {
    public RelicSetRecipe(ResourceLocation id) {
        super(id);
    }


    public boolean matches(CraftingInventory inv, World world) {
        RelicSet set = null;
        Set<RelicPartItem> parts = new HashSet<>();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.getItem() instanceof RelicPartItem) {
                if (set != null && ((RelicPartItem) stack.getItem()).getRelicSet() != set) {
                    return false;
                }

                set = ((RelicPartItem) stack.getItem()).getRelicSet();
                parts.add((RelicPartItem) stack.getItem());
            } else if (!stack.isEmpty()) {
                return false;
            }
        }

        return (set != null && parts.size() == set.getItemSet().size());
    }


    public ItemStack assemble(CraftingInventory inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack.getItem() instanceof RelicPartItem) {
                RelicSet set = ((RelicPartItem) stack.getItem()).getRelicSet();
                return RelicStatueBlockItem.withRelicSet(set);
            }
        }

        return ItemStack.EMPTY;
    }


    public boolean canCraftInDimensions(int width, int height) {
        Optional<RelicSet> min = RelicSet.getAll().stream().min(Comparator.comparingInt(o -> o.getItemSet().size()));
        return (min.isPresent() && width * height >= ((RelicSet) min.get()).getItemSet().size());
    }


    public IRecipeSerializer<?> getSerializer() {
        return (IRecipeSerializer<?>) ModRecipes.Serializer.CRAFTING_SPECIAL_RELIC_SET;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\recipe\RelicSetRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */