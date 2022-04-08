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

public class ObeliskBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty COMPLETION = IntegerProperty.create("completion", 0, 4);

    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 32.0D, 14.0D);
    private static final VoxelShape SHAPE_TOP = SHAPE.move(0.0D, -1.0D, 0.0D);

    public ObeliskBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.METAL).strength(-1.0F, 3600000.0F).noDrops());
        registerDefaultState((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) HALF, (Comparable) DoubleBlockHalf.LOWER)).setValue((Property) COMPLETION, Integer.valueOf(0)));
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
            return SHAPE_TOP;
        }
        return SHAPE;
    }


    public boolean hasTileEntity(BlockState state) {
        return (state.getValue((Property) HALF) == DoubleBlockHalf.LOWER);
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (hasTileEntity(state)) {
            return ModBlocks.OBELISK_TILE_ENTITY.create();
        }
        return null;
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
            BlockState downState = world.getBlockState(pos.below());
            if (!(downState.getBlock() instanceof ObeliskBlock)) {
                return ActionResultType.PASS;
            }
            return use(downState, world, pos.below(), player, hand, hit);
        }

        if (((Integer) state.getValue((Property) COMPLETION)).intValue() != 4 &&
                newBlockActivated(state, world, pos, player, hand, hit)) {
            BlockState newState = (BlockState) state.setValue((Property) COMPLETION, Integer.valueOf(4));
            world.setBlockAndUpdate(pos, newState);
            spawnParticles(world, pos);
        }


        return ActionResultType.SUCCESS;
    }

    private boolean newBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) return false;
        VaultRaid vault = VaultRaidData.get((ServerWorld) world).getAt((ServerWorld) world, pos);
        if (vault == null) return false;


        SummonAndKillBossObjective objective = vault.getPlayer(player.getUUID()).flatMap(vaultPlayer -> vaultPlayer.getActiveObjective(SummonAndKillBossObjective.class)).orElseGet(() -> (SummonAndKillBossObjective) vault.getActiveObjective(SummonAndKillBossObjective.class).orElse(null));

        if (objective != null) {
            if (objective.allObelisksClicked()) return false;
            objective.addObelisk();

            if (objective.allObelisksClicked()) {
                LivingEntity boss = VaultBossSpawner.spawnBoss(vault, (ServerWorld) world, pos);
                objective.setBoss(boss);
            }
            return true;
        }
        return false;
    }

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 20; i++) {
            double d0 = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;

            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.POOF, pos
                    .getX() + world.random.nextDouble() - d0, pos
                    .getY() + world.random.nextDouble() - d1, pos
                    .getZ() + world.random.nextDouble() - d2, 10, d0, d1, d2, 1.0D);
        }

        world.playSound(null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) HALF}).add(new Property[]{(Property) COMPLETION});
    }


    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) {
            if (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) {
                BlockState otherState = world.getBlockState(pos.below());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.below(), isMoving);
                }
            } else {
                BlockState otherState = world.getBlockState(pos.above());
                if (otherState.is(state.getBlock())) {
                    world.removeBlock(pos.above(), isMoving);
                }
            }
        }
    }


    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Lists.newArrayList();
    }


    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\ObeliskBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */