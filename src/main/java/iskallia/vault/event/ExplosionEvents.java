// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.block.BlockState;
import iskallia.vault.util.flag.ExplosionImmune;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExplosionEvents
{
    @SubscribeEvent
    static void preventBlocksFromExploding(final ExplosionEvent.Detonate event) {
        if (!event.getWorld().isClientSide) {
            final ServerWorld world = (ServerWorld)event.getWorld();
            event.getAffectedBlocks().removeIf(blockPos -> {
                final BlockState blockState = world.getBlockState(blockPos);
                return blockState != null && blockState.getBlock() instanceof ExplosionImmune;
            });
        }
    }
    
    @SubscribeEvent
    static void preventItemsFromExploding(final ExplosionEvent.Detonate event) {
        if (!event.getWorld().isClientSide) {
            event.getAffectedEntities().removeIf(entity -> {
                if (!(entity instanceof ItemEntity)) {
                    return false;
                }
                else {
                    final Item item = ((ItemEntity)entity).getItem().getItem();
                    return item instanceof ExplosionImmune;
                }
            });
        }
    }
}
