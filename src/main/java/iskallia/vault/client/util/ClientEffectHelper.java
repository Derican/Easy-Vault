// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.util;

import net.minecraft.client.particle.ParticleManager;
import java.awt.Color;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.SoundEvent;
import net.minecraft.network.PacketBuffer;
import iskallia.vault.network.message.EffectMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientEffectHelper
{
    public static void doEffect(final EffectMessage pkt) {
        final EffectMessage.Type type = pkt.getEffectType();
        switch (type) {
            case COLORED_FIREWORK: {
                spawnColoredFirework(pkt.getPos(), pkt.getData().readInt());
                break;
            }
            case PLAY_SOUND: {
                playClientSound(pkt.getData());
                break;
            }
        }
    }
    
    private static void playClientSound(final PacketBuffer data) {
        final SoundEvent event = Registry.SOUND_EVENT.get(new ResourceLocation(data.readUtf()));
        final SoundCategory category = (SoundCategory)data.readEnum((Class)SoundCategory.class);
        final float pitch = data.readFloat();
        final float volume = data.readFloat();
        final PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final Vector3d pos = player.position();
        player.getCommandSenderWorld().playLocalSound(pos.x, pos.y + 1.0, pos.z, event, category, pitch, volume, false);
    }
    
    private static void spawnColoredFirework(final Vector3d pos, final int color) {
        final ParticleManager mgr = Minecraft.getInstance().particleEngine;
        final SimpleAnimatedParticle fwParticle = (SimpleAnimatedParticle)mgr.createParticle(ParticleTypes.FIREWORK, pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0);
        final Color c = new Color(color);
        fwParticle.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
//        fwParticle.baseGravity = 0.0f;
    }
}
