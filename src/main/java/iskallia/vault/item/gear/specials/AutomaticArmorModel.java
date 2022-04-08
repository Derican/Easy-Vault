package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class AutomaticArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public AutomaticArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 64 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(28, 22).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(47, 87).addBox(-4.0F, -7.0F, -6.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(66, 69).addBox(-6.0F, -4.0F, -4.0F, 1.0F, 5.0F, 8.0F, 0.0F, false);
        this.Head.texOffs(76, 14).addBox(-4.0F, -8.0F, 5.0F, 8.0F, 9.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(78, 77).addBox(-6.0F, -8.0F, -2.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(77, 0).addBox(5.0F, -8.0F, -2.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(16, 67).addBox(5.0F, -4.0F, -4.0F, 1.0F, 5.0F, 8.0F, 0.0F, false);
        this.Head.texOffs(86, 28).addBox(3.0F, -7.0F, -6.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(32, 5).addBox(-3.0F, -7.0F, -7.0F, 6.0F, 1.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(40, 38).addBox(-2.0F, -8.0F, -6.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(74, 31).addBox(-1.5F, -12.0F, -4.5F, 3.0F, 3.0F, 6.0F, 0.0F, false);
        this.Head.texOffs(85, 0).addBox(-1.5F, -11.0F, 1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        this.Head.texOffs(40, 8).addBox(-1.0F, -10.0F, -6.5F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(2.0F, -13.0F, -5.0F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.6545F);
        cube_r1.texOffs(0, 47).addBox(2.0F, -9.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(41, 83).addBox(2.0F, -6.5F, -0.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-2.0F, -13.0F, -5.0F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.6545F);
        cube_r2.texOffs(37, 51).addBox(-3.0F, -9.75F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        cube_r2.texOffs(16, 84).addBox(-4.0F, -6.75F, -0.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(0.0F, -4.5F, -5.5F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3054F, 0.0F, 0.0F);
        cube_r3.texOffs(50, 61).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(0.1564F, -1.0F, -5.5F);
        this.Head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.1886F, -0.3864F, -0.0718F);
        cube_r4.texOffs(54, 44).addBox(-0.4697F, -2.0F, -1.1339F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(0.1564F, -1.0F, -5.5F);
        this.Head.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.1886F, 0.3864F, 0.0718F);
        cube_r5.texOffs(72, 0).addBox(-3.5303F, -2.0F, -1.1339F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 47).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(0, 0).addBox(-6.0F, 1.0F, -4.0F, 12.0F, 8.0F, 8.0F, 0.0F, false);
        this.Body.texOffs(76, 69).addBox(-4.0F, 1.0F, 4.0F, 8.0F, 7.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(66, 82).addBox(-2.0F, 1.0F, 5.0F, 4.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(-13.0833F, 8.0F, 5.5F);
        this.Body.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.1756F, 0.218F, 0.6642F);
        cube_r6.texOffs(29, 16).addBox(-12.1667F, -12.25F, 3.25F, 19.0F, 4.0F, 1.0F, 0.0F, false);
        cube_r6.texOffs(22, 84).addBox(-0.9167F, -8.25F, 3.25F, 3.0F, 9.0F, 1.0F, 0.0F, false);
        cube_r6.texOffs(16, 63).addBox(-4.6667F, -8.25F, 3.25F, 3.0F, 11.0F, 1.0F, 0.0F, false);
        cube_r6.texOffs(0, 79).addBox(-8.4167F, -8.25F, 3.25F, 3.0F, 14.0F, 1.0F, 0.0F, false);
        cube_r6.texOffs(50, 70).addBox(-12.1667F, -8.25F, 3.25F, 3.0F, 17.0F, 1.0F, 0.0F, false);
        cube_r6.texOffs(40, 40).addBox(-7.1667F, -15.25F, 3.25F, 9.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(12.8367F, 0.2338F, 5.5F);
        this.Body.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, -0.2182F, -0.6545F);
        cube_r7.texOffs(78, 87).addBox(-6.5417F, -3.0F, 2.25F, 3.0F, 9.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(33, 83).addBox(-2.5417F, -3.0F, 2.25F, 3.0F, 11.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(8, 79).addBox(1.2083F, -3.0F, 2.25F, 3.0F, 14.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(58, 77).addBox(4.9583F, -3.0F, 2.25F, 3.0F, 17.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(0, 43).addBox(-6.0417F, -10.0F, 2.25F, 9.0F, 3.0F, 1.0F, 0.0F, false);
        cube_r7.texOffs(32, 0).addBox(-11.0417F, -7.0F, 2.25F, 19.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(0.0F, 10.0F, 6.0F);
        this.Body.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.6981F, 0.0F, 0.0F);
        cube_r8.texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(0.0F, 5.9997F, -5.7916F);
        this.Body.addChild(cube_r9);
        setRotationAngle(cube_r9, -1.5708F, 0.0F, 0.0F);
        cube_r9.texOffs(74, 62).addBox(-3.0F, -3.5F, -1.5F, 6.0F, 3.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(0.0F, 7.9997F, -3.2916F);
        this.Body.addChild(cube_r10);
        setRotationAngle(cube_r10, -1.1781F, 0.0F, 0.0F);
        cube_r10.texOffs(62, 5).addBox(-2.0F, -1.0F, -2.75F, 4.0F, 2.0F, 7.0F, 0.0F, false);

        ModelRenderer cube_r11 = new ModelRenderer((Model) this);
        cube_r11.setPos(0.0F, 5.0F, -0.5F);
        this.Body.addChild(cube_r11);
        setRotationAngle(cube_r11, -1.0036F, 0.0F, 0.0F);
        cube_r11.texOffs(0, 16).addBox(-5.5F, -3.0F, -5.5F, 11.0F, 7.0F, 7.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(65, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r12 = new ModelRenderer((Model) this);
        cube_r12.setPos(-4.875F, 5.25F, 0.0F);
        this.RightArm.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, -0.2182F);
        cube_r12.texOffs(24, 52).addBox(-0.625F, -3.5F, -3.5F, 3.0F, 8.0F, 7.0F, 0.0F, false);
        cube_r12.texOffs(83, 48).addBox(-2.625F, -1.5F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        cube_r12.texOffs(20, 30).addBox(-3.625F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        cube_r12.texOffs(74, 49).addBox(-1.625F, -2.5F, -3.5F, 1.0F, 6.0F, 7.0F, 0.0F, false);

        ModelRenderer cube_r13 = new ModelRenderer((Model) this);
        cube_r13.setPos(0.0F, 0.0F, 0.0F);
        this.RightArm.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, 0.48F);
        cube_r13.texOffs(36, 59).addBox(-11.0F, -2.5F, -4.0F, 3.0F, 3.0F, 8.0F, 0.0F, false);
        cube_r13.texOffs(80, 24).addBox(-9.0F, -4.5F, -3.0F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r13.texOffs(81, 44).addBox(-9.0F, -4.5F, 1.0F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r13.texOffs(52, 50).addBox(-6.0F, -0.5F, -4.5F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        cube_r13.texOffs(20, 38).addBox(-8.0F, -2.5F, -4.0F, 6.0F, 5.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(64, 21).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r14 = new ModelRenderer((Model) this);
        cube_r14.setPos(5.15F, -4.0F, 0.0F);
        this.LeftArm.addChild(cube_r14);
        setRotationAngle(cube_r14, -3.1416F, 0.0F, 2.6616F);
        cube_r14.texOffs(16, 80).addBox(-2.15F, -3.25F, 1.0F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r14.texOffs(77, 40).addBox(-2.15F, -3.25F, -3.0F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r14.texOffs(0, 30).addBox(-1.15F, -1.25F, -4.0F, 6.0F, 5.0F, 8.0F, 0.0F, false);
        cube_r14.texOffs(51, 29).addBox(0.85F, 0.75F, -4.5F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        cube_r14.texOffs(40, 5).addBox(-4.15F, -1.25F, -4.0F, 3.0F, 3.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r15 = new ModelRenderer((Model) this);
        cube_r15.setPos(4.875F, 5.25F, 0.0F);
        this.LeftArm.addChild(cube_r15);
        setRotationAngle(cube_r15, -3.1416F, 0.0F, -2.9671F);
        cube_r15.texOffs(26, 67).addBox(-2.375F, -1.5F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        cube_r15.texOffs(0, 30).addBox(-3.375F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        cube_r15.texOffs(41, 44).addBox(-0.375F, -3.5F, -3.5F, 3.0F, 8.0F, 7.0F, 0.0F, false);
        cube_r15.texOffs(34, 70).addBox(-1.375F, -2.5F, -3.5F, 1.0F, 6.0F, 7.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(0, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r16 = new ModelRenderer((Model) this);
        cube_r16.setPos(0.0F, 8.0F, -3.25F);
        this.RightBoot.addChild(cube_r16);
        setRotationAngle(cube_r16, -0.2182F, 0.0F, 0.0F);
        cube_r16.texOffs(24, 51).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r17 = new ModelRenderer((Model) this);
        cube_r17.setPos(0.0F, 10.5F, -3.5F);
        this.RightBoot.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.3054F, 0.0F, 0.0F);
        cube_r17.texOffs(54, 5).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(58, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r18 = new ModelRenderer((Model) this);
        cube_r18.setPos(0.2F, 8.0F, -3.25F);
        this.LeftBoot.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.2182F, 0.0F, 0.0F);
        cube_r18.texOffs(0, 16).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r19 = new ModelRenderer((Model) this);
        cube_r19.setPos(0.2F, 10.5F, -3.5F);
        this.LeftBoot.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.3054F, 0.0F, 0.0F);
        cube_r19.texOffs(52, 21).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 9).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
        this.Belt.texOffs(34, 35).addBox(-2.0F, 9.0F, -4.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
        this.Belt.texOffs(34, 17).addBox(-2.0F, 9.0F, 2.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
        this.Belt.texOffs(0, 0).addBox(-5.0F, 10.0F, -3.0F, 10.0F, 3.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_r1_l2 = new ModelRenderer((Model) this);
        cube_r1_l2.setPos(-3.5F, 11.5F, 3.0F);
        this.Belt.addChild(cube_r1_l2);
        setRotationAngle(cube_r1_l2, 0.0F, 0.0F, 0.829F);
        cube_r1_l2.texOffs(36, 24).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r1_l2.texOffs(24, 37).addBox(-1.5F, -2.5F, -6.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r2_l2 = new ModelRenderer((Model) this);
        cube_r2_l2.setPos(3.5F, 11.5F, 3.0F);
        this.Belt.addChild(cube_r2_l2);
        setRotationAngle(cube_r2_l2, 0.0F, 0.0F, -0.829F);
        cube_r2_l2.texOffs(26, 0).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r2_l2.texOffs(16, 37).addBox(-1.5F, -2.5F, -6.5F, 3.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r3_l2 = new ModelRenderer((Model) this);
        cube_r3_l2.setPos(5.5F, 11.5F, 0.0F);
        this.Belt.addChild(cube_r3_l2);
        setRotationAngle(cube_r3_l2, 0.0F, 0.0F, 0.2618F);
        cube_r3_l2.texOffs(24, 9).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r4_l2 = new ModelRenderer((Model) this);
        cube_r4_l2.setPos(-5.5F, 11.5F, 0.0F);
        this.Belt.addChild(cube_r4_l2);
        setRotationAngle(cube_r4_l2, 0.0F, 0.0F, -0.2618F);
        cube_r4_l2.texOffs(31, 4).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(0, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(20, 21).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\AutomaticArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */