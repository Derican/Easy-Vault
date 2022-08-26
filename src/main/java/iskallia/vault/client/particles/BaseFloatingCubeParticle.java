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

public abstract class BaseFloatingCubeParticle extends Particle {
    private static final Random rand;
    private final BlockPos originPos;
    private final float size;
    private final IAnimatedSprite spriteSet;
    private float effectPercent;
    private float prevEffectPercent;
    private final Vector3d rotationChange;
    private Vector3d rotationDegreeAxis;
    private Vector3d prevRotationDegreeAxis;

    protected BaseFloatingCubeParticle(final ClientWorld world, final IAnimatedSprite spriteSet, final double x, final double y, final double z) {
        super(world, x, y, z);
        this.effectPercent = 0.0f;
        this.prevEffectPercent = 0.0f;
        this.prevRotationDegreeAxis = Vector3d.ZERO;
        this.spriteSet = spriteSet;
        this.originPos = new BlockPos(x, y, z);
        this.size = 0.45f;
        final Vector3d change = new Vector3d(BaseFloatingCubeParticle.rand.nextFloat() * (BaseFloatingCubeParticle.rand.nextBoolean() ? 1 : -1), BaseFloatingCubeParticle.rand.nextFloat() * (BaseFloatingCubeParticle.rand.nextBoolean() ? 1 : -1), BaseFloatingCubeParticle.rand.nextFloat() * (BaseFloatingCubeParticle.rand.nextBoolean() ? 1 : -1));
        this.rotationChange = change.multiply(5.0, 5.0, 5.0);
        final Vector3d axis = new Vector3d(BaseFloatingCubeParticle.rand.nextFloat() * (BaseFloatingCubeParticle.rand.nextBoolean() ? 1 : -1), BaseFloatingCubeParticle.rand.nextFloat(), BaseFloatingCubeParticle.rand.nextFloat() * (BaseFloatingCubeParticle.rand.nextBoolean() ? 1 : -1));
        this.rotationDegreeAxis = axis.multiply(18.0, 18.0, 18.0);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;
        if (!this.isAlive()) {
            return;
        }
        if (!this.isValid()) {
            this.remove();
            return;
        }
        this.prevEffectPercent = this.effectPercent;
        if (this.isActive()) {
            this.effectPercent = Math.min(this.effectPercent + 0.02f, 1.0f);
        } else {
            this.effectPercent = Math.max(this.effectPercent - 0.01f, 0.0f);
        }
        this.updateRotations();
    }

    private void updateRotations() {
        if (this.effectPercent > 0.0f && this.rotationChange.lengthSqr() > 0.0) {
            final Vector3d modify = this.rotationChange.multiply(this.effectPercent, this.effectPercent, this.effectPercent);
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.scale(1.0);
            this.rotationDegreeAxis = this.rotationDegreeAxis.add(modify);
            this.rotationDegreeAxis = new Vector3d(this.rotationDegreeAxis.x() % 360.0, this.rotationDegreeAxis.y() % 360.0, this.rotationDegreeAxis.z() % 360.0);
            if (!this.rotationDegreeAxis.add(modify).equals(this.rotationDegreeAxis)) {
                this.prevRotationDegreeAxis = this.rotationDegreeAxis.subtract(modify);
            }
        } else {
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.scale(1.0);
        }
    }

    protected abstract boolean isValid();

    protected abstract boolean isActive();

    private Vector3d getInterpolatedRotation(final float partialTicks) {
        return new Vector3d(MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.x(), this.rotationDegreeAxis.x()), MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.y(), this.rotationDegreeAxis.y()), MathHelper.lerp(partialTicks, this.prevRotationDegreeAxis.z(), this.rotationDegreeAxis.z()));
    }

    private double getYOffset(final float partialTicks) {
        final double offset = (Math.sin(this.effectPercent * 3.141592653589793 + 4.71238898038469) + 1.0) / 2.0;
        final double offsetPrev = (Math.sin(this.prevEffectPercent * 3.141592653589793 + 4.71238898038469) + 1.0) / 2.0;
        return MathHelper.lerp(partialTicks, offsetPrev, offset);
    }

    public void render(final IVertexBuilder buffer, final ActiveRenderInfo ari, final float partialTicks) {
        RenderSystem.disableAlphaTest();
        final float effectPart = MathHelper.lerp(partialTicks, this.prevEffectPercent, this.effectPercent);
        final Color color = new Color(MiscUtils.blendColors(this.getActiveColor(), 5263440, effectPart));
        float x = (float) MathHelper.lerp(partialTicks, this.xo, this.x);
        float y = (float) MathHelper.lerp(partialTicks, this.yo, this.y);
        float z = (float) MathHelper.lerp(partialTicks, this.zo, this.z);
        final Vector3d cameraPos = ari.getPosition();
        x -= (float) cameraPos.x();
        y -= (float) cameraPos.y();
        z -= (float) cameraPos.z();
        final Vector3d iRotation = this.getInterpolatedRotation(partialTicks);
        final Matrix4f offsetMatrix = new Matrix4f();
        offsetMatrix.setIdentity();
        offsetMatrix.multiply(Matrix4f.createTranslateMatrix(x, (float) (y + 1.25 + this.getYOffset(partialTicks) * 0.4), z));
        offsetMatrix.multiply(Vector3f.XP.rotationDegrees((float) iRotation.x()));
        offsetMatrix.multiply(Vector3f.YP.rotationDegrees((float) iRotation.y()));
        offsetMatrix.multiply(Vector3f.ZP.rotationDegrees((float) iRotation.z()));
        offsetMatrix.multiply(Matrix4f.createScaleMatrix(this.size, this.size, this.size));
        this.renderTexturedCube(buffer, offsetMatrix, color.getRed(), color.getGreen(), color.getBlue(), 255);
        RenderSystem.enableAlphaTest();
    }

    protected abstract int getActiveColor();

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void renderTexturedCube(final IVertexBuilder buf, final Matrix4f offset, final int r, final int g, final int b, final int a) {
        final int combinedLight = LightmapHelper.getPackedFullbrightCoords();
        final TextureAtlasSprite tas = this.spriteSet.get(BaseFloatingCubeParticle.rand);
        final float minU = tas.getU0();
        final float minV = tas.getV0();
        final float maxU = tas.getU1();
        final float maxV = tas.getV1();
        buf.vertex(offset, -0.5f, -0.5f, -0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, -0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, 0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, -0.5f, 0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, 0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, 0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, -0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, -0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, -0.5f, 0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, 0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, -0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, -0.5f, -0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, -0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, -0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, 0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, 0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, -0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, -0.5f, -0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, -0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, -0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, -0.5f, 0.5f).uv(minU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, -0.5f, 0.5f).uv(maxU, minV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, 0.5f, 0.5f, 0.5f).uv(maxU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
        buf.vertex(offset, -0.5f, 0.5f, 0.5f).uv(minU, maxV).color(r, g, b, a).uv2(combinedLight).endVertex();
    }

    public boolean shouldCull() {
        return false;
    }

    static {
        rand = new Random();
    }
}
