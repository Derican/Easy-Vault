package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class LeprechaunArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 32 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
            this.Head.texOffs(0, 26).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 22).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 12).addBox(-5.0F, -2.0F, -4.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);
            this.Body.texOffs(16, 41).addBox(-5.25F, -1.5F, -4.5F, 2.0F, 13.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(44, 38).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 41).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(40, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(28, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(12, 16).addBox(-2.0F, 9.0F, -3.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\LeprechaunArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */