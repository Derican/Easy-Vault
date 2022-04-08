package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CreateArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public CreateArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 64 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(56, 39).addBox(-5.0F, -6.0F, -6.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(24, 19).addBox(-6.0F, -7.0F, -5.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(56, 34).addBox(1.0F, -6.0F, -6.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 16).addBox(1.5F, -5.5F, -7.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(56, 29).addBox(-2.0F, -10.0F, -5.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 20).addBox(-1.0F, -6.0F, -5.75F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(32, 29).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(14, 55).addBox(-3.0F, 4.0F, -4.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(58, 2).addBox(-2.0F, 8.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(58, 0).addBox(-2.0F, 3.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(16, 39).addBox(-0.5F, 5.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(4, 28).addBox(-0.5F, 4.0F, -4.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 0).addBox(-4.0F, 5.0F, 3.0F, 8.0F, 4.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 17).addBox(-3.0F, 9.0F, 3.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 5).addBox(-3.0F, 4.0F, 3.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(50, 13).addBox(-2.0F, 10.0F, 3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(37, 18).addBox(-2.0F, 3.0F, 3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(12, 39).addBox(-1.5F, 2.0F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 22).addBox(-1.5F, 11.0F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 39).addBox(0.5F, 2.0F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(28, 22).addBox(0.5F, 11.0F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 34).addBox(4.0F, 5.5F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(3, 33).addBox(4.0F, 7.25F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(28, 32).addBox(-5.0F, 7.25F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(24, 32).addBox(-5.0F, 5.5F, 2.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(28, 1).addBox(-0.5F, 6.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(3.0F, 2.5F, 5.25F);
        this.Body.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.7854F);
        cube_r1.texOffs(24, 34).addBox(0.55F, 9.05F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(3, 31).addBox(4.65F, 4.75F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(0, 32).addBox(-3.6F, 5.0F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(28, 34).addBox(0.65F, 0.75F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, 7.5F, -3.75F);
        this.Body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.7854F);
        cube_r2.texOffs(0, 28).addBox(0.5F, -1.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(48, 45).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(24, 29).addBox(-6.0F, -3.0F, 1.0F, 5.0F, 2.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(37, 6).addBox(-6.0F, -8.0F, 1.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(42, 0).addBox(-7.0F, -7.0F, 1.0F, 7.0F, 5.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(50, 24).addBox(-5.0F, -7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(50, 26).addBox(-5.0F, -2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(0, 55).addBox(-6.0F, -6.0F, -3.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(46, 17).addBox(-7.0F, -5.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(32, 45).addBox(-4.5F, -8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(12, 41).addBox(-2.75F, -9.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(16, 41).addBox(-5.25F, -9.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(0, 41).addBox(-8.0F, -6.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(28, 39).addBox(-8.0F, -4.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(28, 41).addBox(-2.5F, -8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(44, 45).addBox(-7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(-3.5F, -1.5F, 0.0F);
        this.RightArm.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, -0.4363F);
        cube_r3.texOffs(24, 20).addBox(-4.5F, 1.25F, -4.0F, 9.0F, 1.0F, 8.0F, 0.0F, false);
        cube_r3.texOffs(0, 28).addBox(-3.5F, -1.75F, -4.0F, 8.0F, 3.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(32, 45).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(1.5782F, -1.1195F, 0.0F);
        this.LeftArm.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.4363F);
        cube_r4.texOffs(24, 8).addBox(-4.25F, 0.0F, -4.0F, 9.0F, 1.0F, 8.0F, 0.0F, false);
        cube_r4.texOffs(0, 16).addBox(-4.25F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(16, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightBoot.texOffs(0, 4).addBox(-4.0F, 11.0F, -2.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.RightBoot.texOffs(50, 17).addBox(-5.0F, 9.0F, -1.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(0, 0).addBox(3.2F, 11.0F, -2.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.LeftBoot.texOffs(50, 6).addBox(3.2F, 9.0F, -1.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.LeftBoot.texOffs(0, 39).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(20, 0).addBox(4.5F, 10.0F, -2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.Belt.texOffs(0, 0).addBox(4.5F, 11.0F, -1.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(12, 16).addBox(4.5F, 7.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.Belt.texOffs(0, 16).addBox(4.5F, 9.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Belt.texOffs(24, 0).addBox(4.0F, 9.0F, 0.0F, 2.0F, 6.0F, 3.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\CreateArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */