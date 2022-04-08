package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PowahArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public PowahArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(4.0F, 0.5F, -7.5F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.2856F, 0.5973F, 0.4812F);
        cube_r1.texOffs(56, 40).addBox(-0.25F, -0.25F, -2.75F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r1.texOffs(32, 21).addBox(-2.25F, 0.75F, -1.75F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-4.0F, 0.5F, -7.5F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.4047F, -0.5437F, -0.6912F);
        cube_r2.texOffs(56, 46).addBox(-1.6F, -0.25F, -2.8F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r2.texOffs(35, 0).addBox(0.4F, 0.75F, -1.8F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(0.0F, -1.0134F, -4.8941F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.2618F, 0.0F, 0.0F);
        cube_r3.texOffs(52, 33).addBox(-2.0F, -2.0F, -3.5F, 4.0F, 4.0F, 3.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(0.0F, -6.0F, -3.5F);
        this.Head.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.5672F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 16).addBox(-5.5F, -4.0F, -3.0F, 11.0F, 10.0F, 5.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 31).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(46, 21).addBox(1.0F, 1.0F, 3.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        this.Body.texOffs(48, 52).addBox(1.5F, 5.0F, 3.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.Body.texOffs(36, 52).addBox(-3.5F, 5.0F, 3.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        this.Body.texOffs(0, 4).addBox(-3.0F, 8.25F, 3.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.Body.texOffs(0, 0).addBox(2.0F, 8.25F, 3.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.Body.texOffs(24, 0).addBox(-4.0F, 1.0F, 3.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(49, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(27, 8).addBox(-5.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(26, 52).addBox(-5.0F, 0.0F, -2.0F, 1.0F, 11.0F, 4.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(16, 48).addBox(4.0F, 0.0F, -2.0F, 1.0F, 11.0F, 4.0F, 0.0F, false);
        this.LeftArm.texOffs(24, 23).addBox(-2.0F, -5.0F, -4.0F, 7.0F, 5.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 47).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(40, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(24, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\PowahArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */