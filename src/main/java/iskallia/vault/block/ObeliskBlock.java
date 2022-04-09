// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import com.google.common.collect.Lists;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.VaultBossSpawner;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ObeliskBlock extends Block
{
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final IntegerProperty COMPLETION;
    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_TOP;
    
    public ObeliskBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.METAL).strength(-1.0f, 3600000.0f).noDrops());
        this.registerDefaultState(((this.stateDefinition.any()).setValue(ObeliskBlock.HALF, DoubleBlockHalf.LOWER)).setValue(ObeliskBlock.COMPLETION, 0));
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        if (state.getValue(ObeliskBlock.HALF) == DoubleBlockHalf.UPPER) {
            return ObeliskBlock.SHAPE_TOP;
        }
        return ObeliskBlock.SHAPE;
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return state.getValue(ObeliskBlock.HALF) == DoubleBlockHalf.LOWER;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (this.hasTileEntity(state)) {
            return ModBlocks.OBELISK_TILE_ENTITY.create();
        }
        return null;
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (state.getValue(ObeliskBlock.HALF) != DoubleBlockHalf.UPPER) {
            if ((int)state.getValue(ObeliskBlock.COMPLETION) != 4 && this.newBlockActivated(state, world, pos, player, hand, hit)) {
                final BlockState newState = state.setValue(ObeliskBlock.COMPLETION, 4);
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
        final VaultRaid vault = VaultRaidData.get((ServerWorld)world).getAt((ServerWorld)world, pos);
        if (vault == null) {
            return false;
        }
        final SummonAndKillBossObjective objective = vault.getPlayer(player.getUUID()).flatMap(vaultPlayer -> vaultPlayer.getActiveObjective(SummonAndKillBossObjective.class)).orElseGet(() -> vault.getActiveObjective(SummonAndKillBossObjective.class).orElse(null));
        if (objective == null) {
            return false;
        }
        if (objective.allObelisksClicked()) {
            return false;
        }
        objective.addObelisk();
        if (objective.allObelisksClicked()) {
            final LivingEntity boss = VaultBossSpawner.spawnBoss(vault, (ServerWorld)world, pos);
            objective.setBoss(boss);
        }
        return true;
    }
    
    private void spawnParticles(final World world, final BlockPos pos) {
        for (int i = 0; i < 20; ++i) {
            final double d0 = world.random.nextGaussian() * 0.02;
            final double d2 = world.random.nextGaussian() * 0.02;
            final double d3 = world.random.nextGaussian() * 0.02;
            ((ServerWorld)world).sendParticles((IParticleData)ParticleTypes.POOF, pos.getX() + world.random.nextDouble() - d0, pos.getY() + world.random.nextDouble() - d2, pos.getZ() + world.random.nextDouble() - d3, 10, d0, d2, d3, 1.0);
        }
        world.playSound((PlayerEntity)null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { ObeliskBlock.HALF }).add(new Property[] { ObeliskBlock.COMPLETION });
    }
    
    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            if (state.getValue(ObeliskBlock.HALF) == DoubleBlockHalf.UPPER) {
                final BlockState otherState = world.getBlockState(pos.below());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.below(), isMoving);
                }
            }
            else {
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
