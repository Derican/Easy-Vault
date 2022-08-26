package iskallia.vault.block;

import net.minecraft.util.IStringSerializable;

import java.util.Arrays;

import iskallia.vault.init.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.VoxelShape;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.IWorld;
import iskallia.vault.world.vault.logic.VaultCowOverrides;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.vault.VaultUtils;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import iskallia.vault.Vault;
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
import net.minecraft.state.EnumProperty;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.NetherPortalBlock;

public class VaultPortalBlock extends NetherPortalBlock {
    public static final AbstractBlock.IPositionPredicate FRAME;
    public static final EnumProperty<Style> STYLE;

    public VaultPortalBlock() {
        super(AbstractBlock.Properties.copy(Blocks.NETHER_PORTAL));
        this.registerDefaultState(this.stateDefinition.any().setValue((Property) VaultPortalBlock.AXIS, (Comparable) Direction.Axis.X).setValue((Property) VaultPortalBlock.STYLE, (Comparable) Style.RAINBOW));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VaultPortalBlock.STYLE);
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
        final RegistryKey<World> destinationKey = (world.dimension() == Vault.VAULT_KEY) ? World.OVERWORLD : Vault.VAULT_KEY;
        final ServerWorld destination = ((ServerWorld) world).getServer().getLevel(destinationKey);
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
            if (builder != null) {
                final VaultRaid vault2 = VaultRaidData.get(destination).startVault(destination, builder);
                if (CrystalData.shouldForceCowVault(data)) {
                    vault2.getProperties().create(VaultRaid.COW_VAULT, true);
                    data.clearModifiers();
                    data.setSelectedObjective(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId());
                    VaultCowOverrides.setupVault(vault2);
                }
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                if (data.getType() == CrystalData.Type.FINAL_LOBBY) {
                    final List<BlockPos> frame = VaultPortalSize.getFrame(world, pos);
                    frame.forEach(frameBlock -> {
                        if (world.getBlockState(frameBlock).getBlock() == ModBlocks.FINAL_VAULT_FRAME) {
                            world.setBlock(frameBlock, Blocks.BLACKSTONE.defaultBlockState(), 11);
                        }
                        return;
                    });
                }
            }
            player.setPortalCooldown();
        }
    }

    public BlockState updateShape(final BlockState state, final Direction facing, final BlockState facingState, final IWorld iworld, final BlockPos currentPos, final BlockPos facingPos) {
        if (!(iworld instanceof ServerWorld)) {
            return state;
        }
        if (((World) iworld).dimension() != Vault.VAULT_KEY) {
            final Direction.Axis facingAxis = facing.getAxis();
            final Direction.Axis portalAxis = (Direction.Axis) state.getValue((Property) VaultPortalBlock.AXIS);
            final boolean flag = portalAxis != facingAxis && facingAxis.isHorizontal();
            return (flag || facingState.is(this) || new VaultPortalSize(iworld, currentPos, portalAxis, VaultPortalBlock.FRAME).validatePortal()) ? super.updateShape(state, facing, facingState, iworld, currentPos, facingPos) : Blocks.AIR.defaultBlockState();
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
            if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            } else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            world.addParticle(ParticleTypes.ASH, d0, d2, d3, d4, d5, d6);
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
        STYLE = EnumProperty.create("style", (Class) Style.class);
    }

    public enum Style implements IStringSerializable {
        RAINBOW,
        FINAL,
        VELARA,
        TENOS,
        WENDARR,
        IDONA;

        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
