package iskallia.vault.item;

import iskallia.vault.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfiniteWaterBucketItem extends BucketItem {
    public InfiniteWaterBucketItem(final ResourceLocation id) {
        super(() -> Fluids.WATER, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.setRegistryName(id);
    }

    public Fluid getFluid() {
        return Fluids.WATER;
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);
        final BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);
        final ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, itemStack, rayTraceResult);
        if (ret != null) {
            return ret;
        }
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.pass(itemStack);
        }
        if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.pass(itemStack);
        }
        final BlockPos pos = rayTraceResult.getBlockPos();
        final Direction direction = rayTraceResult.getDirection();
        if (!world.mayInteract(player, pos) || !player.mayUseItemAt(pos, direction, itemStack)) {
            return ActionResult.fail(itemStack);
        }
        final BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.CAULDRON)) {
            final int cauldronLevel = state.getValue(CauldronBlock.LEVEL);
            if (cauldronLevel < 3) {
                player.awardStat(Stats.FILL_CAULDRON);
                world.setBlock(pos, state.setValue(CauldronBlock.LEVEL, 3), 3);
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
                world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return ActionResult.success(itemStack);
        }
        return super.use(world, player, hand);
    }

    public boolean isEnchantable(final ItemStack stack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return false;
    }

    public boolean isBookEnchantable(final ItemStack stack, final ItemStack book) {
        return false;
    }

    protected ItemStack getEmptySuccessItem(final ItemStack stack, final PlayerEntity player) {
        return stack;
    }

    public ItemStack getContainerItem(final ItemStack itemStack) {
        return new ItemStack(ModItems.INFINITE_WATER_BUCKET);
    }

    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
        return new InfiniteWaterBucketHandler(stack);
    }

    public static class InfiniteWaterBucketHandler implements IFluidHandlerItem, ICapabilityProvider {
        private final LazyOptional<IFluidHandlerItem> holder;
        protected ItemStack container;

        public InfiniteWaterBucketHandler(@Nonnull final ItemStack container) {
            this.holder = LazyOptional.of(() -> this);
            this.container = container;
        }

        @Nonnull
        public ItemStack getContainer() {
            return this.container;
        }

        public int getTanks() {
            return 1;
        }

        @Nonnull
        public FluidStack getFluidInTank(final int tank) {
            return new FluidStack(Fluids.WATER, 1000);
        }

        public int getTankCapacity(final int tank) {
            return 1000;
        }

        public boolean isFluidValid(final int tank, @Nonnull final FluidStack stack) {
            return false;
        }

        public int fill(final FluidStack resource, final IFluidHandler.FluidAction action) {
            return 0;
        }

        @Nonnull
        public FluidStack drain(final FluidStack resource, final IFluidHandler.FluidAction action) {
            if (resource.isEmpty() || resource.getFluid() != Fluids.WATER) {
                return FluidStack.EMPTY;
            }
            return new FluidStack(Fluids.WATER, resource.getAmount());
        }

        @Nonnull
        public FluidStack drain(final int maxDrain, final IFluidHandler.FluidAction action) {
            return new FluidStack(Fluids.WATER, maxDrain);
        }

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
            return (LazyOptional<T>) CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty((Capability) cap, this.holder);
        }
    }
}
