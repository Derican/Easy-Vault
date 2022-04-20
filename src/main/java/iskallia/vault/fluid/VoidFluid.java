package iskallia.vault.fluid;

import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModFluids;
import iskallia.vault.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;

import javax.annotation.Nonnull;

public abstract class VoidFluid extends FlowingFluid {
    @Nonnull
    public Fluid getFlowing() {
        return (Fluid) ModFluids.FLOWING_VOID_LIQUID.get();
    }

    @Nonnull
    public Fluid getSource() {
        return (Fluid) ModFluids.VOID_LIQUID.get();
    }

    @Nonnull
    public Item getBucket() {
        return (Item) ModItems.VOID_LIQUID_BUCKET;
    }

    protected boolean canConvertToSource() {
        return false;
    }

    protected void beforeDestroyingBlock(@Nonnull final IWorld world, @Nonnull final BlockPos pos, @Nonnull final BlockState state) {
    }

    protected int getSlopeFindDistance(@Nonnull final IWorldReader world) {
        return 2;
    }

    protected int getDropOff(@Nonnull final IWorldReader world) {
        return 2;
    }

    protected boolean canBeReplacedWith(final FluidState fluidState, @Nonnull final IBlockReader blockReader, @Nonnull final BlockPos pos, @Nonnull final Fluid fluid, @Nonnull final Direction direction) {
        return fluidState.getHeight(blockReader, pos) >= 0.44444445f;
    }

    public int getTickDelay(@Nonnull final IWorldReader world) {
        return 30;
    }

    protected float getExplosionResistance() {
        return 100.0f;
    }

    public int getSpreadDelay(@Nonnull final World world, final BlockPos pos, final FluidState p_215667_3_, final FluidState p_215667_4_) {
        int i = this.getTickDelay((IWorldReader) world);
        if (!p_215667_3_.isEmpty() && !p_215667_4_.isEmpty() && !(boolean) p_215667_3_.getValue(VoidFluid.FALLING) && !(boolean) p_215667_4_.getValue(VoidFluid.FALLING) && p_215667_4_.getHeight((IBlockReader) world, pos) > p_215667_3_.getHeight((IBlockReader) world, pos) && world.getRandom().nextInt(4) != 0) {
            i *= 4;
        }
        return i;
    }

    public boolean isSame(final Fluid fluid) {
        return fluid == ModFluids.VOID_LIQUID.get() || fluid == ModFluids.FLOWING_VOID_LIQUID.get();
    }

    @Nonnull
    protected BlockState createLegacyBlock(@Nonnull final FluidState state) {
        return ModBlocks.VOID_LIQUID_BLOCK.defaultBlockState().setValue(FlowingFluidBlock.LEVEL, getLegacyLevel(state));
    }

    @Nonnull
    protected FluidAttributes createAttributes() {
        return FluidAttributes.Water.builder(Vault.id("block/fluid/void_liquid"), Vault.id("block/fluid/flowing_void_liquid")).overlay(new ResourceLocation("block/water_overlay")).translationKey("block.the_vault.void_liquid").density(3000).viscosity(6000).temperature(1300).color(16777215).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY).build((Fluid) this);
    }

    public static class Flowing extends VoidFluid {
        protected void createFluidStateDefinition(final StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition((StateContainer.Builder) builder);
            builder.add(new Property[]{Flowing.LEVEL});
        }

        public int getAmount(final FluidState state) {
            return (int) state.getValue(Flowing.LEVEL);
        }

        public boolean isSource(final FluidState state) {
            return false;
        }
    }

    public static class Source extends VoidFluid {
        public int getAmount(final FluidState state) {
            return 8;
        }

        public boolean isSource(final FluidState state) {
            return true;
        }
    }
}
