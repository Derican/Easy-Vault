package iskallia.vault.network.message;

import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.FighterEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FighterSizeMessage {
    private int entityId;
    private float size;

    public FighterSizeMessage() {
    }

    public FighterSizeMessage(final int entityId, final float size) {
        this.entityId = entityId;
        this.size = size;
    }

    public FighterSizeMessage(final Entity entity, final float size) {
        this(entity.getId(), size);
    }

    public static void encode(final FighterSizeMessage message, final PacketBuffer buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeFloat(message.size);
    }

    public static FighterSizeMessage decode(final PacketBuffer buffer) {
        final FighterSizeMessage message = new FighterSizeMessage();
        message.entityId = buffer.readInt();
        message.size = buffer.readFloat();
        return message;
    }

    public static void handle(final FighterSizeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final Minecraft minecraft = Minecraft.getInstance();
            final ClientPlayerEntity player = minecraft.player;
            final World world = player.level;
            final Entity entity = world.getEntity(message.entityId);
            if (entity == null || !entity.isAlive()) {
                return;
            } else {
                if (entity instanceof FighterEntity) {
                    ((FighterEntity) entity).changeSize(message.size);
                }
                if (entity instanceof EternalEntity) {
                    ((EternalEntity) entity).changeSize(message.size);
                }
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
