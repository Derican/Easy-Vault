// 
// Decompiled by Procyon v0.6.0
// 

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

public class RenameUIMessage
{
    public RenameType renameType;
    public CompoundNBT payload;
    
    public static void encode(final RenameUIMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.renameType.ordinal());
        buffer.writeNbt(message.payload);
    }
    
    public static RenameUIMessage decode(final PacketBuffer buffer) {
        final RenameUIMessage message = new RenameUIMessage();
        message.renameType = RenameType.values()[buffer.readInt()];
        message.payload = buffer.readNbt();
        return message;
    }
    
    public static void handle(final RenameUIMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final CompoundNBT data = message.payload.getCompound("Data");
            final ServerPlayerEntity sender = context.getSender();
            switch (message.renameType) {
                case PLAYER_STATUE: {
                    final BlockPos statuePos = new BlockPos(data.getInt("x"), data.getInt("y"), data.getInt("z"));
                    final TileEntity te = sender.getCommandSenderWorld().getBlockEntity(statuePos);
                    if (te instanceof LootStatueTileEntity) {
                        final LootStatueTileEntity statue = (LootStatueTileEntity)te;
                        statue.getSkin().updateSkin(data.getString("PlayerNickname"));
                        statue.sendUpdates();
                        break;
                    }
                    else {
                        break;
                    }
                }
                case VAULT_CRYSTAL:
                case TRADER_CORE: {
                    sender.inventory.items.set(sender.inventory.selected, ItemStack.of(data));
                    break;
                }
                case CRYO_CHAMBER: {
                    final BlockPos pos = NBTUtil.readBlockPos(data.getCompound("BlockPos"));
                    final String name = data.getString("EternalName");
                    final TileEntity te2 = sender.getCommandSenderWorld().getBlockEntity(pos);
                    if (te2 instanceof CryoChamberTileEntity) {
                        final CryoChamberTileEntity chamber = (CryoChamberTileEntity)te2;
                        chamber.renameEternal(name);
                        chamber.getSkin().updateSkin(name);
                        chamber.sendUpdates();
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            return;
        });
        context.setPacketHandled(true);
    }
    
    public static RenameUIMessage updateName(final RenameType type, final CompoundNBT nbt) {
        final RenameUIMessage message = new RenameUIMessage();
        message.renameType = type;
        message.payload = nbt;
        return message;
    }
}
