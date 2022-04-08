package iskallia.vault.network.message;

import iskallia.vault.block.entity.LootStatueTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OmegaStatueUIMessage {
    public Opcode opcode;
    public CompoundNBT payload;

    public enum Opcode {
        SELECT_ITEM;
    }


    public static void encode(OmegaStatueUIMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeNbt(message.payload);
    }

    public static OmegaStatueUIMessage decode(PacketBuffer buffer) {
        OmegaStatueUIMessage message = new OmegaStatueUIMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }

    public static void handle(OmegaStatueUIMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.SELECT_ITEM) {
                ItemStack stack = ItemStack.of(message.payload.getCompound("Item"));
                BlockPos statuePos = NBTUtil.readBlockPos(message.payload.getCompound("Position"));
                ServerWorld serverWorld = context.getSender().getLevel();
                TileEntity te = serverWorld.getBlockEntity(statuePos);
                if (te instanceof LootStatueTileEntity) {
                    ((LootStatueTileEntity) te).setLootItem(stack);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static OmegaStatueUIMessage selectItem(ItemStack stack, BlockPos statuePos) {
        OmegaStatueUIMessage message = new OmegaStatueUIMessage();
        message.opcode = Opcode.SELECT_ITEM;
        message.payload = new CompoundNBT();
        message.payload.put("Item", (INBT) stack.serializeNBT());
        message.payload.put("Position", (INBT) NBTUtil.writeBlockPos(statuePos));
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\OmegaStatueUIMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */