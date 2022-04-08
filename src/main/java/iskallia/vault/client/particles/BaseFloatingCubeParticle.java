package iskallia.vault.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.client.gui.helper.LightmapHelper;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.awt.*;
import java.util.Random;

public abstract class BaseFloatingCubeParticle
        extends Particle {
    private static final Random rand = new Random();

    private final BlockPos originPos;

    private final float size;

    private final IAnimatedSprite spriteSet;
    private float effectPercent = 0.0F;
    private float prevEffectPercent = 0.0F;

    private final Vector3d rotationChange;
    private Vector3d rotationDegreeAxis;
    private Vector3d prevRotationDegreeAxis = Vector3d.ZERO;

    protected BaseFloatingCubeParticle(ClientWorld world, IAnimatedSprite spriteSet, double x, double y, double z) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.originPos = new BlockPos(x, y, z);
        this.size = 0.45F;

        Vector3d change = new Vector3d((rand.nextFloat() * (rand.nextBoolean() ? true : -1)), (rand.nextFloat() * (rand.nextBoolean() ? true : -1)), (rand.nextFloat() * (rand.nextBoolean() ? true : -1)));
        this.rotationChange = change.multiply(5.0D, 5.0D, 5.0D);
        Vector3d axis = new Vector3d((rand.nextFloat() * (rand.nextBoolean() ? true : -1)), rand.nextFloat(), (rand.nextFloat() * (rand.nextBoolean() ? true : -1)));
        this.rotationDegreeAxis = axis.multiply(18.0D, 18.0D, 18.0D);
    }


    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;

        if (!isAlive()) {
            return;
        }
        if (!isValid()) {
            remove();

            return;
        }
        this.prevEffectPercent = this.effectPercent;
        if (isActive()) {
            this.effectPercent = Math.min(this.effectPercent + 0.02F, 1.0F);
        } else {
            this.effectPercent = Math.max(this.effectPercent - 0.01F, 0.0F);
        }
        updateRotations();
    }

    private void updateRotations() {
        if (this.effectPercent > 0.0F && this.rotationChange.lengthSqr() > 0.0D) {
            Vector3d modify = this.rotationChange.multiply(this.effectPercent, this.effectPercent, this.effectPercent);
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.scale(1.0D);
            this.rotationDegreeAxis = this.rotationDegreeAxis.add(modify);
            this


                    .rotationDegreeAxis = new Vector3d(this.rotationDegreeAxis.x() % 360.0D, this.rotationDegreeAxis.y() % 360.0D, this.rotationDegreeAxis.z() % 360.0D);

            if (!this.rotationDegreeAxis.add(modify).equals(this.rotationDegreeAxis)) {
                this.prevRotationDegreeAxis = this.rotationDegreeAxis.subtract(modify);
            }
        } else {
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.scale(1.0D);
        }
    }

    protected abstract boolean isValid();

    protected abstract boolean isActive();

    private Vector3d getInterpolatedRotation(float partialTicks) {
        return new Vector3d(
                MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.x(), this.rotationDegreeAxis.x()),
                MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.y(), this.rotationDegreeAxis.y()),
                MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.z(), this.rotationDegreeAxis.z()));
    }

    private double getYOffset(float partialTicks) {
        double offset = (Math.sin(this.effectPercent * Math.PI + 4.71238898038469D) + 1.0D) / 2.0D;
        double offsetPrev = (Math.sin(this.prevEffectPercent * Math.PI + 4.71238898038469D) + 1.0D) / 2.0D;
        return MathHelper.lerp(partialTicks, offsetPrev, offset);
    }


    public void render(IVertexBuilder buffer, ActiveRenderInfo ari, float partialTicks) {
        RenderSystem.disableAlphaTest();

        float effectPart = MathHelper.lerp(partialTicks, this.prevEffectPercent, this.effectPercent);
        Color color = new Color(MiscUtils.blendColors(getActiveColor(), 5263440, effectPart));
        float x = (float) MathHelper.lerp(partialTicks, this.xo, this.x);
        float y = (float) MathHelper.lerp(partialTicks, this.yo, this.y);
        float z = (float) MathHelper.lerp(partialTicks, this.zo, this.z);
        Vector3d cameraPos = ari.getPosition();
        x = (float) (x - cameraPos.x());
        y = (float) (y - cameraPos.y());
        z = (float) (z - cameraPos.z());
        Vector3d iRotation = getInterpolatedRotation(partialTicks);

        Matrix4f offsetMatrix = new Matrix4f();
        offsetMatrix.setIdentity();
        offsetMatrix.multiply(Matrix4f.createTranslateMatrix(x, (float) (y + 1.25D + getYOffset(partialTicks) * 0.4D), z));
        offsetMatrix.multiply(Vector3f.XP.rotationDegrees((float) iRotation.x()));
        offsetMatrix.multiply(Vector3f.YP.rotationDegrees((float) iRotation.y()));
        offsetMatrix.multiply(Vector3f.ZP.rotationDegrees((float) iRotation.z()));
        offsetMatrix.multiply(Matrix4f.createScaleMatrix(this.size, this.size, this.size));

        renderTexturedCube(buffer, offsetMatrix, color.getRed(), color.getGreen(), color.getBlue(), 255);

        RenderSystem.enableAlphaTest();
    }


    protected abstract int getActiveColor();

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void renderTexturedCube(IVertexBuilder buf, Matrix4f offset, int r, int g, int b, int a) {
        int combinedLight = LightmapHelper.getPackedFullbrightCoords();
        TextureAtlasSprite tas = this.spriteSet.get(rand);
        float minU = tas.getU0();
        float minV = tas.getV0();
        float maxU = tas.getU1();
        float maxV = tas.getV1();

        buf.vertex(offset, -0.5F, -0.5F, -0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, -0.5F, -0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, -0.5F, 0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, -0.5F, 0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();

        buf.vertex(offset, -0.5F, 0.5F, 0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, 0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, -0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, 0.5F, -0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();

        buf.vertex(offset, -0.5F, -0.5F, 0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, 0.5F, 0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, 0.5F, -0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, -0.5F, -0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();

        buf.vertex(offset, 0.5F, -0.5F, -0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, -0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, 0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, -0.5F, 0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();

        buf.vertex(offset, 0.5F, -0.5F, -0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, -0.5F, -0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, 0.5F, -0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, -0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();

        buf.vertex(offset, -0.5F, -0.5F, 0.5F).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, -0.5F, 0.5F).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5F, 0.5F, 0.5F).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5F, 0.5F, 0.5F).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
    }


    public boolean shouldCull() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\particles\BaseFloatingCubeParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */