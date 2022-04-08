package iskallia.vault.block.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;


public class ScavengerChestModel
        extends Model {
    private final ModelRenderer chest;
    private final ModelRenderer horn_R3_r1;
    private final ModelRenderer horn_R2_r1;
    private final ModelRenderer horn_R1_r1;
    private final ModelRenderer horn_L3_r1;
    private final ModelRenderer horn_L2_r1;
    private final ModelRenderer horn_L1_r1;
    private final ModelRenderer angle_R_r1;
    private final ModelRenderer angle_L_r1;
    private final ModelRenderer nose_r1;
    private final ModelRenderer eyelarge_r1;
    private final ModelRenderer eyesmall_r1;
    private final ModelRenderer lid;
    private final ModelRenderer bottom;

    public ScavengerChestModel() {
        super(RenderType::entityCutout);
        this.texWidth = 128;
        this.texHeight = 128;

        this.chest = new ModelRenderer(this);
        this.chest.setPos(0.0F, 16.0F, 0.0F);
        setRotationAngle(this.chest, 0.0F, 0.0F, -3.1416F);

        this.lid = new ModelRenderer(this);
        this.lid.setPos(0.0F, 1.0F, -7.0F);
        this.chest.addChild(this.lid);
        this.lid.texOffs(0, 43).addBox(-4.0F, 0.25F, -1.0F, 8.0F, 5.0F, 16.0F, 0.0F, false);
        this.lid.texOffs(0, 24).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F, false);

        this.horn_R3_r1 = new ModelRenderer(this);
        this.horn_R3_r1.setPos(3.8366F, 3.3584F, 15.0F);
        this.lid.addChild(this.horn_R3_r1);
        setRotationAngle(this.horn_R3_r1, 0.0F, 0.0F, -0.3491F);
        this.horn_R3_r1.texOffs(42, 0).addBox(-0.4013F, -0.3725F, -1.6F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        this.horn_R2_r1 = new ModelRenderer(this);
        this.horn_R2_r1.setPos(3.8366F, 3.3584F, 15.0F);
        this.lid.addChild(this.horn_R2_r1);
        setRotationAngle(this.horn_R2_r1, 0.0F, 0.0F, -0.7418F);
        this.horn_R2_r1.texOffs(42, 0).addBox(-0.8131F, -1.6893F, -1.3F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.horn_R1_r1 = new ModelRenderer(this);
        this.horn_R1_r1.setPos(3.8366F, 3.3584F, 15.0F);
        this.lid.addChild(this.horn_R1_r1);
        setRotationAngle(this.horn_R1_r1, 0.0F, 0.0F, -1.4399F);
        this.horn_R1_r1.texOffs(42, 0).addBox(0.0469F, -2.5778F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.horn_L3_r1 = new ModelRenderer(this);
        this.horn_L3_r1.setPos(-3.6253F, 3.3311F, 15.0F);
        this.lid.addChild(this.horn_L3_r1);
        setRotationAngle(this.horn_L3_r1, 0.0F, 0.0F, 0.3491F);
        this.horn_L3_r1.texOffs(42, 0).addBox(-0.5983F, -0.4446F, -1.35F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        this.horn_L2_r1 = new ModelRenderer(this);
        this.horn_L2_r1.setPos(-3.6253F, 3.3311F, 15.0F);
        this.lid.addChild(this.horn_L2_r1);
        setRotationAngle(this.horn_L2_r1, 0.0F, 0.0F, 0.7418F);
        this.horn_L2_r1.texOffs(42, 0).addBox(-1.0105F, -1.9365F, -1.3F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.horn_L1_r1 = new ModelRenderer(this);
        this.horn_L1_r1.setPos(-3.6253F, 3.3311F, 15.0F);
        this.lid.addChild(this.horn_L1_r1);
        setRotationAngle(this.horn_L1_r1, 0.0F, 0.0F, 1.4399F);
        this.horn_L1_r1.texOffs(42, 0).addBox(-2.0469F, -2.5483F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        this.angle_R_r1 = new ModelRenderer(this);
        this.angle_R_r1.setPos(-0.0127F, -0.2087F, 15.4708F);
        this.lid.addChild(this.angle_R_r1);
        setRotationAngle(this.angle_R_r1, -0.0295F, -0.0322F, -0.8286F);
        this.angle_R_r1.texOffs(2, 1).addBox(0.813F, -1.0545F, -0.7208F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.angle_L_r1 = new ModelRenderer(this);
        this.angle_L_r1.setPos(-0.0127F, -0.2087F, 15.4708F);
        this.lid.addChild(this.angle_L_r1);
        setRotationAngle(this.angle_L_r1, -0.0295F, 0.0322F, 0.8286F);
        this.angle_L_r1.texOffs(2, 0).addBox(-1.8568F, -1.0258F, -0.7208F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.nose_r1 = new ModelRenderer(this);
        this.nose_r1.setPos(-0.0127F, -0.2087F, 15.4708F);
        this.lid.addChild(this.nose_r1);
        setRotationAngle(this.nose_r1, -0.0436F, 0.0F, 0.0F);
        this.nose_r1.texOffs(0, 10).addBox(-0.9873F, -2.7913F, -0.4708F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        this.eyelarge_r1 = new ModelRenderer(this);
        this.eyelarge_r1.setPos(-0.0127F, -0.2087F, 15.7208F);
        this.lid.addChild(this.eyelarge_r1);
        setRotationAngle(this.eyelarge_r1, -0.0436F, 0.0F, 0.0F);
        this.eyelarge_r1.texOffs(0, 29).addBox(0.5127F, 0.4587F, -0.4708F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.eyelarge_r1.texOffs(0, 0).addBox(-1.9873F, -0.7913F, -1.7208F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        this.eyesmall_r1 = new ModelRenderer(this);
        this.eyesmall_r1.setPos(-1.0F, 0.8221F, 16.2055F);
        this.lid.addChild(this.eyesmall_r1);
        setRotationAngle(this.eyesmall_r1, -0.0436F, 0.0F, 0.0F);
        this.eyesmall_r1.texOffs(0, 29).addBox(-0.5F, -0.5721F, -0.9555F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.bottom = new ModelRenderer(this);
        this.bottom.setPos(0.0F, 8.0F, 0.0F);
        this.chest.addChild(this.bottom);
        this.bottom.texOffs(40, 27).addBox(-4.0F, -16.0F, -8.0F, 8.0F, 9.0F, 16.0F, 0.0F, false);
        this.bottom.texOffs(0, 0).addBox(-7.0F, -16.0F, -7.0F, 14.0F, 10.0F, 14.0F, 0.0F, false);
    }

    private void setRotationAngle(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }


    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.lid.render(matrixStack, buffer, packedLight, packedOverlay);
        this.bottom.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setLidAngle(float lidAngle) {
        this.lid.xRot = -(lidAngle * 1.5707964F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\model\ScavengerChestModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */