// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.Vault;
import iskallia.vault.entity.TreasureGoblinEntity;
import iskallia.vault.entity.model.TreasureGoblinModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

public class TreasureGoblinRenderer extends LivingRenderer<TreasureGoblinEntity, TreasureGoblinModel>
{
    public static final ResourceLocation TREASURE_GOBLIN_TEXTURES;
    
    public TreasureGoblinRenderer(final EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TreasureGoblinModel(), 0.5f);
    }
    
    public ResourceLocation getTextureLocation(final TreasureGoblinEntity entity) {
        return TreasureGoblinRenderer.TREASURE_GOBLIN_TEXTURES;
    }
    
    protected void preRenderCallback(final TreasureGoblinEntity entity, final MatrixStack matrixStack, final float partialTickTime) {
        final float f = 0.75f;
        matrixStack.scale(f, f, f);
    }
    
    public Vector3d getRenderOffset(final TreasureGoblinEntity entityIn, final float partialTicks) {
        return entityIn.isCrouching() ? new Vector3d(0.0, -0.125, 0.0) : super.getRenderOffset(entityIn, partialTicks);
    }
    
    public void render(final TreasureGoblinEntity entity, final float entityYaw, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int packedLightIn) {
        this.setModelVisibilities(entity);
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
    }
    
    protected void renderName(final TreasureGoblinEntity entityIn, final ITextComponent displayNameIn, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int packedLightIn) {
    }
    
    protected boolean canRenderName(final TreasureGoblinEntity entity) {
        return false;
    }
    
    private void setModelVisibilities(final TreasureGoblinEntity entity) {
        final TreasureGoblinModel model = (TreasureGoblinModel)this.getModel();
        if (entity.isSpectator()) {
            model.setAllVisible(false);
            model.head.visible = true;
            model.hat.visible = true;
        }
        else {
            model.setAllVisible(true);
            model.crouching = entity.isCrouching();
            final BipedModel.ArmPose bipedmodel$armpose = getArmPose(entity, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose2 = getArmPose(entity, Hand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose2 = (entity.getOffhandItem().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM);
            }
            if (entity.getMainArm() == HandSide.RIGHT) {
                model.rightArmPose = bipedmodel$armpose;
                model.leftArmPose = bipedmodel$armpose2;
            }
            else {
                model.rightArmPose = bipedmodel$armpose2;
                model.leftArmPose = bipedmodel$armpose;
            }
        }
    }
    
    private static BipedModel.ArmPose getArmPose(final TreasureGoblinEntity entity, final Hand hand) {
        return BipedModel.ArmPose.EMPTY;
    }
    
    protected void applyRotations(final TreasureGoblinEntity entityLiving, final MatrixStack matrixStack, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        final float f = entityLiving.getSwimAmount(partialTicks);
        if (entityLiving.isFallFlying()) {
            super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
            final float f2 = entityLiving.getFallFlyingTicks() + partialTicks;
            final float f3 = MathHelper.clamp(f2 * f2 / 100.0f, 0.0f, 1.0f);
            if (!entityLiving.isAutoSpinAttack()) {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(f3 * (-90.0f - entityLiving.xRot)));
            }
            final Vector3d vector3d = entityLiving.getViewVector(partialTicks);
            final Vector3d vector3d2 = entityLiving.getDeltaMovement();
            final double d0 = Entity.getHorizontalDistanceSqr(vector3d2);
            final double d2 = Entity.getHorizontalDistanceSqr(vector3d);
            if (d0 > 0.0 && d2 > 0.0) {
                final double d3 = (vector3d2.x * vector3d.x + vector3d2.z * vector3d.z) / Math.sqrt(d0 * d2);
                final double d4 = vector3d2.x * vector3d.z - vector3d2.z * vector3d.x;
                matrixStack.mulPose(Vector3f.YP.rotation((float)(Math.signum(d4) * Math.acos(d3))));
            }
        }
        else if (f > 0.0f) {
            super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
            final float f4 = entityLiving.isInWater() ? (-90.0f - entityLiving.xRot) : -90.0f;
            final float f5 = MathHelper.lerp(f, 0.0f, f4);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(f5));
            if (entityLiving.isVisuallySwimming()) {
                matrixStack.translate(0.0, -1.0, 0.30000001192092896);
            }
        }
        else {
            super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        }
    }
    
    static {
        TREASURE_GOBLIN_TEXTURES = Vault.id("textures/entity/treasure_goblin.png");
    }
}
