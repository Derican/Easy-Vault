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

public class VoidFluidBlock
        extends FlowingFluidBlock {
    public VoidFluidBlock(Supplier<? extends VoidFluid> supplier, AbstractBlock.Properties properties) {
        super(supplier, properties);
    }


    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);

        entity.clearFire();

        if (!world.isClientSide && entity instanceof net.minecraft.entity.player.PlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            affectPlayer(player);
        } else if (entity instanceof ItemEntity && state.getFluidState().isSource()) {
            ItemEntity itemEntity = (ItemEntity) entity;
            ItemStack itemStack = itemEntity.getItem();
            Item item = itemStack.getItem();
            if (item instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) item;
                if (blockItem.getBlock() instanceof VaultOreBlock) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    transformOre(itemEntity, (VaultOreBlock) blockItem.getBlock());
                }
            }
        }
    }


    public static void affectPlayer(ServerPlayerEntity player) {
        if (!player.hasEffect(ModEffects.TIMER_ACCELERATION) || player
                .getEffect(ModEffects.TIMER_ACCELERATION).getDuration() < 40) {
            int duration = 100;
            int amplifier = 1;
            EffectInstance acceleration = new EffectInstance(ModEffects.TIMER_ACCELERATION, duration, amplifier);
            EffectInstance blindness = new EffectInstance(Effects.BLINDNESS, duration, amplifier);
            player.addEffect(acceleration);
            player.addEffect(blindness);
        }
    }

    public static void transformOre(ItemEntity itemEntity, VaultOreBlock oreBlock) {
        World world = itemEntity.level;
        BlockPos pos = itemEntity.blockPosition();

        ItemStack itemStack = itemEntity.getItem();
        itemStack.shrink(1);

        if (itemStack.getCount() <= 0) {
            itemEntity.remove();
        }

        if (!world.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.playSound(null, pos
                            .getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.MASTER, 1.0F,

                    (float) Math.random());
            serverWorld.sendParticles((IParticleData) ParticleTypes.WITCH, pos
                    .getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 100, 0.0D, 0.0D, 0.0D, Math.PI);
        }


        ItemEntity gemEntity = createGemEntity(world, oreBlock, pos);
        world.addFreshEntity((Entity) gemEntity);
    }

    @Nonnull
    private static ItemEntity createGemEntity(World world, VaultOreBlock oreBlock, BlockPos pos) {
        double x = (pos.getX() + 0.5F);
        double y = (pos.getY() + 0.5F);
        double z = (pos.getZ() + 0.5F);
        ItemStack itemStack = new ItemStack((IItemProvider) oreBlock.getAssociatedGem(), 2);
        ItemEntity itemEntity = new ItemEntity(world, x, y, z, itemStack);

        itemEntity.setPickUpDelay(40);


        float mag = world.random.nextFloat() * 0.2F;
        float angle = world.random.nextFloat() * 6.2831855F;
        itemEntity.setDeltaMovement((-MathHelper.sin(angle) * mag), 0.20000000298023224D, (MathHelper.cos(angle) * mag));

        return itemEntity;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\fluid\block\VoidFluidBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */