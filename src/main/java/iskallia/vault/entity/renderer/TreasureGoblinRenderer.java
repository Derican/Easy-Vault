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

public class TreasureGoblinRenderer extends LivingRenderer<TreasureGoblinEntity, TreasureGoblinModel> {
    public static final ResourceLocation TREASURE_GOBLIN_TEXTURES = Vault.id("textures/entity/treasure_goblin.png");

    public TreasureGoblinRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn,  new TreasureGoblinModel(), 0.5F);
    }


    public ResourceLocation getTextureLocation(TreasureGoblinEntity entity) {
        return TREASURE_GOBLIN_TEXTURES;
    }


    protected void preRenderCallback(TreasureGoblinEntity entity, MatrixStack matrixStack, float partialTickTime) {
        float f = 0.75F;
        matrixStack.scale(f, f, f);
    }


    public Vector3d getRenderOffset(TreasureGoblinEntity entityIn, float partialTicks) {
        return entityIn.isCrouching() ? new Vector3d(0.0D, -0.125D, 0.0D) : super.getRenderOffset( entityIn, partialTicks);
    }


    public void render(TreasureGoblinEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
        setModelVisibilities(entity);
        super.render( entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
    }


    protected void renderName(TreasureGoblinEntity entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    }


    protected boolean canRenderName(TreasureGoblinEntity entity) {
        return false;
    }

    private void setModelVisibilities(TreasureGoblinEntity entity) {
        TreasureGoblinModel model = (TreasureGoblinModel) getModel();
        if (entity.isSpectator()) {
            model.setAllVisible(false);
            model.head.visible = true;
            model.hat.visible = true;
        } else {
            model.setAllVisible(true);


            model.crouching = entity.isCrouching();
            BipedModel.ArmPose bipedmodel$armpose = getArmPose(entity, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose1 = getArmPose(entity, Hand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose1 = entity.getOffhandItem().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
            }

            if (entity.getMainArm() == HandSide.RIGHT) {
                model.rightArmPose = bipedmodel$armpose;
                model.leftArmPose = bipedmodel$armpose1;
            } else {
                model.rightArmPose = bipedmodel$armpose1;
                model.leftArmPose = bipedmodel$armpose;
            }
        }
    }


    private static BipedModel.ArmPose getArmPose(TreasureGoblinEntity entity, Hand hand) {
        return BipedModel.ArmPose.EMPTY;
    }


    protected void applyRotations(TreasureGoblinEntity entityLiving, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = entityLiving.getSwimAmount(partialTicks);
        if (entityLiving.isFallFlying()) {
            super.setupRotations( entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
            float f1 = entityLiving.getFallFlyingTicks() + partialTicks;
            float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isAutoSpinAttack()) {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(f2 * (-90.0F - entityLiving.xRot)));
            }

            Vector3d vector3d = entityLiving.getViewVector(partialTicks);
            Vector3d vector3d1 = entityLiving.getDeltaMovement();
            double d0 = Entity.getHorizontalDistanceSqr(vector3d1);
            double d1 = Entity.getHorizontalDistanceSqr(vector3d);
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations( entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
            float f3 = entityLiving.isInWater() ? (-90.0F - entityLiving.xRot) : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(f4));
            if (entityLiving.isVisuallySwimming()) {
                matrixStack.translate(0.0D, -1.0D, 0.30000001192092896D);
            }
        } else {
            super.setupRotations( entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\renderer\TreasureGoblinRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */