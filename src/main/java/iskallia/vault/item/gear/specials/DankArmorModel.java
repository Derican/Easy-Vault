package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class DankArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public DankArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 32 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(66, 62).addBox(-5.0F, -9.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(16, 66).addBox(3.0F, -9.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(8, 66).addBox(3.0F, -9.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(48, 64).addBox(-5.0F, -9.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(10, 46).addBox(-5.0F, -9.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(40, 44).addBox(3.0F, -9.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(30, 42).addBox(3.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(42, 26).addBox(-5.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(64, 58).addBox(-3.0F, -1.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(64, 46).addBox(-3.0F, -9.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(64, 42).addBox(-3.0F, -9.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(32, 64).addBox(-3.0F, -1.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(60, 36).addBox(-3.0F, 10.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(58, 30).addBox(-3.0F, 2.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(56, 12).addBox(-3.0F, 2.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(56, 4).addBox(-3.0F, 10.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(42, 18).addBox(-5.0F, 10.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(20, 40).addBox(3.0F, 10.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(40, 8).addBox(-5.0F, 2.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(40, 0).addBox(3.0F, 2.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(24, 62).addBox(-5.0F, 2.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(58, 60).addBox(3.0F, 2.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(0, 60).addBox(3.0F, 2.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(58, 48).addBox(-5.0F, 2.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(50, 40).addBox(-8.0F, 3.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(50, 8).addBox(-8.0F, -5.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(50, 0).addBox(-8.0F, -5.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(46, 34).addBox(-8.0F, 3.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(32, 24).addBox(-10.0F, 3.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.RightArm.texOffs(32, 16).addBox(-2.0F, 3.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.RightArm.texOffs(16, 30).addBox(-10.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.RightArm.texOffs(34, 50).addBox(-10.0F, -5.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(22, 22).addBox(-2.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.RightArm.texOffs(26, 50).addBox(-2.0F, -5.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(0, 48).addBox(-2.0F, -5.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(16, 0).addBox(-10.0F, -5.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.RightArm.texOffs(24, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(52, 26).addBox(2.0F, 3.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(52, 20).addBox(2.0F, -5.0F, -5.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(52, 16).addBox(2.0F, -5.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(50, 44).addBox(2.0F, 3.0F, 3.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 40).addBox(0.0F, 3.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.LeftArm.texOffs(10, 38).addBox(8.0F, 3.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.LeftArm.texOffs(36, 34).addBox(0.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.LeftArm.texOffs(16, 54).addBox(0.0F, -5.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(26, 32).addBox(8.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.LeftArm.texOffs(8, 54).addBox(8.0F, -5.0F, 3.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(50, 52).addBox(8.0F, -5.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(42, 52).addBox(0.0F, -5.0F, -5.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(12, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(0, 16).addBox(-2.0F, 12.0F, 2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(0, 16).addBox(-2.0F, 10.0F, 2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(0, 0).addBox(1.0F, 11.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(0, 0).addBox(-2.0F, 11.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\DankArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */