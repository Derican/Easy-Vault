package iskallia.vault.item.gear.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class BarbarianArmorModel {
    public static class Variant1<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant1(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 64;
            this.texHeight = isLayer2() ? 32 : 64;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 14).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(8.25F, -7.0F, -1.5F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, -1.0908F, 0.0F, 0.0F);
            cube_r1.texOffs(0, 0).addBox(-1.25F, -1.0F, -3.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(0, 4).addBox(-0.25F, -1.0F, -7.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r1.texOffs(28, 0).addBox(-0.25F, -1.0F, -5.5F, 2.0F, 2.0F, 4.0F, 0.0F, false);
            cube_r1.texOffs(36, 0).addBox(-3.25F, -1.0F, -1.5F, 5.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-8.25F, -7.0F, -1.5F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, -1.1781F, 0.0F, 0.0F);
            cube_r2.texOffs(0, 14).addBox(0.25F, -1.0F, -3.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r2.texOffs(0, 18).addBox(-0.75F, -1.0F, -7.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r2.texOffs(16, 30).addBox(-1.75F, -1.0F, -5.5F, 2.0F, 2.0F, 4.0F, 0.0F, false);
            cube_r2.texOffs(36, 6).addBox(-1.75F, -1.0F, -1.5F, 5.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(2.1367F, -4.5F, -5.5F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, -0.3927F, 0.0F);
            cube_r3.texOffs(0, 46).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(-2.1F, -4.5F, -5.5F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, 0.0F, 0.3927F, 0.0F);
            cube_r4.texOffs(48, 10).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(28, 26).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(32, 42).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.0F, 0.0F, 0.0F);
            this.RightArm.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.0F, 0.0F, -0.6981F);
            cube_r5.texOffs(0, 0).addBox(-7.0F, -6.0F, -4.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 38).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(32, 10).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 30).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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

    public static class Variant2<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant2(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(0.0F, -13.4726F, -11.8976F);
            this.Head.addChild(cube_r1);
            setRotationAngle(cube_r1, 1.0472F, 0.0F, 0.0F);
            cube_r1.texOffs(52, 62).addBox(-1.0F, -0.2F, -1.4F, 2.0F, 1.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(0.0F, -12.4726F, -10.3976F);
            this.Head.addChild(cube_r2);
            setRotationAngle(cube_r2, 1.5708F, 0.0F, 0.0F);
            cube_r2.texOffs(28, 62).addBox(-1.5F, -3.0F, -4.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(0.0F, -9.0F, -6.5F);
            this.Head.addChild(cube_r3);
            setRotationAngle(cube_r3, 1.1345F, 0.0F, 0.0F);
            cube_r3.texOffs(22, 49).addBox(-2.0F, -6.0F, -3.5F, 4.0F, 10.0F, 3.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(0.0F, -5.5F, -3.0F);
            this.Head.addChild(cube_r4);
            setRotationAngle(cube_r4, -1.0472F, 0.0F, 0.0F);
            cube_r4.texOffs(56, 17).addBox(-6.0F, -9.5F, -3.0F, 12.0F, 3.0F, 4.0F, 0.0F, false);
            cube_r4.texOffs(0, 0).addBox(-6.0F, -10.5F, 1.0F, 12.0F, 11.0F, 6.0F, 0.0F, false);
            cube_r4.texOffs(36, 0).addBox(-6.0F, -6.5F, -5.0F, 12.0F, 11.0F, 6.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(32, 17).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
            this.Body.texOffs(36, 55).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 5.0F, 1.0F, 0.0F, false);
            this.Body.texOffs(14, 62).addBox(-3.0F, 6.0F, -3.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(0.0F, 3.0F, 3.5F);
            this.Body.addChild(cube_r5);
            setRotationAngle(cube_r5, 0.4363F, 0.0F, 0.0F);
            cube_r5.texOffs(42, 62).addBox(-2.0F, 3.0F, -3.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(0, 62).addBox(-3.0F, 1.0F, -2.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(36, 49).addBox(-4.0F, -1.0F, -1.5F, 8.0F, 5.0F, 1.0F, 0.0F, false);
            cube_r5.texOffs(0, 49).addBox(-5.0F, -2.0F, -0.5F, 10.0F, 4.0F, 1.0F, 0.0F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(32, 33).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(16, 33).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

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

    public static class Variant3<T extends LivingEntity> extends VaultGearModel<T> {
        public Variant3(float modelSize, EquipmentSlotType slotType) {
            super(modelSize, slotType);
            this.texWidth = isLayer2() ? 64 : 128;
            this.texHeight = isLayer2() ? 32 : 128;

            this.Head = new ModelRenderer((Model) this);
            this.Head.setPos(0.0F, 0.0F, 0.0F);
            this.Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
            this.Head.texOffs(32, 0).addBox(-1.0F, -10.0F, -6.0F, 2.0F, 3.0F, 12.0F, 0.0F, false);
            this.Head.texOffs(22, 67).addBox(-0.5F, -12.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(18, 64).addBox(-0.5F, -14.0F, -3.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
            this.Head.texOffs(60, 0).addBox(-0.5F, -18.0F, -8.0F, 1.0F, 4.0F, 12.0F, 0.0F, false);
            this.Head.texOffs(16, 48).addBox(-0.5F, -14.0F, -1.0F, 1.0F, 2.0F, 8.0F, 0.0F, false);

            this.Body = new ModelRenderer((Model) this);
            this.Body.setPos(0.0F, 0.0F, 0.0F);
            this.Body.texOffs(56, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);

            this.RightArm = new ModelRenderer((Model) this);
            this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
            this.RightArm.texOffs(0, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.RightArm.texOffs(28, 16).addBox(-5.0F, -5.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.RightArm.texOffs(22, 64).addBox(-2.0F, -9.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r1 = new ModelRenderer((Model) this);
            cube_r1.setPos(-1.5F, -6.0F, 0.0F);
            this.RightArm.addChild(cube_r1);
            setRotationAngle(cube_r1, -2.3557F, 1.5091F, -2.3567F);
            cube_r1.texOffs(6, 64).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r2 = new ModelRenderer((Model) this);
            cube_r2.setPos(-7.0F, -3.25F, 0.0F);
            this.RightArm.addChild(cube_r2);
            setRotationAngle(cube_r2, 0.0F, 0.0F, -0.6545F);
            cube_r2.texOffs(12, 64).addBox(-1.0F, 0.25F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
            cube_r2.texOffs(34, 54).addBox(-1.0F, 2.25F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r3 = new ModelRenderer((Model) this);
            cube_r3.setPos(-6.5F, -8.3333F, 0.0F);
            this.RightArm.addChild(cube_r3);
            setRotationAngle(cube_r3, 0.0F, 0.0F, 0.3491F);
            cube_r3.texOffs(0, 64).addBox(1.5F, -6.1667F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
            cube_r3.texOffs(34, 48).addBox(0.5F, -2.1667F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
            cube_r3.texOffs(16, 58).addBox(0.5F, 1.8333F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

            this.LeftArm = new ModelRenderer((Model) this);
            this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
            this.LeftArm.texOffs(32, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
            this.LeftArm.texOffs(0, 16).addBox(-1.0F, -5.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
            this.LeftArm.texOffs(22, 64).addBox(1.5F, -9.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

            ModelRenderer cube_r4 = new ModelRenderer((Model) this);
            cube_r4.setPos(2.0F, -6.0F, 0.0F);
            this.LeftArm.addChild(cube_r4);
            setRotationAngle(cube_r4, -2.3557F, 1.5091F, -2.3567F);
            cube_r4.texOffs(6, 64).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r5 = new ModelRenderer((Model) this);
            cube_r5.setPos(-16.5F, -8.3333F, 0.0F);
            this.LeftArm.addChild(cube_r5);
            setRotationAngle(cube_r5, -3.1416F, 0.0F, 2.7053F);
            cube_r5.texOffs(0, 64).addBox(-19.5F, 3.8333F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
            cube_r5.texOffs(34, 48).addBox(-20.5F, 7.8333F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
            cube_r5.texOffs(16, 58).addBox(-20.5F, 11.8333F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

            ModelRenderer cube_r6 = new ModelRenderer((Model) this);
            cube_r6.setPos(-17.0F, -3.25F, 0.0F);
            this.LeftArm.addChild(cube_r6);
            setRotationAngle(cube_r6, 3.1416F, 0.0F, -2.618F);
            cube_r6.texOffs(34, 54).addBox(-22.0F, -9.75F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
            cube_r6.texOffs(12, 64).addBox(-22.0F, -11.75F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

            this.RightBoot = new ModelRenderer((Model) this);
            this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
            this.RightBoot.texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

            this.LeftBoot = new ModelRenderer((Model) this);
            this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
            this.LeftBoot.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\model\BarbarianArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */