package iskallia.vault.block;

import net.minecraft.state.properties.BlockStateProperties;

import java.util.Optional;

import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockRenderType;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.LivingEntity;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.SummonAndKillAllBossesObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import iskallia.vault.world.vault.logic.VaultBossSpawner;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import iskallia.vault.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.EnumProperty;
import net.minecraft.block.Block;

public class ObeliskBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final IntegerProperty COMPLETION;
    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_TOP;

    public ObeliskBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.METAL).strength(-1.0f, 3600000.0f).noDrops());
        this.registerDefaultState((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) ObeliskBlock.HALF, (Comparable) DoubleBlockHalf.LOWER)).setValue((Property) ObeliskBlock.COMPLETION, (Comparable) 0));
    }

    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        if (state.getValue((Property) ObeliskBlock.HALF) == DoubleBlockHalf.UPPER) {
            return ObeliskBlock.SHAPE_TOP;
        }
        return ObeliskBlock.SHAPE;
    }

    public boolean hasTileEntity(final BlockState state) {
        return state.getValue((Property) ObeliskBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (this.hasTileEntity(state)) {
            return ModBlocks.OBELISK_TILE_ENTITY.create();
        }
        return null;
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (state.getValue((Property) ObeliskBlock.HALF) != DoubleBlockHalf.UPPER) {
            if ((int) state.getValue((Property) ObeliskBlock.COMPLETION) != 4 && this.newBlockActivated(state, world, pos, player, hand, hit)) {
                final BlockState newState = (BlockState) state.setValue((Property) ObeliskBlock.COMPLETION, (Comparable) 4);
                world.setBlockAndUpdate(pos, newState);
                this.spawnParticles(world, pos);
            }
            return ActionResultType.SUCCESS;
        }
        final BlockState downState = world.getBlockState(pos.below());
        if (!(downState.getBlock() instanceof ObeliskBlock)) {
            return ActionResultType.PASS;
        }
        return this.use(downState, world, pos.below(), player, hand, hit);
    }

    private boolean newBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return false;
        }
        final VaultRaid vault = VaultRaidData.get((ServerWorld) world).getAt((ServerWorld) world, pos);
        if (vault == null) {
            return false;
        }
        final SummonAndKillBossObjective objective = vault.getPlayer(player.getUUID()).flatMap(vaultPlayer -> vaultPlayer.getActiveObjective(SummonAndKillBossObjective.class)).orElseGet(() -> vault.getActiveObjective(SummonAndKillBossObjective.class).orElse(null));
        if (objective != null) {
            if (objective.allObelisksClicked()) {
                return false;
            }
            objective.addObelisk();
            if (objective.allObelisksClicked()) {
                final LivingEntity boss = VaultBossSpawner.spawnBoss(vault, (ServerWorld) world, pos);
                objective.setBoss(boss);
            }
            return true;
        } else {
            final ArchitectSummonAndKillBossesObjective objective2 = vault.getPlayer(player.getUUID()).flatMap(vaultPlayer -> vaultPlayer.getActiveObjective(ArchitectSummonAndKillBossesObjective.class)).orElseGet(() -> vault.getActiveObjective(ArchitectSummonAndKillBossesObjective.class).orElse(null));
            if (objective2 != null) {
                final LivingEntity boss2 = VaultBossSpawner.spawnBoss(vault, (ServerWorld) world, pos);
                objective2.setBoss(boss2);
                return true;
            }
            final SummonAndKillAllBossesObjective objective3 = vault.getPlayer(player.getUUID()).flatMap(vaultPlayer -> vaultPlayer.getActiveObjective(SummonAndKillAllBossesObjective.class)).orElseGet(() -> vault.getActiveObjective(SummonAndKillAllBossesObjective.class).orElse(null));
            if (objective3 == null) {
                return false;
            }
            if (objective3.allObelisksClicked() || objective3.allBossesDefeated()) {
                return false;
            }
            objective3.addObelisk();
            final LivingEntity boss3 = VaultBossSpawner.spawnBoss(vault, (ServerWorld) world, pos);
            objective3.addBoss(boss3);
            return true;
        }
    }

    private void spawnParticles(final World world, final BlockPos pos) {
        for (int i = 0; i < 20; ++i) {
            final double d0 = world.random.nextGaussian() * 0.02;
            final double d2 = world.random.nextGaussian() * 0.02;
            final double d3 = world.random.nextGaussian() * 0.02;
            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.POOF, pos.getX() + world.random.nextDouble() - d0, pos.getY() + world.random.nextDouble() - d2, pos.getZ() + world.random.nextDouble() - d3, 10, d0, d2, d3, 1.0);
        }
        world.playSound((PlayerEntity) null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) ObeliskBlock.HALF}).add(new Property[]{(Property) ObeliskBlock.COMPLETION});
    }

    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            if (state.getValue((Property) ObeliskBlock.HALF) == DoubleBlockHalf.UPPER) {
                final BlockState otherState = world.getBlockState(pos.below());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.below(), isMoving);
                }
            } else {
                final BlockState otherState = world.getBlockState(pos.above());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.above(), isMoving);
                }
            }
        }
    }

    public List<ItemStack> getDrops(final BlockState state, final LootContext.Builder builder) {
        return Lists.newArrayList();
    }

    public BlockRenderType getRenderShape(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        COMPLETION = IntegerProperty.create("completion", 0, 4);
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 32.0, 14.0);
        SHAPE_TOP = ObeliskBlock.SHAPE.move(0.0, -1.0, 0.0);
    }
}
