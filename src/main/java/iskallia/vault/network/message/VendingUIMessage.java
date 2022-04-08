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

    public enum Opcode {
        SELECT_TRADE,
        EJECT_CORE;
    }


    public static void encode(VendingUIMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeNbt(message.payload);
    }

    public static VendingUIMessage decode(PacketBuffer buffer) {
        VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }

    public static void handle(VendingUIMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.SELECT_TRADE) {
                int index = message.payload.getInt("Index");
                ServerPlayerEntity sender = context.getSender();
                Container openContainer = sender.containerMenu;
                if (openContainer instanceof VendingMachineContainer) {
                    VendingMachineContainer vendingMachineContainer = (VendingMachineContainer) openContainer;
                    vendingMachineContainer.selectTrade(index);
                }
            } else if (message.opcode == Opcode.EJECT_CORE) {
                int index = message.payload.getInt("Index");
                ServerPlayerEntity sender = context.getSender();
                Container openContainer = sender.containerMenu;
                if (openContainer instanceof VendingMachineContainer) {
                    VendingMachineContainer vendingMachineContainer = (VendingMachineContainer) openContainer;
                    vendingMachineContainer.ejectCore(index);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static VendingUIMessage selectTrade(int index) {
        VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.SELECT_TRADE;
        message.payload = new CompoundNBT();
        message.payload.putInt("Index", index);
        return message;
    }

    public static VendingUIMessage ejectTrade(int index) {
        VendingUIMessage message = new VendingUIMessage();
        message.opcode = Opcode.EJECT_CORE;
        message.payload = new CompoundNBT();
        message.payload.putInt("Index", index);
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\VendingUIMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */