// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModConfigs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GatedLootableItem extends BasicItem
{
    ITextComponent[] tooltip;
    
    public GatedLootableItem(final ResourceLocation id, final Item.Properties properties) {
        super(id, properties);
    }
    
    public GatedLootableItem(final ResourceLocation id, final Item.Properties properties, final ITextComponent... tooltip) {
        super(id, properties);
        this.tooltip = tooltip;
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (!world.isClientSide) {
            final ItemStack heldStack = player.getItemInHand(hand);
            ItemStack stack = ItemStack.EMPTY;
            ProductEntry productEntry;
            productEntry = ModConfigs.MOD_BOX.POOL.get("None").getRandom(world.random);
            if (productEntry != null) {
                stack = productEntry.generateItemStack();
            }
            if (!stack.isEmpty()) {
                while (stack.getCount() > 0) {
                    final int amount = Math.min(stack.getCount(), stack.getMaxStackSize());
                    final ItemStack copy = stack.copy();
                    copy.setCount(amount);
                    stack.shrink(amount);
                    player.drop(copy, false, false);
                }
                heldStack.shrink(1);
                ItemRelicBoosterPack.successEffects(world, player.position());
            }
            else {
                ItemRelicBoosterPack.failureEffects(world, player.position());
            }
        }
        return (ActionResult<ItemStack>)super.use(world, player, hand);
    }
    
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (this.tooltip != null) {
            tooltip.addAll(Arrays.asList(this.tooltip));
        }
    }
}
