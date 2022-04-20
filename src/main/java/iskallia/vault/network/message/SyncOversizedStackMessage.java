package iskallia.vault.network.message;

import iskallia.vault.container.inventory.ShardPouchContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncOversizedStackMessage {
    private int windowId;
    private int slot;
    private ItemStack stack;

    public SyncOversizedStackMessage() {
        this.windowId = 0;
        this.slot = 0;
        this.stack = ItemStack.EMPTY;
    }

    public SyncOversizedStackMessage(final int windowId, final int slot, final ItemStack stack) {
        this.windowId = 0;
        this.slot = 0;
        this.windowId = windowId;
        this.slot = slot;
        this.stack = stack.copy();
    }

    public SyncOversizedStackMessage(final PacketBuffer buf) {
        this.windowId = 0;
        this.slot = 0;
        this.windowId = buf.readInt();
        this.slot = buf.readInt();
        (this.stack = buf.readItem()).setCount(buf.readInt());
    }

    public static void encode(final SyncOversizedStackMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.windowId);
        buffer.writeInt(message.slot);
        buffer.writeItem(message.stack);
        buffer.writeInt(message.stack.getCount());
    }

    public static SyncOversizedStackMessage decode(final PacketBuffer buffer) {
        return new SyncOversizedStackMessage(buffer);
    }

    public static void handle(final SyncOversizedStackMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> setClientStack(message));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void setClientStack(final SyncOversizedStackMessage message) {
        final PlayerEntity player = (PlayerEntity) Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.containerMenu instanceof ShardPouchContainer && message.windowId == player.containerMenu.containerId) {
            player.containerMenu.slots.get(message.slot).set(message.stack);
        }
    }
}
