// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.container.KeyPressContainer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class KeyPressBlock extends FallingBlock
{
    public static final DirectionProperty FACING;
    private static final VoxelShape PART_BASE;
    private static final VoxelShape PART_LOWER_X;
    private static final VoxelShape PART_MID_X;
    private static final VoxelShape PART_UPPER_X;
    private static final VoxelShape PART_LOWER_Z;
    private static final VoxelShape PART_MID_Z;
    private static final VoxelShape PART_UPPER_Z;
    private static final VoxelShape X_AXIS_AABB;
    private static final VoxelShape Z_AXIS_AABB;
    
    public KeyPressBlock() {
        super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).sound(SoundType.ANVIL).strength(2.0f, 3600000.0f));
    }
    
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.defaultBlockState().setValue(KeyPressBlock.FACING, context.getHorizontalDirection().getClockWise());
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        final Direction direction = state.getValue(KeyPressBlock.FACING);
        return (direction.getAxis() == Direction.Axis.X) ? KeyPressBlock.X_AXIS_AABB : KeyPressBlock.Z_AXIS_AABB;
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return (ITextComponent)new StringTextComponent("Key Press");
            }
            
            @Nullable
            public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
                return new KeyPressContainer(windowId, player);
            }
        });
        return ActionResultType.SUCCESS;
    }
    
    protected void falling(final FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtsEntities(true);
    }
    
    public void onLand(final World worldIn, final BlockPos pos, final BlockState fallingState, final BlockState hitState, final FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            worldIn.levelEvent(1031, pos, 0);
        }
    }
    
    public void onBroken(final World worldIn, final BlockPos pos, final FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            worldIn.levelEvent(1029, pos, 0);
        }
    }
    
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(KeyPressBlock.FACING, rot.rotate(state.getValue(KeyPressBlock.FACING)));
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { KeyPressBlock.FACING });
    }
    
    public boolean isPathfindable(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getDustColor(final BlockState state, final IBlockReader reader, final BlockPos pos) {
        return state.getMapColor(reader, pos).col;
    }
    
    static {
        FACING = HorizontalBlock.FACING;
        PART_BASE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
        PART_LOWER_X = Block.box(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);
        PART_MID_X = Block.box(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
        PART_UPPER_X = Block.box(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);
        PART_LOWER_Z = Block.box(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);
        PART_MID_Z = Block.box(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
        PART_UPPER_Z = Block.box(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);
        X_AXIS_AABB = VoxelShapes.or(KeyPressBlock.PART_BASE, new VoxelShape[] { KeyPressBlock.PART_LOWER_X, KeyPressBlock.PART_MID_X, KeyPressBlock.PART_UPPER_X });
        Z_AXIS_AABB = VoxelShapes.or(KeyPressBlock.PART_BASE, new VoxelShape[] { KeyPressBlock.PART_LOWER_Z, KeyPressBlock.PART_MID_Z, KeyPressBlock.PART_UPPER_Z });
    }
}
