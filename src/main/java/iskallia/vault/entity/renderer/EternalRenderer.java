package iskallia.vault.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import iskallia.vault.Vault;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.model.EternalModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EternalRenderer extends LivingRenderer<EternalEntity, EternalModel> {
    public EternalRenderer(final EntityRendererManager renderManager) {
        this(renderManager, false);
    }

    public EternalRenderer(final EntityRendererManager renderManager, final boolean useSmallArms) {
        super(renderManager, new EternalModel(0.0f, useSmallArms), 0.5f);
        this.addLayer(new BipedArmorLayer(this, new BipedModel(0.5f), new BipedModel(1.0f)));
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new ArrowLayer(this));
        this.addLayer(new HeadLayer(this));
        this.addLayer(new ElytraLayer(this));
        this.addLayer(new BeeStingerLayer(this));
    }

    protected void preRenderCallback(final EternalEntity entity, final MatrixStack matrixStack, final float partialTickTime) {
        final float f = entity.sizeMultiplier;
        matrixStack.scale(f, f, f);
    }

    public void render(final EternalEntity entity, final float entityYaw, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int packedLightIn) {
        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 0.5f);
        this.setModelVisibilities(entity);
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
    }

    public void renderCrown(final EternalEntity entity, final MatrixStack matrixStack, final IRenderTypeBuffer buffer) {
        matrixStack.pushPose();
        final float sizeMultiplier = entity.getSizeMultiplier();
        matrixStack.scale(sizeMultiplier, sizeMultiplier, sizeMultiplier);
        matrixStack.translate(0.0, 2.5, 0.0);
        final float scale = 2.5f;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) entity.tickCount));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(20.0f));
        final ItemStack itemStack = new ItemStack(Registry.ITEM.get(Vault.id("mvp_crown")));
        final IBakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemStack, null, null);
        Minecraft.getInstance().getItemRenderer().render(itemStack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, 15728864, 655360, ibakedmodel);
        matrixStack.popPose();
    }

    public Vector3d getRenderOffset(final EternalEntity entityIn, final float partialTicks) {
        return entityIn.isCrouching() ? new Vector3d(0.0, -0.125, 0.0) : super.getRenderOffset(entityIn, partialTicks);
    }

    private void setModelVisibilities(final EternalEntity clientPlayer) {
        final EternalModel playermodel = this.getModel();
        if (clientPlayer.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.crouching = clientPlayer.isCrouching();
            final BipedModel.ArmPose bipedmodel$armpose = getArmPose(clientPlayer, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose2 = getArmPose(clientPlayer, Hand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose2 = (clientPlayer.getOffhandItem().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM);
            }
            if (clientPlayer.getMainArm() == HandSide.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose2;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose2;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }
    }

    private static BipedModel.ArmPose getArmPose(final EternalEntity p_241741_0_, final Hand p_241741_1_) {
        final ItemStack itemstack = p_241741_0_.getItemInHand(p_241741_1_);
        if (itemstack.isEmpty()) {
            return BipedModel.ArmPose.EMPTY;
        }
        if (p_241741_0_.getUsedItemHand() == p_241741_1_ && p_241741_0_.getUseItemRemainingTicks() > 0) {
            final UseAction useaction = itemstack.getUseAnimation();
            if (useaction == UseAction.BLOCK) {
                return BipedModel.ArmPose.BLOCK;
            }
            if (useaction == UseAction.BOW) {
                return BipedModel.ArmPose.BOW_AND_ARROW;
            }
            if (useaction == UseAction.SPEAR) {
                return BipedModel.ArmPose.THROW_SPEAR;
            }
            if (useaction == UseAction.CROSSBOW && p_241741_1_ == p_241741_0_.getUsedItemHand()) {
                return BipedModel.ArmPose.CROSSBOW_CHARGE;
            }
        } else if (!p_241741_0_.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
            return BipedModel.ArmPose.CROSSBOW_HOLD;
        }
        return BipedModel.ArmPose.ITEM;
    }

    public ResourceLocation getTextureLocation(final EternalEntity entity) {
        return entity.getLocationSkin();
    }

    protected void preRenderCallback(final AbstractClientPlayerEntity entitylivingbaseIn, final MatrixStack matrixStackIn, final float partialTickTime) {
        final float f = 0.9375f;
        matrixStackIn.scale(0.9375f, 0.9375f, 0.9375f);
    }

    protected void renderName(final EternalEntity entityIn, final ITextComponent displayNameIn, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int packedLightIn) {
        final double d0 = this.entityRenderDispatcher.distanceToSqr(entityIn);
        matrixStackIn.pushPose();
        super.renderNameTag(entityIn, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    public void renderRightArm(final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn, final EternalEntity playerIn) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, this.model.rightArm, this.model.rightSleeve);
    }

    public void renderLeftArm(final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn, final EternalEntity playerIn) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, this.model.leftArm, this.model.leftSleeve);
    }

    private void renderItem(final MatrixStack matrixStackIn, final IRenderTypeBuffer buffer, final int combinedLight, final EternalEntity entity, final ModelRenderer rendererArm, final ModelRenderer rendererArmWear) {
        final EternalModel playermodel = this.getModel();
        this.setModelVisibilities(entity);
        playermodel.attackTime = 0.0f;
        playermodel.crouching = false;
        playermodel.setupAnim(entity, playermodel.swimAmount = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        rendererArm.xRot = 0.0f;
        rendererArm.render(matrixStackIn, buffer.getBuffer(RenderType.entitySolid(this.getTextureLocation(entity))), combinedLight, OverlayTexture.NO_OVERLAY);
        rendererArmWear.xRot = 0.0f;
        rendererArmWear.render(matrixStackIn, buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity))), combinedLight, OverlayTexture.NO_OVERLAY);
    }

    protected void applyRotations(final EternalEntity entityLiving, final MatrixStack matrixStack, final float ageInTicks, final float rotationYaw, final float partialTicks) {
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
                matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.signum(d4) * Math.acos(d3))));
            }
        } else if (f > 0.0f) {
            super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
            final float f4 = entityLiving.isInWater() ? (-90.0f - entityLiving.xRot) : -90.0f;
            final float f5 = MathHelper.lerp(f, 0.0f, f4);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(f5));
            if (entityLiving.isVisuallySwimming()) {
                matrixStack.translate(0.0, -1.0, 0.30000001192092896);
            }
        } else {
            super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        }
    }
}
