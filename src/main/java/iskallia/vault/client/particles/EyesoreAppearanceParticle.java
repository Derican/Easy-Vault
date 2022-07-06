// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.particles;

import net.minecraft.particles.IParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3f;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.ActiveRenderInfo;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nonnull;
import net.minecraft.client.particle.IParticleRenderType;
import iskallia.vault.entity.renderer.EyesoreRenderer;
import iskallia.vault.entity.model.EyesoreModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.particle.Particle;

public class EyesoreAppearanceParticle extends Particle
{
    private final Model model;
    private final RenderType renderType;
    
    protected EyesoreAppearanceParticle(final ClientWorld world, final double x, final double y, final double z) {
        super(world, x, y, z);
        this.model = (Model)new EyesoreModel();
        this.renderType = RenderType.entityTranslucent(EyesoreRenderer.SORE_EYE_TEXTURE);
        this.gravity = 0.0f;
        this.lifetime = 30;
    }
    
    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }
    
    public void render(@Nonnull final IVertexBuilder buffer, final ActiveRenderInfo renderInfo, final float partialTicks) {
        final float f = (this.age + partialTicks) / this.lifetime;
        final float f2 = 0.05f + 0.5f * MathHelper.sin(f * 3.1415927f);
        final MatrixStack matrixstack = new MatrixStack();
        matrixstack.mulPose(renderInfo.rotation());
        matrixstack.mulPose(Vector3f.XP.rotationDegrees(150.0f * f - 60.0f));
        matrixstack.scale(-1.0f, -1.0f, 1.0f);
        matrixstack.translate(0.0, -1.1009999513626099, 1.5);
        final IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        final IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(this.renderType);
        this.model.renderToBuffer(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, f2);
        irendertypebuffer$impl.endBatch();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteSet;
        
        public Factory(final IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }
        
        public Particle createParticle(@Nonnull final BasicParticleType typeIn, @Nonnull final ClientWorld worldIn, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
            return new EyesoreAppearanceParticle(worldIn, x, y, z);
        }
    }
}
