package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ThermalArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public ThermalArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(64, 49).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(76, 19).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(88, 47).addBox(-5.0F, -9.0F, -6.0F, 10.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(18, 69).addBox(-5.0F, -9.0F, 5.0F, 10.0F, 5.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(16, 92).addBox(1.5F, -7.0F, 7.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        this.Body.texOffs(88, 52).addBox(-5.5F, -7.0F, 7.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        this.Body.texOffs(24, 76).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(32, 12).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 9.0F, 12.0F, 0.0F, false);
        this.Body.texOffs(64, 19).addBox(-4.0F, 5.0F, -4.0F, 8.0F, 3.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(32, 92).addBox(-3.0F, 5.0F, 3.0F, 6.0F, 3.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(70, 27).addBox(-6.5F, -6.0F, 6.0F, 6.0F, 14.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(0, 69).addBox(0.5F, -6.0F, 6.0F, 6.0F, 14.0F, 6.0F, 0.0F, false);
        this.Body.texOffs(6, 11).addBox(2.5F, 7.75F, 8.25F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(30, 33).addBox(3.0F, 8.75F, 8.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(68, 78).addBox(3.0F, 9.75F, 2.75F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        this.Body.texOffs(60, 40).addBox(-4.0F, 9.75F, 2.75F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        this.Body.texOffs(30, 12).addBox(-4.0F, 8.75F, 8.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 10).addBox(-4.5F, 7.75F, 8.25F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(0.0F, 8.5F, 3.5F);
        this.Body.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.texOffs(34, 0).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, 7.6455F, -3.1695F);
        this.Body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3491F, 0.0F, 0.0F);
        cube_r2.texOffs(0, 0).addBox(-2.0F, -3.5F, -1.5F, 4.0F, 7.0F, 3.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(0, 89).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(0, 24).addBox(-8.0F, -8.0F, -7.0F, 8.0F, 10.0F, 14.0F, 0.0F, false);
        this.RightArm.texOffs(0, 48).addBox(-8.0F, 4.0F, -6.0F, 8.0F, 9.0F, 12.0F, 0.0F, false);
        this.RightArm.texOffs(76, 78).addBox(-6.0F, -9.0F, -4.0F, 6.0F, 1.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(88, 0).addBox(-5.0F, -10.0F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);
        this.RightArm.texOffs(62, 66).addBox(-7.0F, 2.0F, -5.0F, 6.0F, 2.0F, 10.0F, 0.0F, false);
        this.RightArm.texOffs(30, 35).addBox(-9.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(30, 0).addBox(-10.0F, -1.0F, 2.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(33, 34).addBox(-9.0F, 7.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(34, 8).addBox(-9.0F, 7.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(8, 24).addBox(-10.0F, -1.0F, -3.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(34, 12).addBox(-9.0F, -1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(30, 0).addBox(1.0F, 2.0F, -5.0F, 6.0F, 2.0F, 10.0F, 0.0F, false);
        this.LeftArm.texOffs(32, 36).addBox(0.0F, 4.0F, -6.0F, 8.0F, 9.0F, 12.0F, 0.0F, false);
        this.LeftArm.texOffs(48, 78).addBox(0.0F, -9.0F, -4.0F, 6.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 0).addBox(0.0F, -8.0F, -7.0F, 8.0F, 10.0F, 14.0F, 0.0F, false);
        this.LeftArm.texOffs(80, 87).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.LeftArm.texOffs(84, 65).addBox(0.0F, -10.0F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);
        this.LeftArm.texOffs(8, 34).addBox(8.0F, -1.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(34, 6).addBox(8.0F, 7.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(4, 24).addBox(9.0F, -1.0F, -3.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 34).addBox(8.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 24).addBox(9.0F, -1.0F, 2.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(4, 34).addBox(8.0F, 7.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(64, 87).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightBoot.texOffs(64, 0).addBox(-4.0F, 2.0F, -4.0F, 8.0F, 11.0F, 8.0F, 0.0F, false);
        this.RightBoot.texOffs(52, 0).addBox(-3.95F, 8.0F, -6.0F, 8.0F, 5.0F, 2.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(48, 87).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.LeftBoot.texOffs(40, 57).addBox(-3.8F, 2.0F, -4.0F, 8.0F, 11.0F, 8.0F, 0.0F, false);
        this.LeftBoot.texOffs(88, 26).addBox(-3.75F, 8.0F, -6.0F, 8.0F, 5.0F, 2.0F, 0.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\ThermalArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */