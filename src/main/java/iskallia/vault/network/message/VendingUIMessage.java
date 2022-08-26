package iskallia.vault.network.message;

import iskallia.vault.container.VendingMachineContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class VendingUIMessage {
    public Opcode opcode;
    public CompoundNBT payload;

    public static void encode(final VendingUIMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeNbt(message.payload);
    }

    public static VendingUIMessage decode(final PacketBuffer buffer) {
        final VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }

    public static void handle(final VendingUIMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.SELECT_TRADE) {
                final int index = message.payload.getInt("Index");
                final ServerPlayerEntity sender = context.getSender();
                final Container openContainer = sender.containerMenu;
                if (openContainer instanceof VendingMachineContainer) {
                    final VendingMachineContainer vendingMachineContainer = (VendingMachineContainer) openContainer;
                    vendingMachineContainer.selectTrade(index);
                }
            } else if (message.opcode == Opcode.EJECT_CORE) {
                final int index2 = message.payload.getInt("Index");
                final ServerPlayerEntity sender2 = context.getSender();
                final Container openContainer2 = sender2.containerMenu;
                if (openContainer2 instanceof VendingMachineContainer) {
                    final VendingMachineContainer vendingMachineContainer2 = (VendingMachineContainer) openContainer2;
                    vendingMachineContainer2.ejectCore(index2);
                }
            }
            return;
        });
        context.setPacketHandled(true);
    }

    public static VendingUIMessage selectTrade(final int index) {
        final VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.SELECT_TRADE;
        (message.payload = new CompoundNBT()).putInt("Index", index);
        return message;
    }

    public static VendingUIMessage ejectTrade(final int index) {
        final VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.EJECT_CORE;
        (message.payload = new CompoundNBT()).putInt("Index", index);
        return message;
    }

    public enum Opcode {
        SELECT_TRADE,
        EJECT_CORE
    }
}
