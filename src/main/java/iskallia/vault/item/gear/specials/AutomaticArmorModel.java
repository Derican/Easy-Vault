// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class AutomaticArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public AutomaticArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 64 : 128);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(28, 22).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(47, 87).addBox(-4.0f, -7.0f, -6.0f, 1.0f, 8.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(66, 69).addBox(-6.0f, -4.0f, -4.0f, 1.0f, 5.0f, 8.0f, 0.0f, false);
        this.Head.texOffs(76, 14).addBox(-4.0f, -8.0f, 5.0f, 8.0f, 9.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(78, 77).addBox(-6.0f, -8.0f, -2.0f, 1.0f, 4.0f, 6.0f, 0.0f, false);
        this.Head.texOffs(77, 0).addBox(5.0f, -8.0f, -2.0f, 1.0f, 4.0f, 6.0f, 0.0f, false);
        this.Head.texOffs(16, 67).addBox(5.0f, -4.0f, -4.0f, 1.0f, 5.0f, 8.0f, 0.0f, false);
        this.Head.texOffs(86, 28).addBox(3.0f, -7.0f, -6.0f, 1.0f, 8.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(32, 5).addBox(-3.0f, -7.0f, -7.0f, 6.0f, 1.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(40, 38).addBox(-2.0f, -8.0f, -6.0f, 4.0f, 1.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(74, 31).addBox(-1.5f, -12.0f, -4.5f, 3.0f, 3.0f, 6.0f, 0.0f, false);
        this.Head.texOffs(85, 0).addBox(-1.5f, -11.0f, 1.5f, 3.0f, 2.0f, 3.0f, 0.0f, false);
        this.Head.texOffs(40, 8).addBox(-1.0f, -10.0f, -6.5f, 2.0f, 3.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
        cube_r1.setPos(2.0f, -13.0f, -5.0f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.0f, 0.0f, 0.6545f);
        cube_r1.texOffs(0, 47).addBox(2.0f, -9.5f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        cube_r1.texOffs(41, 83).addBox(2.0f, -6.5f, -0.5f, 2.0f, 12.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
        cube_r2.setPos(-2.0f, -13.0f, -5.0f);
        this.Head.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.0f, 0.0f, -0.6545f);
        cube_r2.texOffs(37, 51).addBox(-3.0f, -9.75f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        cube_r2.texOffs(16, 84).addBox(-4.0f, -6.75f, -0.5f, 2.0f, 12.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
        cube_r3.setPos(0.0f, -4.5f, -5.5f);
        this.Head.addChild(cube_r3);
        this.setRotationAngle(cube_r3, -0.3054f, 0.0f, 0.0f);
        cube_r3.texOffs(50, 61).addBox(-0.5f, -1.5f, -0.5f, 1.0f, 4.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
        cube_r4.setPos(0.1564f, -1.0f, -5.5f);
        this.Head.addChild(cube_r4);
        this.setRotationAngle(cube_r4, 0.1886f, -0.3864f, -0.0718f);
        cube_r4.texOffs(54, 44).addBox(-0.4697f, -2.0f, -1.1339f, 4.0f, 4.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r5 = new ModelRenderer((Model)this);
        cube_r5.setPos(0.1564f, -1.0f, -5.5f);
        this.Head.addChild(cube_r5);
        this.setRotationAngle(cube_r5, 0.1886f, 0.3864f, 0.0718f);
        cube_r5.texOffs(72, 0).addBox(-3.5303f, -2.0f, -1.1339f, 4.0f, 4.0f, 1.0f, 0.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 47).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(0, 0).addBox(-6.0f, 1.0f, -4.0f, 12.0f, 8.0f, 8.0f, 0.0f, false);
        this.Body.texOffs(76, 69).addBox(-4.0f, 1.0f, 4.0f, 8.0f, 7.0f, 1.0f, 0.0f, false);
        this.Body.texOffs(66, 82).addBox(-2.0f, 1.0f, 5.0f, 4.0f, 7.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r6 = new ModelRenderer((Model)this);
        cube_r6.setPos(-13.0833f, 8.0f, 5.5f);
        this.Body.addChild(cube_r6);
        this.setRotationAngle(cube_r6, 0.1756f, 0.218f, 0.6642f);
        cube_r6.texOffs(29, 16).addBox(-12.1667f, -12.25f, 3.25f, 19.0f, 4.0f, 1.0f, 0.0f, false);
        cube_r6.texOffs(22, 84).addBox(-0.9167f, -8.25f, 3.25f, 3.0f, 9.0f, 1.0f, 0.0f, false);
        cube_r6.texOffs(16, 63).addBox(-4.6667f, -8.25f, 3.25f, 3.0f, 11.0f, 1.0f, 0.0f, false);
        cube_r6.texOffs(0, 79).addBox(-8.4167f, -8.25f, 3.25f, 3.0f, 14.0f, 1.0f, 0.0f, false);
        cube_r6.texOffs(50, 70).addBox(-12.1667f, -8.25f, 3.25f, 3.0f, 17.0f, 1.0f, 0.0f, false);
        cube_r6.texOffs(40, 40).addBox(-7.1667f, -15.25f, 3.25f, 9.0f, 3.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r7 = new ModelRenderer((Model)this);
        cube_r7.setPos(12.8367f, 0.2338f, 5.5f);
        this.Body.addChild(cube_r7);
        this.setRotationAngle(cube_r7, 0.0f, -0.2182f, -0.6545f);
        cube_r7.texOffs(78, 87).addBox(-6.5417f, -3.0f, 2.25f, 3.0f, 9.0f, 1.0f, 0.0f, false);
        cube_r7.texOffs(33, 83).addBox(-2.5417f, -3.0f, 2.25f, 3.0f, 11.0f, 1.0f, 0.0f, false);
        cube_r7.texOffs(8, 79).addBox(1.2083f, -3.0f, 2.25f, 3.0f, 14.0f, 1.0f, 0.0f, false);
        cube_r7.texOffs(58, 77).addBox(4.9583f, -3.0f, 2.25f, 3.0f, 17.0f, 1.0f, 0.0f, false);
        cube_r7.texOffs(0, 43).addBox(-6.0417f, -10.0f, 2.25f, 9.0f, 3.0f, 1.0f, 0.0f, false);
        cube_r7.texOffs(32, 0).addBox(-11.0417f, -7.0f, 2.25f, 19.0f, 4.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r8 = new ModelRenderer((Model)this);
        cube_r8.setPos(0.0f, 10.0f, 6.0f);
        this.Body.addChild(cube_r8);
        this.setRotationAngle(cube_r8, -0.6981f, 0.0f, 0.0f);
        cube_r8.texOffs(0, 0).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r9 = new ModelRenderer((Model)this);
        cube_r9.setPos(0.0f, 5.9997f, -5.7916f);
        this.Body.addChild(cube_r9);
        this.setRotationAngle(cube_r9, -1.5708f, 0.0f, 0.0f);
        cube_r9.texOffs(74, 62).addBox(-3.0f, -3.5f, -1.5f, 6.0f, 3.0f, 4.0f, 0.0f, false);
        final ModelRenderer cube_r10 = new ModelRenderer((Model)this);
        cube_r10.setPos(0.0f, 7.9997f, -3.2916f);
        this.Body.addChild(cube_r10);
        this.setRotationAngle(cube_r10, -1.1781f, 0.0f, 0.0f);
        cube_r10.texOffs(62, 5).addBox(-2.0f, -1.0f, -2.75f, 4.0f, 2.0f, 7.0f, 0.0f, false);
        final ModelRenderer cube_r11 = new ModelRenderer((Model)this);
        cube_r11.setPos(0.0f, 5.0f, -0.5f);
        this.Body.addChild(cube_r11);
        this.setRotationAngle(cube_r11, -1.0036f, 0.0f, 0.0f);
        cube_r11.texOffs(0, 16).addBox(-5.5f, -3.0f, -5.5f, 11.0f, 7.0f, 7.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(65, 40).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r12 = new ModelRenderer((Model)this);
        cube_r12.setPos(-4.875f, 5.25f, 0.0f);
        this.RightArm.addChild(cube_r12);
        this.setRotationAngle(cube_r12, 0.0f, 0.0f, -0.2182f);
        cube_r12.texOffs(24, 52).addBox(-0.625f, -3.5f, -3.5f, 3.0f, 8.0f, 7.0f, 0.0f, false);
        cube_r12.texOffs(83, 48).addBox(-2.625f, -1.5f, -2.0f, 1.0f, 4.0f, 4.0f, 0.0f, false);
        cube_r12.texOffs(20, 30).addBox(-3.625f, -0.5f, -1.0f, 2.0f, 4.0f, 2.0f, 0.0f, false);
        cube_r12.texOffs(74, 49).addBox(-1.625f, -2.5f, -3.5f, 1.0f, 6.0f, 7.0f, 0.0f, false);
        final ModelRenderer cube_r13 = new ModelRenderer((Model)this);
        cube_r13.setPos(0.0f, 0.0f, 0.0f);
        this.RightArm.addChild(cube_r13);
        this.setRotationAngle(cube_r13, 0.0f, 0.0f, 0.48f);
        cube_r13.texOffs(36, 59).addBox(-11.0f, -2.5f, -4.0f, 3.0f, 3.0f, 8.0f, 0.0f, false);
        cube_r13.texOffs(80, 24).addBox(-9.0f, -4.5f, -3.0f, 7.0f, 2.0f, 2.0f, 0.0f, false);
        cube_r13.texOffs(81, 44).addBox(-9.0f, -4.5f, 1.0f, 7.0f, 2.0f, 2.0f, 0.0f, false);
        cube_r13.texOffs(52, 50).addBox(-6.0f, -0.5f, -4.5f, 2.0f, 2.0f, 9.0f, 0.0f, false);
        cube_r13.texOffs(20, 38).addBox(-8.0f, -2.5f, -4.0f, 6.0f, 5.0f, 8.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(64, 21).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r14 = new ModelRenderer((Model)this);
        cube_r14.setPos(5.15f, -4.0f, 0.0f);
        this.LeftArm.addChild(cube_r14);
        this.setRotationAngle(cube_r14, -3.1416f, 0.0f, 2.6616f);
        cube_r14.texOffs(16, 80).addBox(-2.15f, -3.25f, 1.0f, 7.0f, 2.0f, 2.0f, 0.0f, false);
        cube_r14.texOffs(77, 40).addBox(-2.15f, -3.25f, -3.0f, 7.0f, 2.0f, 2.0f, 0.0f, false);
        cube_r14.texOffs(0, 30).addBox(-1.15f, -1.25f, -4.0f, 6.0f, 5.0f, 8.0f, 0.0f, false);
        cube_r14.texOffs(51, 29).addBox(0.85f, 0.75f, -4.5f, 2.0f, 2.0f, 9.0f, 0.0f, false);
        cube_r14.texOffs(40, 5).addBox(-4.15f, -1.25f, -4.0f, 3.0f, 3.0f, 8.0f, 0.0f, false);
        final ModelRenderer cube_r15 = new ModelRenderer((Model)this);
        cube_r15.setPos(4.875f, 5.25f, 0.0f);
        this.LeftArm.addChild(cube_r15);
        this.setRotationAngle(cube_r15, -3.1416f, 0.0f, -2.9671f);
        cube_r15.texOffs(26, 67).addBox(-2.375f, -1.5f, -2.0f, 1.0f, 4.0f, 4.0f, 0.0f, false);
        cube_r15.texOffs(0, 30).addBox(-3.375f, -0.5f, -1.0f, 2.0f, 4.0f, 2.0f, 0.0f, false);
        cube_r15.texOffs(41, 44).addBox(-0.375f, -3.5f, -3.5f, 3.0f, 8.0f, 7.0f, 0.0f, false);
        cube_r15.texOffs(34, 70).addBox(-1.375f, -2.5f, -3.5f, 1.0f, 6.0f, 7.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 63).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r16 = new ModelRenderer((Model)this);
        cube_r16.setPos(0.0f, 8.0f, -3.25f);
        this.RightBoot.addChild(cube_r16);
        this.setRotationAngle(cube_r16, -0.2182f, 0.0f, 0.0f);
        cube_r16.texOffs(24, 51).addBox(-1.0f, -4.0f, -0.5f, 2.0f, 6.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r17 = new ModelRenderer((Model)this);
        cube_r17.setPos(0.0f, 10.5f, -3.5f);
        this.RightBoot.addChild(cube_r17);
        this.setRotationAngle(cube_r17, 0.3054f, 0.0f, 0.0f);
        cube_r17.texOffs(54, 5).addBox(-2.5f, -2.5f, -0.5f, 5.0f, 5.0f, 1.0f, 0.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(58, 61).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r18 = new ModelRenderer((Model)this);
        cube_r18.setPos(0.2f, 8.0f, -3.25f);
        this.LeftBoot.addChild(cube_r18);
        this.setRotationAngle(cube_r18, -0.2182f, 0.0f, 0.0f);
        cube_r18.texOffs(0, 16).addBox(-1.0f, -4.0f, -0.5f, 2.0f, 6.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r19 = new ModelRenderer((Model)this);
        cube_r19.setPos(0.2f, 10.5f, -3.5f);
        this.LeftBoot.addChild(cube_r19);
        this.setRotationAngle(cube_r19, 0.3054f, 0.0f, 0.0f);
        cube_r19.texOffs(52, 21).addBox(-2.5f, -2.5f, -0.5f, 5.0f, 5.0f, 1.0f, 0.0f, false);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(0, 9).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        this.Belt.texOffs(34, 35).addBox(-2.0f, 9.0f, -4.0f, 4.0f, 5.0f, 2.0f, 0.0f, false);
        this.Belt.texOffs(34, 17).addBox(-2.0f, 9.0f, 2.0f, 4.0f, 5.0f, 2.0f, 0.0f, false);
        this.Belt.texOffs(0, 0).addBox(-5.0f, 10.0f, -3.0f, 10.0f, 3.0f, 6.0f, 0.0f, false);
        final ModelRenderer cube_r1_l2 = new ModelRenderer((Model)this);
        cube_r1_l2.setPos(-3.5f, 11.5f, 3.0f);
        this.Belt.addChild(cube_r1_l2);
        this.setRotationAngle(cube_r1_l2, 0.0f, 0.0f, 0.829f);
        cube_r1_l2.texOffs(36, 24).addBox(-1.5f, -2.5f, -0.5f, 3.0f, 5.0f, 1.0f, 0.0f, false);
        cube_r1_l2.texOffs(24, 37).addBox(-1.5f, -2.5f, -6.5f, 3.0f, 5.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r2_l2 = new ModelRenderer((Model)this);
        cube_r2_l2.setPos(3.5f, 11.5f, 3.0f);
        this.Belt.addChild(cube_r2_l2);
        this.setRotationAngle(cube_r2_l2, 0.0f, 0.0f, -0.829f);
        cube_r2_l2.texOffs(26, 0).addBox(-1.5f, -2.5f, -0.5f, 3.0f, 5.0f, 1.0f, 0.0f, false);
        cube_r2_l2.texOffs(16, 37).addBox(-1.5f, -2.5f, -6.5f, 3.0f, 5.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r3_l2 = new ModelRenderer((Model)this);
        cube_r3_l2.setPos(5.5f, 11.5f, 0.0f);
        this.Belt.addChild(cube_r3_l2);
        this.setRotationAngle(cube_r3_l2, 0.0f, 0.0f, 0.2618f);
        cube_r3_l2.texOffs(24, 9).addBox(-0.5f, -2.5f, -2.5f, 1.0f, 5.0f, 5.0f, 0.0f, false);
        final ModelRenderer cube_r4_l2 = new ModelRenderer((Model)this);
        cube_r4_l2.setPos(-5.5f, 11.5f, 0.0f);
        this.Belt.addChild(cube_r4_l2);
        this.setRotationAngle(cube_r4_l2, 0.0f, 0.0f, -0.2618f);
        cube_r4_l2.texOffs(31, 4).addBox(-0.5f, -2.5f, -2.5f, 1.0f, 5.0f, 5.0f, 0.0f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 25).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(20, 21).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
    }
}
