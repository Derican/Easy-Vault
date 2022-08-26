package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class HellcowArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public HellcowArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (64);
        this.texHeight = (64);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(44, 0).addBox(-2.0f, -1.0f, -6.0f, 4.0f, 2.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0f, 2.5f, -6.0f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, -0.6109f, 0.0f, 0.0f);
        cube_r1.texOffs(16, 36).addBox(-2.0f, -1.75f, -0.75f, 4.0f, 3.0f, 0.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0f, -8.0f, 0.0f);
        this.Head.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 1.0472f, 0.0f, 0.0f);
        cube_r2.texOffs(12, 32).addBox(5.0f, 0.0f, -3.0f, 4.0f, 2.0f, 2.0f, 0.0f, false);
        cube_r2.texOffs(0, 0).addBox(9.0f, -4.0f, -3.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        cube_r2.texOffs(24, 0).addBox(-11.0f, -4.0f, -3.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        cube_r2.texOffs(16, 41).addBox(-9.0f, 0.0f, -3.0f, 4.0f, 2.0f, 2.0f, 0.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(40, 25).addBox(-3.0f, 6.0f, -4.0f, 6.0f, 6.0f, 1.0f, 0.0f, false);
        this.Body.texOffs(24, 16).addBox(-5.0f, 1.0f, 3.0f, 10.0f, 7.0f, 2.0f, 0.0f, false);
        this.Body.texOffs(40, 32).addBox(-3.0f, 8.0f, 3.0f, 6.0f, 4.0f, 1.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(36, 37).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(32, 0).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(24, 25).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(0, 0).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        final ModelRenderer cube_r1_l2 = new ModelRenderer(this);
        cube_r1_l2.setPos(0.0f, 16.8221f, 6.7705f);
        this.Belt.addChild(cube_r1_l2);
        this.setRotationAngle(cube_r1_l2, -0.3491f, 0.0f, 0.0f);
        cube_r1_l2.texOffs(12, 16).addBox(-0.5f, -1.5f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r2_l2 = new ModelRenderer(this);
        cube_r2_l2.setPos(-0.25f, 11.5f, 4.0f);
        this.Belt.addChild(cube_r2_l2);
        this.setRotationAngle(cube_r2_l2, -1.0908f, 0.0f, 0.0f);
        cube_r2_l2.texOffs(24, 0).addBox(-1.25f, -0.5f, 2.0f, 3.0f, 2.0f, 4.0f, 0.0f, false);
        cube_r2_l2.texOffs(24, 6).addBox(-0.25f, 0.5f, -2.0f, 1.0f, 1.0f, 4.0f, 0.0f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(16, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
    }
}
