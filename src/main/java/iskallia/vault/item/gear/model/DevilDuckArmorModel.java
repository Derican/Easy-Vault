package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class DevilDuckArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 64 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -10.0F, -0.75F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.7418F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(5.0F, -5.0F, -2.5F, 2.0F, 5.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(36, 0).addBox(-7.0F, 5.0F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(56, 0).addBox(5.0F, 5.0F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(52, 42).addBox(5.0F, 0.0F, -2.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
            cube_r1.texOffs(0, 16).addBox(-7.0F, -5.0F, -2.5F, 2.0F, 5.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(52, 50).addBox(-8.0F, 0.0F, -2.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, -2.5F, -6.8333F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.3491F, 0.0F, 0.0F);
            cube_r2.texOffs(24, 5).addBox(-2.0F, -1.5F, 0.8333F, 4.0F, 1.0F, 2.0F, 0.0F, false);
            cube_r2.texOffs(24, 0).addBox(-2.0F, 0.5F, -1.1667F, 4.0F, 1.0F, 4.0F, 0.0F, false);
            cube_r2.texOffs(44, 27).addBox(-3.0F, -0.5F, -2.1667F, 6.0F, 1.0F, 5.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(0, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(40, 18).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 4.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 8.0F, -3.5F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.2182F, 0.0F, 0.0F);
            cube_r3.texOffs(52, 33).addBox(-3.0F, -4.0F, -0.5F, 6.0F, 7.0F, 2.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(44, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-3.0F, -2.0F, 0.0F);
            this.RightArm.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.0F, 0.2618F);
            cube_r4.texOffs(24, 8).addBox(-2.5F, 1.5F, -4.0F, 6.0F, 2.0F, 8.0F, 0.0F, false);
            cube_r4.texOffs(20, 20).addBox(-2.5F, -2.5F, -4.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(3.0F, -2.0F, 0.0F);
            this.LeftArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -0.3927F);
            cube_r5.texOffs(0, 16).addBox(-4.0F, -2.5F, -4.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);
            cube_r5.texOffs(24, 32).addBox(-4.0F, 1.5F, -4.0F, 6.0F, 2.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(36, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(1.8F, 8.0F, -3.5F);
            this.RightBoot.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.2194F, 0.2143F, -0.7617F);
            cube_r6.texOffs(16, 58).addBox(-3.1642F, -3.4142F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(20, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-2.0F, 8.0F, -3.5F);
            this.LeftBoot.addChild(cube_r7);
            setRotationAngle(cube_r7, 0.2194F, 0.2143F, -0.7617F);
            cube_r7.texOffs(26, 58).addBox(-0.5858F, -0.5858F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

            this.Belt = new ModelRenderer((Model) this);
            this.Belt.setPos(0.0F, 0.0F, 0.0F);
            this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);
            this.Belt.texOffs(0, 16).addBox(-2.0F, 9.0F, -3.5F, 4.0F, 3.0F, 7.0F, 0.0F, false);

            ModelRenderer cube_r1_l2 = new ModelRenderer((Model) this);
            cube_r1_l2.setPos(3.0F, 11.0F, 0.0F);
            this.Belt.addChild(cube_r1_l2);
            setRotationAngle(cube_r1_l2, 0.0F, 0.0F, 0.9163F);
            cube_r1_l2.texOffs(30, 28).addBox(1.0F, -1.0F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r2_l2 = new ModelRenderer((Model) this);
            cube_r2_l2.setPos(3.0F, 11.0F, 0.0F);
            this.Belt.addChild(cube_r2_l2);
            setRotationAngle(cube_r2_l2, 0.0F, 0.0F, 0.48F);
            cube_r2_l2.texOffs(0, 0).addBox(1.0F, -3.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r2_l2.texOffs(0, 16).addBox(1.0F, -3.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r2_l2.texOffs(24, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r3_l2 = new ModelRenderer((Model) this);
            cube_r3_l2.setPos(-6.3172F, 13.2905F, 0.0F);
            this.Belt.addChild(cube_r3_l2);
            setRotationAngle(cube_r3_l2, 0.0F, 0.0F, -0.9163F);
            cube_r3_l2.texOffs(34, 8).addBox(-2.0F, 0.5F, -2.5F, 4.0F, 1.0F, 5.0F, 0.0F, false);

            ModelRenderer cube_r4_l2 = new ModelRenderer((Model) this);
            cube_r4_l2.setPos(-3.0F, 11.0F, 0.0F);
            this.Belt.addChild(cube_r4_l2);
            setRotationAngle(cube_r4_l2, 0.0F, 0.0F, -0.48F);
            cube_r4_l2.texOffs(15, 16).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r4_l2.texOffs(3, 18).addBox(-2.0F, -3.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            cube_r4_l2.texOffs(16, 28).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

            this.RightLeg = new ModelRenderer((Model) this);
            this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
            this.RightLeg.texOffs(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

            this.LeftLeg = new ModelRenderer((Model) this);
            this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
            this.LeftLeg.texOffs(22, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\DevilDuckArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */