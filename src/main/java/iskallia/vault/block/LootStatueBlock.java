// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.StatueType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class LootStatueBlock extends Block
{
    public static final VoxelShape SHAPE_GIFT_NORMAL;
    public static final VoxelShape SHAPE_GIFT_MEGA;
    public static final VoxelShape SHAPE_PLAYER_STATUE;
    public static final VoxelShape SHAPE_OMEGA_VARIANT;
    public static final DirectionProperty FACING;
    public StatueType type;
    
    protected LootStatueBlock(final StatueType type, final AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState((this.getStateDefinition().any()).setValue(LootStatueBlock.FACING, Direction.SOUTH));
        this.type = type;
    }
    
    public LootStatueBlock(final StatueType type) {
        this(type, AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.0f, 3600000.0f).noOcclusion().noCollission());
    }
    
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        final TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof LootStatueTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        final LootStatueTileEntity statue = (LootStatueTileEntity)te;
        if (player.isShiftKeyDown()) {
            final ItemStack chip = statue.removeChip();
            if (chip != ItemStack.EMPTY && !player.addItem(chip)) {
                player.drop(chip, false);
            }
            return ActionResultType.SUCCESS;
        }
        final ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty()) {
            if (statue.getStatueType().allowsRenaming()) {
                final CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("RenameType", RenameType.PLAYER_STATUE.ordinal());
                nbt.put("Data", (INBT)statue.serializeNBT());
                NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)new INamedContainerProvider() {
                    public ITextComponent getDisplayName() {
                        return (ITextComponent)new StringTextComponent("Player Statue");
                    }
                    
                    @Nullable
                    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                        return new RenamingContainer(windowId, nbt);
                    }
                }, buffer -> buffer.writeNbt(nbt));
            }
            return ActionResultType.SUCCESS;
        }
        if (heldItem.getItem() == ModItems.ACCELERATION_CHIP && statue.addChip()) {
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return super.use(state, world, pos, player, handIn, hit);
    }
    
    public void setPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        if (!world.isClientSide) {
            final TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof LootStatueTileEntity) {
                final LootStatueTileEntity lootStatue = (LootStatueTileEntity)tileEntity;
                if (stack.hasTag()) {
                    final CompoundNBT nbt = stack.getOrCreateTag();
                    this.setStatueTileData(lootStatue, nbt.getCompound("BlockEntityTag"));
                    lootStatue.setChanged();
                }
            }
        }
    }
    
    protected void setStatueTileData(final LootStatueTileEntity lootStatue, final CompoundNBT blockEntityTag) {
        final StatueType statueType = StatueType.values()[blockEntityTag.getInt("StatueType")];
        final String playerNickname = blockEntityTag.getString("PlayerNickname");
        lootStatue.setStatueType(statueType);
        lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
        lootStatue.getSkin().updateSkin(playerNickname);
        lootStatue.setItemsRemaining(blockEntityTag.getInt("ItemsRemaining"));
        lootStatue.setTotalItems(blockEntityTag.getInt("TotalItems"));
        lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
    }
    
    public void playerWillDestroy(final World world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!world.isClientSide) {
            final TileEntity tileEntity = world.getBlockEntity(pos);
            final ItemStack itemStack = new ItemStack((IItemProvider)this.getBlock());
            if (tileEntity instanceof LootStatueTileEntity) {
                final LootStatueTileEntity statueTileEntity = (LootStatueTileEntity)tileEntity;
                final CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                final CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", (INBT)statueNBT);
                itemStack.setTag(stackNBT);
            }
            final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity)itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
    
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }
    
    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.LOOT_STATUE_TILE_ENTITY.create();
    }
    
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { LootStatueBlock.FACING });
    }
    
    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        final World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(LootStatueBlock.FACING, context.getHorizontalDirection());
        }
        return null;
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        switch (this.getType()) {
            case GIFT_NORMAL: {
                return LootStatueBlock.SHAPE_GIFT_NORMAL;
            }
            case GIFT_MEGA: {
                return LootStatueBlock.SHAPE_GIFT_MEGA;
            }
            case VAULT_BOSS: {
                return LootStatueBlock.SHAPE_PLAYER_STATUE;
            }
            case OMEGA_VARIANT: {
                return LootStatueBlock.SHAPE_OMEGA_VARIANT;
            }
            default: {
                return VoxelShapes.block();
            }
        }
    }
    
    public StatueType getType() {
        return this.type;
    }
    
    protected LootStatueTileEntity getStatueTileEntity(final World world, final BlockPos pos) {
        final TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof LootStatueTileEntity) {
            return (LootStatueTileEntity)tileEntity;
        }
        return null;
    }
    
    static {
        SHAPE_GIFT_NORMAL = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);
        SHAPE_GIFT_MEGA = Block.box(1.0, 0.0, 1.0, 15.0, 13.0, 15.0);
        SHAPE_PLAYER_STATUE = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);
        SHAPE_OMEGA_VARIANT = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
