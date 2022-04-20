package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ScrappyArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 64);
            this.texHeight = (this.isLayer2() ? 32 : 64);
            (this.Head = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            this.Head.texOffs(0, 0).addBox(5.0f, -5.0f, -5.0f, 1.0f, 3.0f, 3.0f, 0.0f, false);
            this.Head.texOffs(0, 0).addBox(-6.0f, -5.0f, -5.0f, 1.0f, 3.0f, 3.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5161f, 0.0f, 0.1884f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.0f, -0.6545f, 0.0f);
            cube_r1.texOffs(40, 9).addBox(-9.0438f, -9.0f, -4.2832f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r1.texOffs(36, 23).addBox(-9.0438f, -3.0f, -4.2832f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-5.0f, 0.0f, -3.0f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.0f, 0.6545f, 0.0f);
            cube_r2.texOffs(40, 9).addBox(1.1637f, -9.0f, -1.4569f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r2.texOffs(36, 23).addBox(1.1637f, -3.0f, -1.4569f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(28, 28).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, -4.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, 3.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(0, 16).addBox(-5.5f, 11.0f, -3.5f, 11.0f, 2.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(5.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.0f, 0.0f, -1.8326f);
            cube_r3.texOffs(18, 0).addBox(-7.8378f, -0.6224f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-4.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.0f, -1.2217f);
            cube_r4.texOffs(18, 0).addBox(-8.0f, -1.0f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model) this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(16, 40).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 25).addBox(-5.0f, -4.0f, -4.0f, 6.0f, 6.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(32, 0).addBox(-5.0f, 2.0f, -4.0f, 3.0f, 1.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(29, 9).addBox(-5.0f, 5.0f, -3.5f, 2.0f, 7.0f, 7.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model) this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(16, 40).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.RightBoot = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 64);
            this.texHeight = (this.isLayer2() ? 32 : 64);
            (this.Head = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0f, 0.0f, 0.0f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.1309f, 0.0f, 0.0f);
            cube_r1.texOffs(4, 4).addBox(-0.5f, -12.0f, 0.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0f, 0.0f, 0.0f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.2182f, 0.0f, 0.0f);
            cube_r2.texOffs(0, 0).addBox(-0.5f, -14.0f, -3.0f, 1.0f, 6.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(4.5161f, 0.0f, 0.1884f);
            this.Head.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.0f, -0.6545f, 0.0f);
            cube_r3.texOffs(40, 9).addBox(-9.0438f, -9.0f, -4.2832f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r3.texOffs(36, 23).addBox(-9.0438f, -3.0f, -4.2832f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-5.0f, 0.0f, -3.0f);
            this.Head.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.6545f, 0.0f);
            cube_r4.texOffs(40, 9).addBox(1.1637f, -9.0f, -1.4569f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r4.texOffs(36, 23).addBox(1.1637f, -3.0f, -1.4569f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(28, 28).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, -4.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, 3.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(0, 16).addBox(-5.5f, 11.0f, -3.5f, 11.0f, 2.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(5.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r5);
            this.setRotationAngle(cube_r5, 0.0f, 0.0f, -1.8326f);
            cube_r5.texOffs(18, 0).addBox(-7.8378f, 0.4276f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            final ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-4.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r6);
            this.setRotationAngle(cube_r6, 0.0f, 0.0f, -1.2217f);
            cube_r6.texOffs(18, 0).addBox(-8.0f, -1.3f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model) this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(16, 40).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 25).addBox(-5.0f, -4.0f, -4.0f, 6.0f, 6.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(4, 4).addBox(-3.5f, -7.0f, 1.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            this.RightArm.texOffs(4, 4).addBox(-3.5f, -7.0f, -2.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            this.RightArm.texOffs(20, 25).addBox(-7.5f, -2.0f, -2.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);
            this.RightArm.texOffs(20, 25).addBox(-7.5f, -2.0f, 1.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);
            this.RightArm.texOffs(32, 0).addBox(-5.0f, 2.0f, -4.0f, 3.0f, 1.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(29, 9).addBox(-5.0f, 5.0f, -3.5f, 2.0f, 7.0f, 7.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model) this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(16, 40).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.RightBoot = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }

    public static class Variant3<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant3(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 64);
            this.texHeight = (this.isLayer2() ? 32 : 64);
            (this.Head = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            this.Head.texOffs(0, 0).addBox(5.0f, -5.0f, -5.0f, 1.0f, 3.0f, 3.0f, 0.0f, false);
            this.Head.texOffs(0, 0).addBox(-6.0f, -5.0f, -5.0f, 1.0f, 3.0f, 3.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(4.5161f, 0.0f, 0.1884f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.0f, -0.6545f, 0.0f);
            cube_r1.texOffs(40, 9).addBox(-9.0438f, -9.0f, -4.2832f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r1.texOffs(36, 23).addBox(-9.0438f, -3.0f, -4.2832f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-5.0f, 0.0f, -3.0f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.0f, 0.6545f, 0.0f);
            cube_r2.texOffs(40, 9).addBox(1.1637f, -9.0f, -1.4569f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            cube_r2.texOffs(36, 23).addBox(1.1637f, -3.0f, -1.4569f, 6.0f, 4.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(28, 28).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, -4.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(32, 44).addBox(-4.0f, 2.0f, 3.0f, 8.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(0, 16).addBox(-5.5f, 11.0f, -3.5f, 11.0f, 2.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(5.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.0f, 0.0f, -1.8326f);
            cube_r3.texOffs(18, 0).addBox(-7.8378f, 0.2276f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-4.0f, 13.0f, 0.0f);
            this.Body.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.0f, -1.2217f);
            cube_r4.texOffs(18, 0).addBox(-8.0f, -1.2f, -3.0f, 8.0f, 0.1f, 6.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model) this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(16, 40).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 25).addBox(-5.0f, -4.0f, -4.0f, 6.0f, 6.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(32, 0).addBox(-5.0f, 2.0f, -4.0f, 3.0f, 1.0f, 8.0f, 0.0f, false);
            this.RightArm.texOffs(29, 9).addBox(-5.0f, 5.0f, -3.5f, 2.0f, 7.0f, 7.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model) this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(16, 40).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.RightBoot = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(0, 39).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model) this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model) this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model) this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }
}
