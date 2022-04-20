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

public class RelicSetRecipe extends SpecialRecipe {
    public RelicSetRecipe(final ResourceLocation id) {
        super(id);
    }

    public boolean matches(final CraftingInventory inv, final World world) {
        RelicSet set = null;
        final Set<RelicPartItem> parts = new HashSet<RelicPartItem>();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            final ItemStack stack = inv.getItem(i);
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
        return set != null && parts.size() == set.getItemSet().size();
    }

    public ItemStack assemble(final CraftingInventory inv) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            final ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof RelicPartItem) {
                final RelicSet set = ((RelicPartItem) stack.getItem()).getRelicSet();
                return RelicStatueBlockItem.withRelicSet(set);
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(final int width, final int height) {
        final Optional<RelicSet> min = RelicSet.getAll().stream().min(Comparator.comparingInt(o -> o.getItemSet().size()));
        return min.isPresent() && width * height >= min.get().getItemSet().size();
    }

    public IRecipeSerializer<?> getSerializer() {
        return (IRecipeSerializer<?>) ModRecipes.Serializer.CRAFTING_SPECIAL_RELIC_SET;
    }
}
