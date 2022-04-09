// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Entity.class })
public class MixinEntity
{
    @Shadow
    public World level;
    
    @Inject(method = { "baseTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hurt(Lnet/minecraft/util/DamageSource;F)Z") })
    public void baseTick(final CallbackInfo ci) {
        final Entity self = (Entity)this;
        if (self.level.isClientSide) {
            return;
        }
        if (self.getClass() == ItemEntity.class) {
            final ItemEntity itemEntity = (ItemEntity)self;
            final Item artifactItem = (Item)ForgeRegistries.ITEMS.getValue(ModBlocks.VAULT_ARTIFACT.getRegistryName());
            if (itemEntity.getItem().getItem() == artifactItem) {
                final ServerWorld world = (ServerWorld)self.level;
                final ItemEntity newItemEntity = new ItemEntity((World)world, self.getX(), self.getY(), self.getZ());
                newItemEntity.setItem(new ItemStack((IItemProvider)ModItems.ARTIFACT_FRAGMENT));
                this.spawnParticles((World)world, self.blockPosition());
                world.loadFromChunk((Entity)newItemEntity);
                itemEntity.remove();
            }
        }
    }
    
    private void spawnParticles(final World world, final BlockPos pos) {
        for (int i = 0; i < 20; ++i) {
            final double d0 = world.random.nextGaussian() * 0.02;
            final double d2 = world.random.nextGaussian() * 0.02;
            final double d3 = world.random.nextGaussian() * 0.02;
            ((ServerWorld)world).sendParticles((IParticleData)ParticleTypes.FLAME, pos.getX() + world.random.nextDouble() - d0, pos.getY() + world.random.nextDouble() - d2, pos.getZ() + world.random.nextDouble() - d3, 10, d0, d2, d3, 0.5);
        }
        world.playSound((PlayerEntity)null, pos, SoundEvents.GENERIC_BURN, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
