package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class XnetArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public XnetArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 64 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(50, 37).addBox(-6.0F, -6.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(46, 40).addBox(5.0F, -6.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(24, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 4.0F, 4.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(24, 26).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(44, 24).addBox(-4.0F, 1.0F, 3.0F, 8.0F, 3.0F, 3.0F, 0.0F, false);
        this.Body.texOffs(28, 21).addBox(-3.0F, 4.0F, 3.0F, 6.0F, 3.0F, 2.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(48, 8).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(0, 16).addBox(-4.5F, 5.0F, -3.5F, 7.0F, 7.0F, 7.0F, 0.0F, false);
        this.RightArm.texOffs(0, 30).addBox(-4.5F, -2.0F, -3.5F, 4.0F, 6.0F, 7.0F, 0.0F, false);
        this.RightArm.texOffs(24, 8).addBox(-5.5F, 7.25F, -4.0F, 4.0F, 5.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(21, 21).addBox(-7.0F, 8.0F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(48, 30).addBox(-7.0F, 0.0F, 2.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(0, 6).addBox(-6.0F, 0.0F, 2.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(-2.5F, 8.2069F, -3.3363F);
        this.RightArm.addChild(cube_r1);
        setRotationAngle(cube_r1, -1.3526F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-2.5F, 14.0F, -4.0F);
        this.RightArm.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.6545F, 0.0F, 0.0F);
        cube_r2.texOffs(15, 30).addBox(-0.5F, -4.0F, -2.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(-2.5F, 14.0F, 4.0F);
        this.RightArm.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.4363F, 0.0F, 0.0F);
        cube_r3.texOffs(0, 30).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(-2.5F, 14.0F, 1.0F);
        this.RightArm.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.2598F, 0.0173F, -0.0254F);
        cube_r4.texOffs(0, 16).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(-2.5F, 14.0F, -1.0F);
        this.RightArm.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3054F, 0.0F, 0.0F);
        cube_r5.texOffs(40, 8).addBox(-0.5F, -2.25F, -2.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(0, 43).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(34, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(18, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        this.RightLeg.texOffs(4, 32).addBox(-1.0F, 1.0F, 3.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        this.RightLeg.texOffs(12, 16).addBox(-1.0F, 1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightLeg.texOffs(0, 16).addBox(-1.0F, 7.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightLeg.texOffs(30, 1).addBox(-3.0F, 3.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.RightLeg.texOffs(28, 28).addBox(-3.0F, 5.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.RightLeg.texOffs(28, 15).addBox(-3.0F, 7.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(24, 0).addBox(1.95F, 7.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.LeftLeg.texOffs(24, 5).addBox(1.95F, 5.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.LeftLeg.texOffs(24, 10).addBox(1.95F, 3.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        this.LeftLeg.texOffs(0, 0).addBox(0.2F, 7.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftLeg.texOffs(0, 32).addBox(0.2F, 1.0F, 3.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        this.LeftLeg.texOffs(0, 2).addBox(0.2F, 1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\XnetArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */