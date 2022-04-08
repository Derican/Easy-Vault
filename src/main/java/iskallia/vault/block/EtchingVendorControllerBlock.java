package iskallia.vault.block;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EtchingVendorControllerBlock
        extends ContainerBlock {
    public EtchingVendorControllerBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.BARRIER)
                .noCollission()
                .isRedstoneConductor(EtchingVendorControllerBlock::nonSolid)
                .isViewBlocking(EtchingVendorControllerBlock::nonSolid));
    }

    private static boolean nonSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }


    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (context instanceof net.minecraft.util.math.shapes.EntitySelectionContext) {
            Entity e = context.getEntity();
            if (e instanceof PlayerEntity && ((PlayerEntity) e).isCreative()) {
                return VoxelShapes.block();
            }
        }
        return VoxelShapes.empty();
    }


    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }


    @Nullable
    public TileEntity newBlockEntity(IBlockReader world) {
        return ModBlocks.ETCHING_CONTROLLER_TILE_ENTITY.create();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\EtchingVendorControllerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */