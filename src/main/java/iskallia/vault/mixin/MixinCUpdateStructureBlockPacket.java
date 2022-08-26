package iskallia.vault.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import io.netty.buffer.ByteBuf;
import org.spongepowered.asm.mixin.Overwrite;
import java.io.IOException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.StructureBlockTileEntity;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CUpdateStructureBlockPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ CUpdateStructureBlockPacket.class })
public class MixinCUpdateStructureBlockPacket
{
    @Shadow
    private BlockPos pos;
    @Shadow
    private StructureBlockTileEntity.UpdateCommand updateType;
    @Shadow
    private StructureMode mode;
    @Shadow
    private String name;
    @Shadow
    private BlockPos offset;
    @Shadow
    private BlockPos size;
    @Shadow
    private Mirror mirror;
    @Shadow
    private Rotation rotation;
    @Shadow
    private String data;
    @Shadow
    private float integrity;
    @Shadow
    private long seed;
    @Shadow
    private boolean ignoreEntities;
    @Shadow
    private boolean showAir;
    @Shadow
    private boolean showBoundingBox;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void read(final PacketBuffer buf) throws IOException {
        this.pos = buf.readBlockPos();
        this.updateType = (StructureBlockTileEntity.UpdateCommand)buf.readEnum((Class)StructureBlockTileEntity.UpdateCommand.class);
        this.mode = (StructureMode)buf.readEnum((Class)StructureMode.class);
        this.name = buf.readUtf(32767);
        final int i = 48;
        final int j = 48;
        this.offset = new BlockPos(MathHelper.clamp(buf.readVarInt(), -48, 48), MathHelper.clamp(buf.readVarInt(), -48, 48), MathHelper.clamp(buf.readVarInt(), -48, 48));
        this.size = new BlockPos(MathHelper.clamp(buf.readVarInt(), 0, 528), MathHelper.clamp(buf.readVarInt(), 0, 528), MathHelper.clamp(buf.readVarInt(), 0, 528));
        this.mirror = (Mirror)buf.readEnum((Class)Mirror.class);
        this.rotation = (Rotation)buf.readEnum((Class)Rotation.class);
        this.data = buf.readUtf(12);
        this.integrity = MathHelper.clamp(buf.readFloat(), 0.0f, 1.0f);
        this.seed = buf.readVarLong();
        final int k = buf.readVarInt();
        this.ignoreEntities = ((k & 0x1) != 0x0);
        this.showAir = ((k & 0x2) != 0x0);
        this.showBoundingBox = ((k & 0x4) != 0x0);
    }

    @Redirect(method = { "write" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writePacketData(final PacketBuffer buf, int value) {
        buf.writeVarInt(value);
        return buf;
    }
}
