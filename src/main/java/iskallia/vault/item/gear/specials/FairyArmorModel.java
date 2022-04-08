package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class FairyArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public FairyArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 128 : 128;
        this.texHeight = isLayer2() ? 128 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(22, 29).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(39, 24).addBox(-4.0F, -6.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(39, 22).addBox(-1.0F, -8.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(16, 39).addBox(2.0F, -6.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(12, 39).addBox(5.0F, -6.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(8, 39).addBox(5.0F, -7.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(4, 39).addBox(5.0F, -5.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(8, 37).addBox(3.0F, -5.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(4, 37).addBox(0.0F, -7.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 37).addBox(-3.25F, -6.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 39).addBox(-6.0F, -5.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(16, 37).addBox(-6.0F, -7.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(12, 37).addBox(-6.0F, -7.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 0).addBox(-11.0F, -6.0F, -11.0F, 22.0F, 0.0F, 21.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 41).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(36, 27).addBox(-4.0F, 5.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(36, 25).addBox(-1.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(36, 21).addBox(1.0F, 5.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(33, 26).addBox(3.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(36, 23).addBox(-3.0F, 2.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(30, 23).addBox(3.0F, 4.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(30, 25).addBox(0.0F, 5.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(30, 27).addBox(1.0F, 2.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(33, 22).addBox(-3.0F, 2.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(33, 24).addBox(-4.0F, 5.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(-2.0F, 2.1321F, 6.4042F);
        this.Body.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.982F, -0.1733F, -0.2528F);
        cube_r1.texOffs(0, 6).addBox(0.0F, -4.0F, -7.5F, 0.0F, 8.0F, 15.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(2.0F, 2.1321F, 6.4042F);
        this.Body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.982F, 0.1733F, 0.2528F);
        cube_r2.texOffs(0, 14).addBox(0.0F, -4.0F, -7.5F, 0.0F, 8.0F, 15.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(46, 21).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(16, 11).addBox(-3.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(16, 9).addBox(-1.0F, 6.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(16, 7).addBox(-3.0F, 3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(8, 16).addBox(-1.0F, 6.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(15, 19).addBox(-5.0F, 3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.RightArm.texOffs(12, 18).addBox(-5.0F, 7.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(15, 17).addBox(4.0F, 3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 18).addBox(1.0F, 7.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(15, 15).addBox(0.0F, 4.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 2).addBox(0.0F, 4.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(12, 2).addBox(1.0F, 7.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(16, 3).addBox(4.0F, 7.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.LeftArm.texOffs(40, 45).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(24, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);


        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(0, 16).addBox(-5.0F, 10.0F, -3.0F, 10.0F, 1.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_l2_r1 = new ModelRenderer((Model) this);
        cube_l2_r1.setPos(-5.5F, 12.5F, 3.5F);
        this.Belt.addChild(cube_l2_r1);
        setRotationAngle(cube_l2_r1, -0.4971F, -0.1719F, -0.3053F);
        cube_l2_r1.texOffs(42, 38).addBox(-2.5F, 1.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        cube_l2_r1.texOffs(15, 43).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_l2_r2 = new ModelRenderer((Model) this);
        cube_l2_r2.setPos(-5.5F, 12.5F, -3.5F);
        this.Belt.addChild(cube_l2_r2);
        setRotationAngle(cube_l2_r2, 0.3491F, 0.0F, -0.2618F);
        cube_l2_r2.texOffs(0, 39).addBox(-2.5F, 1.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        cube_l2_r2.texOffs(30, 44).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_l2_r3 = new ModelRenderer((Model) this);
        cube_l2_r3.setPos(5.5F, 12.5F, -3.5F);
        this.Belt.addChild(cube_l2_r3);
        setRotationAngle(cube_l2_r3, 0.4232F, -0.1096F, 0.2382F);
        cube_l2_r3.texOffs(27, 37).addBox(-2.5F, 1.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        cube_l2_r3.texOffs(0, 45).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_l2_r4 = new ModelRenderer((Model) this);
        cube_l2_r4.setPos(5.5F, 12.5F, 3.5F);
        this.Belt.addChild(cube_l2_r4);
        setRotationAngle(cube_l2_r4, -0.4363F, 0.0F, 0.3927F);
        cube_l2_r4.texOffs(32, 31).addBox(-2.5F, 1.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        cube_l2_r4.texOffs(45, 45).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_l2_r5 = new ModelRenderer((Model) this);
        cube_l2_r5.setPos(0.0F, 15.0341F, 3.2412F);
        this.Belt.addChild(cube_l2_r5);
        setRotationAngle(cube_l2_r5, 0.5236F, 0.0F, 0.0F);
        cube_l2_r5.texOffs(24, 0).addBox(-5.0F, -4.0F, 0.25F, 10.0F, 8.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_l2_r6 = new ModelRenderer((Model) this);
        cube_l2_r6.setPos(0.0F, 13.0F, 3.5F);
        this.Belt.addChild(cube_l2_r6);
        setRotationAngle(cube_l2_r6, 0.7418F, 0.0F, 0.0F);
        cube_l2_r6.texOffs(32, 17).addBox(-5.0F, -3.0F, 0.2F, 10.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_l2_r7 = new ModelRenderer((Model) this);
        cube_l2_r7.setPos(0.0F, 16.0F, -3.5F);
        this.Belt.addChild(cube_l2_r7);
        setRotationAngle(cube_l2_r7, -0.4363F, 0.0F, 0.0F);
        cube_l2_r7.texOffs(26, 9).addBox(-5.0F, -4.0F, -1.5F, 10.0F, 7.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_l2_r8 = new ModelRenderer((Model) this);
        cube_l2_r8.setPos(0.0F, 13.0F, -3.5F);
        this.Belt.addChild(cube_l2_r8);
        setRotationAngle(cube_l2_r8, -0.7418F, 0.0F, 0.0F);
        cube_l2_r8.texOffs(32, 24).addBox(-5.0F, -3.0F, -1.25F, 10.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_l2_r9 = new ModelRenderer((Model) this);
        cube_l2_r9.setPos(5.5F, 16.0F, 0.0F);
        this.Belt.addChild(cube_l2_r9);
        setRotationAngle(cube_l2_r9, 0.0F, 0.0F, -0.6109F);
        cube_l2_r9.texOffs(14, 49).addBox(0.5F, -4.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_l2_r10 = new ModelRenderer((Model) this);
        cube_l2_r10.setPos(-5.5F, 16.0F, 0.0F);
        this.Belt.addChild(cube_l2_r10);
        setRotationAngle(cube_l2_r10, 0.0F, 0.0F, 0.6981F);
        cube_l2_r10.texOffs(28, 50).addBox(-2.0F, -3.5F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_l2_r11 = new ModelRenderer((Model) this);
        cube_l2_r11.setPos(-5.5F, 13.0F, 0.0F);
        this.Belt.addChild(cube_l2_r11);
        setRotationAngle(cube_l2_r11, 0.0F, 0.0F, 0.8727F);
        cube_l2_r11.texOffs(0, 51).addBox(-1.5F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_l2_r12 = new ModelRenderer((Model) this);
        cube_l2_r12.setPos(5.5F, 13.0F, 0.0F);
        this.Belt.addChild(cube_l2_r12);
        setRotationAngle(cube_l2_r12, 0.0F, 0.0F, -0.7854F);
        cube_l2_r12.texOffs(48, 0).addBox(0.5F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 23).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 23).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\FairyArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */