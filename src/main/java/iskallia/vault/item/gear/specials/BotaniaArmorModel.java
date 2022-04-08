package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class BotaniaArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public BotaniaArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(64, 11).addBox(-8.0F, -9.0F, 3.0F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        this.Head.texOffs(56, 40).addBox(-7.0F, -10.25F, 0.25F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(33, 17).addBox(-7.0F, -11.0F, 4.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 1.2217F, 0.0F, 3.1416F);
        cube_r1.texOffs(48, 11).addBox(1.5F, -2.196F, -0.5641F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r1.texOffs(15, 51).addBox(-5.5F, -2.196F, -0.5641F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r1.texOffs(72, 29).addBox(-5.5F, -2.196F, -7.5641F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r1.texOffs(72, 34).addBox(1.5F, -2.196F, -7.5641F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r1.texOffs(69, 52).addBox(-2.5F, -3.946F, -4.5641F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        cube_r1.texOffs(0, 70).addBox(-1.5F, -2.946F, -3.5641F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.7854F, 0.0F, 3.1416F);
        cube_r2.texOffs(68, 17).addBox(-2.5F, -0.154F, -9.0296F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.8326F, 0.0F, -3.1416F);
        cube_r3.texOffs(48, 66).addBox(-2.5F, -1.3932F, 0.4888F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r4);
        setRotationAngle(cube_r4, 1.1814F, 0.4488F, 2.9654F);
        cube_r4.texOffs(63, 67).addBox(2.1775F, -0.8226F, -4.5641F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r5);
        setRotationAngle(cube_r5, 1.1955F, -0.3678F, -3.0009F);
        cube_r5.texOffs(68, 23).addBox(-7.1316F, -1.2291F, -4.5641F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r6);
        setRotationAngle(cube_r6, 2.0071F, 0.0F, 3.1416F);
        cube_r6.texOffs(0, 51).addBox(-2.5F, -0.8607F, -5.8868F, 5.0F, 7.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r7);
        setRotationAngle(cube_r7, 2.7053F, 0.0F, -3.1416F);
        cube_r7.texOffs(32, 35).addBox(-4.0F, -0.2388F, -9.3947F, 8.0F, 7.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(0.0F, -14.8353F, -7.6856F);
        this.Head.addChild(cube_r8);
        setRotationAngle(cube_r8, 3.098F, 0.0F, 3.1416F);
        cube_r8.texOffs(0, 17).addBox(-5.5F, 1.8353F, -12.8144F, 11.0F, 7.0F, 11.0F, 0.0F, false);
        cube_r8.texOffs(0, 0).addBox(-8.0F, 8.8353F, -15.3144F, 16.0F, 1.0F, 16.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(44, 17).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(70, 0).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(-6.339F, 6.8117F, 4.8011F);
        this.Body.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.6109F, -0.3229F);
        cube_r9.texOffs(24, 35).addBox(-2.217F, -0.8117F, -0.4F, 7.0F, 6.0F, 1.0F, 0.0F, false);
        cube_r9.texOffs(44, 33).addBox(-3.217F, -11.8117F, -0.4F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r9.texOffs(31, 50).addBox(-5.217F, -10.8117F, -0.4F, 10.0F, 10.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(-6.339F, 6.8117F, 4.8011F);
        this.Body.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.3819F, 0.4891F, 0.3844F);
        cube_r10.texOffs(33, 21).addBox(3.7307F, 2.5039F, -0.65F, 2.0F, 6.0F, 1.0F, 0.0F, false);
        cube_r10.texOffs(42, 17).addBox(1.7307F, 6.5039F, -0.65F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r11 = new ModelRenderer((Model) this);
        cube_r11.setPos(6.3701F, 6.3784F, 4.0906F);
        this.Body.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.2455F, -0.3644F, -0.2636F);
        cube_r11.texOffs(56, 35).addBox(-3.7418F, 6.589F, -0.65F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r11.texOffs(0, 35).addBox(-5.7418F, 2.589F, -0.65F, 2.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r12 = new ModelRenderer((Model) this);
        cube_r12.setPos(6.3701F, 6.3784F, 4.0906F);
        this.Body.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, -0.4363F, 0.3491F);
        cube_r12.texOffs(0, 63).addBox(-4.5897F, -0.8784F, -0.4F, 7.0F, 6.0F, 1.0F, 0.0F, false);
        cube_r12.texOffs(69, 58).addBox(-3.5897F, -11.8784F, -0.4F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r12.texOffs(48, 0).addBox(-4.5897F, -10.8784F, -0.4F, 10.0F, 10.0F, 1.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(32, 61).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r13 = new ModelRenderer((Model) this);
        cube_r13.setPos(-1.5F, -0.5F, -5.5F);
        this.RightArm.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.7854F, 0.0F, 0.0F);
        cube_r13.texOffs(60, 11).addBox(-1.5F, 1.3F, -3.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r13.texOffs(65, 46).addBox(-2.5F, 1.3F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        ModelRenderer cube_r14 = new ModelRenderer((Model) this);
        cube_r14.setPos(-6.5F, -0.5F, 0.0F);
        this.RightArm.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.0F, -0.9163F);
        cube_r14.texOffs(0, 22).addBox(-3.5F, 1.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r14.texOffs(63, 60).addBox(-2.5F, 1.5F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(16, 59).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r15 = new ModelRenderer((Model) this);
        cube_r15.setPos(6.5F, -0.5F, 0.0F);
        this.LeftArm.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, 0.9599F);
        cube_r15.texOffs(0, 17).addBox(2.5F, 1.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        cube_r15.texOffs(56, 33).addBox(-2.5F, 1.5F, -3.0F, 5.0F, 1.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_r16 = new ModelRenderer((Model) this);
        cube_r16.setPos(1.5F, -0.5F, -5.5F);
        this.LeftArm.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.7854F, 0.0F, 0.0F);
        cube_r16.texOffs(20, 56).addBox(-1.5F, 1.0F, -3.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r16.texOffs(64, 40).addBox(-2.5F, 1.0F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(53, 50).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\BotaniaArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */