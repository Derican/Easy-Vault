// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class OmarlatifArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public OmarlatifArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 32 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 15).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 31).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
        cube_r1.setPos(0.0f, 3.5f, -3.5f);
        this.Body.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.48f, 0.0f, 0.0f);
        cube_r1.texOffs(38, 3).addBox(-5.0f, -2.5f, -0.5f, 10.0f, 5.0f, 1.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(40, 41).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
        cube_r2.setPos(-3.0f, -1.0f, 0.0f);
        this.RightArm.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.0f, 0.0f, 0.9599f);
        cube_r2.texOffs(34, 15).addBox(-3.0f, -3.0f, -4.0f, 4.0f, 6.0f, 8.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(40, 41).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
        final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
        cube_r3.setPos(3.0f, -1.0f, 0.0f);
        this.LeftArm.addChild(cube_r3);
        this.setRotationAngle(cube_r3, 0.0f, 0.0f, -0.9599f);
        cube_r3.texOffs(34, 15).addBox(-1.0f, -3.0f, -4.0f, 4.0f, 6.0f, 8.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(24, 41).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(24, 41).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(0, 0).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(16, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
    }
}
