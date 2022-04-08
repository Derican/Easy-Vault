package iskallia.vault.item.gear.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;


public abstract class VaultGearModel<T extends LivingEntity>
        extends BipedModel<T> {
    protected static final float VOXEL_SIZE = 0.0625F;
    protected final EquipmentSlotType slotType;
    protected ModelRenderer Head;
    protected ModelRenderer Body;
    protected ModelRenderer RightArm;
    protected ModelRenderer LeftArm;
    protected ModelRenderer RightLeg;
    protected ModelRenderer LeftLeg;
    protected ModelRenderer Belt;
    protected ModelRenderer RightBoot;
    protected ModelRenderer LeftBoot;

    public VaultGearModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, 0.0F, 64, 32);
        this.slotType = slotType;
    }


    public boolean isLayer2() {
        return (this.slotType == EquipmentSlotType.LEGS);
    }


    protected void prepareForRender(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();

        prepareForRender(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (this.slotType == EquipmentSlotType.HEAD) {
            renderWithModelAngles(this.Head, this.head, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.CHEST) {
            renderWithModelAngles(this.Body, this.body, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            renderWithModelAngles(this.RightArm, this.rightArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            renderWithModelAngles(this.LeftArm, this.leftArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.LEGS) {
            renderWithModelAngles(this.Belt, this.body, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            renderWithModelAngles(this.RightLeg, this.rightLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            renderWithModelAngles(this.LeftLeg, this.leftLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.FEET) {
            renderWithModelAngles(this.RightBoot, this.rightLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            renderWithModelAngles(this.LeftBoot, this.leftLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        matrixStack.popPose();
    }

    private void renderWithModelAngles(ModelRenderer renderer, ModelRenderer target, MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (renderer == null || target == null)
            return;
        renderer.copyFrom(target);
        renderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\VaultGearModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */