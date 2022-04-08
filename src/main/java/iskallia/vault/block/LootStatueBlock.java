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

public class LootStatueBlock extends Block {
    public static final VoxelShape SHAPE_GIFT_NORMAL = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);
    public static final VoxelShape SHAPE_GIFT_MEGA = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
    public static final VoxelShape SHAPE_PLAYER_STATUE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);
    public static final VoxelShape SHAPE_OMEGA_VARIANT = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StatueType type;

    protected LootStatueBlock(StatueType type, AbstractBlock.Properties properties) {
        super(properties);

        registerDefaultState((BlockState) ((BlockState) getStateDefinition().any()).setValue((Property) FACING, (Comparable) Direction.SOUTH));
        this.type = type;
    }

    public LootStatueBlock(StatueType type) {
        this(type, AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(1.0F, 3600000.0F)
                .noOcclusion()
                .noCollission());
    }


    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide) return ActionResultType.SUCCESS;

        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof LootStatueTileEntity)) return ActionResultType.SUCCESS;

        LootStatueTileEntity statue = (LootStatueTileEntity) te;


        if (player.isShiftKeyDown()) {
            ItemStack chip = statue.removeChip();
            if (chip != ItemStack.EMPTY &&
                    !player.addItem(chip)) {
                player.drop(chip, false);
            }

            return ActionResultType.SUCCESS;
        }
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty()) {
            if (statue.getStatueType().allowsRenaming()) {
                final CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("RenameType", RenameType.PLAYER_STATUE.ordinal());
                nbt.put("Data", (INBT) statue.serializeNBT());

                NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                    public ITextComponent getDisplayName() {
                        return (ITextComponent) new StringTextComponent("Player Statue");
                    }


                    @Nullable
                    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return (Container) new RenamingContainer(windowId, nbt);
                    }
                },buffer -> buffer.writeNbt(nbt));
            }


            return ActionResultType.SUCCESS;
        } if (heldItem.getItem() == ModItems.ACCELERATION_CHIP &&
                statue.addChip()) {
            if (!player.isCreative())
                heldItem.shrink(1);
            return ActionResultType.SUCCESS;
        }


        return super.use(state, world, pos, player, handIn, hit);
    }


    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);

            if (tileEntity instanceof LootStatueTileEntity) {
                LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
                if (stack.hasTag()) {
                    CompoundNBT nbt = stack.getOrCreateTag();
                    setStatueTileData(lootStatue, nbt.getCompound("BlockEntityTag"));
                    lootStatue.setChanged();
                }
            }
        }
    }

    protected void setStatueTileData(LootStatueTileEntity lootStatue, CompoundNBT blockEntityTag) {
        StatueType statueType = StatueType.values()[blockEntityTag.getInt("StatueType")];
        String playerNickname = blockEntityTag.getString("PlayerNickname");

        lootStatue.setStatueType(statueType);
        lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
        lootStatue.getSkin().updateSkin(playerNickname);
        lootStatue.setItemsRemaining(blockEntityTag.getInt("ItemsRemaining"));
        lootStatue.setTotalItems(blockEntityTag.getInt("TotalItems"));
        lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
    }


    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            ItemStack itemStack = new ItemStack((IItemProvider) getBlock());

            if (tileEntity instanceof LootStatueTileEntity) {
                LootStatueTileEntity statueTileEntity = (LootStatueTileEntity) tileEntity;

                CompoundNBT statueNBT = statueTileEntity.serializeNBT();
                CompoundNBT stackNBT = new CompoundNBT();
                stackNBT.put("BlockEntityTag", (INBT) statueNBT);

                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity) itemEntity);
        }

        super.playerWillDestroy(world, pos, state, player);
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.LOOT_STATUE_TILE_ENTITY.create();
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        if (pos.getY() < 255 && world.getBlockState(pos.above()).canBeReplaced(context)) {
            return (BlockState) defaultBlockState().setValue((Property) FACING, (Comparable) context.getHorizontalDirection());
        }
        return null;
    }


    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (getType()) {
            case GIFT_NORMAL:
                return SHAPE_GIFT_NORMAL;
            case GIFT_MEGA:
                return SHAPE_GIFT_MEGA;
            case VAULT_BOSS:
                return SHAPE_PLAYER_STATUE;
            case OMEGA_VARIANT:
                return SHAPE_OMEGA_VARIANT;
        }
        return VoxelShapes.block();
    }

    public StatueType getType() {
        return this.type;
    }

    protected LootStatueTileEntity getStatueTileEntity(World world, BlockPos pos) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof LootStatueTileEntity) return (LootStatueTileEntity) tileEntity;

        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\LootStatueBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */