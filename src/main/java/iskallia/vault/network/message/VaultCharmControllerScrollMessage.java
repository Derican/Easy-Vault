package iskallia.vault.network.message;

import iskallia.vault.container.VaultCharmControllerContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class VaultCharmControllerScrollMessage {
    public float scroll;

    public VaultCharmControllerScrollMessage(final float scroll) {
        this.scroll = scroll;
    }

    public static void encode(final VaultCharmControllerScrollMessage message, final PacketBuffer buffer) {
        buffer.writeFloat(message.scroll);
    }

    public static VaultCharmControllerScrollMessage decode(final PacketBuffer buffer) {
        return new VaultCharmControllerScrollMessage(buffer.readFloat());
    }

    public static void handle(final VaultCharmControllerScrollMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            } else {
                if (sender.containerMenu instanceof VaultCharmControllerContainer) {
                    ((VaultCharmControllerContainer) sender.containerMenu).scrollTo(message.scroll);
                }
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
