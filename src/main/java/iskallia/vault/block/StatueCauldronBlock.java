package iskallia.vault.block;

import iskallia.vault.block.entity.StatueCauldronTileEntity;
import iskallia.vault.client.gui.screen.StatueCauldronScreen;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.Property;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

public class StatueCauldronBlock extends CauldronBlock {
    public StatueCauldronBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3600000.0F)
                .noOcclusion());
    }


    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlocks.STATUE_CAULDRON_TILE_ENTITY.create();
    }


    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        if (itemstack.isEmpty()) {
            if (worldIn.isClientSide &&
                    handIn == Hand.MAIN_HAND) {
                openStatueScreen(worldIn, pos);
            }

            return ActionResultType.PASS;
        }
        int i = ((Integer) state.getValue((Property) LEVEL)).intValue();
        Item item = itemstack.getItem();
        if (item instanceof BucketItem && (
                (BucketItem) item).getFluid() != Fluids.EMPTY) {
            if (i < 3 && !worldIn.isClientSide) {
                if (!player.isCreative()) {
                    LazyOptional<IFluidHandlerItem> providerOptional = itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                    providerOptional.ifPresent(provider -> provider.drain(1000, IFluidHandler.FluidAction.EXECUTE));
                }

                player.awardStat(Stats.FILL_CAULDRON);
                worldIn.setBlock(pos, (BlockState) state.setValue((Property) LEVEL, Integer.valueOf(3)), 3);
                worldIn.updateNeighbourForOutputSignal(pos, (Block) this);
                worldIn.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }


        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @OnlyIn(Dist.CLIENT)
    private void openStatueScreen(World worldIn, BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen((Screen) new StatueCauldronScreen((ClientWorld) worldIn, pos));
    }


    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState toPlace = defaultBlockState();
        ItemStack stack = context.getItemInHand();
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag", 10)) {
            int cauldronLevel = stack.getTagElement("BlockEntityTag").getInt("Level");
            return (BlockState) toPlace.setValue((Property) LEVEL, Integer.valueOf(cauldronLevel));
        }
        return toPlace;
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide || !(placer instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) placer;
        TileEntity te = worldIn.getBlockEntity(pos);

        if (te instanceof StatueCauldronTileEntity) {
            StatueCauldronTileEntity cauldron = (StatueCauldronTileEntity) te;

            if (stack.getOrCreateTag().contains("BlockEntityTag")) {
                CompoundNBT cauldronNbt = stack.getTagElement("BlockEntityTag");
                cauldron.setOwner(cauldronNbt.getUUID("Owner"));
                cauldron.setRequiredAmount(cauldronNbt.getInt("RequiredAmount"));
                cauldron.setStatueCount(cauldronNbt.getInt("StatueCount"));
                cauldron.setNames(cauldronNbt.getList("NameList", 10));
            } else {
                cauldron.setOwner(player.getUUID());
                cauldron.setRequiredAmount(ModConfigs.STATUE_RECYCLING.getPlayerRequirement(player.getDisplayName().getString()));
            }

            cauldron.sendUpdates();
            cauldron.setChanged();
        }
    }


    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            ItemStack itemStack = new ItemStack((IItemProvider) getBlock());

            if (tileEntity instanceof StatueCauldronTileEntity) {
                StatueCauldronTileEntity cauldron = (StatueCauldronTileEntity) tileEntity;

                CompoundNBT statueNBT = cauldron.serializeNBT();
                CompoundNBT stackNBT = itemStack.getOrCreateTag();
                statueNBT.putInt("Level", ((Integer) state.getValue((Property) LEVEL)).intValue());
                stackNBT.put("BlockEntityTag", (INBT) statueNBT);

                itemStack.setTag(stackNBT);
            }

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity((Entity) itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\StatueCauldronBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */