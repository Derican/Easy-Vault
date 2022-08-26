package iskallia.vault.block;

import iskallia.vault.block.base.FacedBlock;
import iskallia.vault.block.property.HiddenIntegerProperty;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaultArtifactBlock extends FacedBlock {
    public static final int ARTIFACT_COUNT = 25;
    public static final IntegerProperty ORDER_PROPERTY;
    public static final VoxelShape EAST_SHAPE;
    public static final VoxelShape NORTH_SHAPE;
    public static final VoxelShape WEST_SHAPE;
    public static final VoxelShape SOUTH_SHAPE;

    public VaultArtifactBlock() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.WOOD).sound(SoundType.WOOL).noOcclusion());
        this.registerDefaultState((this.stateDefinition.any()).setValue(VaultArtifactBlock.FACING, Direction.SOUTH));
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
    }

    public int getOrder(final ItemStack stack) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("CustomModelData") ? nbt.getInt("CustomModelData") : 1;
    }

    public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
        switch (state.getValue(VaultArtifactBlock.FACING)) {
            case EAST: {
                return VaultArtifactBlock.EAST_SHAPE;
            }
            case NORTH: {
                return VaultArtifactBlock.NORTH_SHAPE;
            }
            case WEST: {
                return VaultArtifactBlock.WEST_SHAPE;
            }
            default: {
                return VaultArtifactBlock.SOUTH_SHAPE;
            }
        }
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final ItemStack artifactBlockItem = context.getItemInHand();
        return super.getStateForPlacement(context).setValue(VaultArtifactBlock.ORDER_PROPERTY, this.getOrder(artifactBlockItem));
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VaultArtifactBlock.ORDER_PROPERTY);
    }

    public void onBlockExploded(final BlockState state, final World world, final BlockPos pos, final Explosion explosion) {
        if (world instanceof ServerWorld) {
            final ServerWorld sWorld = (ServerWorld) world;
            final List<BlockPos> validPositions = isValidArtifactSetup(sWorld, pos, state);
            if (!validPositions.isEmpty()) {
                validPositions.forEach(at -> world.removeBlock(at, false));
                ServerScheduler.INSTANCE.schedule(5, () -> {
                    final ItemStack frameStack = new ItemStack(ModBlocks.FINAL_VAULT_FRAME_BLOCK_ITEM);
                    Block.popResource(sWorld, pos, frameStack);
                });
            }
        }
    }

    public boolean canDropFromExplosion(final BlockState state, final IBlockReader world, final BlockPos pos, final Explosion explosion) {
        return false;
    }

    public static List<BlockPos> isValidArtifactSetup(final ServerWorld world, final BlockPos at, final BlockState state) {
        final int order = (25 - state.getValue(VaultArtifactBlock.ORDER_PROPERTY) + 24) % 25;
        final int shiftVertical = order / 5;
        final int shiftHorizontal = order % 5;
        final BlockPos yPos = at.above(shiftVertical);
        for (final Direction dir : Direction.values()) {
            if (!dir.getAxis().isVertical()) {
                final BlockPos startPos = yPos.relative(dir, -shiftHorizontal);
                final List<BlockPos> artifactPositions = hasFullArtifactSet(world, startPos, dir);
                if (!artifactPositions.isEmpty()) {
                    return artifactPositions;
                }
            }
        }
        return Collections.emptyList();
    }

    private static List<BlockPos> hasFullArtifactSet(final ServerWorld world, final BlockPos start, final Direction facing) {
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        for (int order = 0; order < 25; ++order) {
            final BlockPos at = start.below(order / 5).relative(facing, order % 5);
            final BlockState offsetState = world.getBlockState(at);
            if (!(offsetState.getBlock() instanceof VaultArtifactBlock)) {
                return Collections.emptyList();
            }
            final int orderAt = (25 - offsetState.getValue(VaultArtifactBlock.ORDER_PROPERTY) + 24) % 25;
            if (order != orderAt) {
                return Collections.emptyList();
            }
            positions.add(at);
        }
        return positions;
    }

    public List<ItemStack> getDrops(final BlockState state, final LootContext.Builder builder) {
        final Integer order = state.getValue(VaultArtifactBlock.ORDER_PROPERTY);
        final ItemStack artifactStack = createArtifact(order);
        return new ArrayList<ItemStack>(Collections.singletonList(artifactStack));
    }

    public ItemStack getPickBlock(final BlockState state, final RayTraceResult target, final IBlockReader world, final BlockPos pos, final PlayerEntity player) {
        final Integer order = state.getValue(VaultArtifactBlock.ORDER_PROPERTY);
        return createArtifact(order);
    }

    public static ItemStack createRandomArtifact() {
        return createArtifact(MathUtilities.getRandomInt(0, 25) + 1);
    }

    public static ItemStack createArtifact(final int order) {
        final Item artifactItem = ForgeRegistries.ITEMS.getValue(ModBlocks.VAULT_ARTIFACT.getRegistryName());
        final ItemStack itemStack = new ItemStack(artifactItem, 1);
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CustomModelData", MathHelper.clamp(order, 0, 25));
        itemStack.setTag(nbt);
        return itemStack;
    }

    static {
        ORDER_PROPERTY = HiddenIntegerProperty.create("order", 1, 25);
        EAST_SHAPE = Block.box(15.75, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 0.25);
        WEST_SHAPE = Block.box(0.0, 0.0, 0.0, 0.25, 16.0, 16.0);
        SOUTH_SHAPE = Block.box(0.0, 0.0, 15.75, 16.0, 16.0, 16.0);
    }
}
