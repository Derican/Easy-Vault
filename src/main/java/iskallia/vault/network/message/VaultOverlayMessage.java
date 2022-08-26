package iskallia.vault.network.message;

import iskallia.vault.client.ClientVaultRaidData;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

public class VaultOverlayMessage {
    private OverlayType type;
    private int remainingTicks;
    private boolean earlyKill;
    private boolean showTimer;

    protected VaultOverlayMessage() {
    }

    protected VaultOverlayMessage(final int remainingTicks, final boolean earlyKill, final OverlayType type, final boolean showTimer) {
        this.remainingTicks = remainingTicks;
        this.earlyKill = earlyKill;
        this.type = type;
        this.showTimer = showTimer;
    }

    public static VaultOverlayMessage forArena(final int ticks) {
        return new VaultOverlayMessage(ticks, false, OverlayType.ARENA, true);
    }

    public static VaultOverlayMessage forVault(final int ticks, final boolean earlyKill, final boolean showTimer) {
        return new VaultOverlayMessage(ticks, earlyKill, OverlayType.VAULT, showTimer);
    }

    public static VaultOverlayMessage hide() {
        return new VaultOverlayMessage(0, false, OverlayType.NONE, true);
    }

    public static void encode(final VaultOverlayMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.remainingTicks);
        buffer.writeEnum(message.type);
        buffer.writeBoolean(message.earlyKill);
        buffer.writeBoolean(message.showTimer);
    }

    public static VaultOverlayMessage decode(final PacketBuffer buffer) {
        final VaultOverlayMessage message = new VaultOverlayMessage();
        message.remainingTicks = buffer.readInt();
        message.type = (OverlayType) buffer.readEnum((Class) OverlayType.class);
        message.earlyKill = buffer.readBoolean();
        message.showTimer = buffer.readBoolean();
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

    public boolean showTimer() {
        return this.showTimer;
    }

    public static void handle(final VaultOverlayMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientVaultRaidData.receiveOverlayUpdate(message));
        context.setPacketHandled(true);
    }

    public enum OverlayType {
        VAULT,
        ARENA,
        NONE
    }
}
