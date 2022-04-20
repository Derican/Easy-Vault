package iskallia.vault.item.gear.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class VaultGearModel<T extends LivingEntity> extends BipedModel<T> {
    protected static final float VOXEL_SIZE = 0.0625f;
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

    public VaultGearModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, 0.0f, 64, 32);
        this.slotType = slotType;
    }

    public boolean isLayer2() {
        return this.slotType == EquipmentSlotType.LEGS;
    }

    protected void prepareForRender(final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
    }

    public void renderToBuffer(final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        matrixStack.pushPose();
        this.prepareForRender(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.slotType == EquipmentSlotType.HEAD) {
            this.renderWithModelAngles(this.Head, this.head, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.CHEST) {
            this.renderWithModelAngles(this.Body, this.body, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderWithModelAngles(this.RightArm, this.rightArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderWithModelAngles(this.LeftArm, this.leftArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.LEGS) {
            this.renderWithModelAngles(this.Belt, this.body, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderWithModelAngles(this.RightLeg, this.rightLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderWithModelAngles(this.LeftLeg, this.leftLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slotType == EquipmentSlotType.FEET) {
            this.renderWithModelAngles(this.RightBoot, this.rightLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderWithModelAngles(this.LeftBoot, this.leftLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        matrixStack.popPose();
    }

    private void renderWithModelAngles(final ModelRenderer renderer, final ModelRenderer target, final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        if (renderer == null || target == null) {
            return;
        }
        renderer.copyFrom(target);
        renderer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
