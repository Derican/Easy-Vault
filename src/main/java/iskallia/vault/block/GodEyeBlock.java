package iskallia.vault.block;

import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.item.GodEssenceItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.state.StateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemGroup;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.state.BooleanProperty;
import iskallia.vault.block.base.FacedBlock;

public class GodEyeBlock extends FacedBlock {
    public static final BooleanProperty LIT;
    public static final VoxelShape NORTH_SHAPE;
    public static final VoxelShape EAST_SHAPE;
    public static final VoxelShape SOUTH_SHAPE;
    public static final VoxelShape WEST_SHAPE;
    protected PlayerFavourData.VaultGodType godType;

    public GodEyeBlock(final PlayerFavourData.VaultGodType godType) {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(-1.0f, 3.6E8f).noDrops().noOcclusion().sound(SoundType.STONE));
        this.registerDefaultState((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) GodEyeBlock.FACING, (Comparable) Direction.SOUTH)).setValue((Property) GodEyeBlock.LIT, (Comparable) false));
        this.godType = godType;
    }

    public PlayerFavourData.VaultGodType getGodType() {
        return this.godType;
    }

    public void fillItemCategory(@Nonnull final ItemGroup group, @Nonnull final NonNullList<ItemStack> items) {
        super.fillItemCategory(group, (NonNullList) items);
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{(Property) GodEyeBlock.LIT});
    }

    @Nonnull
    public VoxelShape getShape(final BlockState state, @Nonnull final IBlockReader world, @Nonnull final BlockPos pos, @Nonnull final ISelectionContext context) {
        switch ((Direction) state.getValue((Property) GodEyeBlock.FACING)) {
            case EAST: {
                return GodEyeBlock.WEST_SHAPE;
            }
            case NORTH: {
                return GodEyeBlock.SOUTH_SHAPE;
            }
            case WEST: {
                return GodEyeBlock.EAST_SHAPE;
            }
            default: {
                return GodEyeBlock.NORTH_SHAPE;
            }
        }
    }

    @Nonnull
    public ActionResultType use(@Nonnull final BlockState state, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final PlayerEntity player, @Nonnull final Hand hand, @Nonnull final BlockRayTraceResult hit) {
        if (!world.isClientSide && !(boolean) state.getValue((Property) GodEyeBlock.LIT)) {
            final ItemStack heldItem = player.getItemInHand(hand);
            final Item item = heldItem.getItem();
            if (item instanceof GodEssenceItem) {
                final GodEssenceItem essenceItem = (GodEssenceItem) item;
                if (essenceItem.getGodType() == this.godType) {
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }
                    final BlockState newState = (BlockState) state.setValue((Property) GodEyeBlock.LIT, (Comparable) true);
                    world.setBlock(pos, newState, 3);
                    world.globalLevelEvent(1038, pos, 0);
                    ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.POOF, (double) (pos.getX() + 0.5f), (double) (pos.getY() + 0.5f), (double) (pos.getZ() + 0.5f), 100, 0.0, 0.0, 0.0, 1.5707963267948966);
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    static {
        LIT = BooleanProperty.create("lit");
        NORTH_SHAPE = Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
        EAST_SHAPE = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
        SOUTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
        WEST_SHAPE = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    }
}
