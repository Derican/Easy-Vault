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
    public static final IntegerProperty ORDER_PROPERTY = HiddenIntegerProperty.create("order", 1, 25);

    public static final VoxelShape EAST_SHAPE = Block.box(15.75D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.25D);
    public static final VoxelShape WEST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 0.25D, 16.0D, 16.0D);
    public static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 15.75D, 16.0D, 16.0D, 16.0D);

    public VaultArtifactBlock() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.WOOD)
                .sound(SoundType.WOOL)
                .noOcclusion());

        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) FACING, (Comparable) Direction.SOUTH));
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
    }

    public int getOrder(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        return nbt.contains("CustomModelData") ? nbt.getInt("CustomModelData") : 1;
    }


    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch ((Direction) state.getValue((Property) FACING)) {
            case EAST:
                return EAST_SHAPE;
            case NORTH:
                return NORTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
        }
        return SOUTH_SHAPE;
    }


    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        ItemStack artifactBlockItem = context.getItemInHand();
        return (BlockState) super.getStateForPlacement(context)
                .setValue((Property) ORDER_PROPERTY, Integer.valueOf(getOrder(artifactBlockItem)));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{(Property) ORDER_PROPERTY});
    }


    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (world instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) world;
            List<BlockPos> validPositions = isValidArtifactSetup(sWorld, pos, state);
            if (!validPositions.isEmpty()) {
                validPositions.forEach(at -> world.removeBlock(at, false));
                ServerScheduler.INSTANCE.schedule(5, () -> Block.popResource((World) sWorld, pos, new ItemStack((IItemProvider) ModItems.VAULT_RUNE)));
            }
        }
    }


    public boolean canDropFromExplosion(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return false;
    }

    public static List<BlockPos> isValidArtifactSetup(ServerWorld world, BlockPos at, BlockState state) {
        int order = (25 - ((Integer) state.getValue((Property) ORDER_PROPERTY)).intValue() + 24) % 25;
        int shiftVertical = order / 5;
        int shiftHorizontal = order % 5;

        BlockPos yPos = at.above(shiftVertical);
        for (Direction dir : Direction.values()) {
            if (!dir.getAxis().isVertical()) {


                BlockPos startPos = yPos.relative(dir, -shiftHorizontal);
                List<BlockPos> artifactPositions = hasFullArtifactSet(world, startPos, dir);
                if (!artifactPositions.isEmpty())
                    return artifactPositions;
            }
        }
        return Collections.emptyList();
    }

    private static List<BlockPos> hasFullArtifactSet(ServerWorld world, BlockPos start, Direction facing) {
        List<BlockPos> positions = new ArrayList<>();
        for (int order = 0; order < 25; order++) {
            BlockPos at = start.below(order / 5).relative(facing, order % 5);
            BlockState offsetState = world.getBlockState(at);
            if (!(offsetState.getBlock() instanceof VaultArtifactBlock)) {
                return Collections.emptyList();
            }
            int orderAt = (25 - ((Integer) offsetState.getValue((Property) ORDER_PROPERTY)).intValue() + 24) % 25;
            if (order != orderAt) {
                return Collections.emptyList();
            }
            positions.add(at);
        }
        return positions;
    }


    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        Integer order = (Integer) state.getValue((Property) ORDER_PROPERTY);
        ItemStack artifactStack = createArtifact(order.intValue());
        return new ArrayList<>(Collections.singletonList(artifactStack));
    }


    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        Integer order = (Integer) state.getValue((Property) ORDER_PROPERTY);
        return createArtifact(order.intValue());
    }


    public static ItemStack createRandomArtifact() {
        return createArtifact(MathUtilities.getRandomInt(0, 25) + 1);
    }

    public static ItemStack createArtifact(int order) {
        Item artifactItem = (Item) ForgeRegistries.ITEMS.getValue(ModBlocks.VAULT_ARTIFACT.getRegistryName());
        ItemStack itemStack = new ItemStack((IItemProvider) artifactItem, 1);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CustomModelData", MathHelper.clamp(order, 0, 25));
        itemStack.setTag(nbt);
        return itemStack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultArtifactBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */