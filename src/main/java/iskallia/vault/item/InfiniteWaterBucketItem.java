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
import net.minecraft.state.Property;
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

public class InfiniteWaterBucketItem
        extends BucketItem {
    public InfiniteWaterBucketItem(ResourceLocation id) {
        super(() -> Fluids.WATER, (new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));

        setRegistryName(id);
    }


    public Fluid getFluid() {
        return (Fluid) Fluids.WATER;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, itemStack, (RayTraceResult) rayTraceResult);
        if (ret != null) {
            return ret;
        }
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS)
            return ActionResult.pass(itemStack);
        if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.pass(itemStack);
        }
        BlockPos pos = rayTraceResult.getBlockPos();
        Direction direction = rayTraceResult.getDirection();
        if (world.mayInteract(player, pos) && player.mayUseItemAt(pos, direction, itemStack)) {
            BlockState state = world.getBlockState(pos);
            if (state.is(Blocks.CAULDRON)) {
                int cauldronLevel = ((Integer) state.getValue((Property) CauldronBlock.LEVEL)).intValue();
                if (cauldronLevel < 3) {
                    player.awardStat(Stats.FILL_CAULDRON);
                    world.setBlock(pos, (BlockState) state.setValue((Property) CauldronBlock.LEVEL, Integer.valueOf(3)), 3);
                    world.updateNeighbourForOutputSignal(pos, state.getBlock());
                    world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.success(itemStack);
            }
            return super.use(world, player, hand);
        }
        return ActionResult.fail(itemStack);
    }


    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }


    protected ItemStack getEmptySuccessItem(ItemStack stack, PlayerEntity player) {
        return stack;
    }


    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack((IItemProvider) ModItems.INFINITE_WATER_BUCKET);
    }


    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new InfiniteWaterBucketHandler(stack);
    }

    public static class InfiniteWaterBucketHandler
            implements IFluidHandlerItem, ICapabilityProvider {
        private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

        protected ItemStack container;

        public InfiniteWaterBucketHandler(@Nonnull ItemStack container) {
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
        public FluidStack getFluidInTank(int tank) {
            return new FluidStack((Fluid) Fluids.WATER, 1000);
        }


        public int getTankCapacity(int tank) {
            return 1000;
        }


        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return false;
        }


        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            return 0;
        }


        @Nonnull
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            if (resource.isEmpty() || resource.getFluid() != Fluids.WATER) {
                return FluidStack.EMPTY;
            }
            return new FluidStack((Fluid) Fluids.WATER, resource.getAmount());
        }


        @Nonnull
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            return new FluidStack((Fluid) Fluids.WATER, maxDrain);
        }


        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(cap, this.holder);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\InfiniteWaterBucketItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */