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

    public VaultModifierMessage(final VaultRaid vault, final VaultPlayer player) {
        this.playerModifiers = player.getModifiers();
        this.globalModifiers = vault.getModifiers();
    }

    public VaultModifiers getPlayerModifiers() {
        return this.playerModifiers;
    }

    public VaultModifiers getGlobalModifiers() {
        return this.globalModifiers;
    }

    public static void encode(final VaultModifierMessage message, final PacketBuffer buffer) {
        message.getPlayerModifiers().encode(buffer);
        message.getGlobalModifiers().encode(buffer);
    }

    public static VaultModifierMessage decode(final PacketBuffer buffer) {
        final VaultModifierMessage message = new VaultModifierMessage();
        message.playerModifiers = VaultModifiers.decode(buffer);
        message.globalModifiers = VaultModifiers.decode(buffer);
        return message;
    }

    public static void handle(final VaultModifierMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveModifierUpdate(message));
        context.setPacketHandled(true);
    }
}
