// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.block.entity.LootStatueTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OmegaStatueUIMessage
{
    public Opcode opcode;
    public CompoundNBT payload;
    
    public static void encode(final OmegaStatueUIMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.opcode.ordinal());
        buffer.writeNbt(message.payload);
    }
    
    public static OmegaStatueUIMessage decode(final PacketBuffer buffer) {
        final OmegaStatueUIMessage message = new OmegaStatueUIMessage();
        message.opcode = Opcode.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }
    
    public static void handle(final OmegaStatueUIMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.opcode == Opcode.SELECT_ITEM) {
                final ItemStack stack = ItemStack.of(message.payload.getCompound("Item"));
                final BlockPos statuePos = NBTUtil.readBlockPos(message.payload.getCompound("Position"));
                final World world = (World)context.getSender().getLevel();
                final TileEntity te = world.getBlockEntity(statuePos);
                if (te instanceof LootStatueTileEntity) {
                    ((LootStatueTileEntity)te).setLootItem(stack);
                }
            }
            return;
        });
        context.setPacketHandled(true);
    }
    
    public static OmegaStatueUIMessage selectItem(final ItemStack stack, final BlockPos statuePos) {
        final OmegaStatueUIMessage message = new OmegaStatueUIMessage();
        message.opcode = Opcode.SELECT_ITEM;
        (message.payload = new CompoundNBT()).put("Item", (INBT)stack.serializeNBT());
        message.payload.put("Position", (INBT)NBTUtil.writeBlockPos(statuePos));
        return message;
    }
    
    public enum Opcode
    {
        SELECT_ITEM;
    }
}
