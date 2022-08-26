package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class BuildingArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public BuildingArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (64);
        this.texHeight = (64);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 13).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(0, 0).addBox(-6.0f, -6.0f, -6.0f, 12.0f, 1.0f, 12.0f, 0.0f, false);
        this.Head.texOffs(21, 18).addBox(-2.0f, -10.0f, -5.5f, 4.0f, 4.0f, 11.0f, 0.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 29).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(0, 45).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(40, 33).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(40, 13).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(24, 33).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(0, 0).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        this.Belt.texOffs(27, 27).addBox(-6.0f, 12.0f, -3.0f, 4.0f, 5.0f, 5.0f, 0.0f, false);
        this.Belt.texOffs(24, 0).addBox(2.0f, 12.0f, -3.0f, 4.0f, 5.0f, 5.0f, 0.0f, false);
        this.Belt.texOffs(28, 15).addBox(-2.0f, 11.0f, -3.0f, 4.0f, 3.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r1_l2 = new ModelRenderer(this);
        cube_r1_l2.setPos(5.0f, 11.6f, 0.25f);
        this.Belt.addChild(cube_r1_l2);
        this.setRotationAngle(cube_r1_l2, -0.2618f, 0.0f, 0.0f);
        cube_r1_l2.texOffs(24, 0).addBox(-0.5f, -2.0f, -0.5f, 1.0f, 4.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r2_l2 = new ModelRenderer(this);
        cube_r2_l2.setPos(5.0f, 11.6f, -1.0f);
        this.Belt.addChild(cube_r2_l2);
        this.setRotationAngle(cube_r2_l2, 0.3054f, 0.0f, 0.0f);
        cube_r2_l2.texOffs(0, 32).addBox(-0.5f, -2.0f, -0.5f, 1.0f, 4.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r3_l2 = new ModelRenderer(this);
        cube_r3_l2.setPos(-5.0f, 10.25f, 0.5f);
        this.Belt.addChild(cube_r3_l2);
        this.setRotationAngle(cube_r3_l2, 0.5672f, 0.0f, 0.0f);
        cube_r3_l2.texOffs(24, 10).addBox(-1.0f, -3.25f, -4.5f, 2.0f, 2.0f, 3.0f, 0.0f, false);
        cube_r3_l2.texOffs(4, 32).addBox(-0.5f, -1.25f, -3.5f, 1.0f, 4.0f, 1.0f, 0.0f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(16, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
    }
}
