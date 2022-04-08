package iskallia.vault.block;

import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class VaultAltarBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public VaultAltarBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(3.0F, 3600000.0F).noOcclusion());
        registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) POWERED, Boolean.FALSE));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) defaultBlockState().setValue((Property) POWERED, Boolean.FALSE);
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) POWERED});
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_ALTAR_TILE_ENTITY.create();
    }


    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide || handIn != Hand.MAIN_HAND || !(player instanceof ServerPlayerEntity))
            return ActionResultType.SUCCESS;
        ItemStack heldItem = player.getMainHandItem();

        VaultAltarTileEntity altar = getAltarTileEntity(worldIn, pos);
        if (altar == null) return ActionResultType.SUCCESS;

        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.IDLE) {
            if (heldItem.getItem() != ModItems.VAULT_ROCK) {
                return ActionResultType.SUCCESS;
            }
            return altar.onAddVaultRock((ServerPlayerEntity) player, heldItem);
        }

        if (player.isShiftKeyDown() && (altar
                .getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING || altar
                .getAltarState() == VaultAltarTileEntity.AltarState.COMPLETE)) {
            return altar.onRemoveVaultRock();
        }

        return ActionResultType.SUCCESS;
    }


    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        boolean powered = worldIn.hasNeighborSignal(pos);
        if (powered != ((Boolean) state.getValue((Property) POWERED)).booleanValue() &&
                powered) {
            VaultAltarTileEntity altar = getAltarTileEntity(worldIn, pos);
            if (altar != null) {
                altar.onAltarPowered();
            }
        }

        worldIn.setBlock(pos, (BlockState) state.setValue((Property) POWERED, Boolean.valueOf(powered)), 3);
    }


    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    private VaultAltarTileEntity getAltarTileEntity(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te == null || !(te instanceof VaultAltarTileEntity))
            return null;
        VaultAltarTileEntity altar = (VaultAltarTileEntity) worldIn.getBlockEntity(pos);
        return altar;
    }


    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        VaultAltarTileEntity altar = getAltarTileEntity(world, pos);
        if (altar == null)
            return;
        if (newState.getBlock() != Blocks.AIR)
            return;
        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING || altar.getAltarState() == VaultAltarTileEntity.AltarState.COMPLETE) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, new ItemStack((IItemProvider) ModItems.VAULT_ROCK));
            world.addFreshEntity((Entity) itemEntity);
        }

        ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, new ItemStack((IItemProvider) ModBlocks.VAULT_ALTAR));
        world.addFreshEntity((Entity) entity);

        PlayerVaultAltarData.get((ServerWorld) world).removeAltar(altar.getOwner(), pos);

        super.onRemove(state, world, pos, newState, isMoving);
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide)
            return;
        VaultAltarTileEntity altar = (VaultAltarTileEntity) worldIn.getBlockEntity(pos);
        if (altar == null || !(placer instanceof PlayerEntity))
            return;
        altar.setOwner(placer.getUUID());
        altar.setAltarState(VaultAltarTileEntity.AltarState.IDLE);
        altar.sendUpdates();

        PlayerVaultAltarData.get((ServerWorld) worldIn).addAltar(placer.getUUID(), pos);

        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultAltarBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */