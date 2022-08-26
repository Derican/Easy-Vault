package iskallia.vault.block;

import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.RenameType;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CryoChamberBlock extends Block {
    public static final DirectionProperty FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final EnumProperty<ChamberState> CHAMBER_STATE;

    public CryoChamberBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).strength(5.0f, 3600000.0f).sound(SoundType.METAL).noOcclusion().isRedstoneConductor(CryoChamberBlock::isntSolid).isViewBlocking(CryoChamberBlock::isntSolid));
        this.registerDefaultState((((this.stateDefinition.any()).setValue(CryoChamberBlock.FACING, Direction.NORTH)).setValue(CryoChamberBlock.HALF, DoubleBlockHalf.LOWER)).setValue(CryoChamberBlock.CHAMBER_STATE, ChamberState.NONE));
    }

    private static boolean isntSolid(final BlockState state, final IBlockReader reader, final BlockPos pos) {
        return false;
    }

    public void fillItemCategory(final ItemGroup group, final NonNullList<ItemStack> items) {
        for (final ChamberState state : ChamberState.values()) {
            final ItemStack stack = new ItemStack(this);
            stack.setDamageValue(state.ordinal());
            items.add(stack);
        }
    }

    public boolean hasTileEntity(final BlockState state) {
        return state.getValue(CryoChamberBlock.HALF) == DoubleBlockHalf.LOWER;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (state.getValue(CryoChamberBlock.HALF) != DoubleBlockHalf.LOWER) {
            return null;
        }
        if (state.getValue(CryoChamberBlock.CHAMBER_STATE) == ChamberState.NONE) {
            return ModBlocks.CRYO_CHAMBER_TILE_ENTITY.create();
        }
        return ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY.create();
    }

    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        final World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return ((this.defaultBlockState().setValue(CryoChamberBlock.FACING, context.getHorizontalDirection())).setValue(CryoChamberBlock.HALF, DoubleBlockHalf.LOWER)).setValue(CryoChamberBlock.CHAMBER_STATE, MiscUtils.getEnumEntry(ChamberState.class, context.getItemInHand().getDamageValue()));
        }
        return null;
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CryoChamberBlock.HALF, CryoChamberBlock.FACING, CryoChamberBlock.CHAMBER_STATE);
    }

    public void playerWillDestroy(final World worldIn, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            final DoubleBlockHalf half = state.getValue(CryoChamberBlock.HALF);
            if (half == DoubleBlockHalf.UPPER) {
                final BlockPos blockpos = pos.below();
                final BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue(CryoChamberBlock.HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        final DoubleBlockHalf half = stateIn.getValue(CryoChamberBlock.HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return ((facingState.is(this) && facingState.getValue(CryoChamberBlock.HALF) != half) ? stateIn.setValue(CryoChamberBlock.FACING, facingState.getValue(CryoChamberBlock.FACING)) : Blocks.AIR.defaultBlockState());
        }
        return (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos)) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, final LivingEntity placer, final ItemStack stack) {
        worldIn.setBlock(pos.above(), state.setValue(CryoChamberBlock.HALF, DoubleBlockHalf.UPPER), 3);
        if (placer != null) {
            final CryoChamberTileEntity te = getCryoChamberTileEntity(worldIn, pos, state);
            te.setOwner(placer.getUUID());
        }
    }

    public void onRemove(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (worldIn.isClientSide) {
            return;
        }
        if (!newState.isAir()) {
            return;
        }
        final CryoChamberTileEntity chamber = getCryoChamberTileEntity(worldIn, pos, state);
        if (chamber == null) {
            return;
        }
        if (state.getValue(CryoChamberBlock.HALF) == DoubleBlockHalf.LOWER) {
            this.dropCryoChamber(worldIn, pos, state, chamber);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropCryoChamber(final World world, final BlockPos pos, final BlockState state, final CryoChamberTileEntity te) {
        final ItemStack chamberStack = new ItemStack(ModBlocks.CRYO_CHAMBER);
        chamberStack.setDamageValue(state.getValue(CryoChamberBlock.CHAMBER_STATE).ordinal());
        final CompoundNBT nbt = chamberStack.getOrCreateTag();
        nbt.put("BlockEntityTag", te.serializeNBT());
        chamberStack.setTag(nbt);
        final ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), chamberStack);
        world.addFreshEntity(entity);
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        if (world.isClientSide() || !(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }
        final CryoChamberTileEntity chamber = getCryoChamberTileEntity(world, pos, state);
        if (chamber == null) {
            return ActionResultType.SUCCESS;
        }
        if (chamber.getOwner() != null && !chamber.getOwner().equals(player.getUUID())) {
            return ActionResultType.SUCCESS;
        }
        final ItemStack heldStack = player.getItemInHand(hand);
        if (chamber.getEternal() != null) {
            if (!player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayerEntity) player, chamber, buffer -> buffer.writeBlockPos(pos));
                return ActionResultType.SUCCESS;
            }
            if (heldStack.isEmpty()) {
                final CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("RenameType", RenameType.CRYO_CHAMBER.ordinal());
                nbt.put("Data", chamber.getRenameNBT());
                NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("Cryo Chamber");
                    }

                    @Nullable
                    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                        return new RenamingContainer(windowId, nbt);
                    }
                }, buffer -> buffer.writeNbt(nbt));
                return ActionResultType.SUCCESS;
            }
        } else if (!state.getValue(CryoChamberBlock.CHAMBER_STATE).containsAncient() && heldStack.getItem() == ModItems.TRADER_CORE) {
            final TraderCore coreToInsert = ItemTraderCore.getCoreFromStack(heldStack);
            if (chamber.getOwner() == null) {
                chamber.setOwner(player.getUUID());
            }
            if (chamber.addTraderCore(coreToInsert)) {
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                chamber.sendUpdates();
            }
        }
        return ActionResultType.SUCCESS;
    }

    public static BlockPos getCryoChamberPos(final BlockState state, final BlockPos pos) {
        return (state.getValue(CryoChamberBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos;
    }

    public static CryoChamberTileEntity getCryoChamberTileEntity(final World world, final BlockPos pos, final BlockState state) {
        final BlockPos cryoChamberPos = getCryoChamberPos(state, pos);
        final TileEntity tileEntity = world.getBlockEntity(cryoChamberPos);
        if (!(tileEntity instanceof CryoChamberTileEntity)) {
            return null;
        }
        return (CryoChamberTileEntity) tileEntity;
    }

    static {
        FACING = HorizontalBlock.FACING;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        CHAMBER_STATE = EnumProperty.create("chamber_state", (Class) ChamberState.class);
    }

    public enum ChamberState implements IStringSerializable {
        NONE("none"),
        RUSTY("rusty");

        private final String name;

        ChamberState(final String name) {
            this.name = name;
        }

        public boolean containsAncient() {
            return this == ChamberState.RUSTY;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
