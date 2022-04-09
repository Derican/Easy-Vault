// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class TestDummyArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public TestDummyArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 32).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(0, 0).addBox(-7.0f, -7.0f, -7.0f, 14.0f, 1.0f, 14.0f, 0.0f, false);
        this.Head.texOffs(0, 15).addBox(-5.0f, -16.0f, -5.0f, 10.0f, 7.0f, 10.0f, 0.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
        cube_r1.setPos(2.5f, 0.661f, -8.6148f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.5661f, -0.5338f, -0.3127f);
        cube_r1.texOffs(30, 19).addBox(1.0f, -2.0f, -4.0f, 2.0f, 3.0f, 2.0f, 0.0f, false);
        cube_r1.texOffs(50, 5).addBox(1.5f, 0.0f, -2.0f, 1.0f, 1.0f, 6.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
        cube_r2.setPos(0.0f, -16.5f, 0.0f);
        this.Head.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.5672f, 0.0f, -0.3927f);
        cube_r2.texOffs(30, 22).addBox(1.0f, 3.5f, 0.0f, 10.0f, 1.0f, 10.0f, 0.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(32, 33).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(30, 15).addBox(-11.0f, 0.0f, 3.0f, 22.0f, 2.0f, 2.0f, 0.0f, false);
        this.Body.texOffs(0, 48).addBox(-1.0f, 2.0f, 3.0f, 2.0f, 23.0f, 2.0f, 0.0f, false);
        this.Body.texOffs(0, 32).addBox(-1.0f, 2.0f, -5.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
        cube_r3.setPos(2.0f, 3.0f, -3.5f);
        this.Body.addChild(cube_r3);
        this.setRotationAngle(cube_r3, 0.0f, 0.0f, 0.7854f);
        cube_r3.texOffs(0, 15).addBox(-2.0f, -2.0f, -0.5f, 4.0f, 4.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
        cube_r4.setPos(-2.0f, 3.0f, -3.5f);
        this.Body.addChild(cube_r4);
        this.setRotationAngle(cube_r4, 0.0f, 0.0f, -0.7854f);
        cube_r4.texOffs(0, 20).addBox(-2.0f, -2.0f, -0.5f, 4.0f, 4.0f, 1.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(56, 33).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r5 = new ModelRenderer((Model)this);
        cube_r5.setPos(-4.5f, 8.5f, 0.0f);
        this.RightArm.addChild(cube_r5);
        this.setRotationAngle(cube_r5, 0.0f, 0.0f, 0.3927f);
        cube_r5.texOffs(42, 0).addBox(-0.5f, -2.5f, -3.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
        final ModelRenderer cube_r6 = new ModelRenderer((Model)this);
        cube_r6.setPos(-1.0f, 8.5f, -3.5f);
        this.RightArm.addChild(cube_r6);
        this.setRotationAngle(cube_r6, -0.3927f, 0.0f, 0.0f);
        cube_r6.texOffs(56, 55).addBox(-3.0f, -2.5f, -0.5f, 6.0f, 5.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r7 = new ModelRenderer((Model)this);
        cube_r7.setPos(-1.0f, 8.5f, 3.5f);
        this.RightArm.addChild(cube_r7);
        this.setRotationAngle(cube_r7, 0.3927f, 0.0f, 0.0f);
        cube_r7.texOffs(58, 0).addBox(-3.0f, -2.5f, -0.5f, 6.0f, 5.0f, 1.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(40, 49).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r8 = new ModelRenderer((Model)this);
        cube_r8.setPos(1.0f, 8.5f, -3.5f);
        this.LeftArm.addChild(cube_r8);
        this.setRotationAngle(cube_r8, -0.3927f, 0.0f, 0.0f);
        cube_r8.texOffs(56, 49).addBox(-3.0f, -2.5f, -0.5f, 6.0f, 5.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r9 = new ModelRenderer((Model)this);
        cube_r9.setPos(1.0f, 8.5f, 3.5f);
        this.LeftArm.addChild(cube_r9);
        this.setRotationAngle(cube_r9, 0.3927f, 0.0f, 0.0f);
        cube_r9.texOffs(60, 19).addBox(-3.0f, -2.5f, -0.5f, 6.0f, 5.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r10 = new ModelRenderer((Model)this);
        cube_r10.setPos(4.5f, 8.5f, 0.0f);
        this.LeftArm.addChild(cube_r10);
        this.setRotationAngle(cube_r10, 0.0f, 0.0f, -0.3927f);
        cube_r10.texOffs(0, 0).addBox(-0.5f, -2.5f, -3.0f, 1.0f, 5.0f, 6.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(24, 49).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(8, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
