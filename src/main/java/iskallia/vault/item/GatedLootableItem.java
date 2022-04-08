package iskallia.vault.item;

import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GatedLootableItem
        extends BasicItem {
    ITextComponent[] tooltip;

    public GatedLootableItem(ResourceLocation id, Item.Properties properties) {
        super(id, properties);
    }

    public GatedLootableItem(ResourceLocation id, Item.Properties properties, ITextComponent... tooltip) {
        super(id, properties);
        this.tooltip = tooltip;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ProductEntry productEntry;
            ItemStack heldStack = player.getItemInHand(hand);

            ServerWorld serverWorld = (ServerWorld) world;

            ResearchTree researches = PlayerResearchesData.get(serverWorld).getResearches(player);
            List<String> unlocked = new ArrayList<>(researches.getResearchesDone());

            WeightedList<ProductEntry> list = null;
            while (list == null && !unlocked.isEmpty()) {
                String researchName = unlocked.remove(world.random.nextInt(unlocked.size()));
                list = (WeightedList<ProductEntry>) ModConfigs.MOD_BOX.POOL.get(researchName);
            }

            ItemStack stack = ItemStack.EMPTY;

            if (list == null || list.isEmpty()) {
                productEntry = (ProductEntry) ((WeightedList) ModConfigs.MOD_BOX.POOL.get("None")).getRandom(world.random);
            } else {
                productEntry = (ProductEntry) list.getRandom(world.random);
            }
            if (productEntry != null) {
                stack = productEntry.generateItemStack();
            }

            if (!stack.isEmpty()) {
                while (stack.getCount() > 0) {
                    int amount = Math.min(stack.getCount(), stack.getMaxStackSize());
                    ItemStack copy = stack.copy();
                    copy.setCount(amount);
                    stack.shrink(amount);
                    player.drop(copy, false, false);
                }

                heldStack.shrink(1);
                ItemRelicBoosterPack.successEffects(world, player.position());
            } else {
                ItemRelicBoosterPack.failureEffects(world, player.position());
            }
        }
        return super.use(world, player, hand);
    }


    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (this.tooltip != null)
            tooltip.addAll(Arrays.asList(this.tooltip));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\GatedLootableItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */