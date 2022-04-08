package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class JawboneArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 64 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 15).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(48, 26).addBox(-5.0F, -4.0F, -7.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(32, 49).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(46, 10).addBox(4.0F, -4.0F, -7.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(7, 0).addBox(1.0F, -3.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -2.0F, -1.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.48F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(-6.0F, -2.0F, -5.5F, 12.0F, 4.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(2.0F, 6.5F, 7.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.8727F, 0.0F, 0.0F);
            cube_r2.texOffs(56, 35).addBox(-1.0F, -10.5F, 4.5F, 2.0F, 7.0F, 3.0F, 0.0F, false);
            cube_r2.texOffs(0, 0).addBox(-5.0F, -10.5F, 4.5F, 2.0F, 7.0F, 3.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 33).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(24, 15).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 7.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(24, 33).addBox(-2.0F, 8.0F, -4.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(56, 45).addBox(-2.0F, 8.0F, 3.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
            this.Body.texOffs(12, 41).addBox(-1.0F, 11.0F, 3.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(35, 0).addBox(-4.0F, 1.0F, 3.0F, 8.0F, 7.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-2.0F, 6.5F, 7.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.8727F, 0.0F, 0.0F);
            cube_r3.texOffs(0, 57).addBox(-1.0F, -3.5F, -1.5F, 2.0F, 7.0F, 3.0F, 0.0F, false);
            cube_r3.texOffs(57, 0).addBox(3.0F, -3.5F, -1.5F, 2.0F, 7.0F, 3.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(32, 49).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(42, 19).addBox(-5.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.RightArm.texOffs(42, 15).addBox(-5.0F, -2.0F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-3.5F, 13.5F, 0.0F);
            this.RightArm.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, -0.48F);
            cube_r4.texOffs(0, 31).addBox(-0.25F, -3.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-4.0F, 8.5F, 0.0F);
            this.RightArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3054F);
            cube_r5.texOffs(56, 26).addBox(-1.0F, -2.5F, -2.0F, 2.0F, 5.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-1.5F, 0.0F, 0.0F);
            this.RightArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.0F, 0.0F, -0.3927F);
            cube_r6.texOffs(0, 31).addBox(-4.5F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(28, 41).addBox(4.0F, -2.0F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(0, 41).addBox(4.0F, -2.0F, 2.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            this.LeftArm.texOffs(48, 10).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(4.13F, 10.4988F, 0.0F);
            this.LeftArm.addChild(cube_r7);
            setRotationAngle(cube_r7, 3.1416F, 0.0F, 2.8362F);
            cube_r7.texOffs(48, 49).addBox(-1.725F, -4.3672F, -2.0F, 2.0F, 5.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(4.13F, 10.4988F, 0.0F);
            this.LeftArm.addChild(cube_r8);
            setRotationAngle(cube_r8, -3.1416F, 0.0F, -2.6616F);
            cube_r8.texOffs(0, 15).addBox(-1.3076F, -0.667F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(1.5F, 0.0F, 0.0F);
            this.LeftArm.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.0F, 0.0F, 0.3927F);
            cube_r9.texOffs(24, 23).addBox(-3.5F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(16, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightBoot.texOffs(20, 57).addBox(-2.0F, 8.0F, -4.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(10, 57).addBox(-1.8F, 8.0F, -4.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
            this.LeftBoot.texOffs(0, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(22, 17).addBox(-2.0F, 10.0F, -5.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r1_l2 = new ModelRenderer((Model) this);
            cube_r1_l2.setPos(-3.5F, 12.5F, 0.0F);
            this.Belt.addChild(cube_r1_l2);
            setRotationAngle(cube_r1_l2, 0.0F, 0.0F, 0.3927F);
            cube_r1_l2.texOffs(0, 16).addBox(3.5F, -3.5F, -4.0F, 7.0F, 1.0F, 8.0F, 0.0F, false);

            ModelRenderer cube_r2_l2 = new ModelRenderer((Model) this);
            cube_r2_l2.setPos(-3.5F, 12.5F, 0.0F);
            this.Belt.addChild(cube_r2_l2);
            setRotationAngle(cube_r2_l2, 0.0F, 0.0F, -0.3927F);
            cube_r2_l2.texOffs(22, 8).addBox(-3.5F, -0.5F, -4.0F, 7.0F, 1.0F, 8.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(16, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(0, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\JawboneArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */