package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class FurArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(28, 28).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(36, 16).addBox(-2.0F, -9.0F, -9.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
            this.Head.texOffs(31, 6).addBox(-4.0F, -10.0F, -5.0F, 8.0F, 1.0F, 9.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-3.0F, -17.0F, 0.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(6, 31).addBox(-3.0F, -20.0F, 1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(29, 16).addBox(-8.0F, -18.0F, 1.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(29, 18).addBox(-6.0F, -15.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(29, 18).addBox(3.0F, -15.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(29, 16).addBox(3.0F, -18.0F, 1.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(10, 31).addBox(1.0F, -13.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(10, 31).addBox(-2.0F, -13.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(6, 31).addBox(2.0F, -20.0F, 1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(6, 31).addBox(8.0F, -21.0F, 1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(4, 0).addBox(6.0F, -24.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 20).addBox(5.0F, -23.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 20).addBox(-6.0F, -23.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(4, 0).addBox(-7.0F, -24.0F, 1.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(6, 31).addBox(-9.0F, -21.0F, 1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(2.0F, -17.0F, 0.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.0F, -11.0F, -3.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.6981F);
            cube_r1.texOffs(0, 15).addBox(-0.2F, -2.0F, 1.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-4.0F, -11.0F, -3.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.7854F);
            cube_r2.texOffs(0, 15).addBox(-2.0F, -2.1F, 1.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 44).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 7.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(0, 15).addBox(-5.5F, -1.0F, -3.5F, 11.0F, 9.0F, 7.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(52, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 36).addBox(-5.0F, 4.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, 5.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, 5.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, 1.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, -1.0F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(0, 31).addBox(-5.0F, 1.0F, -5.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.RightArm.texOffs(0, 31).addBox(-5.0F, 1.0F, 3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, -2.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, -2.0F, 4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, -1.0F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(10, 31).addBox(-5.0F, 1.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(52, 52).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(0, 31).addBox(4.0F, 1.0F, -5.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, -2.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, 1.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, -1.0F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, 5.0F, -2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, 5.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, 1.0F, 1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 31).addBox(4.0F, 1.0F, 3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, -1.0F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(10, 31).addBox(4.0F, -2.0F, 4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 36).addBox(-3.0F, 4.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

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
            this.Head.texOffs(24, 0).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 7.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.3333F, -11.8333F, -4.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.5236F);
            cube_r1.texOffs(38, 35).addBox(2.6667F, 0.8333F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(24, 24).addBox(1.6667F, -1.1667F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            cube_r1.texOffs(0, 0).addBox(-0.3333F, -2.1667F, -0.5F, 2.0F, 7.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-4.3333F, -11.8333F, -4.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.5236F);
            cube_r2.texOffs(38, 35).addBox(-3.6667F, 0.8333F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(24, 24).addBox(-2.6667F, -1.1667F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
            cube_r2.texOffs(0, 0).addBox(-1.6667F, -2.1667F, -0.5F, 2.0F, 7.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, -3.5F, -7.5F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.3927F, 0.0F, 0.0F);
            cube_r3.texOffs(42, 14).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 6.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(14, 32).addBox(-5.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(-4.0F, 2.0F, -5.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(3.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(2.0F, 2.0F, -5.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(1.0F, 4.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(-1.0F, 5.0F, -5.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(14, 32).addBox(-3.0F, 4.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-0.5F, 15.5F, 10.5F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, -0.9599F, 0.0F, 0.0F);
            cube_r4.texOffs(34, 32).addBox(-0.5F, 1.0F, 0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r4.texOffs(0, 32).addBox(-1.0F, 0.5F, -0.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-0.5F, 11.5F, 7.0F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, -0.7418F, 0.0F, 0.0F);
            cube_r5.texOffs(0, 32).addBox(-1.0F, 0.5F, -5.0F, 3.0F, 3.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-0.5F, 11.5F, 7.0F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, -0.9599F, 0.0F, 0.0F);
            cube_r6.texOffs(24, 8).addBox(-1.5F, -0.5F, -3.0F, 4.0F, 4.0F, 8.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(38, 38).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(40, 0).addBox(-5.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(40, 0).addBox(-5.0F, 8.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(38, 38).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftArm.texOffs(40, 0).addBox(2.0F, 8.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(40, 0).addBox(2.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(22, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(24, 24).addBox(-3.5F, 12.25F, -3.75F, 7.0F, 1.0F, 7.0F, 0.0F, false);
            this.RightBoot.texOffs(24, 20).addBox(-2.5F, 13.25F, -2.75F, 5.0F, 1.0F, 1.0F, 0.0F, false);
            this.RightBoot.texOffs(24, 20).addBox(-2.5F, 13.25F, 1.25F, 5.0F, 1.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(22, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftBoot.texOffs(24, 20).addBox(-2.3F, 13.25F, -2.75F, 5.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(24, 20).addBox(-2.3F, 13.25F, 1.25F, 5.0F, 1.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(24, 24).addBox(-3.3F, 12.25F, -3.75F, 7.0F, 1.0F, 7.0F, 0.0F, false);

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
            this.Head.texOffs(0, 27).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(52, 0).addBox(-3.5F, -7.25F, -7.5F, 7.0F, 4.0F, 3.0F, 0.0F, false);
            this.Head.texOffs(4, 6).addBox(-3.0F, -3.25F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 6).addBox(2.0F, -3.25F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-1.5F, -7.25F, -8.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.0F, -9.75F, -2.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.7854F);
            cube_r1.texOffs(0, 3).addBox(0.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-4.0F, -9.75F, -2.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.7854F);
            cube_r2.texOffs(0, 12).addBox(-2.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(24, 44).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, 1.0F, -4.0F, 12.0F, 4.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(0, 12).addBox(-5.5F, 2.0F, -3.5F, 11.0F, 8.0F, 7.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(60, 25).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(52, 12).addBox(-6.0F, -4.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(0, 43).addBox(-5.0F, 4.0F, -4.0F, 4.0F, 8.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(62, 62).addBox(-6.0F, 5.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
            this.RightArm.texOffs(29, 15).addBox(-4.0F, 12.0F, 1.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(29, 12).addBox(-4.0F, 12.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(24, 27).addBox(-4.0F, 12.0F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(32, 60).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(48, 61).addBox(5.0F, 5.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
            this.LeftArm.texOffs(36, 4).addBox(1.0F, 4.0F, -4.0F, 4.0F, 8.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(4, 27).addBox(3.0F, 12.0F, -2.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 27).addBox(3.0F, 12.0F, 1.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 15).addBox(3.0F, 12.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(48, 48).addBox(1.0F, -4.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(16, 60).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(32, 32).addBox(-3.5F, 8.25F, -3.75F, 7.0F, 5.0F, 7.0F, 0.0F, false);
            this.RightBoot.texOffs(59, 43).addBox(-3.0F, 9.25F, -4.75F, 6.0F, 4.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftBoot.texOffs(52, 7).addBox(-3.1F, 9.25F, -4.75F, 6.0F, 4.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(29, 20).addBox(-3.6F, 8.25F, -3.75F, 7.0F, 5.0F, 7.0F, 0.0F, false);

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
            this.Head.texOffs(0, 28).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 0).addBox(-5.0F, -17.0F, -5.0F, 10.0F, 7.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(32, 32).addBox(-4.0F, -17.0F, -13.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);
            this.Head.texOffs(0, 44).addBox(-3.5F, -13.0F, -12.0F, 7.0F, 3.0F, 7.0F, 0.0F, false);
            this.Head.texOffs(0, 17).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(0, 54).addBox(-1.0F, -17.0F, 5.0F, 2.0F, 18.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(3.0F, -21.0F, 3.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.48F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(-1.0F, -4.0F, -2.5F, 2.0F, 8.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-3.0F, -21.0F, 3.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.5236F, 0.0F, 0.0F);
            cube_r2.texOffs(0, 0).addBox(-1.0F, -4.0F, -2.5F, 2.0F, 8.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(40, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(24, 28).addBox(-3.0F, 1.0F, -4.0F, 6.0F, 6.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(0, 17).addBox(-2.0F, 7.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(56, 26).addBox(-4.0F, 1.0F, 3.0F, 8.0F, 8.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(44, 45).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(44, 45).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(28, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(30, 17).addBox(-3.45F, 8.0F, -4.0F, 7.0F, 1.0F, 8.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(28, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
            this.LeftBoot.texOffs(30, 17).addBox(-3.5F, 8.0F, -4.0F, 7.0F, 1.0F, 8.0F, 0.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\FurArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */