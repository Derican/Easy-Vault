package iskallia.vault.block;

import java.util.Arrays;

import iskallia.vault.init.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.item.OtherSideData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.VoxelShape;
import iskallia.vault.world.vault.VaultUtils;
import net.minecraft.util.math.vector.Vector3d;
import iskallia.vault.item.BurntCrystalItem;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.block.entity.OtherSidePortalTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import iskallia.vault.Vault;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.Block;
import net.minecraft.state.StateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.NetherPortalBlock;

public class OtherSidePortalBlock extends NetherPortalBlock {
    public static final AbstractBlock.IPositionPredicate FRAME;

    public OtherSidePortalBlock() {
        super(AbstractBlock.Properties.copy(Blocks.NETHER_PORTAL));
        this.registerDefaultState(this.stateDefinition.any().setValue((Property) OtherSidePortalBlock.AXIS, (Comparable) Direction.Axis.X));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
    }

    public void entityInside(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        if (world.isClientSide || !(entity instanceof PlayerEntity)) {
            return;
        }
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) {
            return;
        }
        final VoxelShape playerVoxel = VoxelShapes.create(entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()));
        if (!VoxelShapes.joinIsNotEmpty(playerVoxel, state.getShape(world, pos), IBooleanFunction.AND)) {
            return;
        }
        final VaultPortalSize current = new VaultPortalSize(world, pos, (Direction.Axis) state.getValue((Property) OtherSidePortalBlock.AXIS), OtherSidePortalBlock.FRAME);
        if (!current.validatePortal()) {
            return;
        }
        final RegistryKey<World> destinationKey = (world.dimension() == Vault.OTHER_SIDE_KEY) ? World.OVERWORLD : Vault.OTHER_SIDE_KEY;
        final ServerWorld destination = ((ServerWorld) world).getServer().getLevel(destinationKey);
        if (destination == null) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) entity;
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();
            return;
        }
        final TileEntity te = world.getBlockEntity(pos);
        final OtherSidePortalTileEntity portal = (te instanceof OtherSidePortalTileEntity) ? ((OtherSidePortalTileEntity) te) : null;
        if (portal == null) {
            return;
        }
        final OtherSideData data = portal.getData();
        if (data == null) {
            return;
        }
        BlockPos targetPos = data.getLinkedPos();
        final RegistryKey<World> targetDim = data.getLinkedDim();
        if (targetPos == null || targetDim == null) {
            return;
        }
        final ServerWorld target = world.getServer().getLevel(targetDim);
        if (target == null) {
            return;
        }
        if (target.getBlockState(targetPos).getBlock() != ModBlocks.OTHER_SIDE_PORTAL) {
            targetPos = BurntCrystalItem.forcePlace((ServerWorld) world, current.getBottomLeft(), target, current);
            data.setLinkedPos(targetPos);
        }
        VaultUtils.moveTo(target, player, new Vector3d(targetPos.getX() + 0.2, targetPos.getY(), targetPos.getZ() + 0.2));
        player.setPortalCooldown();
    }

    public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final IWorld iworld, final BlockPos currentPos, final BlockPos facingPos) {
        if (!(iworld instanceof ServerWorld)) {
            return state;
        }
        final Direction.Axis facingAxis = facing.getAxis();
        final Direction.Axis portalAxis = (Direction.Axis) state.getValue((Property) OtherSidePortalBlock.AXIS);
        final boolean flag = portalAxis != facingAxis && facingAxis.isHorizontal();
        return (flag || facingState.is(this) || new VaultPortalSize(iworld, currentPos, portalAxis, OtherSidePortalBlock.FRAME).validatePortal()) ? super.updateShape(state, facing, facingState, iworld, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
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
            if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            } else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            world.addParticle(ParticleTypes.WHITE_ASH, d0, d2, d3, d4, d5, d6);
        }
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.OTHER_SIDE_PORTAL_TILE_ENTITY.create();
    }

    static {
        FRAME = ((state, reader, p) -> Arrays.stream(ModConfigs.OTHER_SIDE.getValidFrameBlocks()).anyMatch(b -> b == state.getBlock()));
    }
}
