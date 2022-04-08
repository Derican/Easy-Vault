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
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<ChamberState> CHAMBER_STATE = EnumProperty.create("chamber_state", ChamberState.class);

    public CryoChamberBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL)
                .strength(5.0F, 3600000.0F)
                .sound(SoundType.METAL)
                .noOcclusion()
                .isRedstoneConductor(CryoChamberBlock::isntSolid)
                .isViewBlocking(CryoChamberBlock::isntSolid));

        registerDefaultState((BlockState) ((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any())
                .setValue((Property) FACING, (Comparable) Direction.NORTH))
                .setValue((Property) HALF, (Comparable) DoubleBlockHalf.LOWER))
                .setValue((Property) CHAMBER_STATE, ChamberState.NONE));
    }

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        for (ChamberState state : ChamberState.values()) {
            ItemStack stack = new ItemStack((IItemProvider) this);
            stack.setDamageValue(state.ordinal());
            items.add(stack);
        }
    }


    public boolean hasTileEntity(BlockState state) {
        return (state.getValue((Property) HALF) == DoubleBlockHalf.LOWER);
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue((Property) HALF) == DoubleBlockHalf.LOWER) {
            if (state.getValue((Property) CHAMBER_STATE) == ChamberState.NONE) {
                return ModBlocks.CRYO_CHAMBER_TILE_ENTITY.create();
            }
            return ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY.create();
        }

        return null;
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return (BlockState) ((BlockState) ((BlockState) defaultBlockState()
                    .setValue((Property) FACING, (Comparable) context.getHorizontalDirection()))
                    .setValue((Property) HALF, (Comparable) DoubleBlockHalf.LOWER))
                    .setValue((Property) CHAMBER_STATE, MiscUtils.getEnumEntry(ChamberState.class, context.getItemInHand().getDamageValue()));
        }
        return null;
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) HALF, (Property) FACING, (Property) CHAMBER_STATE});
    }


    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            DoubleBlockHalf half = (DoubleBlockHalf) state.getValue((Property) HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos blockpos = pos.below();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue((Property) HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }


    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = (DoubleBlockHalf) stateIn.getValue((Property) HALF);
        if (facing.getAxis() == Direction.Axis.Y)
            if (((half == DoubleBlockHalf.LOWER) ? true : false) == ((facing == Direction.UP) ? true : false)) {
                return (facingState.is(this) && facingState.getValue((Property) HALF) != half) ? (BlockState) stateIn.setValue((Property) FACING, facingState.getValue((Property) FACING)) : Blocks.AIR.defaultBlockState();
            }
        return (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive((IWorldReader) worldIn, currentPos)) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlock(pos.above(), (BlockState) state.setValue((Property) HALF, (Comparable) DoubleBlockHalf.UPPER), 3);

        if (placer != null) {
            CryoChamberTileEntity te = getCryoChamberTileEntity(worldIn, pos, state);
            te.setOwner(placer.getUUID());
        }
    }


    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        if (!newState.isAir())
            return;
        CryoChamberTileEntity chamber = getCryoChamberTileEntity(worldIn, pos, state);
        if (chamber == null)
            return;
        if (state.getValue((Property) HALF) == DoubleBlockHalf.LOWER) {
            dropCryoChamber(worldIn, pos, state, chamber);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropCryoChamber(World world, BlockPos pos, BlockState state, CryoChamberTileEntity te) {
        ItemStack chamberStack = new ItemStack((IItemProvider) ModBlocks.CRYO_CHAMBER);
        chamberStack.setDamageValue(((ChamberState) state.getValue((Property) CHAMBER_STATE)).ordinal());
        CompoundNBT nbt = chamberStack.getOrCreateTag();
        nbt.put("BlockEntityTag", (INBT) te.serializeNBT());
        chamberStack.setTag(nbt);
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), chamberStack);
        world.addFreshEntity((Entity) entity);
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide() || !(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }
        CryoChamberTileEntity chamber = getCryoChamberTileEntity(world, pos, state);
        if (chamber == null) {
            return ActionResultType.SUCCESS;
        }
        if (chamber.getOwner() != null && !chamber.getOwner().equals(player.getUUID())) {
            return ActionResultType.SUCCESS;
        }
        ItemStack heldStack = player.getItemInHand(hand);
        if (chamber.getEternal() != null) {
            if (player.isShiftKeyDown()) {
                if (heldStack.isEmpty()) {
                    final CompoundNBT nbt = new CompoundNBT();
                    nbt.putInt("RenameType", RenameType.CRYO_CHAMBER.ordinal());
                    nbt.put("Data", (INBT) chamber.getRenameNBT());

                    NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                        public ITextComponent getDisplayName() {
                            return (ITextComponent) new StringTextComponent("Cryo Chamber");
                        }


                        @Nullable
                        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return (Container) new RenamingContainer(windowId, nbt);
                        }
                    },buffer -> buffer.writeNbt(nbt));


                    return ActionResultType.SUCCESS;
                }
            } else {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) chamber, buffer -> buffer.writeBlockPos(pos));
                return ActionResultType.SUCCESS;
            }
        } else if (!((ChamberState) state.getValue((Property) CHAMBER_STATE)).containsAncient() &&
                heldStack.getItem() == ModItems.TRADER_CORE) {
            TraderCore coreToInsert = ItemTraderCore.getCoreFromStack(heldStack);
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

    public static BlockPos getCryoChamberPos(BlockState state, BlockPos pos) {
        return (state.getValue((Property) HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos;
    }

    public static CryoChamberTileEntity getCryoChamberTileEntity(World world, BlockPos pos, BlockState state) {
        BlockPos cryoChamberPos = getCryoChamberPos(state, pos);
        TileEntity tileEntity = world.getBlockEntity(cryoChamberPos);
        if (!(tileEntity instanceof CryoChamberTileEntity)) {
            return null;
        }
        return (CryoChamberTileEntity) tileEntity;
    }

    public enum ChamberState
            implements IStringSerializable {
        NONE("none"),
        RUSTY("rusty");

        private final String name;

        ChamberState(String name) {
            this.name = name;
        }

        public boolean containsAncient() {
            return (this == RUSTY);
        }


        public String getSerializedName() {
            return this.name;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\CryoChamberBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */