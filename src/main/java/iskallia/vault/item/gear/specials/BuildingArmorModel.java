package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class BuildingArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public BuildingArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 64;
        this.texHeight = isLayer2() ? 64 : 64;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 13).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
        this.Head.texOffs(21, 18).addBox(-2.0F, -10.0F, -5.5F, 4.0F, 4.0F, 11.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 29).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(0, 45).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(40, 33).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(40, 13).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(24, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(27, 27).addBox(-6.0F, 12.0F, -3.0F, 4.0F, 5.0F, 5.0F, 0.0F, false);
        this.Belt.texOffs(24, 0).addBox(2.0F, 12.0F, -3.0F, 4.0F, 5.0F, 5.0F, 0.0F, false);
        this.Belt.texOffs(28, 15).addBox(-2.0F, 11.0F, -3.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1_l2 = new ModelRenderer((Model) this);
        cube_r1_l2.setPos(5.0F, 11.6F, 0.25F);
        this.Belt.addChild(cube_r1_l2);
        setRotationAngle(cube_r1_l2, -0.2618F, 0.0F, 0.0F);
        cube_r1_l2.texOffs(24, 0).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2_l2 = new ModelRenderer((Model) this);
        cube_r2_l2.setPos(5.0F, 11.6F, -1.0F);
        this.Belt.addChild(cube_r2_l2);
        setRotationAngle(cube_r2_l2, 0.3054F, 0.0F, 0.0F);
        cube_r2_l2.texOffs(0, 32).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3_l2 = new ModelRenderer((Model) this);
        cube_r3_l2.setPos(-5.0F, 10.25F, 0.5F);
        this.Belt.addChild(cube_r3_l2);
        setRotationAngle(cube_r3_l2, 0.5672F, 0.0F, 0.0F);
        cube_r3_l2.texOffs(24, 10).addBox(-1.0F, -3.25F, -4.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        cube_r3_l2.texOffs(4, 32).addBox(-0.5F, -1.25F, -3.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\BuildingArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */