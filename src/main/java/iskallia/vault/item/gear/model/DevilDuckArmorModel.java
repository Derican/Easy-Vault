// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class DevilDuckArmorModel
{
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T>
    {
        public Variant1(final float modelSize, final EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = (this.isLayer2() ? 64 : 128);
            this.texHeight = (this.isLayer2() ? 64 : 128);
            (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
            final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
            cube_r1.setPos(0.0f, -10.0f, -0.75f);
            this.Head.addChild(cube_r1);
            this.setRotationAngle(cube_r1, 0.7418f, 0.0f, 0.0f);
            cube_r1.texOffs(0, 0).addBox(5.0f, -5.0f, -2.5f, 2.0f, 5.0f, 2.0f, 0.0f, false);
            cube_r1.texOffs(36, 0).addBox(-7.0f, 5.0f, -2.5f, 2.0f, 1.0f, 3.0f, 0.0f, false);
            cube_r1.texOffs(56, 0).addBox(5.0f, 5.0f, -2.5f, 2.0f, 1.0f, 3.0f, 0.0f, false);
            cube_r1.texOffs(52, 42).addBox(5.0f, 0.0f, -2.5f, 3.0f, 5.0f, 3.0f, 0.0f, false);
            cube_r1.texOffs(0, 16).addBox(-7.0f, -5.0f, -2.5f, 2.0f, 5.0f, 2.0f, 0.0f, false);
            cube_r1.texOffs(52, 50).addBox(-8.0f, 0.0f, -2.5f, 3.0f, 5.0f, 3.0f, 0.0f, false);
            final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
            cube_r2.setPos(0.0f, -2.5f, -6.8333f);
            this.Head.addChild(cube_r2);
            this.setRotationAngle(cube_r2, 0.3491f, 0.0f, 0.0f);
            cube_r2.texOffs(24, 5).addBox(-2.0f, -1.5f, 0.8333f, 4.0f, 1.0f, 2.0f, 0.0f, false);
            cube_r2.texOffs(24, 0).addBox(-2.0f, 0.5f, -1.1667f, 4.0f, 1.0f, 4.0f, 0.0f, false);
            cube_r2.texOffs(44, 27).addBox(-3.0f, -0.5f, -2.1667f, 6.0f, 1.0f, 5.0f, 0.0f, false);
            (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Body.texOffs(0, 28).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
            this.Body.texOffs(40, 18).addBox(-5.0f, 1.0f, -5.0f, 10.0f, 4.0f, 2.0f, 0.0f, false);
            final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
            cube_r3.setPos(0.0f, 8.0f, -3.5f);
            this.Body.addChild(cube_r3);
            this.setRotationAngle(cube_r3, 0.2182f, 0.0f, 0.0f);
            cube_r3.texOffs(52, 33).addBox(-3.0f, -4.0f, -0.5f, 6.0f, 7.0f, 2.0f, 0.0f, false);
            (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
            this.RightArm.texOffs(44, 0).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
            cube_r4.setPos(-3.0f, -2.0f, 0.0f);
            this.RightArm.addChild(cube_r4);
            this.setRotationAngle(cube_r4, 0.0f, 0.0f, 0.2618f);
            cube_r4.texOffs(24, 8).addBox(-2.5f, 1.5f, -4.0f, 6.0f, 2.0f, 8.0f, 0.0f, false);
            cube_r4.texOffs(20, 20).addBox(-2.5f, -2.5f, -4.0f, 6.0f, 4.0f, 8.0f, 0.0f, false);
            (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
            this.LeftArm.texOffs(0, 44).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r5 = new ModelRenderer((Model)this);
            cube_r5.setPos(3.0f, -2.0f, 0.0f);
            this.LeftArm.addChild(cube_r5);
            this.setRotationAngle(cube_r5, 0.0f, 0.0f, -0.3927f);
            cube_r5.texOffs(0, 16).addBox(-4.0f, -2.5f, -4.0f, 6.0f, 4.0f, 8.0f, 0.0f, false);
            cube_r5.texOffs(24, 32).addBox(-4.0f, 1.5f, -4.0f, 6.0f, 2.0f, 8.0f, 0.0f, false);
            (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightBoot.texOffs(36, 42).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r6 = new ModelRenderer((Model)this);
            cube_r6.setPos(1.8f, 8.0f, -3.5f);
            this.RightBoot.addChild(cube_r6);
            this.setRotationAngle(cube_r6, 0.2194f, 0.2143f, -0.7617f);
            cube_r6.texOffs(16, 58).addBox(-3.1642f, -3.4142f, -0.5f, 4.0f, 4.0f, 1.0f, 0.0f, false);
            (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftBoot.texOffs(20, 42).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
            final ModelRenderer cube_r7 = new ModelRenderer((Model)this);
            cube_r7.setPos(-2.0f, 8.0f, -3.5f);
            this.LeftBoot.addChild(cube_r7);
            this.setRotationAngle(cube_r7, 0.2194f, 0.2143f, -0.7617f);
            cube_r7.texOffs(26, 58).addBox(-0.5858f, -0.5858f, -0.5f, 4.0f, 4.0f, 1.0f, 0.0f, false);
            (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
            this.Belt.texOffs(0, 0).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
            this.Belt.texOffs(0, 16).addBox(-2.0f, 9.0f, -3.5f, 4.0f, 3.0f, 7.0f, 0.0f, false);
            final ModelRenderer cube_r1_l2 = new ModelRenderer((Model)this);
            cube_r1_l2.setPos(3.0f, 11.0f, 0.0f);
            this.Belt.addChild(cube_r1_l2);
            this.setRotationAngle(cube_r1_l2, 0.0f, 0.0f, 0.9163f);
            cube_r1_l2.texOffs(30, 28).addBox(1.0f, -1.0f, -2.5f, 5.0f, 1.0f, 5.0f, 0.0f, false);
            final ModelRenderer cube_r2_l2 = new ModelRenderer((Model)this);
            cube_r2_l2.setPos(3.0f, 11.0f, 0.0f);
            this.Belt.addChild(cube_r2_l2);
            this.setRotationAngle(cube_r2_l2, 0.0f, 0.0f, 0.48f);
            cube_r2_l2.texOffs(0, 0).addBox(1.0f, -3.0f, 2.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            cube_r2_l2.texOffs(0, 16).addBox(1.0f, -3.0f, -3.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            cube_r2_l2.texOffs(24, 0).addBox(-2.0f, -1.0f, -3.0f, 4.0f, 2.0f, 6.0f, 0.0f, false);
            final ModelRenderer cube_r3_l2 = new ModelRenderer((Model)this);
            cube_r3_l2.setPos(-6.3172f, 13.2905f, 0.0f);
            this.Belt.addChild(cube_r3_l2);
            this.setRotationAngle(cube_r3_l2, 0.0f, 0.0f, -0.9163f);
            cube_r3_l2.texOffs(34, 8).addBox(-2.0f, 0.5f, -2.5f, 4.0f, 1.0f, 5.0f, 0.0f, false);
            final ModelRenderer cube_r4_l2 = new ModelRenderer((Model)this);
            cube_r4_l2.setPos(-3.0f, 11.0f, 0.0f);
            this.Belt.addChild(cube_r4_l2);
            this.setRotationAngle(cube_r4_l2, 0.0f, 0.0f, -0.48f);
            cube_r4_l2.texOffs(15, 16).addBox(-2.0f, -3.0f, -3.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            cube_r4_l2.texOffs(3, 18).addBox(-2.0f, -3.0f, 2.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
            cube_r4_l2.texOffs(16, 28).addBox(-2.0f, -1.0f, -3.0f, 4.0f, 2.0f, 6.0f, 0.0f, false);
            (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
            this.RightLeg.texOffs(0, 26).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
            (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
            this.LeftLeg.texOffs(22, 12).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        }
    }
}
