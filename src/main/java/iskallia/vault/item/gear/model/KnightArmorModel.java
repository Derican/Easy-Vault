package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class KnightArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 0).addBox(5.0F, -5.0F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-6.0F, -5.0F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5161F, 0.0F, 0.1884F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, -0.6545F, 0.0F);
            cube_r1.texOffs(40, 9).addBox(-9.0438F, -9.0F, -4.2832F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(36, 23).addBox(-9.0438F, -3.0F, -4.2832F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-5.0F, 0.0F, -3.0F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.6545F, 0.0F);
            cube_r2.texOffs(40, 9).addBox(1.1637F, -9.0F, -1.4569F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(36, 23).addBox(1.1637F, -3.0F, -1.4569F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(32, 44).addBox(-4.0F, 2.0F, 3.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 16).addBox(-5.5F, 11.0F, -3.5F, 11.0F, 2.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(5.0F, 13.0F, 0.0F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.0F, -1.8326F);
            cube_r3.texOffs(18, 0).addBox(-7.8378F, 0.6276F, -3.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-4.0F, 13.0F, 0.0F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -1.2217F);
            cube_r4.texOffs(18, 0).addBox(-8.0F, -1.1F, -3.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.0F, 0.0F, 0.0F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.1745F, 0.0F, 0.0F);
            cube_r5.texOffs(32, 44).addBox(-4.0F, 1.0F, -6.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(16, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 25).addBox(-5.0F, -4.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(32, 0).addBox(-5.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-4.0F, 4.5F, 0.0F);
            this.RightArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, 0.4363F);
            cube_r6.texOffs(29, 9).addBox(-2.0F, -4.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r6.texOffs(29, 9).addBox(-4.0F, -6.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 25).addBox(-1.0F, -4.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(32, 0).addBox(2.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(4.0F, 4.5F, 0.0F);
            this.LeftArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, -0.4363F);
            cube_r7.texOffs(29, 9).addBox(0.0F, -4.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r7.texOffs(29, 9).addBox(2.0F, -6.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, 0.0F, 0.0F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.2182F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 25).addBox(-1.5F, -13.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(32, 50).addBox(-1.5F, -16.0F, 5.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(47, 18).addBox(-1.5F, -18.0F, 2.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(0, 0).addBox(-2.5F, -16.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(0, 0).addBox(1.5F, -16.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(47, 9).addBox(-1.5F, -17.0F, -1.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(0, 16).addBox(-0.5F, -14.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(4.5161F, 0.0F, 0.1884F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, -0.6545F, 0.0F);
            cube_r2.texOffs(46, 0).addBox(-9.0438F, -9.0F, -4.2832F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(49, 49).addBox(-9.0438F, -3.0F, -4.2832F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-5.0F, 0.0F, -3.0F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.6545F, 0.0F);
            cube_r3.texOffs(46, 0).addBox(1.1637F, -9.0F, -1.4569F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(49, 49).addBox(1.1637F, -3.0F, -1.4569F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 25).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(32, 41).addBox(-4.0F, 2.0F, 3.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 16).addBox(-5.5F, 11.0F, -3.5F, 11.0F, 2.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(5.0F, 13.0F, 0.0F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -1.8326F);
            cube_r4.texOffs(18, 0).addBox(-7.8378F, 0.4276F, -3.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-4.0F, 13.0F, 0.0F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -1.2217F);
            cube_r5.texOffs(18, 0).addBox(-8.0F, -1.3F, -3.0F, 8.0F, 0.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(0.0F, 6.0F, -3.5F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.3054F, 0.0F, 0.0F);
            cube_r6.texOffs(32, 41).addBox(-4.0F, -4.0F, -0.5F, 8.0F, 8.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(16, 39).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 25).addBox(-5.0F, -4.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(32, 0).addBox(-5.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-4.0F, 8.5F, 0.0F);
            this.RightArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, -0.7418F);
            cube_r7.texOffs(29, 9).addBox(0.0F, -7.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(-4.0F, 4.5F, 0.0F);
            this.RightArm.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.0F, 0.0F, -0.6545F);
            cube_r8.texOffs(29, 9).addBox(-1.0F, -6.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 25).addBox(-1.0F, -4.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(32, 0).addBox(2.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(16, 39).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(4.0F, 4.5F, 0.0F);
            this.LeftArm.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, 0.6109F);
            cube_r9.texOffs(29, 9).addBox(-1.0F, -6.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(4.0F, 8.5F, 0.0F);
            this.LeftArm.addChild(cube_r10);
            setRotationAngle(cube_r10, -0.0174F, -0.0013F, 0.7727F);
            cube_r10.texOffs(29, 9).addBox(-2.0F, -7.5F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

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
            this.Head.texOffs(26, 6).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 14).addBox(5.0F, -5.0F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-6.0F, -5.0F, -5.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5161F, 0.0F, 0.1884F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, -0.6545F, 0.0F);
            cube_r1.texOffs(26, 0).addBox(-9.0438F, -3.3F, -4.2832F, 6.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-0.1275F, -5.95F, -6.3931F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.5807F, -0.5704F, 0.3405F);
            cube_r2.texOffs(57, 48).addBox(-1.3533F, -2.05F, -1.8886F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-5.0F, 0.0F, -3.0F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.6545F, 0.0F);
            cube_r3.texOffs(40, 0).addBox(1.1637F, -3.3F, -1.4569F, 6.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-0.1275F, -5.95F, -6.3931F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.5807F, 0.5704F, -0.3405F);
            cube_r4.texOffs(72, 38).addBox(-4.7675F, -1.95F, -1.7312F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(58, 16).addBox(-4.0F, 1.0F, 3.0F, 8.0F, 9.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(62, 30).addBox(-3.0F, 2.0F, 5.0F, 6.0F, 7.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(27, 22).addBox(-5.5F, 11.0F, -3.5F, 11.0F, 2.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.0F, 6.2822F, -2.9637F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.1745F, 0.0F, 0.0F);
            cube_r5.texOffs(0, 28).addBox(4.0F, -0.5F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(20, 28).addBox(4.0F, -3.5F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(24, 30).addBox(-5.0F, -2.0F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(42, 31).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 6.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(48, 38).addBox(-5.0F, -5.0F, -1.5F, 10.0F, 8.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(60, 62).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(0.0F, 0.0F, 0.0F);
            this.RightArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, 0.2618F);
            cube_r6.texOffs(9, 68).addBox(-7.0F, -1.0F, -3.5F, 2.0F, 5.0F, 7.0F, 0.0F, false);
            cube_r6.texOffs(0, 44).addBox(-3.0F, 1.0F, -3.5F, 2.0F, 8.0F, 7.0F, 0.0F, false);
            cube_r6.texOffs(46, 51).addBox(-5.0F, -1.0F, -3.5F, 2.0F, 8.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(0.0F, 0.0F, 0.0F);
            this.RightArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.0F, 0.0F, 0.6545F);
            cube_r7.texOffs(16, 40).addBox(-7.0F, 2.0F, -4.0F, 5.0F, 1.0F, 8.0F, 0.0F, false);
            cube_r7.texOffs(0, 14).addBox(-8.0F, -4.0F, -4.0F, 9.0F, 6.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(32, 62).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(2.75F, 0.75F, 0.0F);
            this.LeftArm.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.0F, 0.0F, -0.6545F);
            cube_r8.texOffs(24, 31).addBox(0.25F, -0.75F, -4.0F, 5.0F, 1.0F, 8.0F, 0.0F, false);
            cube_r8.texOffs(0, 0).addBox(-2.75F, -6.75F, -4.0F, 9.0F, 6.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(4.0F, 1.5F, 0.0F);
            this.LeftArm.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, -0.2618F);
            cube_r9.texOffs(35, 42).addBox(-3.0F, -2.5F, -3.5F, 2.0F, 9.0F, 7.0F, 0.0F, false);
            cube_r9.texOffs(64, 48).addBox(1.0F, -3.5F, -3.5F, 2.0F, 5.0F, 7.0F, 0.0F, false);
            cube_r9.texOffs(18, 51).addBox(-1.0F, -3.5F, -3.5F, 2.0F, 8.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(58, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\KnightArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */