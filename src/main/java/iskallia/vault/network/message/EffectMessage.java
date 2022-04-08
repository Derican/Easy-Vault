package iskallia.vault.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import iskallia.vault.client.util.ParticleHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;


public class EffectMessage {
    private final Type effectType;
    private final Vector3d pos;
    private PacketBuffer data = null;


    public EffectMessage(Type effectType, Vector3d pos) {
        this.effectType = effectType;
        this.pos = pos;
    }

    private Consumer<PacketBuffer> encoder = buf -> {

    };

    public Type getEffectType() {
        return this.effectType;
    }

    public Vector3d getPos() {
        return this.pos;
    }

    public PacketBuffer getData() {
        return this.data;
    }

    public EffectMessage addData(Consumer<PacketBuffer> encoder) {
        this.encoder = this.encoder.andThen(encoder);
        return this;
    }

    public static void encode(EffectMessage pkt, PacketBuffer buffer) {
        buffer.writeEnum(pkt.effectType);
        buffer.writeDouble(pkt.pos.x);
        buffer.writeDouble(pkt.pos.y);
        buffer.writeDouble(pkt.pos.z);
        pkt.encoder.accept(buffer);
    }

    public static EffectMessage decode(PacketBuffer buffer) {
        Type type = (Type) buffer.readEnum(Type.class);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        EffectMessage pkt = new EffectMessage(type, new Vector3d(x, y, z));
        ByteBuf buf = Unpooled.buffer(buffer.readableBytes());
        buffer.readBytes(buf);
        pkt.data = new PacketBuffer(buf);
        return pkt;
    }

    public static void handle(EffectMessage pkt, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ParticleHelper.spawnParticle(pkt));
        context.setPacketHandled(true);
    }

    public enum Type {
        COLORED_FIREWORK;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\EffectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */