// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PlatedArmorModel
{
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T>
    {
        public Variant1(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 32 : 128);
            (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 0).addBox(-1.0f, -10.75f, -5.75f, 2.0f, 7.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(0, 29).addBox(-1.0f, -10.75f, -4.75f, 2.0f, 1.0f, 11.0f, 0.0f, false);
            this.Head.texOffs(20, 31).addBox(-0.4f, -11.75f, 3.0f, 1.0f, 1.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(20, 31).addBox(-0.4f, -11.75f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(20, 31).addBox(-0.4f, -11.75f, -3.0f, 1.0f, 1.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(15, 26).addBox(-5.9f, -12.25f, -3.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
            this.Head.texOffs(15, 26).addBox(5.1f, -12.25f, -3.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
            this.Head.texOffs(21, 14).addBox(-1.0f, -9.75f, 5.25f, 2.0f, 6.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
            cube_r1.setPos(4.5f, -8.0f, -5.5f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.0f, 0.0f, 1.2654f);
            cube_r1.texOffs(29, 3).addBox(-2.0f, -1.75f, -0.5f, 1.0f, 1.0f, 11.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
            cube_r2.setPos(-4.5f, -8.0f, -5.5f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.0f, 0.0f, 0.3491f);
            cube_r2.texOffs(29, 3).addBox(-1.75f, -2.0f, -0.5f, 1.0f, 1.0f, 11.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
            cube_r3.setPos(5.6f, -8.0f, 2.0f);
            this.Head.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.0f, 0.0f, 0.5236f);
            cube_r3.texOffs(0, 14).addBox(-0.5f, -4.25f, -1.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
            cube_r4.setPos(-5.4f, -8.0f, 2.0f);
            this.Head.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.0f, -0.5672f);
            cube_r4.texOffs(0, 14).addBox(-0.5f, -4.25f, -1.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
            final ModelRenderer helmet2 = new ModelRenderer((Model)this);
            helmet2.setPos(0.0f, 0.0f, 0.0f);
            this.Head.addChild(helmet2);
            helmet2.texOffs(28, 15).addBox(-5.5f, -9.75f, 4.5f, 11.0f, 8.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(48, 25).addBox(-4.5f, -9.75f, -5.5f, 9.0f, 5.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 0).addBox(-4.5f, -4.75f, -5.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 0).addBox(-5.5f, -1.75f, -4.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 4).addBox(-5.5f, -1.75f, -3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 4).addBox(4.5f, -1.75f, -3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 0).addBox(4.5f, -1.75f, -4.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(0, 34).addBox(-5.5f, -1.75f, -5.5f, 4.0f, 3.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(0, 34).addBox(1.5f, -1.75f, -5.5f, 4.0f, 3.0f, 1.0f, 0.0f, false);
            helmet2.texOffs(32, 0).addBox(3.5f, -4.75f, -5.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            final ModelRenderer Helmet6_r1 = new ModelRenderer((Model)this);
            Helmet6_r1.setPos(0.0f, 0.0f, 0.0f);
            helmet2.addChild(Helmet6_r1);
            this.setRotationAngle(Helmet6_r1, -1.5708f, 0.0f, 0.0f);
            Helmet6_r1.texOffs(43, 43).addBox(-4.5f, -4.5f, -9.75f, 9.0f, 9.0f, 1.0f, 0.0f, false);
            final ModelRenderer Helmet4_r1 = new ModelRenderer((Model)this);
            Helmet4_r1.setPos(0.0f, 0.0f, 0.0f);
            helmet2.addChild(Helmet4_r1);
            this.setRotationAngle(Helmet4_r1, 0.0f, 1.5708f, 0.0f);
            Helmet4_r1.texOffs(42, 0).addBox(-4.5f, -9.75f, -5.5f, 10.0f, 8.0f, 1.0f, 0.0f, false);
            Helmet4_r1.texOffs(42, 0).addBox(-4.5f, -9.75f, 4.5f, 10.0f, 8.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(24, 24).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(0, 0).addBox(-6.0f, -2.0f, -4.0f, 12.0f, 6.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(0, 28).addBox(1.0f, -1.0f, -5.0f, 4.0f, 5.0f, 1.0f, 0.0f, true);
            this.Body.texOffs(0, 28).addBox(-5.0f, -1.0f, -5.0f, 4.0f, 5.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(15, 32).addBox(-1.0f, -1.0f, -5.0f, 2.0f, 3.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(40, 32).addBox(-6.0f, 4.0f, -4.0f, 4.0f, 3.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(40, 32).addBox(2.0f, 4.0f, -4.0f, 4.0f, 3.0f, 8.0f, 0.0f, true);
            this.Body.texOffs(20, 35).addBox(-2.0f, 4.0f, -4.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(20, 35).addBox(-2.0f, 4.0f, 3.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(19, 26).addBox(-1.0f, 4.0f, -4.0f, 2.0f, 1.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(19, 26).addBox(-1.0f, 4.0f, 3.0f, 2.0f, 1.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(20, 35).addBox(1.0f, 4.0f, -4.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(20, 35).addBox(1.0f, 4.0f, 3.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            final ModelRenderer Innerchestplate = new ModelRenderer((Model)this);
            Innerchestplate.setPos(0.0f, 0.0f, 0.0f);
            this.Body.addChild(Innerchestplate);
            Innerchestplate.texOffs(0, 26).addBox(-3.0f, 11.75f, -3.25f, 6.0f, 1.0f, 1.0f, 0.0f, false);
            Innerchestplate.texOffs(16, 52).addBox(-4.0f, 5.0f, -3.5f, 8.0f, 7.0f, 1.0f, 0.0f, false);
            Innerchestplate.texOffs(16, 52).addBox(-4.0f, 5.0f, 2.5f, 8.0f, 7.0f, 1.0f, 0.0f, false);
            Innerchestplate.texOffs(0, 26).addBox(-3.0f, 11.75f, 2.25f, 6.0f, 1.0f, 1.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(0, 41).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 14).addBox(-7.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, true);
            this.RightArm.texOffs(19, 40).addBox(-7.0f, 3.0f, -3.5f, 5.0f, 5.0f, 7.0f, 0.0f, true);
            this.RightArm.texOffs(46, 18).addBox(-6.0f, -5.0f, -3.0f, 4.0f, 1.0f, 6.0f, 0.0f, true);
            (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(0, 41).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            this.LeftArm.texOffs(46, 18).addBox(2.0f, -5.0f, -3.0f, 4.0f, 1.0f, 6.0f, 0.0f, false);
            this.LeftArm.texOffs(0, 14).addBox(0.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            this.LeftArm.texOffs(19, 40).addBox(1.75f, 3.0f, -3.5f, 5.0f, 5.0f, 7.0f, 0.0f, false);
            (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(52, 11).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(52, 11).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            this.RightLeg.texOffs(19, 1).addBox(-3.25f, -2.0f, -3.0f, 2.0f, 6.0f, 6.0f, 0.0f, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.25f, 4.0f, -2.0f, 2.0f, 2.0f, 4.0f, 0.0f, false);
            (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 0).addBox(1.2f, 4.0f, -2.0f, 2.0f, 2.0f, 4.0f, 0.0f, false);
            this.LeftLeg.texOffs(0, 0).addBox(1.2f, -2.0f, -3.0f, 2.0f, 6.0f, 6.0f, 0.0f, false);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }
    
    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T>
    {
        public Variant2(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 32 : 128);
            (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(78, 0).addBox(-4.0f, -7.0f, -4.25f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            this.Head.texOffs(20, 48).addBox(-6.25f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(20, 48).addBox(5.25f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(0, 59).addBox(-6.25f, -1.0f, -6.0f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(0, 59).addBox(3.25f, -1.0f, -6.0f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
            cube_r1.setPos(-2.75f, 0.5f, -5.5f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.0f, -0.3927f, 0.0f);
            cube_r1.texOffs(8, 59).addBox(-1.5f, -0.5f, -0.5f, 3.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
            cube_r2.setPos(2.75f, 0.5f, -5.5f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.0f, 0.3927f, 0.0f);
            cube_r2.texOffs(8, 59).addBox(-1.5f, -0.5f, -0.5f, 3.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
            cube_r3.setPos(5.35f, -9.25f, 3.0f);
            this.Head.addChild(cube_r3);
            this.setRotationAngle(cube_r3, -0.6109f, 0.0f, 0.0f);
            cube_r3.texOffs(56, 48).addBox(-0.5f, -2.75f, -1.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
            cube_r3.texOffs(56, 48).addBox(-11.25f, -2.75f, -1.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
            cube_r4.setPos(3.6f, -8.75f, -1.0f);
            this.Head.addChild(cube_r4);
            this.setRotationAngle(cube_r4, -0.6109f, 0.0f, 0.0f);
            cube_r4.texOffs(62, 48).addBox(-0.5f, -2.25f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
            cube_r4.texOffs(62, 48).addBox(-7.5f, -2.25f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
            final ModelRenderer cube_r5 = new ModelRenderer((Model)this);
            cube_r5.setPos(0.1f, -8.75f, -2.75f);
            this.Head.addChild(cube_r5);
            this.setRotationAngle(cube_r5, -0.6545f, 0.0f, 0.0f);
            cube_r5.texOffs(50, 48).addBox(-0.5f, -2.25f, -1.0f, 1.0f, 7.0f, 2.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(62, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(0, 0).addBox(-6.0f, -2.0f, -4.0f, 12.0f, 6.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(40, 0).addBox(-5.5f, 4.0f, -4.0f, 11.0f, 3.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(0, 16).addBox(-5.25f, 7.0f, -3.5f, 10.0f, 3.0f, 7.0f, 0.0f, false);
            this.Body.texOffs(40, 32).addBox(-5.0f, -1.0f, -5.0f, 10.0f, 3.0f, 2.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(24, 32).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(34, 16).addBox(-7.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            this.RightArm.texOffs(0, 32).addBox(-7.0f, 2.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, true);
            final ModelRenderer cube_r6 = new ModelRenderer((Model)this);
            cube_r6.setPos(-6.0f, -5.0f, 0.0f);
            this.RightArm.addChild(cube_r6);
            this.setRotationAngle(cube_r6, 0.0f, 0.0f, 0.4363f);
            cube_r6.texOffs(0, 48).addBox(0.0f, -1.0f, -3.0f, 4.0f, 2.0f, 6.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(24, 32).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            this.LeftArm.texOffs(0, 32).addBox(1.75f, 2.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, false);
            this.LeftArm.texOffs(34, 16).addBox(0.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r7 = new ModelRenderer((Model)this);
            cube_r7.setPos(4.125f, -4.0f, 0.0f);
            this.LeftArm.addChild(cube_r7);
            this.setRotationAngle(cube_r7, 0.0f, 0.0f, -0.3927f);
            cube_r7.texOffs(40, 37).addBox(-2.125f, -1.0f, -3.0f, 4.0f, 2.0f, 6.0f, 0.0f, false);
            (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(34, 48).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(34, 48).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            final ModelRenderer cube_r1_l = new ModelRenderer((Model)this);
            cube_r1_l.setPos(2.0976f, 20.076f, -0.2181f);
            this.Belt.addChild(cube_r1_l);
            this.setRotationAngle(cube_r1_l, -0.0436f, 0.0f, 0.6981f);
            cube_r1_l.texOffs(0, 0).addBox(-3.25f, -8.25f, -2.75f, 5.0f, 1.0f, 5.0f, 0.0f, false);
            final ModelRenderer cube_r2_l = new ModelRenderer((Model)this);
            cube_r2_l.setPos(-1.9f, 12.0f, 0.0f);
            this.Belt.addChild(cube_r2_l);
            this.setRotationAngle(cube_r2_l, 0.0f, 0.0f, -0.7854f);
            cube_r2_l.texOffs(0, 0).addBox(-7.0f, -2.5f, -2.5f, 5.0f, 1.0f, 5.0f, 0.0f, false);
            (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }
    
    public static class Variant3<T extends LivingEntity> extends VaultGearModel<T>
    {
        public Variant3(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 32 : 128);
            (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 25).addBox(-4.0f, -7.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            this.Head.texOffs(24, 50).addBox(-6.0f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(24, 50).addBox(5.0f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(0, 19).addBox(-6.0f, -1.0f, -6.0f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(0, 19).addBox(3.0f, -1.0f, -6.0f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(0, 0).addBox(-1.0f, -9.0f, -6.0f, 2.0f, 5.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(0, 41).addBox(-1.0f, -9.0f, -5.0f, 2.0f, 1.0f, 10.0f, 0.0f, false);
            this.Head.texOffs(0, 14).addBox(-1.0f, -9.0f, 5.0f, 2.0f, 4.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
            cube_r1.setPos(6.0f, -7.4944f, -4.5165f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.8727f, 0.0f, 0.0f);
            cube_r1.texOffs(30, 18).addBox(-4.0f, -1.5f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            cube_r1.texOffs(30, 18).addBox(-9.0f, -1.5f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
            cube_r2.setPos(-2.5f, 0.5f, -5.5f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.0f, -0.3927f, 0.0f);
            cube_r2.texOffs(0, 6).addBox(-1.5f, -0.5f, -0.5f, 3.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
            cube_r3.setPos(2.5f, 0.5f, -5.5f);
            this.Head.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.0f, 0.3927f, 0.0f);
            cube_r3.texOffs(0, 6).addBox(-1.5f, -0.5f, -0.5f, 3.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
            cube_r4.setPos(2.1f, -7.75f, 5.0f);
            this.Head.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.0f, 0.6545f);
            cube_r4.texOffs(4, 25).addBox(-0.5f, -2.75f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r5 = new ModelRenderer((Model)this);
            cube_r5.setPos(5.6f, -9.5f, 3.5f);
            this.Head.addChild(cube_r5);
            this.setRotationAngle(cube_r5, 0.0f, 0.0f, 0.6545f);
            cube_r5.texOffs(4, 25).addBox(-0.5f, -2.75f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r6 = new ModelRenderer((Model)this);
            cube_r6.setPos(3.6f, -9.0f, -0.5f);
            this.Head.addChild(cube_r6);
            this.setRotationAngle(cube_r6, 0.0f, 0.0f, 0.5236f);
            cube_r6.texOffs(32, 32).addBox(-0.5f, -2.25f, -0.5f, 1.0f, 4.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r7 = new ModelRenderer((Model)this);
            cube_r7.setPos(-1.9f, -7.75f, 4.5f);
            this.Head.addChild(cube_r7);
            this.setRotationAngle(cube_r7, 0.0f, 0.0f, -0.6109f);
            cube_r7.texOffs(4, 25).addBox(-0.5f, -2.75f, 0.0f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r8 = new ModelRenderer((Model)this);
            cube_r8.setPos(-5.4f, -9.25f, 3.0f);
            this.Head.addChild(cube_r8);
            this.setRotationAngle(cube_r8, 0.0f, 0.0f, -0.6109f);
            cube_r8.texOffs(4, 25).addBox(-0.5f, -2.75f, 0.0f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r9 = new ModelRenderer((Model)this);
            cube_r9.setPos(-3.4f, -8.75f, -1.0f);
            this.Head.addChild(cube_r9);
            this.setRotationAngle(cube_r9, 0.0f, 0.0f, -0.5236f);
            cube_r9.texOffs(32, 32).addBox(-0.5f, -2.25f, 0.0f, 1.0f, 4.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(40, 0).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(0, 0).addBox(-6.0f, -2.0f, -4.0f, 12.0f, 6.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(0, 14).addBox(-5.5f, 4.0f, -4.0f, 11.0f, 3.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(31, 18).addBox(-5.25f, 7.0f, -3.5f, 10.0f, 3.0f, 7.0f, 0.0f, false);
            this.Body.texOffs(0, 52).addBox(-5.0f, -1.0f, -5.0f, 10.0f, 3.0f, 2.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(38, 44).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(32, 32).addBox(-6.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            this.RightArm.texOffs(14, 41).addBox(-5.0f, 5.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, true);
            final ModelRenderer cube_r10 = new ModelRenderer((Model)this);
            cube_r10.setPos(-3.0f, -5.0f, -4.0f);
            this.RightArm.addChild(cube_r10);
            this.setRotationAngle(cube_r10, 0.0f, 0.0f, 0.829f);
            cube_r10.texOffs(30, 16).addBox(-1.0f, 0.0f, 2.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r11 = new ModelRenderer((Model)this);
            cube_r11.setPos(-4.0f, -5.0f, -2.0f);
            this.RightArm.addChild(cube_r11);
            this.setRotationAngle(cube_r11, 0.0f, 0.0f, 0.829f);
            cube_r11.texOffs(24, 30).addBox(-3.0f, 0.0f, 2.0f, 7.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r12 = new ModelRenderer((Model)this);
            cube_r12.setPos(-4.875f, -4.5f, 3.0f);
            this.RightArm.addChild(cube_r12);
            this.setRotationAngle(cube_r12, -0.6545f, 0.0f, 0.0f);
            cube_r12.texOffs(0, 25).addBox(-0.125f, -4.5f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r13 = new ModelRenderer((Model)this);
            cube_r13.setPos(-5.0f, -2.0f, 0.0f);
            this.RightArm.addChild(cube_r13);
            this.setRotationAngle(cube_r13, 0.0f, 0.0f, 0.4363f);
            cube_r13.texOffs(24, 28).addBox(-4.0f, 0.0f, 2.0f, 8.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r14 = new ModelRenderer((Model)this);
            cube_r14.setPos(-5.0f, -5.0f, 0.0f);
            this.RightArm.addChild(cube_r14);
            this.setRotationAngle(cube_r14, 0.0f, 0.0f, 0.4363f);
            cube_r14.texOffs(24, 28).addBox(-4.0f, 0.0f, 2.0f, 8.0f, 1.0f, 1.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(38, 44).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            this.LeftArm.texOffs(14, 41).addBox(-0.25f, 5.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, false);
            this.LeftArm.texOffs(32, 32).addBox(-1.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r15 = new ModelRenderer((Model)this);
            cube_r15.setPos(5.0f, -4.0f, 0.0f);
            this.LeftArm.addChild(cube_r15);
            this.setRotationAngle(cube_r15, 0.0f, 0.0f, 2.618f);
            cube_r15.texOffs(24, 28).addBox(-4.0f, 0.0f, 2.0f, 8.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r16 = new ModelRenderer((Model)this);
            cube_r16.setPos(4.125f, -4.5f, 3.0f);
            this.LeftArm.addChild(cube_r16);
            this.setRotationAngle(cube_r16, -0.6545f, 0.0f, 0.0f);
            cube_r16.texOffs(0, 25).addBox(-0.125f, -4.5f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r17 = new ModelRenderer((Model)this);
            cube_r17.setPos(5.0f, -1.0f, 0.0f);
            this.LeftArm.addChild(cube_r17);
            this.setRotationAngle(cube_r17, 0.0f, 0.0f, 2.618f);
            cube_r17.texOffs(24, 28).addBox(-4.0f, 0.0f, 2.0f, 8.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r18 = new ModelRenderer((Model)this);
            cube_r18.setPos(4.0f, -4.0f, -2.0f);
            this.LeftArm.addChild(cube_r18);
            this.setRotationAngle(cube_r18, 0.0f, 0.0f, 2.3562f);
            cube_r18.texOffs(24, 30).addBox(-3.0f, 0.0f, 2.0f, 7.0f, 1.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r19 = new ModelRenderer((Model)this);
            cube_r19.setPos(3.0f, -4.0f, -4.0f);
            this.LeftArm.addChild(cube_r19);
            this.setRotationAngle(cube_r19, 0.0f, 0.0f, 2.3562f);
            cube_r19.texOffs(30, 16).addBox(-1.5f, 0.0f, 2.0f, 5.0f, 1.0f, 1.0f, 0.0f, false);
            (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(53, 28).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, false);
            this.RightBoot.texOffs(44, 5).addBox(-3.25f, 8.0f, -3.75f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            this.RightBoot.texOffs(43, 5).addBox(-2.25f, 7.0f, -3.75f, 4.0f, 1.0f, 1.0f, 0.0f, false);
            (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(43, 5).addBox(-2.05f, 7.0f, -3.75f, 4.0f, 1.0f, 1.0f, 0.0f, false);
            this.LeftBoot.texOffs(53, 28).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, true);
            this.LeftBoot.texOffs(44, 5).addBox(-3.05f, 8.0f, -3.75f, 6.0f, 5.0f, 1.0f, 0.0f, false);
            (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.0f, 3.0f, -3.0f, 5.0f, 5.0f, 6.0f, 0.0f, false);
            (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
            this.LeftLeg.texOffs(0, 0).addBox(-1.8f, 3.0f, -3.0f, 5.0f, 5.0f, 6.0f, 0.0f, false);
        }
    }
    
    public static class Variant4<T extends LivingEntity> extends VaultGearModel<T>
    {
        public Variant4(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 32 : 128);
            (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(40, 0).addBox(-4.0f, -7.0f, -4.25f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            this.Head.texOffs(36, 53).addBox(-6.0f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(36, 53).addBox(5.0f, -4.0f, -5.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(32, 64).addBox(-6.0f, -2.0f, -5.75f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(32, 64).addBox(3.0f, -2.0f, -5.75f, 3.0f, 2.0f, 1.0f, 0.0f, false);
            this.Head.texOffs(72, 0).addBox(-3.0f, -9.0f, -4.5f, 6.0f, 1.0f, 9.0f, 0.0f, false);
            this.Head.texOffs(0, 53).addBox(-5.5f, -7.0f, -2.5f, 1.0f, 5.0f, 6.0f, 0.0f, false);
            this.Head.texOffs(0, 53).addBox(4.5f, -7.0f, -2.5f, 1.0f, 5.0f, 6.0f, 0.0f, true);
            this.Head.texOffs(62, 37).addBox(-2.0f, -10.0f, -3.5f, 4.0f, 1.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
            cube_r1.setPos(0.0f, -5.5f, -5.25f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, -0.7418f, 0.0f, 0.0f);
            cube_r1.texOffs(18, 64).addBox(-3.0f, -0.5f, -0.5f, 6.0f, 1.0f, 1.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(28, 21).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(0, 0).addBox(-6.0f, -2.0f, -4.0f, 12.0f, 13.0f, 8.0f, 0.0f, false);
            this.Body.texOffs(52, 21).addBox(-5.0f, -1.0f, -6.0f, 10.0f, 8.0f, 3.0f, 0.0f, false);
            this.Body.texOffs(40, 37).addBox(-5.0f, -1.0f, 3.0f, 10.0f, 8.0f, 1.0f, 0.0f, false);
            this.Body.texOffs(14, 53).addBox(-4.5f, 7.75f, 2.0f, 9.0f, 1.0f, 2.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
            cube_r2.setPos(0.0f, 1.875f, -5.875f);
            this.Body.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.3927f, 0.0f, 0.0f);
            cube_r2.texOffs(50, 53).addBox(-4.75f, -1.875f, -0.875f, 9.0f, 3.0f, 1.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
            cube_r3.setPos(0.75f, 8.875f, -3.375f);
            this.Body.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.6545f, 0.0f, 0.0f);
            cube_r3.texOffs(0, 64).addBox(-4.5f, -0.875f, -1.375f, 7.0f, 1.0f, 2.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(24, 37).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            this.RightArm.texOffs(0, 21).addBox(-5.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, true);
            this.RightArm.texOffs(78, 21).addBox(-6.25f, -6.25f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, true);
            this.RightArm.texOffs(78, 21).addBox(-6.25f, -6.25f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, true);
            this.RightArm.texOffs(0, 37).addBox(-5.0f, 5.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, true);
            (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(24, 37).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
            this.LeftArm.texOffs(0, 37).addBox(-0.25f, 5.0f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, false);
            this.LeftArm.texOffs(0, 21).addBox(-2.0f, -4.25f, -3.5f, 7.0f, 5.0f, 7.0f, 0.0f, false);
            this.LeftArm.texOffs(78, 21).addBox(1.25f, -6.25f, -3.5f, 5.0f, 2.0f, 7.0f, 0.0f, false);
            (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(50, 57).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, false);
            (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(50, 57).addBox(-2.0f, 9.0f, -2.0f, 4.0f, 3.0f, 4.0f, 1.0f, true);
            (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            this.RightLeg.texOffs(0, 0).addBox(-3.0f, 3.0f, -3.0f, 3.0f, 4.0f, 6.0f, 0.0f, false);
            (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(0, 0).addBox(-0.05f, 3.0f, -3.0f, 3.0f, 4.0f, 6.0f, 0.0f, true);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
        }
    }
}
