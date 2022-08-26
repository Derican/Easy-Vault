package iskallia.vault.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AltarFlameParticle extends DeceleratingParticle {
    public AltarFlameParticle(final ClientWorld world, final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(final double x, final double y, final double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(final float scaleFactor) {
        final float f = (this.age + scaleFactor) / this.lifetime;
        return this.quadSize * (1.0f - f * f * 0.5f);
    }

    public int getLightColor(final float partialTick) {
        float f = (this.age + partialTick) / this.lifetime;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        final int i = super.getLightColor(partialTick);
        int j = i & 0xFF;
        final int k = i >> 16 & 0xFF;
        j += (int) (f * 15.0f * 16.0f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(final IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(final BasicParticleType type, final ClientWorld world, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
            final AltarFlameParticle particle = new AltarFlameParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}
