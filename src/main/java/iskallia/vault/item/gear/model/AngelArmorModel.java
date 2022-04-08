package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class AngelArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(32, 22).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(5.0F, -8.0F, 0.0F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 0.0F, 0.0F, 0.2618F);
            cube_r1.texOffs(15, 35).addBox(1.0F, -4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-5.0F, -8.0F, 0.0F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2618F);
            cube_r2.texOffs(27, 35).addBox(-1.0F, -4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, -12.5F, 0.0F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.1745F, 0.0F, 0.0F);
            cube_r3.texOffs(30, 19).addBox(-3.0F, -0.5F, 3.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(15, 49).addBox(-3.0F, -0.5F, -4.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
            cube_r3.texOffs(72, 73).addBox(3.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
            cube_r3.texOffs(74, 40).addBox(-4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-0.0184F, -4.0F, -5.5F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.1886F, -0.3864F, -0.0718F);
            cube_r4.texOffs(0, 0).addBox(-0.5429F, -5.0F, -1.3107F, 5.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-0.0184F, -4.0F, -5.5F);
            this.Head.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.1886F, 0.3864F, 0.0718F);
            cube_r5.texOffs(32, 74).addBox(-4.4571F, -5.0F, -1.3107F, 5.0F, 10.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(22, 58).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(0, 0).addBox(-6.0F, -2.0F, -7.0F, 12.0F, 6.0F, 13.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(15.1809F, -4.3373F, 3.9788F);
            this.Body.addChild(cube_r6);
            setRotationAngle(cube_r6, -0.6485F, -0.3189F, 0.2333F);
            cube_r6.texOffs(29, 38).addBox(-1.0F, -4.5F, -5.5F, 2.0F, 9.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r7 = new ModelRenderer((Model) this);
            cube_r7.setPos(-15.1809F, -4.3373F, 3.9788F);
            this.Body.addChild(cube_r7);
            setRotationAngle(cube_r7, -0.6485F, 0.3189F, -0.2333F);
            cube_r7.texOffs(0, 41).addBox(-1.0F, -4.5F, -5.5F, 2.0F, 9.0F, 11.0F, 0.0F, false);

            ModelRenderer cube_r8 = new ModelRenderer((Model) this);
            cube_r8.setPos(4.0F, -1.7769F, 7.6355F);
            this.Body.addChild(cube_r8);
            setRotationAngle(cube_r8, -0.6109F, 0.0F, 0.0F);
            cube_r8.texOffs(44, 38).addBox(-18.0F, -4.5F, -1.0F, 13.0F, 9.0F, 2.0F, 0.0F, false);
            cube_r8.texOffs(56, 13).addBox(-3.0F, -4.5F, -1.0F, 13.0F, 9.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r9 = new ModelRenderer((Model) this);
            cube_r9.setPos(0.0F, 7.0F, 4.0F);
            this.Body.addChild(cube_r9);
            setRotationAngle(cube_r9, -0.3927F, 0.0F, 0.0F);
            cube_r9.texOffs(71, 49).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 7.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r10 = new ModelRenderer((Model) this);
            cube_r10.setPos(2.35F, 5.0F, -5.5F);
            this.Body.addChild(cube_r10);
            setRotationAngle(cube_r10, 0.6139F, -0.5198F, -0.3368F);
            cube_r10.texOffs(69, 0).addBox(-3.0F, -3.0F, -0.5F, 7.0F, 10.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r11 = new ModelRenderer((Model) this);
            cube_r11.setPos(-1.9913F, 5.0F, -5.5F);
            this.Body.addChild(cube_r11);
            setRotationAngle(cube_r11, 0.6139F, 0.5198F, 0.3368F);
            cube_r11.texOffs(16, 74).addBox(-4.0F, -3.0F, -0.5F, 7.0F, 10.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(72, 24).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r12 = new ModelRenderer((Model) this);
            cube_r12.setPos(-5.0F, -2.5F, 0.0F);
            this.RightArm.addChild(cube_r12);
            setRotationAngle(cube_r12, 0.0F, 0.0F, -0.7418F);
            cube_r12.texOffs(0, 19).addBox(-6.0F, 4.5F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            cube_r12.texOffs(47, 50).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(62, 63).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r13 = new ModelRenderer((Model) this);
            cube_r13.setPos(5.0F, -2.5F, 0.0F);
            this.LeftArm.addChild(cube_r13);
            setRotationAngle(cube_r13, 0.0F, 0.0F, 0.7418F);
            cube_r13.texOffs(0, 30).addBox(-4.0F, 4.5F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
            cube_r13.texOffs(37, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(46, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r14 = new ModelRenderer((Model) this);
            cube_r14.setPos(3.05F, 8.0F, 0.5F);
            this.RightBoot.addChild(cube_r14);
            setRotationAngle(cube_r14, 0.0F, 0.0F, 0.2618F);
            cube_r14.texOffs(0, 16).addBox(0.75F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r15 = new ModelRenderer((Model) this);
            cube_r15.setPos(-3.2F, 8.0F, 0.5F);
            this.RightBoot.addChild(cube_r15);
            setRotationAngle(cube_r15, 0.0F, 0.0F, -0.2618F);
            cube_r15.texOffs(0, 27).addBox(-0.75F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r16 = new ModelRenderer((Model) this);
            cube_r16.setPos(-3.0F, 8.0F, 0.5F);
            this.LeftBoot.addChild(cube_r16);
            setRotationAngle(cube_r16, 0.0F, 0.0F, -0.2618F);
            cube_r16.texOffs(30, 18).addBox(-0.75F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r17 = new ModelRenderer((Model) this);
            cube_r17.setPos(3.0F, 8.0F, 0.5F);
            this.LeftBoot.addChild(cube_r17);
            setRotationAngle(cube_r17, 0.0F, 0.0F, 0.2618F);
            cube_r17.texOffs(0, 38).addBox(1.0F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\AngelArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */