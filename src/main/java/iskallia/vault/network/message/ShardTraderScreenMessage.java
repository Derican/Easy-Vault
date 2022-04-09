// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.Vault;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ShardTraderScreenMessage
{
    public static void encode(final ShardTraderScreenMessage message, final PacketBuffer buffer) {
    }
    
    public static ShardTraderScreenMessage decode(final PacketBuffer buffer) {
        return new ShardTraderScreenMessage();
    }
    
    public static void handle(final ShardTraderScreenMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerWorld sWorld = context.getSender().getLevel();
            final RegistryKey<World> dimKey = (RegistryKey<World>)sWorld.dimension();
            if (dimKey == Vault.VAULT_KEY) {
                return;
            }
            else {
                SoulShardTraderData.get(sWorld).openTradeContainer(context.getSender());
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
