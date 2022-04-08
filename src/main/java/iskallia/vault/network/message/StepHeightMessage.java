package iskallia.vault.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class StepHeightMessage {
    private final float stepHeight;

    public StepHeightMessage(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public static void encode(StepHeightMessage message, PacketBuffer buffer) {
        buffer.writeFloat(message.stepHeight);
    }

    public static StepHeightMessage decode(PacketBuffer buffer) {
        return new StepHeightMessage(buffer.readFloat());
    }

    public static void handle(StepHeightMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            if ((Minecraft.getInstance()).player != null) {
                (Minecraft.getInstance()).player.maxUpStep = message.stepHeight;
            }
        });

        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\StepHeightMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */