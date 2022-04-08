package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class HellcowArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public HellcowArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 64;
        this.texHeight = isLayer2() ? 64 : 64;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(44, 0).addBox(-2.0F, -1.0F, -6.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(0.0F, 2.5F, -6.0F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.6109F, 0.0F, 0.0F);
        cube_r1.texOffs(16, 36).addBox(-2.0F, -1.75F, -0.75F, 4.0F, 3.0F, 0.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, -8.0F, 0.0F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.0472F, 0.0F, 0.0F);
        cube_r2.texOffs(12, 32).addBox(5.0F, 0.0F, -3.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r2.texOffs(0, 0).addBox(9.0F, -4.0F, -3.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        cube_r2.texOffs(24, 0).addBox(-11.0F, -4.0F, -3.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        cube_r2.texOffs(16, 41).addBox(-9.0F, 0.0F, -3.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(40, 25).addBox(-3.0F, 6.0F, -4.0F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 16).addBox(-5.0F, 1.0F, 3.0F, 10.0F, 7.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(40, 32).addBox(-3.0F, 8.0F, 3.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(36, 37).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(32, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(24, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

        ModelRenderer cube_r1_l2 = new ModelRenderer((Model) this);
        cube_r1_l2.setPos(0.0F, 16.8221F, 6.7705F);
        this.Belt.addChild(cube_r1_l2);
        setRotationAngle(cube_r1_l2, -0.3491F, 0.0F, 0.0F);
        cube_r1_l2.texOffs(12, 16).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r2_l2 = new ModelRenderer((Model) this);
        cube_r2_l2.setPos(-0.25F, 11.5F, 4.0F);
        this.Belt.addChild(cube_r2_l2);
        setRotationAngle(cube_r2_l2, -1.0908F, 0.0F, 0.0F);
        cube_r2_l2.texOffs(24, 0).addBox(-1.25F, -0.5F, 2.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        cube_r2_l2.texOffs(24, 6).addBox(-0.25F, 0.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\HellcowArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */