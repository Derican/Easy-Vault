package iskallia.vault.network.message;

import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class VaultModifierMessage {
    private VaultModifiers playerModifiers;
    private VaultModifiers globalModifiers;

    public VaultModifierMessage() {
    }

    public VaultModifierMessage(VaultRaid vault, VaultPlayer player) {
        this.playerModifiers = player.getModifiers();
        this.globalModifiers = vault.getModifiers();
    }

    public VaultModifiers getPlayerModifiers() {
        return this.playerModifiers;
    }

    public VaultModifiers getGlobalModifiers() {
        return this.globalModifiers;
    }

    public static void encode(VaultModifierMessage message, PacketBuffer buffer) {
        message.getPlayerModifiers().encode(buffer);
        message.getGlobalModifiers().encode(buffer);
    }

    public static VaultModifierMessage decode(PacketBuffer buffer) {
        VaultModifierMessage message = new VaultModifierMessage();
        message.playerModifiers = VaultModifiers.decode(buffer);
        message.globalModifiers = VaultModifiers.decode(buffer);
        return message;
    }

    public static void handle(VaultModifierMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveModifierUpdate(message));
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\VaultModifierMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */