package iskallia.vault.block;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.VaultUtils;
import iskallia.vault.world.vault.logic.VaultCowOverrides;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Random;

public class VaultPortalBlock extends NetherPortalBlock {
    public static final AbstractBlock.IPositionPredicate FRAME;

    public VaultPortalBlock() {
        super(AbstractBlock.Properties.copy((AbstractBlock) Blocks.NETHER_PORTAL));
        this.registerDefaultState((this.stateDefinition.any()).setValue(VaultPortalBlock.AXIS, Direction.Axis.X));
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition((StateContainer.Builder) builder);
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
        final VoxelShape playerVoxel = VoxelShapes.create(entity.getBoundingBox().move((double) (-pos.getX()), (double) (-pos.getY()), (double) (-pos.getZ())));
//        TODO: check if the following expression is necessary
//        if (!VoxelShapes.joinIsNotEmpty(playerVoxel, state.getShape(world, pos), IBooleanFunction.AND)) {
//            return;
//        }
        final RegistryKey<World> destinationKey = (RegistryKey<World>) ((world.dimension() == Vault.VAULT_KEY) ? World.OVERWORLD : Vault.VAULT_KEY);
        final ServerWorld destination = ((ServerWorld) world).getServer().getLevel((RegistryKey) destinationKey);
        if (destination == null) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) entity;
        final TileEntity te = world.getBlockEntity(pos);
        final VaultPortalTileEntity portal = (te instanceof VaultPortalTileEntity) ? ((VaultPortalTileEntity) te) : null;
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();
            return;
        }
        if (destinationKey == World.OVERWORLD) {
            final VaultRaid vault = VaultRaidData.get(destination).getActiveFor(player);
            if (vault == null) {
                VaultUtils.exitSafely(destination, player);
                player.setPortalCooldown();
            }
        } else if (destinationKey == Vault.VAULT_KEY && portal != null) {
            final CrystalData data = portal.getData();
            final VaultRaid.Builder builder = portal.getData().createVault(destination, player);
            final VaultRaid vault2 = VaultRaidData.get(destination).startVault(destination, builder);
            if (CrystalData.shouldForceCowVault(data)) {
                vault2.getProperties().create(VaultRaid.COW_VAULT, true);
                data.clearModifiers();
                data.setSelectedObjective(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId());
                VaultCowOverrides.setupVault(vault2);
            }
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            player.setPortalCooldown();
        }
    }

    public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final IWorld iworld, final BlockPos currentPos, final BlockPos facingPos) {
        if (!(iworld instanceof ServerWorld)) {
            return state;
        }
        if (((World) iworld).dimension() != Vault.VAULT_KEY) {
            final Direction.Axis facingAxis = facing.getAxis();
            final Direction.Axis portalAxis = (Direction.Axis) state.getValue(VaultPortalBlock.AXIS);
            final boolean flag = portalAxis != facingAxis && facingAxis.isHorizontal();
            return (flag || facingState.is((Block) this) || new VaultPortalSize(iworld, currentPos, portalAxis, VaultPortalBlock.FRAME).validatePortal()) ? super.updateShape(state, facing, facingState, iworld, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
        }
        return state;
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

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.VAULT_PORTAL_TILE_ENTITY.create();
    }

    static {
        FRAME = ((state, reader, p) -> Arrays.stream(ModConfigs.VAULT_PORTAL.getValidFrameBlocks()).anyMatch(b -> b == state.getBlock()));
    }
}
