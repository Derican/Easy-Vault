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
public class ParticleHelper {
    public static void spawnParticle(EffectMessage pkt) {
        EffectMessage.Type type = pkt.getEffectType();
        switch (type) {
            case COLORED_FIREWORK:
                spawnColoredFirework(pkt.getPos(), pkt.getData().readInt());
                break;
        }
    }

    private static void spawnColoredFirework(Vector3d pos, int color) {
        ParticleManager mgr = (Minecraft.getInstance()).particleEngine;
        SimpleAnimatedParticle fwParticle = (SimpleAnimatedParticle) mgr.createParticle((IParticleData) ParticleTypes.FIREWORK, pos.x(), pos.y(), pos.z(), 0.0D, 0.0D, 0.0D);
        Color c = new Color(color);
        fwParticle.setColor(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F);
        fwParticle.baseGravity = 0.0F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\ParticleHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */