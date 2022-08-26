package iskallia.vault.block;

import iskallia.vault.container.TransmogTableContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TransmogTableBlock extends Block {
    public static final DirectionProperty FACING;

    public TransmogTableBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5f).lightLevel(state -> 1).noOcclusion());
    }

    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return this.defaultBlockState().setValue(TransmogTableBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Transmogrification Table");
            }

            @Nullable
            public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
                return new TransmogTableContainer(windowId, player);
            }
        });
        return ActionResultType.SUCCESS;
    }

    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(TransmogTableBlock.FACING, rot.rotate(state.getValue(TransmogTableBlock.FACING)));
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TransmogTableBlock.FACING);
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
    }
}
