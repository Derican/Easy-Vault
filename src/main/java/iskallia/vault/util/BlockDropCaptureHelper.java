package iskallia.vault.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


@EventBusSubscriber
public class BlockDropCaptureHelper {
    private static final Stack<List<ItemEntity>> capturing = new Stack<>();


    @SubscribeEvent
    public static void onDrop(EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof net.minecraft.world.server.ServerWorld && event.getEntity() instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity) event.getEntity()).getItem();
            if (!capturing.isEmpty()) {
                event.setCanceled(true);

                if (!itemStack.isEmpty() &&
                        !capturing.isEmpty()) {
                    ((List<ItemEntity>) capturing.peek()).add((ItemEntity) event.getEntity());
                }

                event.getEntity().remove();
            }
        }
    }

    public static void startCapturing() {
        capturing.push(new ArrayList<>());
    }

    public static List<ItemEntity> getCapturedStacksAndStop() {
        return capturing.pop();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\BlockDropCaptureHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */