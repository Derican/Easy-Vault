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
    private PacketBuffer data;
    private Consumer<PacketBuffer> encoder;

    public EffectMessage(final Type effectType, final Vector3d pos) {
        this.data = null;
        this.encoder = (buf -> {
        });
        this.effectType = effectType;
        this.pos = pos;
    }

    public Type getEffectType() {
        return this.effectType;
    }

    public Vector3d getPos() {
        return this.pos;
    }

    public PacketBuffer getData() {
        return this.data;
    }

    public EffectMessage addData(final Consumer<PacketBuffer> encoder) {
        this.encoder = this.encoder.andThen(encoder);
        return this;
    }

    public static void encode(final EffectMessage pkt, final PacketBuffer buffer) {
        buffer.writeEnum((Enum) pkt.effectType);
        buffer.writeDouble(pkt.pos.x);
        buffer.writeDouble(pkt.pos.y);
        buffer.writeDouble(pkt.pos.z);
        pkt.encoder.accept(buffer);
    }

    public static EffectMessage decode(final PacketBuffer buffer) {
        final Type type = (Type) buffer.readEnum((Class) Type.class);
        final double x = buffer.readDouble();
        final double y = buffer.readDouble();
        final double z = buffer.readDouble();
        final EffectMessage pkt = new EffectMessage(type, new Vector3d(x, y, z));
        final ByteBuf buf = Unpooled.buffer(buffer.readableBytes());
        buffer.readBytes(buf);
        pkt.data = new PacketBuffer(buf);
        return pkt;
    }

    public static void handle(final EffectMessage pkt, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ParticleHelper.spawnParticle(pkt));
        context.setPacketHandled(true);
    }

    public enum Type {
        COLORED_FIREWORK;
    }
}
