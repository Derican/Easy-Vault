// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class IndustrialForegoingArmorModel<T extends LivingEntity> extends VaultGearModel<T>
{
    public IndustrialForegoingArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 24).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(0, 0).addBox(-6.0f, -10.0f, -6.0f, 12.0f, 12.0f, 12.0f, 0.0f, false);
        this.Head.texOffs(24, 24).addBox(-3.0f, -11.0f, -3.0f, 6.0f, 1.0f, 6.0f, 0.0f, false);
        this.Head.texOffs(0, 0).addBox(-1.0f, -18.0f, -1.0f, 2.0f, 2.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(0, 4).addBox(-0.5f, -16.0f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
        (this.Body = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(48, 18).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(16, 64).addBox(-3.0f, 2.0f, 3.0f, 6.0f, 10.0f, 3.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer((Model)this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(60, 58).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.RightArm.texOffs(48, 0).addBox(-6.0f, -1.0f, -4.0f, 4.0f, 10.0f, 8.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer((Model)this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(60, 34).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.LeftArm.texOffs(24, 46).addBox(2.0f, -1.0f, -4.0f, 4.0f, 10.0f, 8.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 40).addBox(-4.0f, 7.0f, -4.0f, 8.0f, 6.0f, 8.0f, 0.0f, false);
        (this.LeftBoot = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(24, 32).addBox(-3.8f, 7.0f, -4.0f, 8.0f, 6.0f, 8.0f, 0.0f, false);
        (this.Belt = new ModelRenderer((Model)this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer((Model)this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer((Model)this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
