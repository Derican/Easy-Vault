// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.function.Function;

public class ScavengerChestModel extends Model
{
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
        (this.chest = new ModelRenderer((Model)this)).setPos(0.0f, 16.0f, 0.0f);
        this.setRotationAngle(this.chest, 0.0f, 0.0f, -3.1416f);
        (this.lid = new ModelRenderer((Model)this)).setPos(0.0f, 1.0f, -7.0f);
        this.chest.addChild(this.lid);
        this.lid.texOffs(0, 43).addBox(-4.0f, 0.25f, -1.0f, 8.0f, 5.0f, 16.0f, 0.0f, false);
        this.lid.texOffs(0, 24).addBox(-7.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f, 0.0f, false);
        (this.horn_R3_r1 = new ModelRenderer((Model)this)).setPos(3.8366f, 3.3584f, 15.0f);
        this.lid.addChild(this.horn_R3_r1);
        this.setRotationAngle(this.horn_R3_r1, 0.0f, 0.0f, -0.3491f);
        this.horn_R3_r1.texOffs(42, 0).addBox(-0.4013f, -0.3725f, -1.6f, 1.0f, 2.0f, 2.0f, 0.0f, false);
        (this.horn_R2_r1 = new ModelRenderer((Model)this)).setPos(3.8366f, 3.3584f, 15.0f);
        this.lid.addChild(this.horn_R2_r1);
        this.setRotationAngle(this.horn_R2_r1, 0.0f, 0.0f, -0.7418f);
        this.horn_R2_r1.texOffs(42, 0).addBox(-0.8131f, -1.6893f, -1.3f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        (this.horn_R1_r1 = new ModelRenderer((Model)this)).setPos(3.8366f, 3.3584f, 15.0f);
        this.lid.addChild(this.horn_R1_r1);
        this.setRotationAngle(this.horn_R1_r1, 0.0f, 0.0f, -1.4399f);
        this.horn_R1_r1.texOffs(42, 0).addBox(0.0469f, -2.5778f, -1.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        (this.horn_L3_r1 = new ModelRenderer((Model)this)).setPos(-3.6253f, 3.3311f, 15.0f);
        this.lid.addChild(this.horn_L3_r1);
        this.setRotationAngle(this.horn_L3_r1, 0.0f, 0.0f, 0.3491f);
        this.horn_L3_r1.texOffs(42, 0).addBox(-0.5983f, -0.4446f, -1.35f, 1.0f, 2.0f, 2.0f, 0.0f, false);
        (this.horn_L2_r1 = new ModelRenderer((Model)this)).setPos(-3.6253f, 3.3311f, 15.0f);
        this.lid.addChild(this.horn_L2_r1);
        this.setRotationAngle(this.horn_L2_r1, 0.0f, 0.0f, 0.7418f);
        this.horn_L2_r1.texOffs(42, 0).addBox(-1.0105f, -1.9365f, -1.3f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        (this.horn_L1_r1 = new ModelRenderer((Model)this)).setPos(-3.6253f, 3.3311f, 15.0f);
        this.lid.addChild(this.horn_L1_r1);
        this.setRotationAngle(this.horn_L1_r1, 0.0f, 0.0f, 1.4399f);
        this.horn_L1_r1.texOffs(42, 0).addBox(-2.0469f, -2.5483f, -1.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        (this.angle_R_r1 = new ModelRenderer((Model)this)).setPos(-0.0127f, -0.2087f, 15.4708f);
        this.lid.addChild(this.angle_R_r1);
        this.setRotationAngle(this.angle_R_r1, -0.0295f, -0.0322f, -0.8286f);
        this.angle_R_r1.texOffs(2, 1).addBox(0.813f, -1.0545f, -0.7208f, 1.0f, 2.0f, 1.0f, 0.0f, false);
        (this.angle_L_r1 = new ModelRenderer((Model)this)).setPos(-0.0127f, -0.2087f, 15.4708f);
        this.lid.addChild(this.angle_L_r1);
        this.setRotationAngle(this.angle_L_r1, -0.0295f, 0.0322f, 0.8286f);
        this.angle_L_r1.texOffs(2, 0).addBox(-1.8568f, -1.0258f, -0.7208f, 1.0f, 2.0f, 1.0f, 0.0f, false);
        (this.nose_r1 = new ModelRenderer((Model)this)).setPos(-0.0127f, -0.2087f, 15.4708f);
        this.lid.addChild(this.nose_r1);
        this.setRotationAngle(this.nose_r1, -0.0436f, 0.0f, 0.0f);
        this.nose_r1.texOffs(0, 10).addBox(-0.9873f, -2.7913f, -0.4708f, 2.0f, 2.0f, 1.0f, 0.0f, false);
        (this.eyelarge_r1 = new ModelRenderer((Model)this)).setPos(-0.0127f, -0.2087f, 15.7208f);
        this.lid.addChild(this.eyelarge_r1);
        this.setRotationAngle(this.eyelarge_r1, -0.0436f, 0.0f, 0.0f);
        this.eyelarge_r1.texOffs(0, 29).addBox(0.5127f, 0.4587f, -0.4708f, 1.0f, 1.0f, 1.0f, 0.0f, false);
        this.eyelarge_r1.texOffs(0, 0).addBox(-1.9873f, -0.7913f, -1.7208f, 4.0f, 4.0f, 2.0f, 0.0f, false);
        (this.eyesmall_r1 = new ModelRenderer((Model)this)).setPos(-1.0f, 0.8221f, 16.2055f);
        this.lid.addChild(this.eyesmall_r1);
        this.setRotationAngle(this.eyesmall_r1, -0.0436f, 0.0f, 0.0f);
        this.eyesmall_r1.texOffs(0, 29).addBox(-0.5f, -0.5721f, -0.9555f, 1.0f, 1.0f, 1.0f, 0.0f, false);
        (this.bottom = new ModelRenderer((Model)this)).setPos(0.0f, 8.0f, 0.0f);
        this.chest.addChild(this.bottom);
        this.bottom.texOffs(40, 27).addBox(-4.0f, -16.0f, -8.0f, 8.0f, 9.0f, 16.0f, 0.0f, false);
        this.bottom.texOffs(0, 0).addBox(-7.0f, -16.0f, -7.0f, 14.0f, 10.0f, 14.0f, 0.0f, false);
    }
    
    private void setRotationAngle(final ModelRenderer model, final float x, final float y, final float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
    
    public void renderToBuffer(final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        this.lid.render(matrixStack, buffer, packedLight, packedOverlay);
        this.bottom.render(matrixStack, buffer, packedLight, packedOverlay);
    }
    
    public void setLidAngle(final float lidAngle) {
        this.lid.xRot = -(lidAngle * 1.5707964f);
    }
}
