package iskallia.vault.network.message;

import iskallia.vault.container.AdvancedVendingContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AdvancedVendingUIMessage {
    public Opcode opcode;
    public CompoundNBT payload;

    public enum Opcode {
        SELECT_TRADE,
        EJECT_CORE;
    }


    public static void encode(AdvancedVendingUIMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeNbt(message.payload);
    }

    public static AdvancedVendingUIMessage decode(PacketBuffer buffer) {
        AdvancedVendingUIMessage message = new AdvancedVendingUIMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }

    public static void handle(AdvancedVendingUIMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.SELECT_TRADE) {
                int index = message.payload.getInt("Index");

                ServerPlayerEntity sender = context.getSender();
                Container openContainer = sender.containerMenu;
                if (openContainer instanceof AdvancedVendingContainer) {
                    AdvancedVendingContainer vendingMachineContainer = (AdvancedVendingContainer) openContainer;
                    vendingMachineContainer.selectTrade(index);
                }
            } else if (message.opcode == Opcode.EJECT_CORE) {
                int index = message.payload.getInt("Index");
                ServerPlayerEntity sender = context.getSender();
                Container openContainer = sender.containerMenu;
                if (openContainer instanceof AdvancedVendingContainer) {
                    AdvancedVendingContainer vendingMachineContainer = (AdvancedVendingContainer) openContainer;
                    vendingMachineContainer.ejectCore(index);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static AdvancedVendingUIMessage selectTrade(int index) {
        AdvancedVendingUIMessage message = new AdvancedVendingUIMessage();
        message.opcode = Opcode.SELECT_TRADE;
        message.payload = new CompoundNBT();
        message.payload.putInt("Index", index);
        return message;
    }

    public static AdvancedVendingUIMessage ejectTrade(int index) {
        AdvancedVendingUIMessage message = new AdvancedVendingUIMessage();
        message.opcode = Opcode.EJECT_CORE;
        message.payload = new CompoundNBT();
        message.payload.putInt("Index", index);
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AdvancedVendingUIMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */