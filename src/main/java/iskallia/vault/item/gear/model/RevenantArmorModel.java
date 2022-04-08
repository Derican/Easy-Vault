package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RevenantArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 64 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(8, 63).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(62, 49).addBox(-2.0F, -12.0F, -6.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -11.0F, 0.0F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.4363F, 0.0F, 0.0F);
            cube_r1.texOffs(52, 61).addBox(-7.0F, -8.0F, -3.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(44, 61).addBox(-8.0F, -5.0F, -3.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(24, 0).addBox(-9.0F, 2.0F, -3.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(20, 16).addBox(-8.0F, 4.0F, -3.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(12, 67).addBox(6.0F, -8.0F, -3.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(0, 63).addBox(6.0F, -5.0F, -3.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(40, 27).addBox(6.0F, 2.0F, -3.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(44, 11).addBox(5.0F, 4.0F, -3.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(60, 0).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(46, 15).addBox(-3.0F, 5.0F, -3.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 0).addBox(1.0F, 5.0F, -3.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 32).addBox(-3.0F, 5.0F, 3.0F, 6.0F, 4.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, 11.0F, 3.5F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.5236F, 0.0F, 0.0F);
            cube_r2.texOffs(16, 47).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 3.0F, 4.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.6109F, 0.0F, 0.0F);
            cube_r3.texOffs(0, 38).addBox(-4.0F, -2.0F, -3.5F, 8.0F, 4.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0F, 5.5F, -2.0F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.48F, 0.0F, 0.0F);
            cube_r4.texOffs(32, 0).addBox(-5.0F, -4.5F, -2.0F, 10.0F, 7.0F, 4.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(16, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(26, 38).addBox(-5.0F, 5.0F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-7.4444F, -1.5F, 0.0F);
            this.RightArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -0.5236F);
            cube_r5.texOffs(58, 44).addBox(2.4444F, 0.4F, 3.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(68, 13).addBox(-3.5556F, 2.4F, 3.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(40, 31).addBox(-5.5556F, 2.4F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(52, 66).addBox(-1.5556F, 1.4F, 3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(26, 43).addBox(-5.5556F, 2.4F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(68, 16).addBox(-3.5556F, 2.4F, -4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(62, 66).addBox(-1.5556F, 1.4F, -4.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(32, 60).addBox(2.4444F, 0.4F, -4.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(58, 57).addBox(-1.5556F, -1.6F, -2.0F, 7.0F, 5.0F, 4.0F, 0.0F, false);
            cube_r5.texOffs(18, 27).addBox(-0.5556F, -0.6F, -3.0F, 8.0F, 5.0F, 6.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(52, 11).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(39, 31).addBox(2.0F, 5.0F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(7.3333F, -1.5F, 0.0F);
            this.LeftArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, 0.5236F);
            cube_r6.texOffs(52, 27).addBox(-5.5833F, -1.5F, -2.0F, 7.0F, 5.0F, 4.0F, 0.0F, false);
            cube_r6.texOffs(24, 16).addBox(-7.5833F, -0.5F, -3.0F, 8.0F, 5.0F, 6.0F, 0.0F, false);
            cube_r6.texOffs(32, 11).addBox(-7.5833F, 0.5F, -4.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(21, 38).addBox(-7.5833F, 0.5F, 3.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(64, 8).addBox(-2.5833F, 1.5F, 3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(32, 65).addBox(-2.5833F, 1.5F, -4.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(24, 4).addBox(1.4167F, 2.5F, -4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(0, 6).addBox(3.4167F, 2.5F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(50, 27).addBox(1.4167F, 2.5F, 3.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(24, 20).addBox(3.4167F, 2.5F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 47).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(59, 36).addBox(-2.0F, 6.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(46, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftBoot.texOffs(32, 52).addBox(-1.8F, 6.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 8).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(24, 8).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(26, 3).addBox(-2.0F, 11.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(26, 0).addBox(1.0F, 11.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(34, 7).addBox(-3.0F, 9.0F, -4.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(24, 15).addBox(-4.0F, 10.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 24).addBox(-6.25F, 10.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(32, 0).addBox(-5.25F, 9.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(16, 31).addBox(-5.25F, 9.0F, -2.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(20, 8).addBox(-6.25F, 10.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 0).addBox(5.25F, 10.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 8).addBox(5.25F, 10.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(16, 24).addBox(4.25F, 9.0F, -2.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(30, 8).addBox(4.25F, 9.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(12, 24).addBox(3.0F, 10.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(32, 15).addBox(2.0F, 9.0F, -4.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 0).addBox(-5.0F, 11.0F, -3.0F, 10.0F, 2.0F, 6.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(20, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 64 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 28).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 28).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(62, 42).addBox(-2.0F, -12.0F, -6.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 34).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(32, 59).addBox(-2.0F, 5.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 14).addBox(-3.0F, 5.0F, -3.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 0).addBox(1.0F, 5.0F, -3.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(28, 0).addBox(-3.0F, 5.0F, 3.0F, 6.0F, 4.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, 11.0F, 3.5F);
            this.Body.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.5236F, 0.0F, 0.0F);
            cube_r1.texOffs(61, 64).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, 3.0F, 4.5F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.6109F, 0.0F, 0.0F);
            cube_r2.texOffs(23, 50).addBox(-4.0F, -2.0F, -3.5F, 8.0F, 4.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 5.5F, -2.0F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.48F, 0.0F, 0.0F);
            cube_r3.texOffs(0, 44).addBox(-5.0F, -4.5F, -2.0F, 10.0F, 7.0F, 4.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(60, 14).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(49, 43).addBox(-5.0F, 5.0F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-7.4444F, -1.5F, 0.0F);
            this.RightArm.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -0.5236F);
            cube_r4.texOffs(30, 22).addBox(-3.5556F, -2.6F, -3.25F, 9.0F, 6.0F, 6.0F, 0.0F, false);
            cube_r4.texOffs(0, 14).addBox(-2.5556F, -1.6F, -4.25F, 10.0F, 6.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 59).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(54, 0).addBox(2.0F, 5.0F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(7.3333F, -1.5F, 0.0F);
            this.LeftArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, 0.5236F);
            cube_r5.texOffs(30, 8).addBox(-5.5833F, -2.5F, -3.25F, 9.0F, 6.0F, 6.0F, 0.0F, false);
            cube_r5.texOffs(0, 0).addBox(-7.5833F, -1.5F, -4.25F, 10.0F, 6.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(45, 57).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(56, 34).addBox(-2.0F, 6.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 55).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftBoot.texOffs(44, 0).addBox(-1.8F, 6.0F, -4.0F, 4.0F, 7.0F, 1.0F, 0.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 8).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(24, 8).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(19, 0).addBox(-2.0F, 9.0F, -4.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(20, 0).addBox(1.0F, 9.0F, -4.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(34, 7).addBox(-3.0F, 9.0F, -4.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(24, 15).addBox(-4.0F, 10.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 24).addBox(-6.25F, 10.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(32, 0).addBox(-5.25F, 9.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(16, 31).addBox(-5.25F, 9.0F, -2.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(20, 8).addBox(-6.25F, 10.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 0).addBox(5.25F, 10.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 8).addBox(5.25F, 10.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(16, 24).addBox(4.25F, 9.0F, -2.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(30, 8).addBox(4.25F, 9.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(12, 24).addBox(3.0F, 10.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(32, 15).addBox(2.0F, 9.0F, -4.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(0, 0).addBox(-5.0F, 11.0F, -3.0F, 10.0F, 2.0F, 6.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(20, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\RevenantArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */