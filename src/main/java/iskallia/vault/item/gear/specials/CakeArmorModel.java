package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CakeArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public CakeArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(0, 0).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 5.0F, 12.0F, 0.0F, false);
        this.Head.texOffs(0, 17).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(6, 0).addBox(-0.5F, -16.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        this.Head.texOffs(66, 0).addBox(-1.5F, -21.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, false);
        this.Head.texOffs(0, 6).addBox(-6.0F, -5.0F, 1.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(30, 17).addBox(-6.0F, -5.0F, -3.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.Head.texOffs(6, 6).addBox(-6.0F, -5.0F, -6.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(6, 0).addBox(5.0F, -5.0F, -6.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(5.0F, -5.0F, 1.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(50, 35).addBox(5.0F, -5.0F, -3.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(42, 0).addBox(-4.0F, -5.0F, -6.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(26, 37).addBox(-1.0F, -5.0F, -6.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(36, 7).addBox(-4.0F, -5.0F, 5.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(37, 22).addBox(1.0F, -5.0F, 5.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(28, 37).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(48, 49).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(0, 33).addBox(-5.0F, -4.0F, -4.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(12, 45).addBox(-5.0F, 0.0F, 3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(24, 45).addBox(-5.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(52, 29).addBox(-5.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(36, 0).addBox(-5.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(50, 5).addBox(-4.0F, 0.0F, -4.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(0, 45).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(44, 29).addBox(-1.0F, 0.0F, 3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(50, 0).addBox(-4.0F, 0.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(20, 35).addBox(-5.0F, 0.0F, -4.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(16, 49).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.LeftArm.texOffs(0, 6).addBox(4.0F, 0.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(20, 45).addBox(4.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(24, 17).addBox(4.0F, 0.0F, -3.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 33).addBox(4.0F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(16, 45).addBox(4.0F, 0.0F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(44, 5).addBox(1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(38, 17).addBox(1.0F, 0.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 0).addBox(4.0F, 0.0F, 3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(24, 25).addBox(-1.0F, -4.0F, -4.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(44, 13).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\CakeArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */