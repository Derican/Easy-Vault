package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class DevilArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(32, 22).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(0, 61).addBox(-8.0F, -15.0F, -3.0F, 3.0F, 8.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 61).addBox(5.0F, -15.0F, -3.0F, 3.0F, 8.0F, 6.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(5.0F, -23.0F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);
            this.Head.texOffs(0, 0).addBox(-7.0F, -23.0F, -2.0F, 2.0F, 8.0F, 4.0F, 0.0F, false);
            this.Head.texOffs(15, 41).addBox(-2.0F, -25.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-0.0184F, -7.0F, -5.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -0.4674F, -0.3542F, 0.1733F);
            cube_r1.texOffs(74, 40).addBox(-0.5429F, -5.0F, -1.3107F, 5.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-0.0184F, -7.0F, -5.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, -0.4674F, 0.3542F, -0.1733F);
            cube_r2.texOffs(0, 75).addBox(-4.4571F, -5.0F, -1.3107F, 5.0F, 10.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(22, 58).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -7.0F, 12.0F, 6.0F, 13.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(11.3404F, 0.6929F, 6.8071F);
            this.Body.addChild(cube_r3);
            setRotationAngle(cube_r3, 1.113F, -0.6783F, -0.3741F);
            cube_r3.texOffs(29, 38).addBox(1.694F, -4.5F, -9.0318F, 2.0F, 9.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-11.3404F, 0.9429F, 7.8071F);
            this.Body.addChild(cube_r4);
            setRotationAngle(cube_r4, 1.1569F, 0.7276F, 0.3912F);
            cube_r4.texOffs(0, 41).addBox(-3.694F, -4.5F, -9.0318F, 2.0F, 9.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-11.3404F, 0.9429F, 7.8071F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.9096F, 0.523F, -0.0252F);
            cube_r5.texOffs(44, 38).addBox(-2.6596F, -4.5F, 1.232F, 13.0F, 9.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(11.3404F, 0.6929F, 6.8071F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, 0.8967F, -0.4645F, 0.02F);
            cube_r6.texOffs(56, 13).addBox(-10.3404F, -4.5F, 1.232F, 13.0F, 9.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(0.0F, 7.0F, 4.0F);
            this.Body.addChild(cube_r7);
            setRotationAngle(cube_r7, -0.3927F, 0.0F, 0.0F);
            cube_r7.texOffs(76, 77).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 7.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(2.35F, 5.0F, -5.5F);
            this.Body.addChild(cube_r8);
            setRotationAngle(cube_r8, 0.9855F, -0.9275F, -0.879F);
            cube_r8.texOffs(69, 0).addBox(-3.0F, -3.0F, -2.5F, 7.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(-1.9913F, 5.0F, -5.5F);
            this.Body.addChild(cube_r9);
            setRotationAngle(cube_r9, 0.8213F, 0.8189F, 0.6654F);
            cube_r9.texOffs(30, 74).addBox(-4.0F, -3.0F, -2.5F, 7.0F, 10.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(14, 74).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(-3.0041F, 0.4183F, 0.0F);
            this.RightArm.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.0F, 0.0F, 0.3927F);
            cube_r10.texOffs(0, 19).addBox(-8.5F, -3.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            cube_r10.texOffs(47, 50).addBox(-6.5F, -8.0F, -4.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(72, 24).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(3.0041F, 0.4183F, 0.0F);
            this.LeftArm.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.0F, 0.0F, -0.3927F);
            cube_r11.texOffs(0, 30).addBox(-1.5F, -3.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            cube_r11.texOffs(37, 0).addBox(-1.5F, -8.0F, -4.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(62, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(46, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\DevilArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */