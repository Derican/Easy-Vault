package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class TrashArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public TrashArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 32 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 15).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(36, 49).addBox(-4.0F, -14.0F, -6.0F, 8.0F, 15.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(18, 48).addBox(-4.0F, -14.0F, 5.0F, 8.0F, 15.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 15).addBox(-5.0F, -14.0F, 4.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(8, 5).addBox(4.0F, -14.0F, 4.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(4, 5).addBox(4.0F, -14.0F, -5.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 5).addBox(-5.0F, -14.0F, -5.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(-4.0F, -15.0F, -7.0F, 8.0F, 1.0F, 14.0F, 0.0F, false);
        this.Head.texOffs(24, 17).addBox(-4.0F, -14.0F, -7.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(24, 15).addBox(-4.0F, -14.0F, 6.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(36, 36).addBox(-5.0F, -15.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        this.Head.texOffs(30, 0).addBox(4.0F, -15.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(-0.5F, -17.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(4, 15).addBox(-0.5F, -16.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(8, 11).addBox(-0.5F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(50, 29).addBox(-6.0F, -15.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        this.Head.texOffs(4, 11).addBox(-6.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(10, 3).addBox(-5.0F, -14.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(6, 2).addBox(4.0F, -14.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(4.0F, -14.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(9, 1).addBox(-6.0F, -14.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(6, 0).addBox(5.0F, -14.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 2).addBox(5.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 11).addBox(-5.0F, -14.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(44, 0).addBox(5.0F, -15.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        this.Head.texOffs(62, 40).addBox(-7.0F, -15.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        this.Head.texOffs(62, 30).addBox(6.0F, -15.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        this.Head.texOffs(62, 21).addBox(6.0F, -14.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        this.Head.texOffs(56, 0).addBox(-7.0F, -14.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 1.5708F, 0.0F);
        cube_r1.texOffs(0, 48).addBox(-4.0F, -14.0F, -6.0F, 8.0F, 15.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(48, 13).addBox(-4.0F, -14.0F, 5.0F, 8.0F, 15.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(24, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(32, 65).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(0, 31).addBox(-5.0F, -5.0F, -4.0F, 4.0F, 9.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(16, 64).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightBoot.texOffs(24, 23).addBox(-4.0F, 5.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(24, 23).addBox(-3.8F, 5.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftBoot.texOffs(54, 49).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(12, 16).addBox(-3.0F, 10.0F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(19, 19).addBox(-1.0F, 13.0F, 2.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(0, 0).addBox(-2.5F, 13.0F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\TrashArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */