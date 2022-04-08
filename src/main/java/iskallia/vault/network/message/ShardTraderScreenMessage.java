package iskallia.vault.network.message;

import iskallia.vault.Vault;
import iskallia.vault.world.data.SoulShardTraderData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class ShardTraderScreenMessage {
    public static void encode(ShardTraderScreenMessage message, PacketBuffer buffer) {
    }

    public static ShardTraderScreenMessage decode(PacketBuffer buffer) {
        return new ShardTraderScreenMessage();
    }

    public static void handle(ShardTraderScreenMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerWorld sWorld = context.getSender().getLevel();
            RegistryKey<World> dimKey = sWorld.dimension();
            if (dimKey == Vault.VAULT_KEY) {
                return;
            }
            SoulShardTraderData.get(sWorld).openTradeContainer(context.getSender());
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\ShardTraderScreenMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */