// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import iskallia.vault.Vault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import iskallia.vault.client.util.RenderTypeDecorator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.datasync.DataParameter;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.text.ITextComponent;
import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.entity.model.EyesoreModel;
import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.client.renderer.entity.MobRenderer;

public class EyesoreRenderer extends MobRenderer<EyesoreEntity, EyesoreModel>
{
    public static final ResourceLocation DEFAULT_TEXTURE;
    public static final ResourceLocation SORE_EYE_TEXTURE;
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE;
    private static final RenderType BEAM_RENDER_TYPE;
    
    public EyesoreRenderer(final EntityRendererManager rendererManager) {
        super(rendererManager, new EyesoreModel(), 0.5f);
    }
    
    protected void preRenderCallback(@Nonnull final EyesoreEntity entity, @Nonnull final MatrixStack matrixStack, final float partialTickTime) {
        final float f = 9.0f;
        matrixStack.scale(f, f, f);
    }
    
    protected void renderName(final EyesoreEntity entityIn, final ITextComponent displayNameIn, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int packedLightIn) {
    }
    
    protected boolean canRenderName(final EyesoreEntity entity) {
        return false;
    }
    
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull final EyesoreEntity entity) {
        final UUID targetPlayer = (entity.getEntityData().get(EyesoreEntity.LASER_TARGET)).orElse(null);
        if ((targetPlayer != null && entity.level.getPlayerByUUID(targetPlayer) != null) || (boolean)entity.getEntityData().get((DataParameter)EyesoreEntity.WATCH_CLIENT)) {
            return EyesoreRenderer.SORE_EYE_TEXTURE;
        }
        if (entity.getState() == EyesoreEntity.State.GIVING_BIRTH) {
            return EyesoreRenderer.SORE_EYE_TEXTURE;
        }
        return EyesoreRenderer.DEFAULT_TEXTURE;
    }
    
    public void render(final EyesoreEntity entity, final float entityYaw, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
        final LivingEntity livingentity = (entity.getEntityData().get(EyesoreEntity.LASER_TARGET)).map(playerId -> entity.getCommandSenderWorld().getPlayerByUUID(playerId)).orElse(null);
        ((EyesoreModel)this.model).tentaclesRemaining = entity.getTentaclesRemaining();
        if (livingentity != null) {
            final float f = this.getAttackAnimationScale(entity, partialTicks);
            final float f2 = entity.level.getGameTime() + partialTicks;
            final float f3 = f2 * 0.5f % 1.0f;
            final float f4 = entity.getEyeHeight();
            matrixStack.pushPose();
            matrixStack.translate(0.0, (double)f4, 0.0);
            Vector3d vector3d = this.getPosition(livingentity, livingentity.getBbHeight() * 0.5, partialTicks);
            Vector3d vector3d2 = this.getPosition((LivingEntity)entity, f4, partialTicks);
            final Vector3d eyePos1 = entity.getEyePosition(partialTicks);
            final RayTraceContext context = new RayTraceContext(eyePos1, vector3d, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, (Entity)entity);
            final BlockRayTraceResult result = entity.level.clip(context);
            vector3d2 = eyePos1;
            if (result.getType() != RayTraceResult.Type.MISS) {
                vector3d = result.getLocation();
            }
            Vector3d vector3d3 = vector3d.subtract(vector3d2);
            final float f5 = (float)(vector3d3.length() + 1.0);
            vector3d3 = vector3d3.normalize();
            final float f6 = (float)Math.acos(vector3d3.y);
            final float f7 = (float)Math.atan2(vector3d3.z, vector3d3.x);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees((1.5707964f - f7) * 57.295776f));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(f6 * 57.295776f));
            final int i = 1;
            final float f8 = f2 * 0.05f * -1.5f;
            final float f9 = f * f;
            final int j = 190;
            final int k = 0;
            final int l = 0;
            final float f10 = 0.2f;
            final float f11 = 0.282f;
            final float f12 = MathHelper.cos(f8 + 2.3561945f) * 0.282f;
            final float f13 = MathHelper.sin(f8 + 2.3561945f) * 0.282f;
            final float f14 = MathHelper.cos(f8 + 0.7853982f) * 0.282f;
            final float f15 = MathHelper.sin(f8 + 0.7853982f) * 0.282f;
            final float f16 = MathHelper.cos(f8 + 3.926991f) * 0.282f;
            final float f17 = MathHelper.sin(f8 + 3.926991f) * 0.282f;
            final float f18 = MathHelper.cos(f8 + 5.4977875f) * 0.282f;
            final float f19 = MathHelper.sin(f8 + 5.4977875f) * 0.282f;
            final float f20 = MathHelper.cos(f8 + 3.1415927f) * 0.2f;
            final float f21 = MathHelper.sin(f8 + 3.1415927f) * 0.2f;
            final float f22 = MathHelper.cos(f8 + 0.0f) * 0.2f;
            final float f23 = MathHelper.sin(f8 + 0.0f) * 0.2f;
            final float f24 = MathHelper.cos(f8 + 1.5707964f) * 0.2f;
            final float f25 = MathHelper.sin(f8 + 1.5707964f) * 0.2f;
            final float f26 = MathHelper.cos(f8 + 4.712389f) * 0.2f;
            final float f27 = MathHelper.sin(f8 + 4.712389f) * 0.2f;
            final float f28 = 0.0f;
            final float f29 = 0.4999f;
            final float f30 = -1.0f + f3;
            final float f31 = f5 * 2.5f + f30;
            Vector3d direction = vector3d.subtract(vector3d2);
            final double directionLength = direction.length();
            direction = direction.normalize();
            for (int step = 0; step <= directionLength; ++step) {
                final Vector3d pos = vector3d2.add(direction.scale((double)step));
                entity.level.addParticle((IParticleData)RedstoneParticleData.REDSTONE, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
            }
            final RenderType type = RenderTypeDecorator.decorate(EyesoreRenderer.BEAM_RENDER_TYPE, () -> RenderSystem.disableCull(), () -> {});
            final IVertexBuilder ivertexbuilder = buffer.getBuffer(type);
            final MatrixStack.Entry matrixstack$entry = matrixStack.last();
            final Matrix4f matrix4f = matrixstack$entry.pose();
            final Matrix3f matrix3f = matrixstack$entry.normal();
            vertex(ivertexbuilder, matrix4f, matrix3f, f20, f5, f21, j, k, l, 0.4999f, f31);
            vertex(ivertexbuilder, matrix4f, matrix3f, f20, 0.0f, f21, j, k, l, 0.4999f, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f22, 0.0f, f23, j, k, l, 0.0f, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f22, f5, f23, j, k, l, 0.0f, f31);
            vertex(ivertexbuilder, matrix4f, matrix3f, f24, f5, f25, j, k, l, 0.4999f, f31);
            vertex(ivertexbuilder, matrix4f, matrix3f, f24, 0.0f, f25, j, k, l, 0.4999f, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f26, 0.0f, f27, j, k, l, 0.0f, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f26, f5, f27, j, k, l, 0.0f, f31);
            float f32 = 0.0f;
            if (entity.tickCount % 2 == 0) {
                f32 = 0.5f;
            }
            vertex(ivertexbuilder, matrix4f, matrix3f, f12, f5, f13, j, k, l, 0.5f, f32 + 0.5f);
            vertex(ivertexbuilder, matrix4f, matrix3f, f14, f5, f15, j, k, l, 1.0f, f32 + 0.5f);
            vertex(ivertexbuilder, matrix4f, matrix3f, f18, f5, f19, j, k, l, 1.0f, f32);
            vertex(ivertexbuilder, matrix4f, matrix3f, f16, f5, f17, j, k, l, 0.5f, f32);
            matrixStack.popPose();
            if (buffer instanceof IRenderTypeBuffer.Impl) {
                ((IRenderTypeBuffer.Impl)buffer).endBatch(type);
            }
        }
    }
    
    private static void vertex(final IVertexBuilder builder, final Matrix4f matrix, final Matrix3f normal, final float x, final float y, final float z, final int r, final int g, final int b, final float u, final float v) {
        builder.vertex(matrix, x, y, z).color(r, g, b, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    public float getAttackAnimationScale(final EyesoreEntity entity, final float p_175477_1_) {
        return (entity.laserTick + p_175477_1_) / 80.0f;
    }
    
    private Vector3d getPosition(final LivingEntity entityLivingBaseIn, final double p_177110_2_, final float p_177110_4_) {
        final double d0 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.xOld, entityLivingBaseIn.getX());
        final double d2 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.yOld, entityLivingBaseIn.getY()) + p_177110_2_;
        final double d3 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.zOld, entityLivingBaseIn.getZ());
        return new Vector3d(d0, d2, d3);
    }
    
    static {
        DEFAULT_TEXTURE = Vault.id("textures/entity/eyesore/default.png");
        SORE_EYE_TEXTURE = Vault.id("textures/entity/eyesore/sore_eye.png");
        GUARDIAN_BEAM_TEXTURE = Vault.id("textures/entity/eyesore/laser_beam.png");
        BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(EyesoreRenderer.GUARDIAN_BEAM_TEXTURE);
    }
}
