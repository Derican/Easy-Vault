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
    public static final BooleanProperty MASTER;

    public OmegaStatueBlock() {
        super(StatueType.OMEGA);
        this.registerDefaultState(((this.getStateDefinition().any()).setValue(OmegaStatueBlock.FACING, Direction.SOUTH)).setValue(OmegaStatueBlock.MASTER, Boolean.TRUE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockPos pos = context.getClickedPos();
        if (pos.getY() > 255) {
            return null;
        }
        final World world = context.getLevel();
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                if (!world.getBlockState(pos.offset(x, 0, z)).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return (this.defaultBlockState().setValue(OmegaStatueBlock.FACING, context.getHorizontalDirection())).setValue(OmegaStatueBlock.MASTER, Boolean.TRUE);
    }

    @Override
    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        if (worldIn.isClientSide || !(placer instanceof PlayerEntity)) {
            return;
        }
        final PlayerEntity player = (PlayerEntity) placer;
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                if (x != 0 || z != 0) {
                    final BlockPos newBlockPos = pos.offset(x, 0, z);
                    worldIn.setBlockAndUpdate(newBlockPos, state.setValue(OmegaStatueBlock.MASTER, Boolean.FALSE));
                    final TileEntity te = worldIn.getBlockEntity(newBlockPos);
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
        if (state.getValue(OmegaStatueBlock.MASTER)) {
            final TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof LootStatueTileEntity) {
                final LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
                if (stack.hasTag()) {
                    final CompoundNBT nbt = stack.getTag();
                    final CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
                    final String playerNickname = blockEntityTag.getString("PlayerNickname");
                    lootStatue.setStatueType(StatueType.OMEGA);
                    lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
                    lootStatue.setMaster(true);
                    lootStatue.setMasterPos(pos);
                    lootStatue.setItemsRemaining(-1);
                    lootStatue.setTotalItems(0);
                    lootStatue.setPlayerScale(MathUtilities.randomFloat(2.0f, 4.0f));
                    lootStatue.getSkin().updateSkin(playerNickname);
                    if (nbt.contains("LootItem")) {
                        lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
                    }
                    lootStatue.setChanged();
                    lootStatue.sendUpdates();
                    if (lootStatue.getLootItem() == null || lootStatue.getLootItem().isEmpty()) {
                        final CompoundNBT data = new CompoundNBT();
                        final ListNBT itemList = new ListNBT();
                        final List<ItemStack> options = ModConfigs.STATUE_LOOT.getOmegaOptions();
                        for (final ItemStack option : options) {
                            itemList.add(option.serializeNBT());
                        }
                        data.put("Items", (INBT) itemList);
                        data.put("Position", (INBT) NBTUtil.writeBlockPos(pos));
                        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) new INamedContainerProvider() {
                            public ITextComponent getDisplayName() {
                                return (ITextComponent) new StringTextComponent("Omega Statue Options");
                            }

                            @Nullable
                            public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                                return new OmegaStatueContainer(windowId, data);
                            }
                        }, buffer -> buffer.writeNbt(data));
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{OmegaStatueBlock.FACING});
        builder.add(new Property[]{OmegaStatueBlock.MASTER});
    }

    public void onRemove(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        final TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof LootStatueTileEntity && ((LootStatueTileEntity) te).getMasterPos() != null) {
            final BlockPos masterPos = ((LootStatueTileEntity) te).getMasterPos();
            final TileEntity master = worldIn.getBlockEntity(masterPos);
            if (master instanceof LootStatueTileEntity) {
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        final BlockPos newBlockPos = masterPos.offset(x, 0, z);
                        worldIn.removeBlockEntity(newBlockPos);
                        worldIn.setBlock(newBlockPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType use(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        final LootStatueTileEntity statue = this.getStatueTileEntity(worldIn, pos);
        if (statue != null) {
            final LootStatueTileEntity master = this.getMaster(statue);
            if (master != null) {
                final BlockPos masterPos = master.getBlockPos();
                return super.use(worldIn.getBlockState(masterPos), worldIn, masterPos, player, handIn, hit);
            }
        }
        return ActionResultType.FAIL;
    }

    private LootStatueTileEntity getMaster(final LootStatueTileEntity statue) {
        final World world = statue.getLevel();
        if (world != null && statue.getMasterPos() != null) {
            final TileEntity master = statue.getLevel().getBlockEntity(statue.getMasterPos());
            if (master instanceof LootStatueTileEntity) {
                return (LootStatueTileEntity) master;
            }
        }
        return null;
    }

    @Override
    public void playerWillDestroy(final World world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        final LootStatueTileEntity statue = this.getStatueTileEntity(world, pos);
        if (statue != null) {
            final LootStatueTileEntity master = this.getMaster(statue);
            if (master != null) {
                final BlockPos masterPos = master.getBlockPos();
                super.playerWillDestroy(world, masterPos, world.getBlockState(masterPos), player);
            }
        }
    }

    static {
        MASTER = BooleanProperty.create("master");
    }
}
