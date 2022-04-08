package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.container.OmegaStatueContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.StatueType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class OmegaStatueBlock extends LootStatueBlock {
    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public OmegaStatueBlock() {
        super(StatueType.OMEGA);
        registerDefaultState((BlockState) ((BlockState) ((BlockState) getStateDefinition().any()).setValue((Property) FACING, (Comparable) Direction.SOUTH)).setValue((Property) MASTER, Boolean.TRUE));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        if (pos.getY() > 255) return null;

        World world = context.getLevel();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (!world.getBlockState(pos.offset(x, 0, z)).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return (BlockState) ((BlockState) defaultBlockState().setValue((Property) FACING, (Comparable) context.getHorizontalDirection())).setValue((Property) MASTER, Boolean.TRUE);
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide || !(placer instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) placer;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    BlockPos newBlockPos = pos.offset(x, 0, z);
                    worldIn.setBlockAndUpdate(newBlockPos, (BlockState) state.setValue((Property) MASTER, Boolean.FALSE));
                    TileEntity te = worldIn.getBlockEntity(newBlockPos);
                    if (te instanceof LootStatueTileEntity) {
                        ((LootStatueTileEntity) te).setStatueType(StatueType.OMEGA);
                        ((LootStatueTileEntity) te).setMaster(false);
                        ((LootStatueTileEntity) te).setMasterPos(pos);
                        te.setChanged();
                        ((LootStatueTileEntity) te).sendUpdates();
                    }
                }
            }
        }
        if (((Boolean) state.getValue((Property) MASTER)).booleanValue()) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);

            if (tileEntity instanceof LootStatueTileEntity) {

                LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
                if (stack.hasTag()) {
                    CompoundNBT nbt = stack.getTag();
                    CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");

                    String playerNickname = blockEntityTag.getString("PlayerNickname");
                    lootStatue.setStatueType(StatueType.OMEGA);
                    lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
                    lootStatue.setMaster(true);
                    lootStatue.setMasterPos(pos);
                    lootStatue.setItemsRemaining(-1);
                    lootStatue.setTotalItems(0);
                    lootStatue.setPlayerScale(MathUtilities.randomFloat(2.0F, 4.0F));
                    lootStatue.getSkin().updateSkin(playerNickname);


                    if (nbt.contains("LootItem")) {
                        lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
                    }

                    lootStatue.setChanged();
                    lootStatue.sendUpdates();

                    if (lootStatue.getLootItem() == null || lootStatue.getLootItem().isEmpty()) {
                        final CompoundNBT data = new CompoundNBT();
                        ListNBT itemList = new ListNBT();
                        List<ItemStack> options = ModConfigs.STATUE_LOOT.getOmegaOptions();
                        for (ItemStack option : options) {
                            itemList.add(option.serializeNBT());
                        }
                        data.put("Items", (INBT) itemList);
                        data.put("Position", (INBT) NBTUtil.writeBlockPos(pos));


                        NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                            public ITextComponent getDisplayName() {
                                return (ITextComponent) new StringTextComponent("Omega Statue Options");
                            }


                            @Nullable
                            public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                                return (Container) new OmegaStatueContainer(windowId, data);
                            }
                        },buffer -> buffer.writeNbt(data));
                    }
                }
            }
        }
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
        builder.add(new Property[]{(Property) MASTER});
    }


    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof LootStatueTileEntity && ((LootStatueTileEntity) te).getMasterPos() != null) {
            BlockPos masterPos = ((LootStatueTileEntity) te).getMasterPos();
            TileEntity master = worldIn.getBlockEntity(masterPos);
            if (master instanceof LootStatueTileEntity) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos newBlockPos = masterPos.offset(x, 0, z);
                        worldIn.removeBlockEntity(newBlockPos);
                        worldIn.setBlock(newBlockPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }


    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        LootStatueTileEntity statue = getStatueTileEntity(worldIn, pos);
        if (statue != null) {
            LootStatueTileEntity master = getMaster(statue);
            if (master != null) {
                BlockPos masterPos = master.getBlockPos();
                return super.use(worldIn.getBlockState(masterPos), worldIn, masterPos, player, handIn, hit);
            }
        }
        return ActionResultType.FAIL;
    }

    private LootStatueTileEntity getMaster(LootStatueTileEntity statue) {
        World world = statue.getLevel();
        if (world != null && statue.getMasterPos() != null) {
            TileEntity master = statue.getLevel().getBlockEntity(statue.getMasterPos());
            if (master instanceof LootStatueTileEntity) return (LootStatueTileEntity) master;
        }
        return null;
    }


    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        LootStatueTileEntity statue = getStatueTileEntity(world, pos);
        if (statue != null) {
            LootStatueTileEntity master = getMaster(statue);
            if (master != null) {
                BlockPos masterPos = master.getBlockPos();
                super.playerWillDestroy(world, masterPos, world.getBlockState(masterPos), player);
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\OmegaStatueBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */