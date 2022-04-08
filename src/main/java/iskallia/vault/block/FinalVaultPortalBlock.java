package iskallia.vault.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class FinalVaultPortalBlock extends NetherPortalBlock {
    public FinalVaultPortalBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.NETHER_PORTAL));
        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) AXIS, (Comparable) Direction.Axis.X));
    }

    protected static BlockPos getSpawnPoint(ServerWorld p_241092_0_, int p_241092_1_, int p_241092_2_, boolean p_241092_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
        Biome biome = p_241092_0_.getBiome((BlockPos) blockpos$mutable);
        boolean flag = p_241092_0_.dimensionType().hasCeiling();
        BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
        if (p_241092_3_ && !blockstate.getBlock().is((ITag) BlockTags.VALID_SPAWN)) {
            return null;
        }
        Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
        int i = flag ? p_241092_0_.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 0xF, p_241092_2_ & 0xF);
        if (i < 0) {
            return null;
        }
        int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 0xF, p_241092_2_ & 0xF);
        if (j <= i && j > chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 0xF, p_241092_2_ & 0xF)) {
            return null;
        }
        for (int k = i + 1; k >= 0; k--) {
            blockpos$mutable.set(p_241092_1_, k, p_241092_2_);
            BlockState blockstate1 = p_241092_0_.getBlockState((BlockPos) blockpos$mutable);
            if (!blockstate1.getFluidState().isEmpty()) {
                break;
            }

            if (blockstate1.equals(blockstate)) {
                return blockpos$mutable.above().immutable();
            }
        }

        return null;
    }


    public boolean hasTileEntity(BlockState state) {
        return false;
    }


    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }


    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        World world = null;
        if (worldIn instanceof World) {
            world = (World) worldIn;
        }


        if (world != null &&
                world.dimension() == World.OVERWORLD) {
            Direction.Axis direction$axis = facing.getAxis();
            Direction.Axis direction$axis1 = (Direction.Axis) stateIn.getValue((Property) AXIS);
            boolean bool = (direction$axis1 != direction$axis && direction$axis.isHorizontal()) ? true : false;
        }


        return stateIn;
    }


    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClientSide || !(entity instanceof net.minecraft.entity.player.PlayerEntity))
            return;
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions())
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        VoxelShape playerVoxel = VoxelShapes.create(player.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()));
    }


    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 4; i++) {
            double d0 = pos.getX() + rand.nextDouble();
            double d1 = pos.getY() + rand.nextDouble();
            double d2 = pos.getZ() + rand.nextDouble();
            double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (!world.getBlockState(pos.west()).is((Block) this) && !world.getBlockState(pos.east()).is((Block) this)) {
                d0 = pos.getX() + 0.5D + 0.25D * j;
                d3 = (rand.nextFloat() * 2.0F * j);
            } else {
                d2 = pos.getZ() + 0.5D + 0.25D * j;
                d5 = (rand.nextFloat() * 2.0F * j);
            }

            world.addParticle((IParticleData) ParticleTypes.ASH, d0, d1, d2, d3, d4, d5);
        }
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\FinalVaultPortalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */