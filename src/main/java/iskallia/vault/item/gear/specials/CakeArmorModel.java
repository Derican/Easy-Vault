package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CakeArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public CakeArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 17).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(0, 0).addBox(-6.0f, -10.0f, -6.0f, 12.0f, 5.0f, 12.0f, 0.0f, false);
        this.Head.texOffs(0, 17).addBox(-1.0f, -15.0f, -1.0f, 2.0f, 5.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(6, 0).addBox(-0.5f, -16.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, false);
        this.Head.texOffs(66, 0).addBox(-1.5f, -21.0f, 0.0f, 3.0f, 5.0f, 0.0f, 0.0f, false);
        this.Head.texOffs(0, 6).addBox(-6.0f, -5.0f, 1.0f, 1.0f, 2.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(30, 17).addBox(-6.0f, -5.0f, -3.0f, 1.0f, 3.0f, 3.0f, 0.0f, false);
        this.Head.texOffs(6, 6).addBox(-6.0f, -5.0f, -6.0f, 1.0f, 2.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(6, 0).addBox(5.0f, -5.0f, -6.0f, 1.0f, 2.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(0, 0).addBox(5.0f, -5.0f, 1.0f, 1.0f, 2.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(50, 35).addBox(5.0f, -5.0f, -3.0f, 1.0f, 3.0f, 2.0f, 0.0f, false);
        this.Head.texOffs(42, 0).addBox(-4.0f, -5.0f, -6.0f, 3.0f, 4.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(26, 37).addBox(-1.0f, -5.0f, -6.0f, 2.0f, 2.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(36, 7).addBox(-4.0f, -5.0f, 5.0f, 3.0f, 4.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(37, 22).addBox(1.0f, -5.0f, 5.0f, 2.0f, 2.0f, 1.0f, 0.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(28, 37).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(48, 49).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.RightArm.texOffs(0, 33).addBox(-5.0f, -4.0f, -4.0f, 6.0f, 4.0f, 8.0f, 0.0f, false);
        this.RightArm.texOffs(12, 45).addBox(-5.0f, 0.0f, 3.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(24, 45).addBox(-5.0f, 0.0f, 2.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(52, 29).addBox(-5.0f, 0.0f, -1.0f, 1.0f, 4.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(36, 0).addBox(-5.0f, 0.0f, 0.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
        this.RightArm.texOffs(50, 5).addBox(-4.0f, 0.0f, -4.0f, 2.0f, 4.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(0, 45).addBox(-1.0f, 0.0f, -4.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(44, 29).addBox(-1.0f, 0.0f, 3.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(50, 0).addBox(-4.0f, 0.0f, 3.0f, 2.0f, 4.0f, 1.0f, 0.0f, false);
        this.RightArm.texOffs(20, 35).addBox(-5.0f, 0.0f, -4.0f, 1.0f, 3.0f, 2.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(16, 49).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.LeftArm.texOffs(0, 6).addBox(4.0f, 0.0f, -4.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(20, 45).addBox(4.0f, 0.0f, 2.0f, 1.0f, 2.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(24, 17).addBox(4.0f, 0.0f, -3.0f, 1.0f, 6.0f, 2.0f, 0.0f, false);
        this.LeftArm.texOffs(0, 33).addBox(4.0f, 0.0f, 0.0f, 1.0f, 5.0f, 2.0f, 0.0f, false);
        this.LeftArm.texOffs(16, 45).addBox(4.0f, 0.0f, -1.0f, 1.0f, 4.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(44, 5).addBox(1.0f, 0.0f, -4.0f, 2.0f, 4.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(38, 17).addBox(1.0f, 0.0f, 3.0f, 2.0f, 4.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(0, 0).addBox(4.0f, 0.0f, 3.0f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.LeftArm.texOffs(24, 25).addBox(-1.0f, -4.0f, -4.0f, 6.0f, 4.0f, 8.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 45).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(44, 13).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
