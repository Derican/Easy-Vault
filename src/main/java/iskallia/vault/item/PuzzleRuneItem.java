package iskallia.vault.item;

import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.block.PuzzleRuneBlock;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PuzzleRuneItem extends BasicItem {
    public PuzzleRuneItem(ResourceLocation id, Item.Properties properties) {
        super(id, properties);
    }


    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getLevel();

        if (player != null && player.isCreative() && !world.isClientSide && world
                .getBlockState(context.getClickedPos()).getBlockState().getBlock() != ModBlocks.PUZZLE_RUNE_BLOCK) {
            ModAttributes.PUZZLE_COLOR.create(stack, ((PuzzleRuneBlock.Color) ((EnumAttribute) ModAttributes.PUZZLE_COLOR
                    .getOrCreate(stack, PuzzleRuneBlock.Color.YELLOW)).getValue(stack)).next());
            ItemRelicBoosterPack.successEffects(world, player.position());
            return ActionResultType.SUCCESS;
        }

        return super.onItemUseFirst(stack, context);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\PuzzleRuneItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */