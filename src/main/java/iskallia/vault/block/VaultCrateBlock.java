package iskallia.vault.block;

import iskallia.vault.Vault;
import iskallia.vault.block.entity.VaultCrateTileEntity;
import iskallia.vault.container.VaultCrateContainer;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class VaultCrateBlock extends Block {
    public static final DirectionProperty HORIZONTAL_FACING = DirectionProperty.create("horizontal_facing", (Predicate) Direction.Plane.HORIZONTAL);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public VaultCrateBlock() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL)
                .strength(2.0F, 3600000.0F)
                .sound(SoundType.METAL)
                .noOcclusion());

        registerDefaultState((BlockState) ((BlockState) defaultBlockState().setValue((Property) HORIZONTAL_FACING, (Comparable) Direction.NORTH)).setValue((Property) FACING, (Comparable) Direction.UP));
    }

    public static ItemStack getCrateWithLoot(VaultCrateBlock crateType, NonNullList<ItemStack> items) {
        if (items.size() > 54) {
            Vault.LOGGER.error("Attempted to get a crate with more than 54 items. Check crate loot table.");
            items = NonNullList.of(ItemStack.EMPTY, items.stream().limit(54L).toArray(x$0 -> new ItemStack[x$0]));
        }

        ItemStack crate = new ItemStack((IItemProvider) crateType);
        CompoundNBT nbt = new CompoundNBT();
        ItemStackHelper.saveAllItems(nbt, items);
        if (!nbt.isEmpty()) {
            crate.addTagElement("BlockEntityTag", (INBT) nbt);
        }
        return crate;
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.VAULT_CRATE_TILE_ENTITY.create();
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) HORIZONTAL_FACING, (Property) FACING});
    }


    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof VaultCrateTileEntity) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    public ITextComponent getDisplayName() {
                        return (state.getBlock() == ModBlocks.VAULT_CRATE_ARENA) ? (ITextComponent) new TranslationTextComponent("container.vault.vault_crate_arena") : (

                                (state.getBlock() == ModBlocks.VAULT_CRATE_SCAVENGER) ? (ITextComponent) new TranslationTextComponent("container.vault.vault_crate_scavenger") : (

                                        (state.getBlock() == ModBlocks.VAULT_CRATE_CAKE) ? (ITextComponent) new TranslationTextComponent("container.vault.vault_crate_cake") : (ITextComponent) new TranslationTextComponent("container.vault.vault_crate")));
                    }


                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return (Container) new VaultCrateContainer(i, world, pos, playerInventory, playerEntity);
                    }
                };

                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BARREL_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.playerWillDestroy(world, pos, state, player);

        VaultCrateBlock block = getBlockVariant();
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof VaultCrateTileEntity) {
            VaultCrateTileEntity crate = (VaultCrateTileEntity) tileentity;

            ItemStack itemstack = new ItemStack((IItemProvider) block);
            CompoundNBT compoundnbt = crate.saveToNbt();
            if (!compoundnbt.isEmpty()) {
                itemstack.addTagElement("BlockEntityTag", (INBT) compoundnbt);
            }

            ItemEntity itementity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemstack);
            itementity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity) itementity);
        }
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction placeDir = context.getNearestLookingDirection().getOpposite();
        Direction horizontalDir = Direction.NORTH;
        if (placeDir.getAxis().isVertical()) {
            for (Direction direction : context.getNearestLookingDirections()) {
                if (direction.getAxis().isHorizontal()) {
                    horizontalDir = direction;
                    break;
                }
            }
        }
        return (BlockState) ((BlockState) defaultBlockState()
                .setValue((Property) FACING, (Comparable) placeDir))
                .setValue((Property) HORIZONTAL_FACING, (Comparable) horizontalDir);
    }

    private VaultCrateBlock getBlockVariant() {
        if (getBlock() == ModBlocks.VAULT_CRATE) return ModBlocks.VAULT_CRATE;
        if (getBlock() == ModBlocks.VAULT_CRATE_SCAVENGER) return ModBlocks.VAULT_CRATE_SCAVENGER;
        if (getBlock() == ModBlocks.VAULT_CRATE_CAKE) return ModBlocks.VAULT_CRATE_CAKE;
        return ModBlocks.VAULT_CRATE_ARENA;
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide()) {
            return;
        }

        CompoundNBT tag = stack.getTagElement("BlockEntityTag");
        if (tag == null) {
            return;
        }
        VaultCrateTileEntity crate = getCrateTileEntity(worldIn, pos);
        if (crate == null) {
            return;
        }
        crate.loadFromNBT(tag);


        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    private VaultCrateTileEntity getCrateTileEntity(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (!(te instanceof VaultCrateTileEntity)) {
            return null;
        }
        return (VaultCrateTileEntity) worldIn.getBlockEntity(pos);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultCrateBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */