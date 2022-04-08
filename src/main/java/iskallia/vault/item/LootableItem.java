package iskallia.vault.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class LootableItem
        extends BasicItem {
    private final Supplier<ItemStack> supplier;

    public LootableItem(ResourceLocation id, Item.Properties properties, Supplier<ItemStack> supplier) {
        super(id, properties);
        this.supplier = supplier;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ItemStack heldStack = player.getItemInHand(hand);
            ItemRelicBoosterPack.successEffects(world, player.position());

            ItemStack randomLoot = this.supplier.get();
            while (randomLoot.getCount() > 0) {
                int amount = Math.min(randomLoot.getCount(), randomLoot.getMaxStackSize());
                ItemStack copy = randomLoot.copy();
                copy.setCount(amount);
                randomLoot.shrink(amount);
                player.drop(copy, false, false);
            }

            heldStack.shrink(1);
        }

        return super.use(world, player, hand);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\LootableItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */