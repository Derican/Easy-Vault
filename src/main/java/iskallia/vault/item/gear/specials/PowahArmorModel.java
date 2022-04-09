// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PowahArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public PowahArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
        cube_r1.setPos(4.0f, 0.5f, -7.5f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.2856f, 0.5973f, 0.4812f);
        cube_r1.texOffs(56, 40).addBox(-0.25f, -0.25f, -2.75f, 2.0f, 3.0f, 3.0f, 0.0f, false);
        cube_r1.texOffs(32, 21).addBox(-2.25f, 0.75f, -1.75f, 2.0f, 1.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
        cube_r2.setPos(-4.0f, 0.5f, -7.5f);
        this.Head.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.4047f, -0.5437f, -0.6912f);
        cube_r2.texOffs(56, 46).addBox(-1.6f, -0.25f, -2.8f, 2.0f, 3.0f, 3.0f, 0.0f, false);
        cube_r2.texOffs(35, 0).addBox(0.4f, 0.75f, -1.8f, 2.0f, 1.0f, 1.0f, 0.0f, false);
        final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
        cube_r3.setPos(0.0f, -1.0134f, -4.8941f);
        this.Head.addChild(cube_r3);
        this.setRotationAngle(cube_r3, 0.2618f, 0.0f, 0.0f);
        cube_r3.texOffs(52, 33).addBox(-2.0f, -2.0f, -3.5f, 4.0f, 4.0f, 3.0f, 0.0f, false);
        final ModelRenderer cube_r4 = new ModelRenderer((Model)this);
        cube_r4.setPos(0.0f, -6.0f, -3.5f);
        this.Head.addChild(cube_r4);
        this.setRotationAngle(cube_r4, -0.5672f, 0.0f, 0.0f);
        cube_r4.texOffs(0, 16).addBox(-5.5f, -4.0f, -3.0f, 11.0f, 10.0f, 5.0f, 0.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 31).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(46, 21).addBox(1.0f, 1.0f, 3.0f, 3.0f, 3.0f, 5.0f, 0.0f, false);
        this.Body.texOffs(48, 52).addBox(1.5f, 5.0f, 3.0f, 2.0f, 2.0f, 4.0f, 0.0f, false);
        this.Body.texOffs(36, 52).addBox(-3.5f, 5.0f, 3.0f, 2.0f, 2.0f, 4.0f, 0.0f, false);
        this.Body.texOffs(0, 4).addBox(-3.0f, 8.25f, 3.0f, 1.0f, 1.0f, 3.0f, 0.0f, false);
        this.Body.texOffs(0, 0).addBox(2.0f, 8.25f, 3.0f, 1.0f, 1.0f, 3.0f, 0.0f, false);
        this.Body.texOffs(24, 0).addBox(-4.0f, 1.0f, 3.0f, 3.0f, 3.0f, 5.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(49, 0).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.RightArm.texOffs(27, 8).addBox(-5.0f, -5.0f, -4.0f, 7.0f, 5.0f, 8.0f, 0.0f, false);
        this.RightArm.texOffs(26, 52).addBox(-5.0f, 0.0f, -2.0f, 1.0f, 11.0f, 4.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(16, 48).addBox(4.0f, 0.0f, -2.0f, 1.0f, 11.0f, 4.0f, 0.0f, false);
        this.LeftArm.texOffs(24, 23).addBox(-2.0f, -5.0f, -4.0f, 7.0f, 5.0f, 8.0f, 0.0f, false);
        this.LeftArm.texOffs(0, 47).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(40, 36).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(24, 36).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
