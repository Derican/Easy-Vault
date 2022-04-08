package iskallia.vault.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import iskallia.vault.entity.TreasureGoblinEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class TreasureGoblinModel extends PlayerModel<TreasureGoblinEntity> {
    public TreasureGoblinModel() {
        super(1.0F, false);
        this.texWidth = 64;
        this.texHeight = 64;

        this.head = new ModelRenderer((Model) this);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.head.texOffs(0, 26).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        this.head.texOffs(0, 21).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer ear6_r1 = new ModelRenderer((Model) this);
        ear6_r1.setPos(6.375F, -4.875F, 2.125F);
        this.head.addChild(ear6_r1);
        setRotationAngle(ear6_r1, 0.0F, 0.3927F, 0.0F);
        ear6_r1.texOffs(0, 0).addBox(-0.125F, -2.125F, 0.875F, 0.0F, 1.0F, 2.0F, 0.0F, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125F, -1.125F, -1.125F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125F, -0.125F, -2.125F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        ear6_r1.texOffs(0, 0).addBox(-0.125F, 0.875F, -3.125F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        ModelRenderer ear5_r1 = new ModelRenderer((Model) this);
        ear5_r1.setPos(-6.625F, -4.875F, 2.125F);
        this.head.addChild(ear5_r1);
        setRotationAngle(ear5_r1, 0.0F, -0.7854F, 0.0F);
        ear5_r1.texOffs(0, 0).addBox(-0.125F, -2.125F, 0.875F, 0.0F, 1.0F, 2.0F, 0.0F, false);
        ear5_r1.texOffs(0, 0).addBox(-0.125F, -1.125F, -1.125F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        ear5_r1.texOffs(0, 0).addBox(-0.125F, -0.125F, -2.125F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        ear5_r1.texOffs(0, 1).addBox(-0.125F, 0.875F, -3.125F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        this.body = new ModelRenderer((Model) this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F);
        this.body.setPos(0.0F, 0.0F, 0.0F);

        this.rightArm = new ModelRenderer((Model) this, 40, 16);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);

        this.leftArm = new ModelRenderer((Model) this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);

        this.rightLeg = new ModelRenderer((Model) this, 0, 16);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);

        this.leftLeg = new ModelRenderer((Model) this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.leftLeg.setPos(1.9F, 12.0F, 0.0F);
    }


    public void setRotationAngles(TreasureGoblinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netbipedHeadYaw, float bipedHeadPitch) {
        super.setupAnim( entity, limbSwing, limbSwingAmount, ageInTicks, netbipedHeadYaw, bipedHeadPitch);
    }


    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();


        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\model\TreasureGoblinModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */