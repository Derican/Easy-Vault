// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.ItemRelicBoosterPack;
import iskallia.vault.item.PuzzleRuneItem;
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

public class PuzzleRuneBlock extends Block
{
    public static final DirectionProperty FACING;
    public static final EnumProperty<Color> COLOR;
    public static final BooleanProperty RUNE_PLACED;
    
    public PuzzleRuneBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(-1.0f, 3600000.0f).noOcclusion().noDrops());
        this.registerDefaultState((((this.stateDefinition.any()).setValue(PuzzleRuneBlock.FACING, Direction.SOUTH)).setValue(PuzzleRuneBlock.COLOR, Color.YELLOW)).setValue(PuzzleRuneBlock.RUNE_PLACED, false));
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return ((this.defaultBlockState().setValue(PuzzleRuneBlock.FACING, context.getHorizontalDirection())).setValue(PuzzleRuneBlock.COLOR, ModAttributes.PUZZLE_COLOR.getOrDefault(context.getItemInHand(), Color.YELLOW).getValue(context.getItemInHand()))).setValue(PuzzleRuneBlock.RUNE_PLACED, false);
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { PuzzleRuneBlock.FACING }).add(new Property[] { PuzzleRuneBlock.COLOR }).add(new Property[] { PuzzleRuneBlock.RUNE_PLACED });
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            final ItemStack heldStack = player.getItemInHand(hand);
            if (heldStack.getItem() instanceof PuzzleRuneItem && this.isValidKey(heldStack, state)) {
                heldStack.shrink(1);
                final BlockState blockState = world.getBlockState(pos);
                world.setBlock(pos, blockState.setValue(PuzzleRuneBlock.RUNE_PLACED, true), 3);
                world.playSound((PlayerEntity)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }
    
    public int getSignal(final BlockState state, final IBlockReader blockAccess, final BlockPos pos, final Direction side) {
        return state.getValue(PuzzleRuneBlock.RUNE_PLACED) ? 15 : 0;
    }
    
    private boolean isValidKey(final ItemStack stack, final BlockState state) {
        return !(boolean)state.getValue(PuzzleRuneBlock.RUNE_PLACED) && ModAttributes.PUZZLE_COLOR.get(stack).map(attribute -> attribute.getValue(stack)).filter(value -> value == state.getValue(PuzzleRuneBlock.COLOR)).isPresent();
    }
    
    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        COLOR = EnumProperty.create("color", (Class)Color.class);
        RUNE_PLACED = BooleanProperty.create("rune_placed");
    }
    
    public enum Color implements IStringSerializable
    {
        YELLOW, 
        PINK, 
        GREEN, 
        BLUE;
        
        public Color next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
        
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }
    }
    
    public static class Item extends BlockItem
    {
        public Item(final Block block, final net.minecraft.item.Item.Properties properties) {
            super(block, properties);
        }
        
        public ActionResultType onItemUseFirst(final ItemStack stack, final ItemUseContext context) {
            final PlayerEntity player = context.getPlayer();
            final World world = context.getLevel();
            if (player != null && player.isCreative() && !world.isClientSide && world.getBlockState(context.getClickedPos()).getBlockState().getBlock() == ModBlocks.PUZZLE_RUNE_BLOCK) {
                ModAttributes.PUZZLE_COLOR.create(stack, ModAttributes.PUZZLE_COLOR.getOrCreate(stack, Color.YELLOW).getValue(stack).next());
                ItemRelicBoosterPack.successEffects(world, player.position());
                return ActionResultType.SUCCESS;
            }
            return super.onItemUseFirst(stack, context);
        }
    }
}
