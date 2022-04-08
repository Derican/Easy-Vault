package iskallia.vault.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncOversizedStackMessage {
    private int windowId = 0;
    private int slot = 0;
    private ItemStack stack;

    public SyncOversizedStackMessage() {
        this.stack = ItemStack.EMPTY;
    }

    public SyncOversizedStackMessage(int windowId, int slot, ItemStack stack) {
        this.windowId = windowId;
        this.slot = slot;
        this.stack = stack.copy();
    }

    public SyncOversizedStackMessage(PacketBuffer buf) {
        this.windowId = buf.readInt();
        this.slot = buf.readInt();
        this.stack = buf.readItem();
        this.stack.setCount(buf.readInt());
    }

    public static void encode(SyncOversizedStackMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.windowId);
        buffer.writeInt(message.slot);
        buffer.writeItem(message.stack);
        buffer.writeInt(message.stack.getCount());
    }

    public static SyncOversizedStackMessage decode(PacketBuffer buffer) {
        return new SyncOversizedStackMessage(buffer);
    }

    public static void handle(SyncOversizedStackMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> setClientStack(message));


        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setClientStack(SyncOversizedStackMessage message) {
        ClientPlayerEntity clientPlayerEntity = (Minecraft.getInstance()).player;
        if (clientPlayerEntity == null) {
            return;
        }
        if (((PlayerEntity) clientPlayerEntity).containerMenu instanceof iskallia.vault.container.inventory.ShardPouchContainer && message.windowId == ((PlayerEntity) clientPlayerEntity).containerMenu.containerId)
            ((Slot) ((PlayerEntity) clientPlayerEntity).containerMenu.slots.get(message.slot)).set(message.stack);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\SyncOversizedStackMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */