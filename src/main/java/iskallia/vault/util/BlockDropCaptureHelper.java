// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Mod.EventBusSubscriber
public class BlockDropCaptureHelper
{
    private static final Stack<List<ItemEntity>> capturing;
    
    private BlockDropCaptureHelper() {
    }
    
    @SubscribeEvent
    public static void onDrop(final EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerWorld && event.getEntity() instanceof ItemEntity) {
            final ItemStack itemStack = ((ItemEntity)event.getEntity()).getItem();
            if (!BlockDropCaptureHelper.capturing.isEmpty()) {
                event.setCanceled(true);
                if (!itemStack.isEmpty() && !BlockDropCaptureHelper.capturing.isEmpty()) {
                    BlockDropCaptureHelper.capturing.peek().add((ItemEntity)event.getEntity());
                }
                event.getEntity().remove();
            }
        }
    }
    
    public static void startCapturing() {
        BlockDropCaptureHelper.capturing.push(new ArrayList<ItemEntity>());
    }
    
    public static List<ItemEntity> getCapturedStacksAndStop() {
        return BlockDropCaptureHelper.capturing.pop();
    }
    
    static {
        capturing = new Stack<List<ItemEntity>>();
    }
}
