package iskallia.vault.block;

import net.minecraft.state.StateContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;

import java.util.Random;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.tags.ITag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;

public class FinalVaultPortalBlock extends NetherPortalBlock {
    public FinalVaultPortalBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.NETHER_PORTAL));
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) FinalVaultPortalBlock.AXIS, (Comparable) Direction.Axis.X));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }

    protected static BlockPos getSpawnPoint(final ServerWorld p_241092_0_, final int p_241092_1_, final int p_241092_2_, final boolean p_241092_3_) {
        final BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_241092_1_, 0, p_241092_2_);
        final Biome biome = p_241092_0_.getBiome((BlockPos) blockpos$mutable);
        final boolean flag = p_241092_0_.dimensionType().hasCeiling();
        final BlockState blockstate = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
        if (p_241092_3_ && !blockstate.getBlock().is((ITag) BlockTags.VALID_SPAWN)) {
            return null;
        }
        final Chunk chunk = p_241092_0_.getChunk(p_241092_1_ >> 4, p_241092_2_ >> 4);
        final int i = flag ? p_241092_0_.getChunkSource().getGenerator().getSpawnHeight() : chunk.getHeight(Heightmap.Type.MOTION_BLOCKING, p_241092_1_ & 0xF, p_241092_2_ & 0xF);
        if (i < 0) {
            return null;
        }
        final int j = chunk.getHeight(Heightmap.Type.WORLD_SURFACE, p_241092_1_ & 0xF, p_241092_2_ & 0xF);
        if (j <= i && j > chunk.getHeight(Heightmap.Type.OCEAN_FLOOR, p_241092_1_ & 0xF, p_241092_2_ & 0xF)) {
            return null;
        }
        for (int k = i + 1; k >= 0; --k) {
            blockpos$mutable.set(p_241092_1_, k, p_241092_2_);
            final BlockState blockstate2 = p_241092_0_.getBlockState((BlockPos) blockpos$mutable);
            if (!blockstate2.getFluidState().isEmpty()) {
                break;
            }
            if (blockstate2.equals(blockstate)) {
                return blockpos$mutable.above().immutable();
            }
        }
        return null;
    }

    public boolean hasTileEntity(final BlockState state) {
        return false;
    }

    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
    }

    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        World world = null;
        if (worldIn instanceof World) {
            world = (World) worldIn;
        }
        if (world != null && world.dimension() == World.OVERWORLD) {
            final Direction.Axis direction$axis = facing.getAxis();
            final Direction.Axis direction$axis2 = (Direction.Axis) stateIn.getValue((Property) FinalVaultPortalBlock.AXIS);
            final boolean b = direction$axis2 != direction$axis && direction$axis.isHorizontal();
        }
        return stateIn;
    }

    public void entityInside(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        if (world.isClientSide || !(entity instanceof PlayerEntity)) {
            return;
        }
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) entity;
        final VoxelShape playerVoxel = VoxelShapes.create(player.getBoundingBox().move((double) (-pos.getX()), (double) (-pos.getY()), (double) (-pos.getZ())));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(final BlockState state, final World world, final BlockPos pos, final Random rand) {
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + rand.nextDouble();
            final double d2 = pos.getY() + rand.nextDouble();
            double d3 = pos.getZ() + rand.nextDouble();
            double d4 = (rand.nextFloat() - 0.5) * 0.5;
            final double d5 = (rand.nextFloat() - 0.5) * 0.5;
            double d6 = (rand.nextFloat() - 0.5) * 0.5;
            final int j = rand.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).is((Block) this) && !world.getBlockState(pos.east()).is((Block) this)) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            } else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            world.addParticle((IParticleData) ParticleTypes.ASH, d0, d2, d3, d4, d5, d6);
        }
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition((StateContainer.Builder) builder);
    }
}
