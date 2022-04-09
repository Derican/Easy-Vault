// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.entity.player.PlayerEntity;
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

public class OtherSidePortalBlock extends NetherPortalBlock
{
    public static final AbstractBlock.IPositionPredicate FRAME;
    
    public OtherSidePortalBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock)Blocks.NETHER_PORTAL));
        this.registerDefaultState((this.stateDefinition.any()).setValue(OtherSidePortalBlock.AXIS, Direction.Axis.X));
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition((StateContainer.Builder)builder);
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
        final VoxelShape playerVoxel = VoxelShapes.create(entity.getBoundingBox().move((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ())));
        if (!VoxelShapes.joinIsNotEmpty(playerVoxel, state.getShape((IBlockReader)world, pos), IBooleanFunction.AND)) {
            return;
        }
        final VaultPortalSize current = new VaultPortalSize((IWorld)world, pos, (Direction.Axis)state.getValue(OtherSidePortalBlock.AXIS), OtherSidePortalBlock.FRAME);
        if (!current.validatePortal()) {
            return;
        }
        final RegistryKey<World> destinationKey = (RegistryKey<World>)((world.dimension() == Vault.OTHER_SIDE_KEY) ? World.OVERWORLD : Vault.OTHER_SIDE_KEY);
        final ServerWorld destination = ((ServerWorld)world).getServer().getLevel((RegistryKey)destinationKey);
        if (destination == null) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)entity;
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();
            return;
        }
        final TileEntity te = world.getBlockEntity(pos);
        final OtherSidePortalTileEntity portal = (te instanceof OtherSidePortalTileEntity) ? ((OtherSidePortalTileEntity)te) : null;
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
        final ServerWorld target = world.getServer().getLevel((RegistryKey)targetDim);
        if (target == null) {
            return;
        }
        if (target.getBlockState(targetPos).getBlock() != ModBlocks.OTHER_SIDE_PORTAL) {
            targetPos = BurntCrystalItem.forcePlace((ServerWorld)world, current.getBottomLeft(), target, current);
            data.setLinkedPos(targetPos);
        }
        VaultUtils.moveTo(target, (Entity)player, new Vector3d(targetPos.getX() + 0.2, (double)targetPos.getY(), targetPos.getZ() + 0.2));
        player.setPortalCooldown();
    }
    
    public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final IWorld iworld, final BlockPos currentPos, final BlockPos facingPos) {
        if (!(iworld instanceof ServerWorld)) {
            return state;
        }
        final Direction.Axis facingAxis = facing.getAxis();
        final Direction.Axis portalAxis = (Direction.Axis)state.getValue(OtherSidePortalBlock.AXIS);
        final boolean flag = portalAxis != facingAxis && facingAxis.isHorizontal();
        return (flag || facingState.is((Block)this) || new VaultPortalSize(iworld, currentPos, portalAxis, OtherSidePortalBlock.FRAME).validatePortal()) ? super.updateShape(state, facing, facingState, iworld, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
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
            if (!world.getBlockState(pos.west()).is((Block)this) && !world.getBlockState(pos.east()).is((Block)this)) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            }
            else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            world.addParticle((IParticleData)ParticleTypes.WHITE_ASH, d0, d2, d3, d4, d5, d6);
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
