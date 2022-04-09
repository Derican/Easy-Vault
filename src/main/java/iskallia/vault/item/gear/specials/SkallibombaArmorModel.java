// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class SkallibombaArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public SkallibombaArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 256);
        this.texHeight = (this.isLayer2() ? 64 : 256);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(111, 104).addBox(-5.0f, -27.0f, -6.0f, 10.0f, 24.0f, 11.0f, 0.0f, false);
        this.Head.texOffs(100, 28).addBox(-5.0f, -32.0f, -25.0f, 10.0f, 5.0f, 16.0f, 0.0f, false);
        this.Head.texOffs(106, 87).addBox(-6.0f, -34.0f, -34.0f, 12.0f, 7.0f, 9.0f, 0.0f, false);
        this.Head.texOffs(85, 17).addBox(-5.0f, -36.0f, -34.0f, 2.0f, 2.0f, 5.0f, 0.0f, false);
        this.Head.texOffs(98, 0).addBox(-5.0f, -41.0f, -1.0f, 2.0f, 7.0f, 5.0f, 0.0f, false);
        this.Head.texOffs(0, 16).addBox(3.0f, -36.0f, -34.0f, 2.0f, 2.0f, 5.0f, 0.0f, false);
        this.Head.texOffs(76, 10).addBox(3.0f, -41.0f, -1.0f, 2.0f, 7.0f, 5.0f, 0.0f, false);
        this.Head.texOffs(15, 18).addBox(3.0f, -27.0f, -33.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(9, 16).addBox(-5.0f, -27.0f, -33.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(74, 59).addBox(-8.0f, -34.0f, -9.0f, 16.0f, 12.0f, 16.0f, 0.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer((Model)this);
        cube_r1.setPos(-0.5f, -22.5f, -17.0f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.3491f, 0.0f, 0.0f);
        cube_r1.texOffs(34, 115).addBox(-3.0f, 0.5f, -8.0f, 7.0f, 3.0f, 16.0f, 0.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(0, 0).addBox(-13.0f, -10.0f, -14.0f, 26.0f, 35.0f, 24.0f, 0.0f, false);
        this.Body.texOffs(0, 59).addBox(-13.0f, -3.0f, -25.0f, 26.0f, 28.0f, 11.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer((Model)this);
        cube_r2.setPos(-5.5f, -10.0f, 15.0f);
        this.Body.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.3491f, -0.3054f, 0.0f);
        cube_r2.texOffs(0, 126).addBox(-0.5f, -5.0f, -7.0f, 1.0f, 10.0f, 14.0f, 0.0f, false);
        final ModelRenderer cube_r3 = new ModelRenderer((Model)this);
        cube_r3.setPos(5.5f, -10.0f, 16.0f);
        this.Body.addChild(cube_r3);
        this.setRotationAngle(cube_r3, 0.2618f, 0.3054f, 0.0f);
        cube_r3.texOffs(92, 130).addBox(-0.5f, -5.0f, -7.0f, 1.0f, 10.0f, 14.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(122, 49).addBox(-12.0f, -9.0f, -9.0f, 7.0f, 10.0f, 12.0f, 0.0f, false);
        this.RightArm.texOffs(63, 59).addBox(-12.0f, -3.0f, -15.0f, 5.0f, 4.0f, 6.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(68, 122).addBox(6.0f, -9.0f, -9.0f, 7.0f, 10.0f, 12.0f, 0.0f, false);
        this.LeftArm.texOffs(76, 0).addBox(8.0f, -3.0f, -15.0f, 5.0f, 4.0f, 6.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(100, 0).addBox(-22.1f, -3.0f, -9.0f, 13.0f, 16.0f, 12.0f, 0.0f, false);
        this.RightBoot.texOffs(138, 0).addBox(-22.1f, 10.0f, -15.0f, 13.0f, 3.0f, 6.0f, 0.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(136, 28).addBox(9.1f, 10.0f, -15.0f, 13.0f, 3.0f, 6.0f, 0.0f, false);
        this.LeftBoot.texOffs(0, 98).addBox(9.1f, -3.0f, -9.0f, 13.0f, 16.0f, 12.0f, 0.0f, false);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(0, 0).addBox(-9.0f, 14.0f, 4.0f, 18.0f, 8.0f, 12.0f, 0.0f, false);
        this.Belt.texOffs(0, 34).addBox(-1.0f, 11.0f, 6.0f, 2.0f, 3.0f, 8.0f, 0.0f, false);
        this.Belt.texOffs(31, 20).addBox(-1.0f, 13.0f, 17.0f, 2.0f, 2.0f, 5.0f, 0.0f, false);
        this.Belt.texOffs(12, 34).addBox(-1.0f, 17.0f, 23.0f, 2.0f, 1.0f, 5.0f, 0.0f, false);
        this.Belt.texOffs(0, 20).addBox(-6.0f, 15.0f, 16.0f, 12.0f, 7.0f, 7.0f, 0.0f, false);
        this.Belt.texOffs(31, 27).addBox(-4.0f, 18.0f, 23.0f, 8.0f, 4.0f, 7.0f, 0.0f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
    }
}
