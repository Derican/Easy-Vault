// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientSandEventData;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class SandEventContributorMessage
{
    private final ITextComponent contributor;
    
    public SandEventContributorMessage(final ITextComponent contributor) {
        this.contributor = contributor;
    }
    
    public static void encode(final SandEventContributorMessage message, final PacketBuffer buffer) {
        buffer.writeComponent(message.contributor);
    }
    
    public static SandEventContributorMessage decode(final PacketBuffer buffer) {
        return new SandEventContributorMessage(buffer.readComponent());
    }
    
    public static void handle(final SandEventContributorMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientSandEventData.getInstance().addContributor(message.contributor));
        context.setPacketHandled(true);
    }
}
