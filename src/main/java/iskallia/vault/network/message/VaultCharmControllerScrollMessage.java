package iskallia.vault.network.message;

import iskallia.vault.container.VaultCharmControllerContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class VaultCharmControllerScrollMessage {
    public float scroll;

    public VaultCharmControllerScrollMessage(float scroll) {
        this.scroll = scroll;
    }

    public static void encode(VaultCharmControllerScrollMessage message, PacketBuffer buffer) {
        buffer.writeFloat(message.scroll);
    }

    public static VaultCharmControllerScrollMessage decode(PacketBuffer buffer) {
        return new VaultCharmControllerScrollMessage(buffer.readFloat());
    }

    public static void handle(VaultCharmControllerScrollMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }
            if (sender.containerMenu instanceof VaultCharmControllerContainer) {
                ((VaultCharmControllerContainer) sender.containerMenu).scrollTo(message.scroll);
            }
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\VaultCharmControllerScrollMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */