package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RoyalArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(40, 50).addBox(-3.0F, -6.25F, -5.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(28, 52).addBox(-2.0F, -7.25F, -5.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(28, 50).addBox(-2.0F, -7.25F, 4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 17).addBox(-1.0F, -8.25F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 6).addBox(-1.0F, -8.25F, 4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(36, 0).addBox(-3.0F, -6.25F, 4.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(32, 50).addBox(-5.0F, -6.25F, -3.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(28, 0).addBox(4.0F, -6.25F, -3.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(28, 3).addBox(-4.0F, -6.25F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(28, 0).addBox(-4.0F, -6.25F, 3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 20).addBox(3.0F, -6.25F, 3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 17).addBox(3.0F, -6.25F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(60, 32).addBox(-5.0F, -7.25F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
            this.Head.texOffs(0, 3).addBox(-5.0F, -8.25F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(38, 4).addBox(4.0F, -7.25F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(4.0F, -8.25F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.Head.texOffs(27, 45).addBox(-2.2F, -5.75F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(27, 41).addBox(-2.2F, -5.75F, 4.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 44).addBox(1.2F, -5.75F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 40).addBox(1.2F, -5.75F, 4.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(4, 22).addBox(-0.4F, -6.75F, -5.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 19).addBox(-0.4F, -6.75F, 4.15F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 22).addBox(4.4F, -6.75F, -0.55F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(44, 5).addBox(4.4F, -5.75F, -2.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(44, 3).addBox(4.4F, -5.75F, 1.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(27, 43).addBox(-5.2F, -5.75F, 1.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(24, 42).addBox(-5.2F, -5.75F, -2.35F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(4, 19).addBox(-5.3F, -6.75F, -0.55F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 33).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-5.0F, 1.0F, -4.0F, 10.0F, 9.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(24, 40).addBox(-4.0F, 9.25F, -3.5F, 8.0F, 3.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.048F, 5.4F, -4.3293F);
            this.Body.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.2752F, 0.473F, 0.1279F);
            cube_r1.texOffs(32, 58).addBox(-4.2417F, -4.0F, -1.4261F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-2.0F, 5.3F, 4.55F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.3001F, -0.504F, 0.1483F);
            cube_r2.texOffs(44, 61).addBox(-2.5F, -4.0F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.048F, 5.4F, -4.3293F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.2752F, -0.473F, -0.1279F);
            cube_r3.texOffs(44, 24).addBox(-0.7583F, -4.0F, -1.4261F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(1.8301F, 5.3F, 4.55F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.3001F, 0.504F, -0.1483F);
            cube_r4.texOffs(56, 61).addBox(-2.5F, -4.0F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(56, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(36, 3).addBox(-6.25F, 2.75F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 35).addBox(-6.25F, 2.75F, 0.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(20, 35).addBox(-6.25F, 2.75F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(20, 33).addBox(-6.25F, 2.75F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-3.0F, -0.5F, 0.0F);
            this.RightArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, 0.6981F);
            cube_r5.texOffs(47, 33).addBox(-2.65F, -1.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r5.texOffs(24, 25).addBox(-3.0F, -3.5F, -4.1F, 6.0F, 7.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 50).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(4, 0).addBox(5.25F, 2.75F, 0.35F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(24, 23).addBox(5.25F, 2.75F, 2.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(4, 3).addBox(5.25F, 2.75F, -1.35F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 33).addBox(5.25F, 2.75F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(3.0F, -0.5F, 0.0F);
            this.LeftArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, -0.6981F);
            cube_r6.texOffs(47, 47).addBox(-0.45F, -1.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r6.texOffs(28, 9).addBox(-3.0F, -3.5F, -4.2F, 6.0F, 7.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 49).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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
            this.Head.texOffs(2, 95).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(26, 95).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(36, 74).addBox(-3.0F, -8.0F, -7.0F, 6.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(26, 100).addBox(-5.0F, -1.0F, -6.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(36, 78).addBox(-3.0F, -1.0F, -6.5F, 6.0F, 2.0F, 0.1F, 0.0F, false);
            this.Head.texOffs(2, 74).addBox(-6.0F, -10.0F, -4.0F, 12.0F, 11.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(26, 103).addBox(-5.0F, -11.0F, -3.0F, 10.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(1.6F, -0.7527F, -5.7789F);
            this.Head.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.7418F, -1.5708F, 0.0F);
            cube_r8.texOffs(2, 80).addBox(-0.5F, -0.75F, -0.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-2.4F, -0.7527F, -5.7789F);
            this.Head.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.7418F, -1.5708F, 0.0F);
            cube_r7.texOffs(2, 80).addBox(-0.5F, -0.75F, -0.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(-1.15F, -0.5027F, -5.7789F);
            this.Head.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.7418F, -1.5708F, 0.0F);
            cube_r9.texOffs(2, 80).addBox(-0.5F, -0.7654F, -0.3968F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(0.35F, -0.7527F, -5.7789F);
            this.Head.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.7418F, -1.5708F, 0.0F);
            cube_r10.texOffs(2, 80).addBox(-0.5F, -0.9189F, -0.9343F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(1.6F, -5.2527F, -5.7789F);
            this.Head.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.7418F, -1.5708F, 0.0F);
            cube_r11.texOffs(2, 80).addBox(-0.5F, -0.5657F, -0.9189F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r12 = new ModelRenderer((Model) this);
            cube_r12.setPos(1.6F, -5.2527F, -5.7789F);
            this.Head.addChild(cube_r12);
            setRotationAngle(cube_r12, 0.7418F, -1.5708F, 0.0F);
            cube_r12.texOffs(2, 80).addBox(-0.5F, 2.1368F, 2.0301F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r13 = new ModelRenderer((Model) this);
            cube_r13.setPos(1.6F, -5.2527F, -5.7789F);
            this.Head.addChild(cube_r13);
            setRotationAngle(cube_r13, 0.7418F, -1.5708F, 0.0F);
            cube_r13.texOffs(2, 80).addBox(-0.5F, 0.0945F, 0.1716F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r14 = new ModelRenderer((Model) this);
            cube_r14.setPos(1.6F, -5.2527F, -5.7789F);
            this.Head.addChild(cube_r14);
            setRotationAngle(cube_r14, 0.7418F, -1.5708F, 0.0F);
            cube_r14.texOffs(2, 80).addBox(-0.5F, 1.1079F, 1.2774F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 33).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-5.0F, 1.0F, -4.0F, 10.0F, 9.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(24, 40).addBox(-4.0F, 9.25F, -3.5F, 8.0F, 3.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.048F, 5.4F, -4.3293F);
            this.Body.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.2752F, 0.473F, 0.1279F);
            cube_r1.texOffs(32, 58).addBox(-4.2417F, -4.0F, -1.4261F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-2.0F, 5.3F, 4.55F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.3001F, -0.504F, 0.1483F);
            cube_r2.texOffs(44, 61).addBox(-2.5F, -4.0F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.048F, 5.4F, -4.3293F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.2752F, -0.473F, -0.1279F);
            cube_r3.texOffs(44, 24).addBox(-0.7583F, -4.0F, -1.4261F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(1.8301F, 5.3F, 4.55F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.3001F, 0.504F, -0.1483F);
            cube_r4.texOffs(56, 61).addBox(-2.5F, -4.0F, -0.5F, 5.0F, 8.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(56, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(36, 3).addBox(-6.25F, 2.75F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 35).addBox(-6.25F, 2.75F, 0.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(20, 35).addBox(-6.25F, 2.75F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(20, 33).addBox(-6.25F, 2.75F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-3.0F, -0.5F, 0.0F);
            this.RightArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, 0.6981F);
            cube_r5.texOffs(47, 33).addBox(-2.65F, -1.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r5.texOffs(24, 25).addBox(-3.0F, -3.5F, -4.1F, 6.0F, 7.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 50).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(4, 0).addBox(5.25F, 2.75F, 0.35F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(24, 23).addBox(5.25F, 2.75F, 2.15F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(4, 3).addBox(5.25F, 2.75F, -1.35F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 33).addBox(5.25F, 2.75F, -3.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(3.0F, -0.5F, 0.0F);
            this.LeftArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, -0.6981F);
            cube_r6.texOffs(47, 47).addBox(-0.45F, -1.5F, -3.5F, 3.0F, 7.0F, 7.0F, 0.0F, false);
            cube_r6.texOffs(28, 9).addBox(-3.0F, -3.5F, -4.2F, 6.0F, 7.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 49).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\RoyalArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */