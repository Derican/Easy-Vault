// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.client.ClientVaultRaidData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BossMusicMessage
{
    private final boolean state;
    
    public BossMusicMessage(final boolean state) {
        this.state = state;
    }
    
    public static void encode(final BossMusicMessage message, final PacketBuffer buffer) {
        buffer.writeBoolean(message.state);
    }
    
    public static BossMusicMessage decode(final PacketBuffer buffer) {
        return new BossMusicMessage(buffer.readBoolean());
    }
    
    public boolean isInFight() {
        return this.state;
    }
    
    public static void handle(final BossMusicMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveBossUpdate(message));
        context.setPacketHandled(true);
    }
}
