package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ImmersiveEngineeringArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public ImmersiveEngineeringArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 0).addBox(-8.0f, -15.0f, -8.0f, 16.0f, 17.0f, 16.0f, 0.0f, false);
        this.Head.texOffs(0, 0).addBox(-11.0f, -10.0f, -2.0f, 3.0f, 10.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(0, 0).addBox(8.0f, -10.0f, -2.0f, 3.0f, 10.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(0, 33).addBox(-2.0f, -9.0f, -10.0f, 4.0f, 6.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(18, 71).addBox(-7.0f, -7.0f, -9.0f, 4.0f, 4.0f, 3.0f, 0.0f, false);
        this.Head.texOffs(18, 71).addBox(3.0f, -7.0f, -9.0f, 4.0f, 4.0f, 3.0f, 0.0f, false);
        this.Head.texOffs(24, 56).addBox(-7.0f, -13.0f, -10.0f, 14.0f, 4.0f, 4.0f, 0.0f, false);
        this.Head.texOffs(64, 48).addBox(-7.0f, -2.0f, -9.0f, 14.0f, 4.0f, 3.0f, 0.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(32, 66).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(0, 33).addBox(-8.0f, 2.0f, -8.0f, 16.0f, 7.0f, 16.0f, 0.0f, false);
        this.Body.texOffs(48, 0).addBox(-6.0f, 9.0f, -5.0f, 12.0f, 3.0f, 10.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(64, 13).addBox(-7.0f, 1.0f, -4.0f, 6.0f, 6.0f, 8.0f, 0.0f, false);
        this.RightArm.texOffs(6, 43).addBox(-1.0f, 13.0f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
        this.RightArm.texOffs(48, 6).addBox(-2.0f, 16.0f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false);
        this.RightArm.texOffs(10, 0).addBox(-6.0f, 16.0f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false);
        this.RightArm.texOffs(0, 43).addBox(-7.0f, 13.0f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
        this.RightArm.texOffs(0, 71).addBox(-6.0f, 9.0f, -4.0f, 5.0f, 5.0f, 8.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(52, 56).addBox(1.0f, 1.0f, -4.0f, 6.0f, 6.0f, 8.0f, 0.0f, false);
        this.LeftArm.texOffs(56, 70).addBox(1.0f, 9.0f, -4.0f, 5.0f, 5.0f, 8.0f, 0.0f, false);
        this.LeftArm.texOffs(48, 33).addBox(6.0f, 13.0f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
        this.LeftArm.texOffs(48, 0).addBox(0.0f, 13.0f, -1.0f, 1.0f, 4.0f, 2.0f, 0.0f, false);
        this.LeftArm.texOffs(52, 4).addBox(1.0f, 16.0f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false);
        this.LeftArm.texOffs(48, 13).addBox(5.0f, 16.0f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 56).addBox(-4.0f, 7.0f, -4.0f, 8.0f, 7.0f, 8.0f, 0.0f, false);
        this.RightBoot.texOffs(72, 32).addBox(-4.0f, 11.0f, -6.0f, 8.0f, 3.0f, 2.0f, 0.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(64, 27).addBox(-3.8f, 11.0f, -6.0f, 8.0f, 3.0f, 2.0f, 0.0f, false);
        this.LeftBoot.texOffs(48, 33).addBox(-3.8f, 7.0f, -4.0f, 8.0f, 7.0f, 8.0f, 0.0f, false);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
