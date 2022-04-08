package iskallia.vault.network.message;

import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.util.RenameType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class RenameUIMessage {
    public RenameType renameType;
    public CompoundNBT payload;

    public static void encode(RenameUIMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.renameType.ordinal());
        buffer.writeNbt(message.payload);
    }

    public static RenameUIMessage decode(PacketBuffer buffer) {
        RenameUIMessage message = new RenameUIMessage();
        message.renameType = RenameType.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }

    public static void handle(RenameUIMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            TileEntity te;
            BlockPos statuePos;
            BlockPos pos;
            String name;
            CompoundNBT data = message.payload.getCompound("Data");
            ServerPlayerEntity sender = context.getSender();
            switch (message.renameType) {
                case PLAYER_STATUE:
                    statuePos = new BlockPos(data.getInt("x"), data.getInt("y"), data.getInt("z"));
                    te = sender.getCommandSenderWorld().getBlockEntity(statuePos);
                    if (te instanceof LootStatueTileEntity) {
                        LootStatueTileEntity statue = (LootStatueTileEntity) te;
                        statue.getSkin().updateSkin(data.getString("PlayerNickname"));
                        statue.sendUpdates();
                    }
                    break;
                case VAULT_CRYSTAL:
                case TRADER_CORE:
                    sender.inventory.items.set(sender.inventory.selected, ItemStack.of(data));
                    break;
                case CRYO_CHAMBER:
                    pos = NBTUtil.readBlockPos(data.getCompound("BlockPos"));
                    name = data.getString("EternalName");
                    te = sender.getCommandSenderWorld().getBlockEntity(pos);
                    if (te instanceof CryoChamberTileEntity) {
                        CryoChamberTileEntity chamber = (CryoChamberTileEntity) te;
                        chamber.renameEternal(name);
                        chamber.getSkin().updateSkin(name);
                        chamber.sendUpdates();
                    }
                    break;
            }
        });
        context.setPacketHandled(true);
    }

    public static RenameUIMessage updateName(RenameType type, CompoundNBT nbt) {
        RenameUIMessage message = new RenameUIMessage();
        message.renameType = type;
        message.payload = nbt;
        return message;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\RenameUIMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */