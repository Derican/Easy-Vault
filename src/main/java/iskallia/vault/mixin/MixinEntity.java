package iskallia.vault.mixin;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
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


@Mixin({Entity.class})
public class MixinEntity {
    @Inject(method = {"baseTick"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")})
    public void baseTick(CallbackInfo ci) {
        Entity self = (Entity) this;

        if (self.level.isClientSide) {
            return;
        }

        if (self.getClass() == ItemEntity.class) {
            ItemEntity itemEntity = (ItemEntity) self;
            Item artifactItem = (Item) ForgeRegistries.ITEMS.getValue(ModBlocks.VAULT_ARTIFACT.getRegistryName());

            if (itemEntity.getItem().getItem() == artifactItem) {
                ServerWorld world = (ServerWorld) self.level;
                ItemEntity newItemEntity = new ItemEntity((World) world, self.getX(), self.getY(), self.getZ());
                newItemEntity.setItem(new ItemStack((IItemProvider) ModItems.ARTIFACT_FRAGMENT));

                spawnParticles((World) world, self.blockPosition());
                world.loadFromChunk((Entity) newItemEntity);
                itemEntity.remove();
            }
        }
    }

    @Shadow
    public World level;

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 20; i++) {
            double d0 = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;

            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.FLAME, pos
                    .getX() + world.random.nextDouble() - d0, pos
                    .getY() + world.random.nextDouble() - d1, pos
                    .getZ() + world.random.nextDouble() - d2, 10, d0, d1, d2, 0.5D);
        }


        world.playSound(null, pos, SoundEvents.GENERIC_BURN, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */