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
import net.minecraft.util.Direction;
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
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public TransmogTableBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(0.5F).lightLevel(state -> 1)
                .noOcclusion());
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState().setValue((Property) FACING, (Comparable) context.getHorizontalDirection().getOpposite());
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

            public ITextComponent getDisplayName() {
                return (ITextComponent) new StringTextComponent("Transmogrification Table");
            }


            @Nullable
            public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                return (Container) new TransmogTableContainer(windowId, player);
            }
        });


        return ActionResultType.SUCCESS;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.setValue((Property) FACING, (Comparable) rot.rotate((Direction) state.getValue((Property) FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, IBlockReader reader, BlockPos pos) {
        return (state.getMapColor(reader, pos)).col;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\TransmogTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */