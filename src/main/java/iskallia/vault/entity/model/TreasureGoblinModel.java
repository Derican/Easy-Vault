package iskallia.vault.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.entity.TreasureGoblinEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TreasureGoblinModel extends PlayerModel<TreasureGoblinEntity> {
    public TreasureGoblinModel() {
        super(1.0f, false);
        this.texWidth = 64;
        this.texHeight = 64;
        (this.head = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
        this.head.texOffs(0, 0).addBox(-4.0f, -7.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.head.texOffs(0, 26).addBox(-1.0f, -2.0f, -7.0f, 2.0f, 4.0f, 2.0f, 0.0f, false);
        this.head.texOffs(0, 21).addBox(-4.0f, -4.0f, -6.0f, 8.0f, 1.0f, 1.0f, 0.0f, false);
        final ModelRenderer ear6_r1 = new ModelRenderer((Model) this);
        ear6_r1.setPos(6.375f, -4.875f, 2.125f);
        this.head.addChild(ear6_r1);
        this.setRotationAngle(ear6_r1, 0.0f, 0.3927f, 0.0f);
        ear6_r1.texOffs(0, 0).addBox(-0.125f, -2.125f, 0.875f, 0.0f, 1.0f, 2.0f, 0.0f, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125f, -1.125f, -1.125f, 0.0f, 1.0f, 3.0f, 0.0f, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125f, -0.125f, -2.125f, 0.0f, 1.0f, 3.0f, 0.0f, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125f, 0.875f, -3.125f, 0.0f, 2.0f, 3.0f, 0.0f, false);
        final ModelRenderer ear5_r1 = new ModelRenderer((Model) this);
        ear5_r1.setPos(-6.625f, -4.875f, 2.125f);
        this.head.addChild(ear5_r1);
        this.setRotationAngle(ear5_r1, 0.0f, -0.7854f, 0.0f);
        ear5_r1.texOffs(0, 0).addBox(-0.125f, -2.125f, 0.875f, 0.0f, 1.0f, 2.0f, 0.0f, false);
        ear5_r1.texOffs(0, 0).addBox(-0.125f, -1.125f, -1.125f, 0.0f, 1.0f, 3.0f, 0.0f, false);
        ear5_r1.texOffs(0, 0).addBox(-0.125f, -0.125f, -2.125f, 0.0f, 1.0f, 3.0f, 0.0f, false);
        ear5_r1.texOffs(0, 1).addBox(-0.125f, 0.875f, -3.125f, 0.0f, 2.0f, 3.0f, 0.0f, false);
        (this.body = new ModelRenderer((Model) this, 16, 16)).setPos(0.0f, 0.0f, 0.0f);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f);
        this.body.setPos(0.0f, 0.0f, 0.0f);
        (this.rightArm = new ModelRenderer((Model) this, 40, 16)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f);
        this.rightArm.setPos(-5.0f, 2.0f, 0.0f);
        this.leftArm = new ModelRenderer((Model) this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f);
        this.leftArm.setPos(5.0f, 2.0f, 0.0f);
        (this.rightLeg = new ModelRenderer((Model) this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f);
        this.rightLeg.setPos(-1.9f, 12.0f, 0.0f);
        this.leftLeg = new ModelRenderer((Model) this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f);
        this.leftLeg.setPos(1.9f, 12.0f, 0.0f);
    }

    public void setRotationAngles(final TreasureGoblinEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netbipedHeadYaw, final float bipedHeadPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netbipedHeadYaw, bipedHeadPitch);
    }

    public void renderToBuffer(final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        matrixStack.pushPose();
        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
    }

    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
