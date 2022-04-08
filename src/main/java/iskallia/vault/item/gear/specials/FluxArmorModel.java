package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class FluxArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public FluxArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(0, 24).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);
        this.Head.texOffs(0, 16).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 1.0F, 7.0F, 0.0F, false);
        this.Head.texOffs(68, 69).addBox(5.0F, -7.0F, 1.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(65, 9).addBox(-6.0F, -7.0F, 1.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        this.Head.texOffs(0, 24).addBox(5.75F, -6.0F, 2.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(4, 50).addBox(5.0F, -11.0F, 2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(24, 0).addBox(-5.0F, -7.0F, 5.0F, 10.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 16).addBox(-1.0F, -11.0F, -5.75F, 2.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(0, 32).addBox(-7.0F, -5.0F, 2.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(31, 24).addBox(-7.0F, -5.0F, 4.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(3, 45).addBox(-6.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Head.texOffs(44, 6).addBox(-6.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(1.0F, -11.0F, -5.0F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.6545F);
        cube_r1.texOffs(23, 41).addBox(-1.0F, -5.75F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(64, 36).addBox(2.0F, -5.75F, -0.5F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-1.0F, -11.0F, -5.0F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.6545F);
        cube_r2.texOffs(23, 43).addBox(-2.0F, -5.75F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r2.texOffs(64, 61).addBox(-4.0F, -5.75F, -0.5F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(5.75F, -9.25F, 3.25F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.4363F, 0.0F, 0.0F);
        cube_r3.texOffs(32, 6).addBox(-0.5F, -3.5F, 1.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(32, 34).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(46, 0).addBox(-3.0F, 2.0F, 3.0F, 6.0F, 5.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(38, 18).addBox(-3.0F, 1.0F, -4.0F, 6.0F, 5.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(52, 18).addBox(-2.0F, 6.0F, -3.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(52, 34).addBox(1.75F, 2.25F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(3, 37).addBox(-2.75F, 2.25F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(32, 35).addBox(-1.25F, 2.25F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(25, 32).addBox(-1.25F, 5.25F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(35, 13).addBox(0.25F, 2.25F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 29).addBox(1.0F, 2.25F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(31, 18).addBox(-2.0F, 2.25F, -4.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 4).addBox(3.75F, 2.25F, 3.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.Body.texOffs(26, 50).addBox(-4.75F, 2.25F, 3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(34, 29).addBox(0.25F, 5.25F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(25, 36).addBox(1.75F, 2.25F, 6.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 50).addBox(0.25F, 2.25F, 5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 41).addBox(-1.25F, 2.25F, 5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(36, 6).addBox(-4.75F, 2.25F, 5.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(3.25F, 5.75F, 4.6667F);
        this.Body.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.8727F);
        cube_r4.texOffs(24, 6).addBox(-1.0F, 0.5F, 0.3333F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r4.texOffs(23, 45).addBox(1.0F, 0.5F, -1.6667F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r4.texOffs(32, 14).addBox(-1.0F, 0.5F, -0.6667F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(-3.25F, 5.75F, 4.6667F);
        this.Body.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.8727F);
        cube_r5.texOffs(25, 34).addBox(-2.25F, 0.5F, 1.3333F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r5.texOffs(0, 0).addBox(-2.25F, 0.5F, -1.6667F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        cube_r5.texOffs(40, 50).addBox(-0.25F, 0.5F, -0.6667F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(48, 61).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(57, 0).addBox(-5.0F, 0.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(56, 52).addBox(-5.0F, 2.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(56, 27).addBox(-5.0F, 4.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(56, 18).addBox(-5.0F, 6.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(-4.5F, -3.0F, 0.0F);
        this.RightArm.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.1309F);
        cube_r6.texOffs(0, 41).addBox(-3.5F, -0.5F, -3.5F, 8.0F, 2.0F, 7.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(-4.5F, -3.0F, 0.0F);
        this.RightArm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.4363F);
        cube_r7.texOffs(31, 8).addBox(-4.5F, -3.0F, -4.0F, 9.0F, 2.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(32, 60).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.LeftArm.texOffs(40, 51).addBox(1.0F, 0.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(24, 50).addBox(1.0F, 2.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(0, 50).addBox(1.0F, 4.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(48, 42).addBox(1.0F, 6.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(4.5F, -3.0F, 0.0F);
        this.LeftArm.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.1309F);
        cube_r8.texOffs(0, 32).addBox(-4.5F, -0.5F, -3.5F, 9.0F, 2.0F, 7.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(4.5F, -3.0F, 0.0F);
        this.LeftArm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.4363F);
        cube_r9.texOffs(30, 24).addBox(-4.5F, -3.0F, -4.0F, 9.0F, 2.0F, 8.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(16, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(0.0F, 9.5F, -3.5F);
        this.RightBoot.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0873F, 0.0F, 0.0F);
        cube_r10.texOffs(72, 18).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(0, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r11 = new ModelRenderer((Model) this);
        cube_r11.setPos(0.2F, 9.5F, -3.5F);
        this.LeftBoot.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0873F, 0.0F, 0.0F);
        cube_r11.texOffs(16, 50).addBox(-2.0F, -3.5F, -0.5F, 4.0F, 7.0F, 1.0F, 0.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\FluxArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */