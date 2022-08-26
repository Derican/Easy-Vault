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
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(3.0f, 3600000.0f).noOcclusion());
    }

    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Nullable
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModBlocks.STATUE_CAULDRON_TILE_ENTITY.create();
    }

    public ActionResultType use(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        final ItemStack itemstack = player.getItemInHand(handIn);
        if (itemstack.isEmpty()) {
            if (worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
                this.openStatueScreen(worldIn, pos);
            }
            return ActionResultType.PASS;
        }
        final int i = state.getValue(StatueCauldronBlock.LEVEL);
        final Item item = itemstack.getItem();
        if (item instanceof BucketItem && ((BucketItem) item).getFluid() != Fluids.EMPTY) {
            if (i < 3 && !worldIn.isClientSide) {
                if (!player.isCreative()) {
                    final LazyOptional<IFluidHandlerItem> providerOptional = itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                    providerOptional.ifPresent(provider -> provider.drain(1000, IFluidHandler.FluidAction.EXECUTE));
                }
                player.awardStat(Stats.FILL_CAULDRON);
                worldIn.setBlock(pos, state.setValue(StatueCauldronBlock.LEVEL, 3), 3);
                worldIn.updateNeighbourForOutputSignal(pos, this);
                worldIn.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @OnlyIn(Dist.CLIENT)
    private void openStatueScreen(final World worldIn, final BlockPos pos) {
        final Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new StatueCauldronScreen((ClientWorld) worldIn, pos));
    }

    @Nullable
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        final BlockState toPlace = this.defaultBlockState();
        final ItemStack stack = context.getItemInHand();
        if (stack.hasTag() && stack.getTag().contains("BlockEntityTag", 10)) {
            final int cauldronLevel = stack.getTagElement("BlockEntityTag").getInt("Level");
            return toPlace.setValue(StatueCauldronBlock.LEVEL, cauldronLevel);
        }
        return toPlace;
    }

    public void setPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        if (worldIn.isClientSide || !(placer instanceof PlayerEntity)) {
            return;
        }
        final PlayerEntity player = (PlayerEntity) placer;
        final TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof StatueCauldronTileEntity) {
            final StatueCauldronTileEntity cauldron = (StatueCauldronTileEntity) te;
            if (stack.getOrCreateTag().contains("BlockEntityTag")) {
                final CompoundNBT cauldronNbt = stack.getTagElement("BlockEntityTag");
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

    public void playerWillDestroy(final World world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!world.isClientSide) {
            final TileEntity tileEntity = world.getBlockEntity(pos);
            final ItemStack itemStack = new ItemStack(this.getBlock());
            if (tileEntity instanceof StatueCauldronTileEntity) {
                final StatueCauldronTileEntity cauldron = (StatueCauldronTileEntity) tileEntity;
                final CompoundNBT statueNBT = cauldron.serializeNBT();
                final CompoundNBT stackNBT = itemStack.getOrCreateTag();
                statueNBT.putInt("Level", state.getValue(StatueCauldronBlock.LEVEL));
                stackNBT.put("BlockEntityTag", statueNBT);
                itemStack.setTag(stackNBT);
            }
            final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
}
