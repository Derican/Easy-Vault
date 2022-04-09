// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.block.entity.RelicStatueTileEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RelicStatueBlock extends Block
{
    public static final DirectionProperty FACING;
    public static final VoxelShape SHAPE;
    
    public RelicStatueBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.0f, 3600000.0f).noOcclusion());
        this.registerDefaultState((this.stateDefinition.any()).setValue(RelicStatueBlock.FACING, Direction.SOUTH));
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.defaultBlockState().setValue(RelicStatueBlock.FACING, context.getHorizontalDirection());
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { RelicStatueBlock.FACING });
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return RelicStatueBlock.SHAPE;
    }
    
    public void playerWillDestroy(final World world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!world.isClientSide) {
            final TileEntity tileEntity = world.getBlockEntity(pos);
            final ItemStack itemStack = new ItemStack((IItemProvider)this.getBlock());
            if (tileEntity instanceof RelicStatueTileEntity) {
                final RelicStatueTileEntity statueTileEntity = (RelicStatueTileEntity)tileEntity;
                final CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                final CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", (INBT)statueNBT);
                itemStack.setTag(stackNBT);
            }
            final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity)itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.RELIC_STATUE_TILE_ENTITY.create();
    }
    
    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }
}
