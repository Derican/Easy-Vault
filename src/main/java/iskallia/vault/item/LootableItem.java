package iskallia.vault.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class LootableItem extends BasicItem {
    private final Supplier<ItemStack> supplier;

    public LootableItem(final ResourceLocation id, final Item.Properties properties, final Supplier<ItemStack> supplier) {
        super(id, properties);
        this.supplier = supplier;
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (!world.isClientSide) {
            final ItemStack heldStack = player.getItemInHand(hand);
            ItemRelicBoosterPack.successEffects(world, player.position());
            final ItemStack randomLoot = this.supplier.get();
            while (randomLoot.getCount() > 0) {
                final int amount = Math.min(randomLoot.getCount(), randomLoot.getMaxStackSize());
                final ItemStack copy = randomLoot.copy();
                copy.setCount(amount);
                randomLoot.shrink(amount);
                player.drop(copy, false, false);
            }
            heldStack.shrink(1);
        }
        return (ActionResult<ItemStack>) super.use(world, player, hand);
    }
}
