package iskallia.vault.block;

import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemRelicBoosterPack;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Locale;

public class PuzzleRuneBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Color> COLOR = EnumProperty.create("color", Color.class);
    public static final BooleanProperty RUNE_PLACED = BooleanProperty.create("rune_placed");

    public PuzzleRuneBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(-1.0F, 3600000.0F)
                .noOcclusion()
                .noDrops());

        registerDefaultState((BlockState) ((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) FACING, (Comparable) Direction.SOUTH))
                .setValue((Property) COLOR, Color.YELLOW))
                .setValue((Property) RUNE_PLACED, Boolean.valueOf(false)));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) ((BlockState) ((BlockState) defaultBlockState()
                .setValue((Property) FACING, (Comparable) context.getHorizontalDirection()))
                .setValue((Property) COLOR, (Comparable) ((EnumAttribute) ModAttributes.PUZZLE_COLOR.getOrDefault(context.getItemInHand(), Color.YELLOW)).getValue(context.getItemInHand())))
                .setValue((Property) RUNE_PLACED, Boolean.valueOf(false));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING}).add(new Property[]{(Property) COLOR}).add(new Property[]{(Property) RUNE_PLACED});
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            ItemStack heldStack = player.getItemInHand(hand);

            if (heldStack.getItem() instanceof iskallia.vault.item.PuzzleRuneItem &&
                    isValidKey(heldStack, state)) {
                heldStack.shrink(1);
                BlockState blockState = world.getBlockState(pos);
                world.setBlock(pos, (BlockState) blockState.setValue((Property) RUNE_PLACED, Boolean.valueOf(true)), 3);
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }


        return super.use(state, world, pos, player, hand, hit);
    }


    public int getSignal(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return ((Boolean) state.getValue((Property) RUNE_PLACED)).booleanValue() ? 15 : 0;
    }

    private boolean isValidKey(ItemStack stack, BlockState state) {
        if (((Boolean) state.getValue((Property) RUNE_PLACED)).booleanValue()) return false;

        return ModAttributes.PUZZLE_COLOR.get(stack)
                .map(attribute -> (Color) attribute.getValue(stack))
                .filter(value -> (value == state.getValue((Property) COLOR)))
                .isPresent();
    }

    public enum Color implements IStringSerializable {
        YELLOW, PINK, GREEN, BLUE;

        public Color next() {
            return values()[(ordinal() + 1) % (values()).length];
        }


        public String getSerializedName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public static class Item extends BlockItem {
        public Item(Block block, net.minecraft.item.Item.Properties properties) {
            super(block, properties);
        }


        public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
            PlayerEntity player = context.getPlayer();
            World world = context.getLevel();

            if (player != null && player.isCreative() && !world.isClientSide && world
                    .getBlockState(context.getClickedPos()).getBlockState().getBlock() == ModBlocks.PUZZLE_RUNE_BLOCK) {
                ModAttributes.PUZZLE_COLOR.create(stack, ((PuzzleRuneBlock.Color) ((EnumAttribute) ModAttributes.PUZZLE_COLOR
                        .getOrCreate(stack, PuzzleRuneBlock.Color.YELLOW)).getValue(stack)).next());
                ItemRelicBoosterPack.successEffects(world, player.position());
                return ActionResultType.SUCCESS;
            }

            return super.onItemUseFirst(stack, context);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\PuzzleRuneBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */