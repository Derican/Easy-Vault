// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.block.entity.VendingMachineTileEntity;
import iskallia.vault.container.VendingMachineContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.vending.TraderCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class VendingMachineBlock extends Block
{
    public static final DirectionProperty FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    
    public VendingMachineBlock() {
        this(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).strength(2.0f, 3600000.0f).sound(SoundType.METAL).noOcclusion());
    }
    
    public VendingMachineBlock(final AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(((this.stateDefinition.any()).setValue(VendingMachineBlock.FACING, Direction.NORTH)).setValue(VendingMachineBlock.HALF, DoubleBlockHalf.LOWER));
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return state.getValue(VendingMachineBlock.HALF) == DoubleBlockHalf.LOWER;
    }
    
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        if (state.getValue(VendingMachineBlock.HALF) == DoubleBlockHalf.LOWER) {
            return ModBlocks.VENDING_MACHINE_TILE_ENTITY.create();
        }
        return null;
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        final World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return (this.defaultBlockState().setValue(VendingMachineBlock.FACING, context.getHorizontalDirection())).setValue(VendingMachineBlock.HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { VendingMachineBlock.HALF });
        builder.add(new Property[] { VendingMachineBlock.FACING });
    }
    
    public void playerWillDestroy(final World worldIn, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            final DoubleBlockHalf half = (DoubleBlockHalf)state.getValue(VendingMachineBlock.HALF);
            if (half == DoubleBlockHalf.UPPER) {
                final BlockPos blockpos = pos.below();
                final BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == state.getBlock() && blockstate.getValue(VendingMachineBlock.HALF) == DoubleBlockHalf.LOWER) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }
    
    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        final DoubleBlockHalf half = (DoubleBlockHalf)stateIn.getValue(VendingMachineBlock.HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return ((facingState.is((Block)this) && facingState.getValue(VendingMachineBlock.HALF) != half) ? stateIn.setValue(VendingMachineBlock.FACING, facingState.getValue(VendingMachineBlock.FACING)) : Blocks.AIR.defaultBlockState());
        }
        return (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive((IWorldReader)worldIn, currentPos)) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        worldIn.setBlock(pos.above(), state.setValue(VendingMachineBlock.HALF, DoubleBlockHalf.UPPER), 3);
    }
    
    public void onRemove(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (worldIn.isClientSide) {
            return;
        }
        if (!newState.isAir()) {
            return;
        }
        final VendingMachineTileEntity machine = (VendingMachineTileEntity)getBlockTileEntity(worldIn, pos, state);
        if (machine == null) {
            return;
        }
        if (state.getValue(VendingMachineBlock.HALF) == DoubleBlockHalf.LOWER) {
            final ItemStack stack = new ItemStack((IItemProvider)this.getBlock());
            final CompoundNBT machineNBT = machine.serializeNBT();
            final CompoundNBT stackNBT = new CompoundNBT();
            stackNBT.put("BlockEntityTag", (INBT)machineNBT);
            stack.setTag(stackNBT);
            this.dropVendingMachine(stack, worldIn, pos);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    private void dropVendingMachine(final ItemStack stack, final World world, final BlockPos pos) {
        final ItemEntity entity = new ItemEntity(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), stack);
        world.addFreshEntity((Entity)entity);
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
        final ItemStack heldStack = player.getItemInHand(hand);
        final VendingMachineTileEntity machine = (VendingMachineTileEntity)getBlockTileEntity(world, pos, state);
        if (machine == null) {
            return ActionResultType.SUCCESS;
        }
        if (!world.isClientSide() && player.isShiftKeyDown()) {
            final ItemStack core = machine.getTraderCoreStack();
            if (!player.addItem(core)) {
                player.drop(core, false);
            }
            machine.sendUpdates();
            return ActionResultType.SUCCESS;
        }
        if (heldStack.getItem() instanceof ItemTraderCore) {
            final TraderCore lastCore = machine.getLastCore();
            final TraderCore coreToInsert = ItemTraderCore.getCoreFromStack(heldStack);
            if (coreToInsert == null || coreToInsert.getTrade() == null) {
                return ActionResultType.FAIL;
            }
            if (lastCore == null || lastCore.getName().equalsIgnoreCase(coreToInsert.getName())) {
                machine.addCore(coreToInsert);
                heldStack.shrink(1);
            }
            else {
                final StringTextComponent text = new StringTextComponent("This vending machine is already occupied.");
                text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-10240)));
                player.displayClientMessage((ITextComponent)text, true);
            }
            return ActionResultType.SUCCESS;
        }
        else {
            if (world.isClientSide) {
                playOpenSound();
                return ActionResultType.SUCCESS;
            }
            NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent)new StringTextComponent("Vending Machine");
                }
                
                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                    final BlockState blockState = world.getBlockState(pos);
                    final BlockPos vendingMachinePos = VendingMachineBlock.getTileEntityPos(blockState, pos);
                    return new VendingMachineContainer(windowId, world, vendingMachinePos, playerInventory, playerEntity);
                }
            }, buffer -> {
                final BlockState blockState = world.getBlockState(pos);
                buffer.writeBlockPos(getTileEntityPos(blockState, pos));
                return;
            });
            return ActionResultType.SUCCESS;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playOpenSound() {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().play((ISound)SimpleSound.forUI(ModSounds.VENDING_MACHINE_SFX, 1.0f, 0.3f));
    }
    
    public static BlockPos getTileEntityPos(final BlockState state, final BlockPos pos) {
        return (state.getValue(VendingMachineBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos;
    }
    
    public static TileEntity getBlockTileEntity(final World world, final BlockPos pos, final BlockState state) {
        final BlockPos vendingMachinePos = getTileEntityPos(state, pos);
        final TileEntity tileEntity = world.getBlockEntity(vendingMachinePos);
        return tileEntity;
    }
    
    static {
        FACING = HorizontalBlock.FACING;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }
}
