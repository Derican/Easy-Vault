package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class BoneArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 64 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 37).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(70, 19).addBox(-5.0F, -3.0F, -6.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(46, 0).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(36, 0).addBox(-5.0F, -6.0F, -6.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(32, 30).addBox(4.0F, -6.0F, -6.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(63, 79).addBox(5.0F, -8.0F, -5.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 57).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(30, 6).addBox(-2.0F, -1.0F, -6.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(22, 57).addBox(1.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(44, 58).addBox(-4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(58, 33).addBox(3.0F, -1.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 39).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(42, 86).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 11.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(46, 4).addBox(-2.0F, 1.0F, 3.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(30, 0).addBox(-1.0F, 6.0F, 3.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5F, 9.5F, 4.5F);
            this.Body.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.4363F);
            cube_r1.texOffs(52, 35).addBox(-2.5F, -0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(18, 53).addBox(-2.5F, -0.5F, -8.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(62, 70).addBox(0.5F, -0.5F, -8.5F, 1.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(4.5F, 6.5F, 4.5F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3054F);
            cube_r2.texOffs(52, 22).addBox(-2.5F, -0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(30, 55).addBox(-2.5F, -0.5F, -8.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(70, 10).addBox(0.5F, -0.5F, -8.5F, 1.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(4.25F, 3.5F, 4.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.0F, -0.1571F);
            cube_r3.texOffs(0, 18).addBox(-3.25F, -1.5F, -1.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(0, 24).addBox(-3.25F, -1.5F, -8.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(62, 44).addBox(0.75F, -1.5F, -8.5F, 1.0F, 2.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-4.25F, 3.5F, 4.5F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, 0.1309F);
            cube_r4.texOffs(0, 7).addBox(-0.75F, -1.5F, -1.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(26, 14).addBox(-0.75F, -1.5F, -8.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(70, 0).addBox(-1.75F, -1.5F, -8.5F, 1.0F, 2.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-4.5F, 9.5F, 4.5F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -0.3927F);
            cube_r5.texOffs(24, 43).addBox(-0.5F, -0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(18, 55).addBox(-0.5F, -0.5F, -8.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(32, 71).addBox(-1.5F, -0.5F, -8.5F, 1.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-4.5F, 6.5F, 4.5F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, -0.3054F);
            cube_r6.texOffs(0, 31).addBox(-0.5F, -0.5F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(38, 55).addBox(-0.5F, -0.5F, -8.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r6.texOffs(72, 23).addBox(-1.5F, -0.5F, -8.5F, 1.0F, 1.0F, 8.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(70, 54).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, -0.3491F);
            cube_r7.texOffs(52, 31).addBox(-0.6738F, -9.7517F, -3.476F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(0, 53).addBox(-0.6738F, -9.7517F, 2.524F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(58, 20).addBox(-3.7191F, 6.1096F, -1.476F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(58, 31).addBox(-3.7191F, 6.1096F, 0.524F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(56, 11).addBox(-2.7191F, 6.1096F, -2.976F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(34, 57).addBox(-1.2191F, 6.1096F, -3.476F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(58, 18).addBox(-1.2191F, 6.1096F, 2.524F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(18, 57).addBox(-2.7191F, 6.1096F, 2.024F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r7.texOffs(30, 58).addBox(6.2809F, 1.1096F, -4.976F, 2.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r7.texOffs(42, 71).addBox(-2.7191F, -0.8904F, -2.976F, 3.0F, 2.0F, 6.0F, 0.0F, false);
            cube_r7.texOffs(56, 31).addBox(-3.7191F, 1.1096F, -3.976F, 4.0F, 5.0F, 8.0F, 0.0F, false);
            cube_r7.texOffs(46, 45).addBox(0.2809F, 1.1096F, -4.976F, 3.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r7.texOffs(0, 24).addBox(0.2809F, -1.8904F, -4.976F, 8.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r7.texOffs(26, 27).addBox(0.2809F, 4.1096F, -4.976F, 8.0F, 2.0F, 10.0F, 0.0F, false);
            cube_r7.texOffs(0, 53).addBox(3.2809F, 1.1096F, -3.976F, 5.0F, 4.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.0F, 0.0F, -1.0908F);
            cube_r8.texOffs(26, 28).addBox(6.1281F, -7.7251F, -3.576F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r8.texOffs(0, 29).addBox(6.1281F, -7.7251F, 2.424F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, -1.789F);
            cube_r9.texOffs(0, 27).addBox(1.8449F, -10.7943F, -0.576F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.0F, 0.0F, -1.0472F);
            cube_r10.texOffs(52, 18).addBox(-5.9052F, -9.1209F, -0.476F, 2.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.0F, 0.0F, -1.6581F);
            cube_r11.texOffs(20, 84).addBox(-1.93F, -8.3301F, -0.976F, 3.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r11.texOffs(80, 0).addBox(-2.93F, -4.3301F, -1.576F, 5.0F, 5.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r12 = new ModelRenderer((Model) this);
            cube_r12.setPos(-7.5309F, -4.3596F, -0.024F);
            this.RightArm.addChild(cube_r12);
            setRotationAngle(cube_r12, 0.0F, 0.0F, -0.9599F);
            cube_r12.texOffs(80, 10).addBox(1.7172F, -1.8462F, 1.424F, 5.0F, 5.0F, 3.0F, 0.0F, false);
            cube_r12.texOffs(80, 32).addBox(1.7172F, -1.8462F, -4.576F, 5.0F, 5.0F, 3.0F, 0.0F, false);
            cube_r12.texOffs(86, 52).addBox(2.7172F, -5.8462F, -3.976F, 3.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r12.texOffs(75, 86).addBox(2.7172F, -5.8462F, 2.024F, 3.0F, 9.0F, 2.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 68).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r13 = new ModelRenderer((Model) this);
            cube_r13.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r13);
            setRotationAngle(cube_r13, 0.0F, 0.0F, 0.3491F);
            cube_r13.texOffs(52, 6).addBox(-8.6735F, 1.1096F, -4.008F, 5.0F, 4.0F, 8.0F, 0.0F, false);
            cube_r13.texOffs(54, 18).addBox(-0.6735F, 1.1096F, -4.008F, 4.0F, 5.0F, 8.0F, 0.0F, false);
            cube_r13.texOffs(16, 55).addBox(-8.6735F, 1.1096F, -5.008F, 2.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r13.texOffs(26, 14).addBox(-8.6735F, 4.1096F, -5.008F, 8.0F, 2.0F, 10.0F, 0.0F, false);
            cube_r13.texOffs(30, 1).addBox(-3.6735F, 1.1096F, -5.008F, 3.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r13.texOffs(0, 11).addBox(-8.6735F, -1.8904F, -5.008F, 8.0F, 3.0F, 10.0F, 0.0F, false);
            cube_r13.texOffs(72, 44).addBox(-0.6735F, -0.8904F, -3.008F, 3.0F, 2.0F, 6.0F, 0.0F, false);
            cube_r13.texOffs(30, 57).addBox(2.3265F, 6.1096F, -1.508F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r13.texOffs(56, 4).addBox(2.3265F, 6.1096F, 0.492F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r13.texOffs(26, 53).addBox(-0.1735F, 6.1096F, 2.492F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r13.texOffs(52, 39).addBox(1.5265F, 6.1096F, 1.992F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r13.texOffs(26, 11).addBox(1.2265F, 6.1096F, -3.008F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r13.texOffs(36, 4).addBox(-0.4735F, 6.1096F, -3.508F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r14 = new ModelRenderer((Model) this);
            cube_r14.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r14);
            setRotationAngle(cube_r14, -3.1416F, 0.0F, -2.0508F);
            cube_r14.texOffs(26, 26).addBox(5.6135F, -8.1967F, 2.408F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r14.texOffs(26, 19).addBox(5.6135F, -8.1967F, -3.592F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r15 = new ModelRenderer((Model) this);
            cube_r15.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r15);
            setRotationAngle(cube_r15, -3.1416F, 0.0F, -2.7925F);
            cube_r15.texOffs(0, 41).addBox(-1.3718F, -9.7517F, 2.508F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r15.texOffs(30, 39).addBox(-1.3718F, -9.7517F, -3.492F, 2.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r16 = new ModelRenderer((Model) this);
            cube_r16.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r16);
            setRotationAngle(cube_r16, 3.1416F, 0.0F, -2.1817F);
            cube_r16.texOffs(10, 84).addBox(2.2196F, -6.1947F, 2.008F, 3.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r16.texOffs(47, 79).addBox(1.2196F, -2.1947F, 1.408F, 5.0F, 5.0F, 3.0F, 0.0F, false);
            cube_r16.texOffs(0, 81).addBox(2.2196F, -6.1947F, -3.992F, 3.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r16.texOffs(77, 78).addBox(1.2196F, -2.1947F, -4.592F, 5.0F, 5.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r17 = new ModelRenderer((Model) this);
            cube_r17.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r17);
            setRotationAngle(cube_r17, 3.1416F, 0.0F, -1.3526F);
            cube_r17.texOffs(26, 17).addBox(1.8423F, -10.8145F, -0.492F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r18 = new ModelRenderer((Model) this);
            cube_r18.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r18);
            setRotationAngle(cube_r18, -3.1416F, 0.0F, -2.0944F);
            cube_r18.texOffs(24, 39).addBox(-5.9208F, -9.134F, -0.392F, 2.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r19 = new ModelRenderer((Model) this);
            cube_r19.setPos(7.9235F, -4.1096F, 0.008F);
            this.LeftArm.addChild(cube_r19);
            setRotationAngle(cube_r19, -3.1416F, 0.0F, -1.4835F);
            cube_r19.texOffs(32, 80).addBox(-1.9353F, -8.3498F, -0.892F, 3.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r19.texOffs(72, 70).addBox(-2.9353F, -4.3498F, -1.492F, 5.0F, 5.0F, 3.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 65).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(26, 30).addBox(-1.0F, 3.0F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightBoot.texOffs(0, 11).addBox(-2.0F, 6.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 0).addBox(-1.8F, 6.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(0, 37).addBox(-0.8F, 3.0F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(54, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(28, 13).addBox(-2.0F, 8.0F, -3.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(20, 0).addBox(-2.0F, 8.0F, -4.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            this.Belt.texOffs(12, 16).addBox(-1.0F, 11.0F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_l2_1 = new ModelRenderer((Model) this);
            cube_l2_1.setPos(4.5F, 14.5F, 0.0F);
            this.Belt.addChild(cube_l2_1);
            setRotationAngle(cube_l2_1, 0.0F, 0.0F, -0.2182F);
            cube_l2_1.texOffs(24, 0).addBox(-0.5F, -3.5F, -3.0F, 1.0F, 7.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_l2_2 = new ModelRenderer((Model) this);
            cube_l2_2.setPos(-4.5F, 14.5F, 0.0F);
            this.Belt.addChild(cube_l2_2);
            setRotationAngle(cube_l2_2, 0.0F, 0.0F, 0.1745F);
            cube_l2_2.texOffs(26, 26).addBox(-0.5F, -3.5F, -3.0F, 1.0F, 7.0F, 6.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\BoneArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */