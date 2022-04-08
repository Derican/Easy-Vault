package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ScaleArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 14).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-7.3333F, -8.1667F, -0.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.829F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(14.8333F, -3.8333F, -1.75F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(0, 14).addBox(-1.1667F, -3.8333F, -1.75F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(24, 15).addBox(14.3333F, -0.8333F, -2.25F, 2.0F, 3.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(42, 0).addBox(-1.6667F, -0.8333F, -2.25F, 2.0F, 3.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(30, 0).addBox(12.3333F, 0.1667F, -2.75F, 2.0F, 3.0F, 4.0F, 0.0F, false);
            cube_r1.texOffs(32, 18).addBox(0.3333F, 0.1667F, -2.75F, 2.0F, 3.0F, 4.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 26).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-5.5F, 0.0F, -4.0F, 11.0F, 6.0F, 8.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(48, 38).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(31, 7).addBox(-5.25F, -4.25F, -3.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);
            this.RightArm.texOffs(4, 0).addBox(-2.5F, -2.75F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 5).addBox(-4.5F, -2.75F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(4, 5).addBox(-4.5F, -2.75F, -3.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(4, 14).addBox(-2.5F, -2.75F, -3.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 19).addBox(-5.5F, -2.75F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(4, 19).addBox(-5.5F, -2.75F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 30).addBox(-5.5F, -2.75F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(32, 42).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(0, 30).addBox(-0.75F, -4.25F, -3.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);
            this.LeftArm.texOffs(19, 32).addBox(4.5F, -2.75F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 32).addBox(4.5F, -2.75F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(3, 31).addBox(4.5F, -2.75F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(23, 30).addBox(3.5F, -2.75F, -3.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(19, 30).addBox(1.5F, -2.75F, -3.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(30, 2).addBox(1.5F, -2.75F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(30, 0).addBox(3.5F, -2.75F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(16, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 15).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 0).addBox(-1.0F, -11.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(0, 4).addBox(-0.5F, -15.0F, -1.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(1.8321F, -9.2935F, -0.35F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.2618F);
            cube_r1.texOffs(35, 6).addBox(-2.0F, -0.5F, -4.5F, 5.0F, 2.0F, 9.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-1.8675F, -7.7152F, 0.0F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2618F);
            cube_r2.texOffs(0, 44).addBox(-2.5F, -2.0F, -4.85F, 5.0F, 2.0F, 9.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(30, 36).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, 1.0F, -5.0F, 12.0F, 5.0F, 10.0F, 0.0F, false);
            this.Body.texOffs(34, 0).addBox(-4.0F, 6.0F, -4.0F, 8.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(24, 17).addBox(-4.0F, 6.0F, 3.0F, 8.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(54, 9).addBox(-3.0F, 9.0F, -3.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(63, 18).addBox(-3.0F, 9.0F, 2.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(40, 56).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 31).addBox(-6.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(47, 45).addBox(-5.0F, -1.0F, -3.5F, 5.0F, 4.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-4.2495F, -5.2887F, 0.0F);
            this.RightArm.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.0F, -0.4363F);
            cube_r3.texOffs(63, 9).addBox(-1.7F, -0.5F, -3.9F, 3.0F, 2.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-1.5F, -5.5F, 0.0F);
            this.RightArm.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, 0.4363F);
            cube_r4.texOffs(9, 64).addBox(-1.5F, -0.5F, -3.9F, 3.0F, 2.0F, 7.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 55).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(24, 23).addBox(-1.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(46, 17).addBox(0.0F, -1.0F, -3.5F, 5.0F, 4.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(4.6F, -5.85F, 0.0F);
            this.LeftArm.addChild(cube_r5);
            setRotationAngle(cube_r5, -3.1416F, 0.0F, 2.7053F);
            cube_r5.texOffs(56, 56).addBox(1.5F, -1.5F, -3.9F, 3.0F, 2.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(1.9727F, -5.2161F, 0.0F);
            this.LeftArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 3.1416F, 0.0F, -2.7053F);
            cube_r6.texOffs(54, 0).addBox(-3.7F, -1.5F, -3.9F, 3.0F, 2.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(54, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(24, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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
            this.texWidth = isLayer2() ? 64 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 15).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-0.1327F, -6.906F, -5.7145F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.5074F, -0.5162F, 0.2679F);
            cube_r1.texOffs(36, 23).addBox(-1.273F, -1.0F, -1.6649F, 6.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-0.0912F, -1.0F, -6.1377F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.3596F, -0.5462F, -0.1929F);
            cube_r2.texOffs(0, 40).addBox(-1.2073F, -4.0F, -1.6417F, 6.0F, 6.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-0.1327F, -6.906F, -5.7145F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, -0.5175F, 0.5467F, -0.2877F);
            cube_r3.texOffs(24, 0).addBox(-4.5957F, -1.0F, -1.7112F, 5.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-0.0912F, -1.0F, -6.1377F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.3674F, 0.5788F, 0.2075F);
            cube_r4.texOffs(14, 40).addBox(-4.5301F, -4.0F, -1.7344F, 5.0F, 6.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 24).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-0.0133F, 4.0F, -3.5F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.2934F, -0.4623F, -0.1339F);
            cube_r5.texOffs(14, 40).addBox(-0.7378F, -3.0F, -1.4173F, 5.0F, 6.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-0.0133F, 4.0F, -3.5F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.2934F, 0.4623F, 0.1339F);
            cube_r6.texOffs(14, 40).addBox(-4.2622F, -3.0F, -1.4173F, 5.0F, 6.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(32, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(24, 16).addBox(-4.0F, -4.0F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-2.5F, -0.5F, -3.5F);
            this.RightArm.addChild(cube_r7);
            setRotationAngle(cube_r7, -0.3927F, 0.0F, 0.0F);
            cube_r7.texOffs(40, 16).addBox(-1.5F, -2.5F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(-2.0F, -0.5F, 3.5F);
            this.RightArm.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.3491F, 0.0F, 0.0F);
            cube_r8.texOffs(40, 16).addBox(-2.0F, -2.5F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(-4.5F, -0.5F, 0.0F);
            this.RightArm.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, 0.3491F);
            cube_r9.texOffs(34, 34).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(24, 16).addBox(-1.0F, -4.0F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);
            this.LeftArm.texOffs(32, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(4.5F, -0.5F, 0.0F);
            this.LeftArm.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.0F, 0.0F, -0.3491F);
            cube_r10.texOffs(34, 34).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(2.0F, -0.5F, 3.5F);
            this.LeftArm.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.3491F, 0.0F, 0.0F);
            cube_r11.texOffs(40, 16).addBox(-2.0F, -2.5F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r12 = new ModelRenderer((Model) this);
            cube_r12.setPos(1.5F, -0.5F, -3.5F);
            this.LeftArm.addChild(cube_r12);
            setRotationAngle(cube_r12, -0.3927F, 0.0F, 0.0F);
            cube_r12.texOffs(40, 16).addBox(-1.5F, -2.5F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(24, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(24, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

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

    public static class Variant4<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant4(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 15).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 0).addBox(-6.0F, -10.0F, -8.0F, 12.0F, 1.0F, 14.0F, 0.0F, false);
            this.Head.texOffs(20, 19).addBox(-6.0F, -9.0F, -8.0F, 1.0F, 4.0F, 12.0F, 0.0F, false);
            this.Head.texOffs(20, 19).addBox(5.0F, -9.0F, -8.0F, 1.0F, 4.0F, 12.0F, 0.0F, false);
            this.Head.texOffs(24, 35).addBox(-6.0F, -10.0F, -9.0F, 12.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 15).addBox(4.0F, -5.0F, -9.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 15).addBox(-6.0F, -5.0F, -9.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 15).addBox(-5.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 15).addBox(3.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-6.0F, -5.0F, -5.0F, 1.0F, 6.0F, 5.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(5.0F, -5.0F, -5.0F, 1.0F, 6.0F, 5.0F, 0.0F, false);
            this.Head.texOffs(38, 0).addBox(-6.0F, 0.0F, -7.0F, 12.0F, 1.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-6.0F, -10.5F, -0.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.9163F, 0.0F, -0.0436F);
            cube_r1.texOffs(0, 11).addBox(-0.2F, -5.5F, -0.5F, 0.0F, 8.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(6.1F, -10.5F, -0.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.9163F, 0.0F, 0.0436F);
            cube_r2.texOffs(0, 11).addBox(0.1F, -5.5F, -0.5F, 0.0F, 8.0F, 4.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 31).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 3.5F, -3.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.48F, 0.0F, 0.0F);
            cube_r3.texOffs(38, 3).addBox(-5.0F, -2.5F, -0.5F, 10.0F, 5.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(40, 41).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-3.0F, -1.0F, 0.0F);
            this.RightArm.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, 0.9599F);
            cube_r4.texOffs(34, 15).addBox(-3.0F, -3.0F, -4.0F, 4.0F, 6.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(40, 41).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(3.0F, -1.0F, 0.0F);
            this.LeftArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -0.9599F);
            cube_r5.texOffs(34, 15).addBox(-1.0F, -3.0F, -4.0F, 4.0F, 6.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(24, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(24, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\ScaleArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */