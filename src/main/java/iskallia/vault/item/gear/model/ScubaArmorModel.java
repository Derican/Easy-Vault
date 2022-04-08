package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ScubaArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(26, 6).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(5.0F, -3.25F, -5.0F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.3491F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(1.0F, -5.75F, -0.25F, 2.0F, 7.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(24, 0).addBox(-7.0F, 1.25F, -0.25F, 10.0F, 2.0F, 2.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 44).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 35).addBox(-8.0F, 8.0F, -6.0F, 16.0F, 3.0F, 4.0F, 0.0F, false);
            this.Body.texOffs(0, 28).addBox(-8.0F, 8.0F, 2.0F, 16.0F, 3.0F, 4.0F, 0.0F, false);
            this.Body.texOffs(32, 60).addBox(4.0F, 8.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
            this.Body.texOffs(58, 12).addBox(-8.0F, 8.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, 6.5F, 5.5F);
            this.Body.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.6109F, 0.0F, 0.0F);
            cube_r2.texOffs(0, 14).addBox(-2.0F, -1.5F, 0.5F, 4.0F, 3.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, 5.0F, -4.0F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.48F, -0.0436F, 0.0F);
            cube_r3.texOffs(0, 18).addBox(-1.0F, -3.0F, -8.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(34, 22).addBox(-1.0F, -5.0F, -8.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
            cube_r3.texOffs(50, 0).addBox(-2.0F, -5.0F, -4.0F, 4.0F, 9.0F, 3.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(56, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(0, 14).addBox(-6.0F, 2.0F, -5.0F, 7.0F, 4.0F, 10.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(0, 0).addBox(-1.0F, 2.0F, -5.0F, 7.0F, 4.0F, 10.0F, 0.0F, false);
            this.LeftArm.texOffs(16, 55).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 55).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0F, 11.5F, -4.5F);
            this.RightBoot.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.48F, 0.0F);
            cube_r4.texOffs(0, 42).addBox(-4.5F, -0.5F, -5.5F, 5.0F, 2.0F, 11.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.2F, 11.5F, -4.5F);
            this.LeftBoot.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, -0.5236F, 0.0F);
            cube_r5.texOffs(29, 31).addBox(-0.25F, -0.5F, -5.75F, 5.0F, 2.0F, 11.0F, 0.0F, false);

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
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\ScubaArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */