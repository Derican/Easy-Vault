package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ScubaArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 32 : 128);
            (this.Head = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(26, 6).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(5.0f, -3.25f, -5.0f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, -0.3491f, 0.0f, 0.0f);
            cube_r1.texOffs(0, 0).addBox(1.0f, -5.75f, -0.25f, 2.0f, 7.0f, 2.0f, 0.0f, false);
            cube_r1.texOffs(24, 0).addBox(-7.0f, 1.25f, -0.25f, 10.0f, 2.0f, 2.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(32, 44).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(0, 35).addBox(-8.0f, 8.0f, -6.0f, 16.0f, 3.0f, 4.0f, 0.0f, false);
            this.Body.texOffs(0, 28).addBox(-8.0f, 8.0f, 2.0f, 16.0f, 3.0f, 4.0f, 0.0f, false);
            this.Body.texOffs(32, 60).addBox(4.0f, 8.0f, -2.0f, 4.0f, 3.0f, 4.0f, 0.0f, false);
            this.Body.texOffs(58, 12).addBox(-8.0f, 8.0f, -2.0f, 4.0f, 3.0f, 4.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0f, 6.5f, 5.5f);
            this.Body.addChild(cube_r2);
            this.setRotationAngle(cube_r2, -0.6109f, 0.0f, 0.0f);
            cube_r2.texOffs(0, 14).addBox(-2.0f, -1.5f, 0.5f, 4.0f, 3.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0f, 5.0f, -4.0f);
            this.Body.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.48f, -0.0436f, 0.0f);
            cube_r3.texOffs(0, 18).addBox(-1.0f, -3.0f, -8.0f, 2.0f, 1.0f, 1.0f, 0.0f, false);
            cube_r3.texOffs(34, 22).addBox(-1.0f, -5.0f, -8.0f, 2.0f, 2.0f, 4.0f, 0.0f, false);
            cube_r3.texOffs(50, 0).addBox(-2.0f, -5.0f, -4.0f, 4.0f, 9.0f, 3.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model) this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(56, 44).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 14).addBox(-6.0f, 2.0f, -5.0f, 7.0f, 4.0f, 10.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model) this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(0, 0).addBox(-1.0f, 2.0f, -5.0f, 7.0f, 4.0f, 10.0f, 0.0f, false);
            this.LeftArm.texOffs(16, 55).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            (this.RightBoot = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(0, 55).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0f, 11.5f, -4.5f);
            this.RightBoot.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.48f, 0.0f);
            cube_r4.texOffs(0, 42).addBox(-4.5f, -0.5f, -5.5f, 5.0f, 2.0f, 11.0f, 0.0f, false);
            (this.LeftBoot = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(50, 22).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.2f, 11.5f, -4.5f);
            this.LeftBoot.addChild(cube_r5);
            this.setRotationAngle(cube_r5, 0.0f, -0.5236f, 0.0f);
            cube_r5.texOffs(29, 31).addBox(-0.25f, -0.5f, -5.75f, 5.0f, 2.0f, 11.0f, 0.0f, false);
            (this.Belt = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }
}
