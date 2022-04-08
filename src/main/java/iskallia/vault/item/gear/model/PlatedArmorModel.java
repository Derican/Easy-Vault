package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PlatedArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-1.0F, -10.75F, -5.75F, 2.0F, 7.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 29).addBox(-1.0F, -10.75F, -4.75F, 2.0F, 1.0F, 11.0F, 0.0F, false);
            this.Head.texOffs(20, 31).addBox(-0.4F, -11.75F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(20, 31).addBox(-0.4F, -11.75F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(20, 31).addBox(-0.4F, -11.75F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(15, 26).addBox(-5.9F, -12.25F, -3.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(15, 26).addBox(5.1F, -12.25F, -3.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(21, 14).addBox(-1.0F, -9.75F, 5.25F, 2.0F, 6.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5F, -8.0F, -5.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 1.2654F);
            cube_r1.texOffs(29, 3).addBox(-2.0F, -1.75F, -0.5F, 1.0F, 1.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-4.5F, -8.0F, -5.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3491F);
            cube_r2.texOffs(29, 3).addBox(-1.75F, -2.0F, -0.5F, 1.0F, 1.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(5.6F, -8.0F, 2.0F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.0F, 0.5236F);
            cube_r3.texOffs(0, 14).addBox(-0.5F, -4.25F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-5.4F, -8.0F, 2.0F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -0.5672F);
            cube_r4.texOffs(0, 14).addBox(-0.5F, -4.25F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

            ModelRenderer helmet2 = new ModelRenderer((Model) this);
            helmet2.setPos(0.0F, 0.0F, 0.0F);
            this.Head.addChild(helmet2);
            helmet2.texOffs(28, 15).addBox(-5.5F, -9.75F, 4.5F, 11.0F, 8.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(48, 25).addBox(-4.5F, -9.75F, -5.5F, 9.0F, 5.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 0).addBox(-4.5F, -4.75F, -5.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 0).addBox(-5.5F, -1.75F, -4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 4).addBox(-5.5F, -1.75F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 4).addBox(4.5F, -1.75F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 0).addBox(4.5F, -1.75F, -4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(0, 34).addBox(-5.5F, -1.75F, -5.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(0, 34).addBox(1.5F, -1.75F, -5.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            helmet2.texOffs(32, 0).addBox(3.5F, -4.75F, -5.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer Helmet6_r1 = new ModelRenderer((Model) this);
            Helmet6_r1.setPos(0.0F, 0.0F, 0.0F);
            helmet2.addChild(Helmet6_r1);
            setRotationAngle(Helmet6_r1, -1.5708F, 0.0F, 0.0F);
            Helmet6_r1.texOffs(43, 43).addBox(-4.5F, -4.5F, -9.75F, 9.0F, 9.0F, 1.0F, 0.0F, false);

            ModelRenderer Helmet4_r1 = new ModelRenderer((Model) this);
            Helmet4_r1.setPos(0.0F, 0.0F, 0.0F);
            helmet2.addChild(Helmet4_r1);
            setRotationAngle(Helmet4_r1, 0.0F, 1.5708F, 0.0F);
            Helmet4_r1.texOffs(42, 0).addBox(-4.5F, -9.75F, -5.5F, 10.0F, 8.0F, 1.0F, 0.0F, false);
            Helmet4_r1.texOffs(42, 0).addBox(-4.5F, -9.75F, 4.5F, 10.0F, 8.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(24, 24).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 6.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(0, 28).addBox(1.0F, -1.0F, -5.0F, 4.0F, 5.0F, 1.0F, 0.0F, true);
            this.Body.texOffs(0, 28).addBox(-5.0F, -1.0F, -5.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(15, 32).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(40, 32).addBox(-6.0F, 4.0F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(40, 32).addBox(2.0F, 4.0F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, true);
            this.Body.texOffs(20, 35).addBox(-2.0F, 4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(20, 35).addBox(-2.0F, 4.0F, 3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(19, 26).addBox(-1.0F, 4.0F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(19, 26).addBox(-1.0F, 4.0F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(20, 35).addBox(1.0F, 4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(20, 35).addBox(1.0F, 4.0F, 3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer Innerchestplate = new ModelRenderer((Model) this);
            Innerchestplate.setPos(0.0F, 0.0F, 0.0F);
            this.Body.addChild(Innerchestplate);
            Innerchestplate.texOffs(0, 26).addBox(-3.0F, 11.75F, -3.25F, 6.0F, 1.0F, 1.0F, 0.0F, false);
            Innerchestplate.texOffs(16, 52).addBox(-4.0F, 5.0F, -3.5F, 8.0F, 7.0F, 1.0F, 0.0F, false);
            Innerchestplate.texOffs(16, 52).addBox(-4.0F, 5.0F, 2.5F, 8.0F, 7.0F, 1.0F, 0.0F, false);
            Innerchestplate.texOffs(0, 26).addBox(-3.0F, 11.75F, 2.25F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(0, 41).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 14).addBox(-7.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, true);
            this.RightArm.texOffs(19, 40).addBox(-7.0F, 3.0F, -3.5F, 5.0F, 5.0F, 7.0F, 0.0F, true);
            this.RightArm.texOffs(46, 18).addBox(-6.0F, -5.0F, -3.0F, 4.0F, 1.0F, 6.0F, 0.0F, true);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 41).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(46, 18).addBox(2.0F, -5.0F, -3.0F, 4.0F, 1.0F, 6.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 14).addBox(0.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(19, 40).addBox(1.75F, 3.0F, -3.5F, 5.0F, 5.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(52, 11).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(52, 11).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
            this.RightLeg.texOffs(19, 1).addBox(-3.25F, -2.0F, -3.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.25F, 4.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 0).addBox(1.2F, 4.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
            this.LeftLeg.texOffs(0, 0).addBox(1.2F, -2.0F, -3.0F, 2.0F, 6.0F, 6.0F, 0.0F, false);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(78, 0).addBox(-4.0F, -7.0F, -4.25F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(20, 48).addBox(-6.25F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(20, 48).addBox(5.25F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 59).addBox(-6.25F, -1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 59).addBox(3.25F, -1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-2.75F, 0.5F, -5.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, -0.3927F, 0.0F);
            cube_r1.texOffs(8, 59).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(2.75F, 0.5F, -5.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.3927F, 0.0F);
            cube_r2.texOffs(8, 59).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(5.35F, -9.25F, 3.0F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, -0.6109F, 0.0F, 0.0F);
            cube_r3.texOffs(56, 48).addBox(-0.5F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
            cube_r3.texOffs(56, 48).addBox(-11.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(3.6F, -8.75F, -1.0F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.6109F, 0.0F, 0.0F);
            cube_r4.texOffs(62, 48).addBox(-0.5F, -2.25F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
            cube_r4.texOffs(62, 48).addBox(-7.5F, -2.25F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.1F, -8.75F, -2.75F);
            this.Head.addChild(cube_r5);
            setRotationAngle(cube_r5, -0.6545F, 0.0F, 0.0F);
            cube_r5.texOffs(50, 48).addBox(-0.5F, -2.25F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(62, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 6.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(40, 0).addBox(-5.5F, 4.0F, -4.0F, 11.0F, 3.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(0, 16).addBox(-5.25F, 7.0F, -3.5F, 10.0F, 3.0F, 7.0F, 0.0F, false);
            this.Body.texOffs(40, 32).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(24, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(34, 16).addBox(-7.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
            this.RightArm.texOffs(0, 32).addBox(-7.0F, 2.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-6.0F, -5.0F, 0.0F);
            this.RightArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, 0.4363F);
            cube_r6.texOffs(0, 48).addBox(0.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(24, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(0, 32).addBox(1.75F, 2.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(34, 16).addBox(0.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(4.125F, -4.0F, 0.0F);
            this.LeftArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, -0.3927F);
            cube_r7.texOffs(40, 37).addBox(-2.125F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(34, 48).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(34, 48).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            ModelRenderer cube_r1_l = new ModelRenderer((Model) this);
            cube_r1_l.setPos(2.0976F, 20.076F, -0.2181F);
            this.Belt.addChild(cube_r1_l);
            setRotationAngle(cube_r1_l, -0.0436F, 0.0F, 0.6981F);
            cube_r1_l.texOffs(0, 0).addBox(-3.25F, -8.25F, -2.75F, 5.0F, 1.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r2_l = new ModelRenderer((Model) this);
            cube_r2_l.setPos(-1.9F, 12.0F, 0.0F);
            this.Belt.addChild(cube_r2_l);
            setRotationAngle(cube_r2_l, 0.0F, 0.0F, -0.7854F);
            cube_r2_l.texOffs(0, 0).addBox(-7.0F, -2.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }

    public static class Variant3<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant3(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 25).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(24, 50).addBox(-6.0F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(24, 50).addBox(5.0F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(-6.0F, -1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(3.0F, -1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-1.0F, -9.0F, -6.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 41).addBox(-1.0F, -9.0F, -5.0F, 2.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(0, 14).addBox(-1.0F, -9.0F, 5.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(6.0F, -7.4944F, -4.5165F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.8727F, 0.0F, 0.0F);
            cube_r1.texOffs(30, 18).addBox(-4.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(30, 18).addBox(-9.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-2.5F, 0.5F, -5.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, -0.3927F, 0.0F);
            cube_r2.texOffs(0, 6).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(2.5F, 0.5F, -5.5F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.3927F, 0.0F);
            cube_r3.texOffs(0, 6).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(2.1F, -7.75F, 5.0F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, 0.6545F);
            cube_r4.texOffs(4, 25).addBox(-0.5F, -2.75F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(5.6F, -9.5F, 3.5F);
            this.Head.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, 0.6545F);
            cube_r5.texOffs(4, 25).addBox(-0.5F, -2.75F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(3.6F, -9.0F, -0.5F);
            this.Head.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, 0.5236F);
            cube_r6.texOffs(32, 32).addBox(-0.5F, -2.25F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-1.9F, -7.75F, 4.5F);
            this.Head.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, -0.6109F);
            cube_r7.texOffs(4, 25).addBox(-0.5F, -2.75F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(-5.4F, -9.25F, 3.0F);
            this.Head.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.0F, 0.0F, -0.6109F);
            cube_r8.texOffs(4, 25).addBox(-0.5F, -2.75F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(-3.4F, -8.75F, -1.0F);
            this.Head.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, -0.5236F);
            cube_r9.texOffs(32, 32).addBox(-0.5F, -2.25F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(40, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 6.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(0, 14).addBox(-5.5F, 4.0F, -4.0F, 11.0F, 3.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(31, 18).addBox(-5.25F, 7.0F, -3.5F, 10.0F, 3.0F, 7.0F, 0.0F, false);
            this.Body.texOffs(0, 52).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 3.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(38, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(32, 32).addBox(-6.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
            this.RightArm.texOffs(14, 41).addBox(-5.0F, 5.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(-3.0F, -5.0F, -4.0F);
            this.RightArm.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.0F, 0.0F, 0.829F);
            cube_r10.texOffs(30, 16).addBox(-1.0F, 0.0F, 2.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(-4.0F, -5.0F, -2.0F);
            this.RightArm.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.0F, 0.0F, 0.829F);
            cube_r11.texOffs(24, 30).addBox(-3.0F, 0.0F, 2.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r12 = new ModelRenderer((Model) this);
            cube_r12.setPos(-4.875F, -4.5F, 3.0F);
            this.RightArm.addChild(cube_r12);
            setRotationAngle(cube_r12, -0.6545F, 0.0F, 0.0F);
            cube_r12.texOffs(0, 25).addBox(-0.125F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r13 = new ModelRenderer((Model) this);
            cube_r13.setPos(-5.0F, -2.0F, 0.0F);
            this.RightArm.addChild(cube_r13);
            setRotationAngle(cube_r13, 0.0F, 0.0F, 0.4363F);
            cube_r13.texOffs(24, 28).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r14 = new ModelRenderer((Model) this);
            cube_r14.setPos(-5.0F, -5.0F, 0.0F);
            this.RightArm.addChild(cube_r14);
            setRotationAngle(cube_r14, 0.0F, 0.0F, 0.4363F);
            cube_r14.texOffs(24, 28).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(38, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(14, 41).addBox(-0.25F, 5.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(32, 32).addBox(-1.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r15 = new ModelRenderer((Model) this);
            cube_r15.setPos(5.0F, -4.0F, 0.0F);
            this.LeftArm.addChild(cube_r15);
            setRotationAngle(cube_r15, 0.0F, 0.0F, 2.618F);
            cube_r15.texOffs(24, 28).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r16 = new ModelRenderer((Model) this);
            cube_r16.setPos(4.125F, -4.5F, 3.0F);
            this.LeftArm.addChild(cube_r16);
            setRotationAngle(cube_r16, -0.6545F, 0.0F, 0.0F);
            cube_r16.texOffs(0, 25).addBox(-0.125F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r17 = new ModelRenderer((Model) this);
            cube_r17.setPos(5.0F, -1.0F, 0.0F);
            this.LeftArm.addChild(cube_r17);
            setRotationAngle(cube_r17, 0.0F, 0.0F, 2.618F);
            cube_r17.texOffs(24, 28).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r18 = new ModelRenderer((Model) this);
            cube_r18.setPos(4.0F, -4.0F, -2.0F);
            this.LeftArm.addChild(cube_r18);
            setRotationAngle(cube_r18, 0.0F, 0.0F, 2.3562F);
            cube_r18.texOffs(24, 30).addBox(-3.0F, 0.0F, 2.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r19 = new ModelRenderer((Model) this);
            cube_r19.setPos(3.0F, -4.0F, -4.0F);
            this.LeftArm.addChild(cube_r19);
            setRotationAngle(cube_r19, 0.0F, 0.0F, 2.3562F);
            cube_r19.texOffs(30, 16).addBox(-1.5F, 0.0F, 2.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(53, 28).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(44, 5).addBox(-3.25F, 8.0F, -3.75F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            this.RightBoot.texOffs(43, 5).addBox(-2.25F, 7.0F, -3.75F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(43, 5).addBox(-2.05F, 7.0F, -3.75F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(53, 28).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, true);
            this.LeftBoot.texOffs(44, 5).addBox(-3.05F, 8.0F, -3.75F, 6.0F, 5.0F, 1.0F, 0.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.0F, 3.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
            this.LeftLeg.texOffs(0, 0).addBox(-1.8F, 3.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
        }
    }

    public static class Variant4<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant4(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(40, 0).addBox(-4.0F, -7.0F, -4.25F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(36, 53).addBox(-6.0F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(36, 53).addBox(5.0F, -4.0F, -5.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(32, 64).addBox(-6.0F, -2.0F, -5.75F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(32, 64).addBox(3.0F, -2.0F, -5.75F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(72, 0).addBox(-3.0F, -9.0F, -4.5F, 6.0F, 1.0F, 9.0F, 0.0F, false);
            this.Head.texOffs(0, 53).addBox(-5.5F, -7.0F, -2.5F, 1.0F, 5.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 53).addBox(4.5F, -7.0F, -2.5F, 1.0F, 5.0F, 6.0F, 0.0F, true);
            this.Head.texOffs(62, 37).addBox(-2.0F, -10.0F, -3.5F, 4.0F, 1.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -5.5F, -5.25F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.7418F, 0.0F, 0.0F);
            cube_r1.texOffs(18, 64).addBox(-3.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 13.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(52, 21).addBox(-5.0F, -1.0F, -6.0F, 10.0F, 8.0F, 3.0F, 0.0F, false);
            this.Body.texOffs(40, 37).addBox(-5.0F, -1.0F, 3.0F, 10.0F, 8.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(14, 53).addBox(-4.5F, 7.75F, 2.0F, 9.0F, 1.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, 1.875F, -5.875F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
            cube_r2.texOffs(50, 53).addBox(-4.75F, -1.875F, -0.875F, 9.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.75F, 8.875F, -3.375F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.6545F, 0.0F, 0.0F);
            cube_r3.texOffs(0, 64).addBox(-4.5F, -0.875F, -1.375F, 7.0F, 1.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(24, 37).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 21).addBox(-5.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, true);
            this.RightArm.texOffs(78, 21).addBox(-6.25F, -6.25F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);
            this.RightArm.texOffs(78, 21).addBox(-6.25F, -6.25F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);
            this.RightArm.texOffs(0, 37).addBox(-5.0F, 5.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, true);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(24, 37).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(0, 37).addBox(-0.25F, 5.0F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 21).addBox(-2.0F, -4.25F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(78, 21).addBox(1.25F, -6.25F, -3.5F, 5.0F, 2.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(50, 57).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(50, 57).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.0F, 3.0F, -3.0F, 3.0F, 4.0F, 6.0F, 0.0F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 0).addBox(-0.05F, 3.0F, -3.0F, 3.0F, 4.0F, 6.0F, 0.0F, true);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\PlatedArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */