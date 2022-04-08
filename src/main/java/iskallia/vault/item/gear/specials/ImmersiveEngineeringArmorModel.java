package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ImmersiveEngineeringArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public ImmersiveEngineeringArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 17.0F, 16.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(-11.0F, -10.0F, -2.0F, 3.0F, 10.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(8.0F, -10.0F, -2.0F, 3.0F, 10.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(0, 33).addBox(-2.0F, -9.0F, -10.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(18, 71).addBox(-7.0F, -7.0F, -9.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.Head.texOffs(18, 71).addBox(3.0F, -7.0F, -9.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.Head.texOffs(24, 56).addBox(-7.0F, -13.0F, -10.0F, 14.0F, 4.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(64, 48).addBox(-7.0F, -2.0F, -9.0F, 14.0F, 4.0F, 3.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(32, 66).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(0, 33).addBox(-8.0F, 2.0F, -8.0F, 16.0F, 7.0F, 16.0F, 0.0F, false);
        this.Body.texOffs(48, 0).addBox(-6.0F, 9.0F, -5.0F, 12.0F, 3.0F, 10.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(64, 13).addBox(-7.0F, 1.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(6, 43).addBox(-1.0F, 13.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(48, 6).addBox(-2.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(10, 0).addBox(-6.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(0, 43).addBox(-7.0F, 13.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(0, 71).addBox(-6.0F, 9.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(52, 56).addBox(1.0F, 1.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(56, 70).addBox(1.0F, 9.0F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(48, 33).addBox(6.0F, 13.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(48, 0).addBox(0.0F, 13.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(52, 4).addBox(1.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(48, 13).addBox(5.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 56).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
        this.RightBoot.texOffs(72, 32).addBox(-4.0F, 11.0F, -6.0F, 8.0F, 3.0F, 2.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(64, 27).addBox(-3.8F, 11.0F, -6.0F, 8.0F, 3.0F, 2.0F, 0.0F, false);
        this.LeftBoot.texOffs(48, 33).addBox(-3.8F, 7.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\ImmersiveEngineeringArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */