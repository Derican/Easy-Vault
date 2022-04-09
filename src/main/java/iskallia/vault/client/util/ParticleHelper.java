// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.util;

import iskallia.vault.network.message.EffectMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ParticleHelper
{
    public static void spawnParticle(final EffectMessage pkt) {
        final EffectMessage.Type type = pkt.getEffectType();
        switch (type) {
            case COLORED_FIREWORK: {
                spawnColoredFirework(pkt.getPos(), pkt.getData().readInt());
                break;
            }
        }
    }
    
    private static void spawnColoredFirework(final Vector3d pos, final int color) {
        final ParticleManager mgr = Minecraft.getInstance().particleEngine;
        final SimpleAnimatedParticle fwParticle = (SimpleAnimatedParticle)mgr.createParticle((IParticleData)ParticleTypes.FIREWORK, pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0);
        final Color c = new Color(color);
        fwParticle.setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
//        fwParticle.baseGravity = 0.0f;
    }
}
