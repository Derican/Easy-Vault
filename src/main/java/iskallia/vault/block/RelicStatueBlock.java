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

public class RelicStatueBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public RelicStatueBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(1.0F, 3600000.0F)
                .noOcclusion());

        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) FACING, (Comparable) Direction.SOUTH));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState()
                .setValue((Property) FACING, (Comparable) context.getHorizontalDirection());
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }


    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            ItemStack itemStack = new ItemStack((IItemProvider) getBlock());

            if (tileEntity instanceof RelicStatueTileEntity) {
                RelicStatueTileEntity statueTileEntity = (RelicStatueTileEntity) tileEntity;

                CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", (INBT) statueNBT);

                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity) itemEntity);
        }

        super.playerWillDestroy(world, pos, state, player);
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.RELIC_STATUE_TILE_ENTITY.create();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\RelicStatueBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */