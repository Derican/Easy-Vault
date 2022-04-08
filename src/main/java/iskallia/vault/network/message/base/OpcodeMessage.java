package iskallia.vault.network.message.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;


public class OpcodeMessage<OPC extends Enum<OPC>> {
    public OPC opcode;
    public CompoundNBT payload;

    public void encodeSelf(OpcodeMessage<OPC> message, PacketBuffer buffer) {
        buffer.writeEnum((Enum) message.opcode);
        buffer.writeNbt(message.payload);
    }

    public void decodeSelf(PacketBuffer buffer, Class<OPC> enumClass) {
        this.opcode = (OPC) buffer.readEnum(enumClass);
        this.payload = buffer.readNbt();
    }

    public static <O extends Enum<O>, T extends OpcodeMessage<O>> T composeMessage(T message, O opcode, Consumer<CompoundNBT> payloadSerializer) {
        ((OpcodeMessage) message).opcode = (OPC) opcode;
        CompoundNBT payload = new CompoundNBT();
        payloadSerializer.accept(payload);
        ((OpcodeMessage) message).payload = payload;
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\base\OpcodeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */