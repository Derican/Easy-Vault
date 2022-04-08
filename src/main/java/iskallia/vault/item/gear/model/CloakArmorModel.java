package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CloakArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 32 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 14).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 19).addBox(-4.0F, 0.0F, -7.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(2.0F, 0.0F, -7.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 19).addBox(-5.0F, -1.0F, -7.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 19).addBox(4.0F, -1.0F, -7.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(16, 46).addBox(-6.0F, -6.0F, -6.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(16, 46).addBox(5.0F, -6.0F, -6.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 14).addBox(-5.0F, -9.0F, -7.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(0, 14).addBox(4.0F, -9.0F, -7.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(36, 33).addBox(-4.0F, -10.0F, -7.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 15).addBox(-1.5F, -11.0F, -8.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
            this.Head.texOffs(36, 33).addBox(1.0F, -10.0F, -7.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(24, 33).addBox(5.0F, -6.0F, -5.0F, 1.0F, 5.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(24, 33).addBox(-6.0F, -6.0F, -5.0F, 1.0F, 5.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(26, 4).addBox(-4.0F, -10.0F, -5.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(26, 4).addBox(1.0F, -10.0F, -5.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(20, 20).addBox(-1.5F, -11.0F, -5.0F, 3.0F, 1.0F, 12.0F, 0.0F, false);
            this.Head.texOffs(36, 38).addBox(-3.5F, -11.0F, -5.0F, 2.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(36, 38).addBox(1.5F, -11.0F, -5.0F, 2.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(32, 49).addBox(-4.0F, -10.0F, 5.0F, 8.0F, 9.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(50, 50).addBox(-3.0F, -10.0F, 6.0F, 6.0F, 6.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(29, 0).addBox(-2.0F, -10.0F, 8.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(33, 15).addBox(-2.0F, -10.0F, 9.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 30).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-5.5F, 0.0F, -3.6F, 11.0F, 7.0F, 7.0F, 0.0F, false);
            this.Body.texOffs(36, 36).addBox(-5.0F, 12.0F, -3.5F, 10.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-5.5F, 17.0F, 0.0F);
            this.Body.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.4363F);
            cube_r1.texOffs(38, 15).addBox(-1.5F, -5.0F, -4.0F, 1.0F, 10.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(5.5F, 17.0F, -0.5F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.4363F);
            cube_r2.texOffs(38, 15).addBox(0.5F, -5.0F, -3.5F, 1.0F, 10.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 17.0F, 3.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.4363F, 0.0F, 0.0F);
            cube_r3.texOffs(42, 0).addBox(-5.0F, -5.0F, 0.5F, 10.0F, 10.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(0, 0).addBox(-3.0F, 8.0F, 2.75F, 2.0F, 4.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(29, 4).addBox(-2.5F, 12.0F, 2.75F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(24, 33).addBox(-4.0F, 4.0F, 3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(16, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(24, 33).addBox(0.0F, 4.0F, 3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(16, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(0, 0).addBox(1.0F, 8.0F, 2.75F, 2.0F, 4.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(29, 4).addBox(1.5F, 12.0F, 2.75F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 32 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(36, 28).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 0).addBox(-8.0F, -6.5F, -9.0F, 16.0F, 1.0F, 18.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(-5.5F, -12.5F, -5.5F, 11.0F, 6.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -19.9218F, 5.1644F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -1.0036F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 19).addBox(-1.0F, -0.8F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, -20.0F, 2.25F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.5672F, 0.0F, 0.0F);
            cube_r2.texOffs(42, 57).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, -15.0F, 0.25F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, -0.1745F, 0.0F, 0.0F);
            cube_r3.texOffs(40, 44).addBox(-4.0F, -1.5F, -3.25F, 8.0F, 5.0F, 8.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(50, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 36).addBox(-6.0F, 8.0F, -4.0F, 12.0F, 12.0F, 8.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(26, 56).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 56).addBox(-5.0F, -4.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(26, 56).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(0, 56).addBox(0.0F, -4.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }

    public static class Variant3<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant3(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 32 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(35, 30).addBox(-5.5F, -6.0F, -5.5F, 11.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(35, 25).addBox(-5.5F, -6.0F, 4.5F, 11.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(29, 7).addBox(4.5F, -6.0F, -4.5F, 1.0F, 4.0F, 9.0F, 0.0F, false);
            this.Head.texOffs(24, 26).addBox(-5.5F, -6.0F, -4.5F, 1.0F, 4.0F, 9.0F, 0.0F, false);
            this.Head.texOffs(36, 20).addBox(-2.0F, -5.5F, -6.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 16).addBox(-1.0F, -5.0F, -6.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 26).addBox(-3.0F, -5.0F, -6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(2.0F, -5.0F, -6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 3).addBox(-2.0F, -6.0F, 5.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(1.0F, 0.5F, 5.75F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, -0.5236F);
            cube_r1.texOffs(0, 0).addBox(1.0F, -3.5F, 0.0F, 2.0F, 7.0F, 0.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-1.0F, 0.5F, 5.75F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, 0.5236F);
            cube_r2.texOffs(4, 0).addBox(-3.0F, -3.5F, 0.0F, 2.0F, 7.0F, 0.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 26).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 16).addBox(-5.5F, 10.0F, -3.5F, 11.0F, 3.0F, 7.0F, 0.0F, false);
            this.Body.texOffs(24, 0).addBox(-2.5F, 8.0F, -3.25F, 5.0F, 2.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(20, 26).addBox(-2.5F, 8.0F, 2.25F, 5.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 16.0F, 3.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.1309F, 0.0F, 0.0F);
            cube_r3.texOffs(52, 35).addBox(-3.0F, -4.5F, -0.25F, 6.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0F, 16.3986F, -4.4074F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.1745F, 0.0F, 0.0F);
            cube_r4.texOffs(52, 46).addBox(-3.0F, -5.0F, 0.0F, 6.0F, 10.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(0, 42).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(40, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(36, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(20, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }

    public static class Variant4<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant4(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 32 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 27).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-2.8378F, -9.7364F, 0.7885F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.5672F, 0.0F, 0.0F);
            cube_r1.texOffs(10, 0).addBox(-4.4122F, -7.1054F, -1.654F, 0.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(6, 0).addBox(-4.4122F, -9.1054F, 3.346F, 0.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(0, 26).addBox(-4.4122F, -9.1054F, 2.346F, 0.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(8, 0).addBox(-4.4122F, -9.1054F, 1.346F, 0.0F, 6.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(48, 14).addBox(-4.4122F, -9.1054F, 0.346F, 0.0F, 9.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(46, 14).addBox(-4.4122F, -8.1054F, -0.654F, 0.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-2.8378F, -9.7364F, 0.7885F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.1745F, 0.0F, 0.0F);
            cube_r2.texOffs(2, 5).addBox(-4.1622F, -1.7636F, -1.0385F, 1.0F, 1.0F, 4.0F, 0.0F, false);
            cube_r2.texOffs(0, 59).addBox(-4.1622F, -0.7636F, -3.0385F, 1.0F, 1.0F, 7.0F, 0.0F, false);
            cube_r2.texOffs(41, 42).addBox(-4.1622F, 0.2364F, -7.0385F, 1.0F, 2.0F, 12.0F, 0.0F, false);
            cube_r2.texOffs(40, 0).addBox(-1.1622F, -2.7636F, -5.0385F, 8.0F, 1.0F, 8.0F, 0.0F, false);
            cube_r2.texOffs(0, 13).addBox(-2.1622F, -1.7636F, -6.0385F, 10.0F, 4.0F, 10.0F, 0.0F, false);
            cube_r2.texOffs(60, 34).addBox(-1.1622F, 2.2364F, -11.0385F, 8.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(53, 32).addBox(-2.1622F, 2.2364F, -10.0385F, 10.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(53, 30).addBox(-2.1622F, 2.2364F, 5.9615F, 10.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(53, 28).addBox(-3.1622F, 2.2364F, -9.0385F, 12.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(30, 13).addBox(-4.1622F, 2.2364F, 4.9615F, 14.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(40, 9).addBox(-4.1622F, 2.2364F, -8.0385F, 14.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(0, 0).addBox(-4.1622F, 2.2364F, -7.0385F, 14.0F, 1.0F, 12.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-2.8378F, -9.7364F, 0.7885F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.1129F, 0.1334F, -0.8651F);
            cube_r3.texOffs(28, 15).addBox(3.8444F, 8.6166F, -7.0385F, 3.0F, 1.0F, 12.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 43).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 17).addBox(-2.0F, 1.0F, -3.6F, 4.0F, 2.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(24, 28).addBox(-3.0F, 3.0F, -4.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 10).addBox(-2.0F, 7.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0F, -2.75F, 4.4F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -0.6981F);
            cube_r4.texOffs(8, 7).addBox(-7.0F, -2.25F, -0.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(8, 7).addBox(-7.0F, -4.25F, -0.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(8, 7).addBox(-2.0F, -4.25F, -0.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(8, 7).addBox(-2.0F, -2.25F, -0.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(0, 13).addBox(-6.0F, -4.25F, -0.4F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(0, 0).addBox(-5.0F, -3.25F, -1.4F, 2.0F, 8.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(56, 56).addBox(-7.0F, 4.75F, -1.4F, 6.0F, 8.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r4_r1 = new ModelRenderer((Model) this);
            cube_r4_r1.setPos(0.0F, 0.0F, 0.0F);
            cube_r4.addChild(cube_r4_r1);
            setRotationAngle(cube_r4_r1, 0.0F, 1.5708F, 0.0F);
            cube_r4_r1.texOffs(17, 60).addBox(-0.75F, -3.25F, -5.0F, 0.1F, 15.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(40, 56).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(25, 41).addBox(-4.5F, 2.0F, -3.5F, 7.0F, 6.0F, 7.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(56, 37).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(32, 28).addBox(-2.5F, 2.0F, -3.5F, 7.0F, 6.0F, 7.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(56, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(24, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\CloakArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */