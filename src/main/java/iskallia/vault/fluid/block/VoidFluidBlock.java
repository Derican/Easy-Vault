package iskallia.vault.fluid.block;

import iskallia.vault.block.VaultOreBlock;
import iskallia.vault.fluid.VoidFluid;
import iskallia.vault.init.ModEffects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class VoidFluidBlock extends FlowingFluidBlock {
    public VoidFluidBlock(final Supplier<? extends VoidFluid> supplier, final AbstractBlock.Properties properties) {
        super((Supplier) supplier, properties);
    }

    public void entityInside(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        super.entityInside(state, world, pos, entity);
        entity.clearFire();
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            affectPlayer(player);
        } else if (entity instanceof ItemEntity && state.getFluidState().isSource()) {
            final ItemEntity itemEntity = (ItemEntity) entity;
            final ItemStack itemStack = itemEntity.getItem();
            final Item item = itemStack.getItem();
            if (item instanceof BlockItem) {
                final BlockItem blockItem = (BlockItem) item;
                if (blockItem.getBlock() instanceof VaultOreBlock) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    transformOre(itemEntity, (VaultOreBlock) blockItem.getBlock());
                }
            }
        }
    }

    public static void affectPlayer(final ServerPlayerEntity player) {
        if (!player.hasEffect(ModEffects.TIMER_ACCELERATION) || player.getEffect(ModEffects.TIMER_ACCELERATION).getDuration() < 40) {
            final int duration = 100;
            final int amplifier = 1;
            final EffectInstance acceleration = new EffectInstance(ModEffects.TIMER_ACCELERATION, duration, amplifier);
            final EffectInstance blindness = new EffectInstance(Effects.BLINDNESS, duration, amplifier);
            player.addEffect(acceleration);
            player.addEffect(blindness);
        }
    }

    public static void transformOre(final ItemEntity itemEntity, final VaultOreBlock oreBlock) {
        final World world = itemEntity.level;
        final BlockPos pos = itemEntity.blockPosition();
        final ItemStack itemStack = itemEntity.getItem();
        itemStack.shrink(1);
        if (itemStack.getCount() <= 0) {
            itemEntity.remove();
        }
        if (!world.isClientSide) {
            final ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.playSound((PlayerEntity) null, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.MASTER, 1.0f, (float) Math.random());
            serverWorld.sendParticles((IParticleData) ParticleTypes.WITCH, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 100, 0.0, 0.0, 0.0, 3.141592653589793);
        }
        final ItemEntity gemEntity = createGemEntity(world, oreBlock, pos);
        world.addFreshEntity((Entity) gemEntity);
    }

    @Nonnull
    private static ItemEntity createGemEntity(final World world, final VaultOreBlock oreBlock, final BlockPos pos) {
        final double x = pos.getX() + 0.5f;
        final double y = pos.getY() + 0.5f;
        final double z = pos.getZ() + 0.5f;
        final ItemStack itemStack = new ItemStack((IItemProvider) oreBlock.getAssociatedGem(), 2);
        final ItemEntity itemEntity = new ItemEntity(world, x, y, z, itemStack);
        itemEntity.setPickUpDelay(40);
        final float mag = world.random.nextFloat() * 0.2f;
        final float angle = world.random.nextFloat() * 6.2831855f;
        itemEntity.setDeltaMovement((double) (-MathHelper.sin(angle) * mag), 0.20000000298023224, (double) (MathHelper.cos(angle) * mag));
        return itemEntity;
    }
}
