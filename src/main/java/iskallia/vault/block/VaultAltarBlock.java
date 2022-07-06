package iskallia.vault.block;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.item.ItemStack;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.init.ModBlocks;
import net.minecraft.world.IBlockReader;
import iskallia.vault.altar.AltarInfusionRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import iskallia.vault.block.entity.VaultAltarTileEntity;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.Block;

public class VaultAltarBlock extends Block {
    public static final BooleanProperty POWERED;

    public VaultAltarBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(3.0f, 3600000.0f).noOcclusion());
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue((Property) VaultAltarBlock.POWERED, (Comparable) Boolean.FALSE));
    }

    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return (BlockState) this.defaultBlockState().setValue((Property) VaultAltarBlock.POWERED, (Comparable) Boolean.FALSE);
    }

    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) VaultAltarBlock.POWERED});
    }

    public void animateTick(@Nonnull final BlockState state, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final Random rand) {
        final TileEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof VaultAltarTileEntity)) {
            return;
        }
        final VaultAltarTileEntity altarTileEntity = (VaultAltarTileEntity) tileEntity;
        final AltarInfusionRecipe recipe = altarTileEntity.getRecipe();
        if (recipe == null || !recipe.isPogInfused()) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + rand.nextDouble();
            final double d2 = pos.getY() + rand.nextDouble();
            double d3 = pos.getZ() + rand.nextDouble();
            double d4 = (rand.nextFloat() - 0.5) * 0.5;
            final double d5 = (rand.nextFloat() - 0.5) * 0.5;
            double d6 = (rand.nextFloat() - 0.5) * 0.5;
            final int j = rand.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).is((Block) this) && !world.getBlockState(pos.east()).is((Block) this)) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            } else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            world.addParticle((IParticleData) ParticleTypes.WITCH, d0, d2, d3, d4, d5, d6);
        }
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.VAULT_ALTAR_TILE_ENTITY.create();
    }

    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (world.isClientSide || handIn != Hand.MAIN_HAND || !(player instanceof ServerPlayerEntity)) {
            return ActionResultType.SUCCESS;
        }
        final ItemStack heldItem = player.getMainHandItem();
        final VaultAltarTileEntity altar = this.getAltarTileEntity(world, pos);
        if (altar == null) {
            return ActionResultType.SUCCESS;
        }
        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.IDLE) {
            if (heldItem.getItem() == ModItems.VAULT_ROCK) {
                return altar.onAddVaultRock((ServerPlayerEntity) player, heldItem);
            }
            return ActionResultType.SUCCESS;
        } else if (altar.getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING && heldItem.getItem() == ModItems.POG) {
            if (altar.getRecipe().isPogInfused()) {
                return ActionResultType.FAIL;
            }
            return altar.onPogRightClick((ServerPlayerEntity) player, heldItem);
        } else {
            if (player.isShiftKeyDown() && (altar.getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING || altar.getAltarState() == VaultAltarTileEntity.AltarState.COMPLETE)) {
                final ActionResultType result = (altar.getRecipe() != null && altar.getRecipe().isPogInfused()) ? altar.onRemovePogInfusion() : altar.onRemoveVaultRock();
                PlayerVaultAltarData.get((ServerWorld) world).setDirty();
                return result;
            }
            return ActionResultType.SUCCESS;
        }
    }

    public void neighborChanged(final BlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos, final boolean isMoving) {
        if (worldIn.isClientSide) {
            return;
        }
        final boolean powered = worldIn.hasNeighborSignal(pos);
        if (powered != (boolean) state.getValue((Property) VaultAltarBlock.POWERED) && powered) {
            final VaultAltarTileEntity altar = this.getAltarTileEntity(worldIn, pos);
            if (altar != null) {
                altar.onAltarPowered();
            }
        }
        worldIn.setBlock(pos, (BlockState) state.setValue((Property) VaultAltarBlock.POWERED, (Comparable) powered), 3);
    }

    public boolean canConnectRedstone(final BlockState state, final IBlockReader world, final BlockPos pos, @Nullable final Direction side) {
        return true;
    }

    private VaultAltarTileEntity getAltarTileEntity(final World worldIn, final BlockPos pos) {
        final TileEntity te = worldIn.getBlockEntity(pos);
        if (te == null || !(te instanceof VaultAltarTileEntity)) {
            return null;
        }
        final VaultAltarTileEntity altar = (VaultAltarTileEntity) worldIn.getBlockEntity(pos);
        return altar;
    }

    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        final VaultAltarTileEntity altar = this.getAltarTileEntity(world, pos);
        if (altar == null) {
            return;
        }
        if (newState.getBlock() != Blocks.AIR) {
            return;
        }
        if (altar.getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING || altar.getAltarState() == VaultAltarTileEntity.AltarState.COMPLETE) {
            final ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, new ItemStack((IItemProvider) ModItems.VAULT_ROCK));
            world.addFreshEntity((Entity) entity);
        }
        final ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, new ItemStack((IItemProvider) ModBlocks.VAULT_ALTAR));
        world.addFreshEntity((Entity) entity);
        PlayerVaultAltarData.get((ServerWorld) world).removeAltar(altar.getOwner(), pos);
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        if (worldIn.isClientSide) {
            return;
        }
        final VaultAltarTileEntity altar = (VaultAltarTileEntity) worldIn.getBlockEntity(pos);
        if (altar == null || !(placer instanceof PlayerEntity)) {
            return;
        }
        altar.setOwner(placer.getUUID());
        altar.setAltarState(VaultAltarTileEntity.AltarState.IDLE);
        altar.sendUpdates();
        PlayerVaultAltarData.get((ServerWorld) worldIn).addAltar(placer.getUUID(), pos);
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    static {
        POWERED = BlockStateProperties.POWERED;
    }
}
