package iskallia.vault.block;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.OtherSidePortalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.BurntCrystalItem;
import iskallia.vault.item.OtherSideData;
import iskallia.vault.world.vault.VaultUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Random;

public class OtherSidePortalBlock
        extends NetherPortalBlock {
    public static final AbstractBlock.IPositionPredicate FRAME = (state, reader, p) -> Arrays.<Block>stream(ModConfigs.OTHER_SIDE.getValidFrameBlocks()).anyMatch((Block block) -> block.defaultBlockState() == state);

    public OtherSidePortalBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.NETHER_PORTAL));
        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) AXIS, (Comparable) Direction.Axis.X));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }


    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }


    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClientSide || !(entity instanceof net.minecraft.entity.player.PlayerEntity))
            return;
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions())
            return;
        VoxelShape playerVoxel = VoxelShapes.create(entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()));
        if (!VoxelShapes.joinIsNotEmpty(playerVoxel, state.getShape((IBlockReader) world, pos), IBooleanFunction.AND))
            return;
        VaultPortalSize current = new VaultPortalSize((IWorld) world, pos, (Direction.Axis) state.getValue((Property) AXIS), FRAME);
        if (!current.validatePortal())
            return;
        RegistryKey<World> destinationKey = (world.dimension() == Vault.OTHER_SIDE_KEY) ? World.OVERWORLD : Vault.OTHER_SIDE_KEY;
        ServerWorld destination = ((ServerWorld) world).getServer().getLevel(destinationKey);
        if (destination == null)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();

            return;
        }
        TileEntity te = world.getBlockEntity(pos);
        OtherSidePortalTileEntity portal = (te instanceof OtherSidePortalTileEntity) ? (OtherSidePortalTileEntity) te : null;
        if (portal == null)
            return;
        OtherSideData data = portal.getData();
        if (data == null)
            return;
        BlockPos targetPos = data.getLinkedPos();
        RegistryKey<World> targetDim = data.getLinkedDim();
        if (targetPos == null || targetDim == null)
            return;
        ServerWorld target = world.getServer().getLevel(targetDim);
        if (target == null)
            return;
        if (target.getBlockState(targetPos).getBlock() != ModBlocks.OTHER_SIDE_PORTAL) {
            targetPos = BurntCrystalItem.forcePlace((ServerWorld) world, current.getBottomLeft(), target, current);
            data.setLinkedPos(targetPos);
        }

        VaultUtils.moveTo(target, (Entity) player, new Vector3d(targetPos.getX() + 0.2D, targetPos.getY(), targetPos.getZ() + 0.2D));
        player.setPortalCooldown();
    }


    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld iworld, BlockPos currentPos, BlockPos facingPos) {
        if (!(iworld instanceof ServerWorld)) return state;

        Direction.Axis facingAxis = facing.getAxis();
        Direction.Axis portalAxis = (Direction.Axis) state.getValue((Property) AXIS);

        boolean flag = (portalAxis != facingAxis && facingAxis.isHorizontal());
        return (!flag && !facingState.is((Block) this) && !(new VaultPortalSize(iworld, currentPos, portalAxis, FRAME)).validatePortal()) ? Blocks.AIR
                .defaultBlockState() : super.updateShape(state, facing, facingState, iworld, currentPos, facingPos);
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

            world.addParticle((IParticleData) ParticleTypes.WHITE_ASH, d0, d1, d2, d3, d4, d5);
        }
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.OTHER_SIDE_PORTAL_TILE_ENTITY.create();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\OtherSidePortalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */