// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.SoundCategory;
import iskallia.vault.init.ModSounds;
import iskallia.vault.init.ModParticles;
import net.minecraft.particles.IParticleData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class EnteredEyesoreDomainMessage
{
    public static void encode(final EnteredEyesoreDomainMessage message, final PacketBuffer buffer) {
        final CompoundNBT nbt = new CompoundNBT();
        buffer.writeNbt(nbt);
    }
    
    public static EnteredEyesoreDomainMessage decode(final PacketBuffer buffer) {
        return new EnteredEyesoreDomainMessage();
    }
    
    public static void handle(final EnteredEyesoreDomainMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final Minecraft minecraft = Minecraft.getInstance();
            final ClientPlayerEntity player = minecraft.player;
            final ClientWorld world = minecraft.level;
            if (world == null) {
                return;
            }
            else if (player == null) {
                return;
            }
            else {
                final Vector3d pos = player.position();
                for (int count = 4, dx = -count; dx <= count; ++dx) {
                    for (int dy = -count; dy <= count; ++dy) {
                        for (int dz = -count; dz <= count; ++dz) {
                            world.addParticle((IParticleData)ModParticles.EYESORE_APPEARANCE.get(), true, pos.x + dx * 8, pos.y + dy * 8, pos.z + dz * 8, 0.0, 0.0, 0.0);
                        }
                    }
                }
                player.playNotifySound(ModSounds.EYESORE_GRAWL, SoundCategory.HOSTILE, 1.0f, 1.0f);
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
