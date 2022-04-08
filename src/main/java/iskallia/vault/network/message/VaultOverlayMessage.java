package iskallia.vault.network.message;

import iskallia.vault.client.ClientVaultRaidData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class VaultOverlayMessage {
    private OverlayType type;
    private int remainingTicks;
    private boolean earlyKill;

    protected VaultOverlayMessage() {
    }

    protected VaultOverlayMessage(int remainingTicks, boolean earlyKill, OverlayType type) {
        this.remainingTicks = remainingTicks;
        this.earlyKill = earlyKill;
        this.type = type;
    }

    public static VaultOverlayMessage forArena(int ticks) {
        return new VaultOverlayMessage(ticks, false, OverlayType.ARENA);
    }

    public static VaultOverlayMessage forVault(int ticks, boolean earlyKill) {
        return new VaultOverlayMessage(ticks, earlyKill, OverlayType.VAULT);
    }

    public static VaultOverlayMessage hide() {
        return new VaultOverlayMessage(0, false, OverlayType.NONE);
    }

    public static void encode(VaultOverlayMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.remainingTicks);
        buffer.writeEnum(message.type);
        buffer.writeBoolean(message.earlyKill);
    }

    public static VaultOverlayMessage decode(PacketBuffer buffer) {
        VaultOverlayMessage message = new VaultOverlayMessage();
        message.remainingTicks = buffer.readInt();
        message.type = (OverlayType) buffer.readEnum(OverlayType.class);
        message.earlyKill = buffer.readBoolean();
        return message;
    }

    public int getRemainingTicks() {
        return this.remainingTicks;
    }

    public boolean canGetRecordTime() {
        return this.earlyKill;
    }

    public OverlayType getOverlayType() {
        return this.type;
    }

    public static void handle(VaultOverlayMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveOverlayUpdate(message));
        context.setPacketHandled(true);
    }

    public enum OverlayType {
        VAULT,
        ARENA,
        NONE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\VaultOverlayMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */