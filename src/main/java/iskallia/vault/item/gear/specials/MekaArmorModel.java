package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class MekaArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public MekaArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 32 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(-0.149F, -9.338301F, 0.8529F);
        this.Head.texOffs(0, 24).addBox(-3.851F, -6.6617002F, -4.8529F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(32, 67).addBox(-1.851F, -5.6617002F, 4.1471F, 4.0F, 11.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(-0.0199F, -0.3451F, -0.3529F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 1.5708F, 0.0F);
        cube_r1.texOffs(12, 48).addBox(-5.3311F, -8.8166F, 1.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(28, 61).addBox(-5.3311F, -8.8166F, -2.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(42, 37).addBox(-5.3311F, -9.3166F, -1.0F, 11.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(0, 24).addBox(4.9189F, 0.68340015F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(0, 40).addBox(-5.8311F, -7.3166F, -5.0F, 1.0F, 9.0F, 10.0F, 0.0F, false);
        cube_r1.texOffs(0, 13).addBox(-5.3311F, -8.3166F, -5.0F, 11.0F, 1.0F, 10.0F, 0.0F, false);
        cube_r1.texOffs(70, 9).addBox(-5.3311F, -2.3165998F, -6.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(52, 16).addBox(-5.3311F, -7.3166F, -6.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(40, 0).addBox(-1.3311F, -7.3166F, -6.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(66, 0).addBox(-1.3311F, -7.3166F, 5.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(74, 14).addBox(-5.3311F, -7.3166F, 5.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(70, 51).addBox(-5.3311F, -2.3165998F, 5.0F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(-5.0811F, 1.6834002F, -6.0F, 11.0F, 1.0F, 12.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-0.0199F, -0.8451F, -0.3529F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, -1.5708F, 1.405F, -1.5708F);
        cube_r2.texOffs(50, 22).addBox(5.652F, -5.1296997F, -5.0F, 1.0F, 5.0F, 10.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(-0.0199F, -0.8451F, -0.3529F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.5708F, 1.3788F, 1.5708F);
        cube_r3.texOffs(38, 53).addBox(5.2676F, -2.4659F, -5.0F, 1.0F, 4.0F, 10.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(50, 53).addBox(-4.0F, 1.0F, 3.0F, 8.0F, 5.0F, 4.0F, 0.0F, false);
        this.Body.texOffs(68, 46).addBox(-3.5F, 2.0F, -4.0F, 7.0F, 4.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(40, 53).addBox(-2.25F, 6.0F, -3.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(38, 13).addBox(0.25F, 6.0F, -3.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(46, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(18, 40).addBox(-1.0F, 9.0F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(6, 0).addBox(1.5F, 9.0F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        this.Body.texOffs(0, 28).addBox(-3.5F, 9.0F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(0.3729F, 7.9218F, 4.0F);
        this.Body.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.3927F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 40).addBox(-2.0F, -2.5F, -1.0F, 3.0F, 7.0F, 2.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(60, 62).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightArm.texOffs(34, 16).addBox(-6.0F, 4.0F, -4.0F, 5.0F, 8.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(70, 0).addBox(-5.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        this.RightArm.texOffs(12, 40).addBox(-6.25F, 8.25F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(-6.0F, 3.0F, 0.0F);
        this.RightArm.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.1745F);
        cube_r5.texOffs(74, 32).addBox(-0.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(-3.5F, -2.5F, 0.0F);
        this.RightArm.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.1745F);
        cube_r6.texOffs(22, 48).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(24, 24).addBox(5.25F, 8.25F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.LeftArm.texOffs(60, 37).addBox(2.0F, 2.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(24, 32).addBox(1.0F, 4.0F, -4.0F, 5.0F, 8.0F, 8.0F, 0.0F, false);
        this.LeftArm.texOffs(62, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(3.5F, -2.5F, 0.0F);
        this.LeftArm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.1745F);
        cube_r7.texOffs(42, 40).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 8.0F, 0.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(6.0F, 3.0F, 0.0F);
        this.LeftArm.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.1745F);
        cube_r8.texOffs(48, 67).addBox(-1.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(16, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
        this.RightBoot.texOffs(70, 3).addBox(-1.0F, 4.0F, -4.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.RightBoot.texOffs(42, 32).addBox(-1.0F, 4.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(3.5F, 10.0F, 0.0F);
        this.RightBoot.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, 0.1309F);
        cube_r9.texOffs(32, 13).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(-3.5F, 10.0F, 0.0F);
        this.RightBoot.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.1309F);
        cube_r10.texOffs(34, 0).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r11 = new ModelRenderer((Model) this);
        cube_r11.setPos(0.0F, 10.0F, 3.5F);
        this.RightBoot.addChild(cube_r11);
        setRotationAngle(cube_r11, -0.1309F, 0.0F, 0.0F);
        cube_r11.texOffs(76, 63).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r12 = new ModelRenderer((Model) this);
        cube_r12.setPos(0.0F, 10.0F, -3.5F);
        this.RightBoot.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.1309F, 0.0F, 0.0F);
        cube_r12.texOffs(76, 70).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(60, 40).addBox(-0.8F, 4.0F, -4.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.LeftBoot.texOffs(22, 50).addBox(-0.8F, 4.0F, 3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        this.LeftBoot.texOffs(0, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r13 = new ModelRenderer((Model) this);
        cube_r13.setPos(0.2F, 10.0F, 3.5F);
        this.LeftBoot.addChild(cube_r13);
        setRotationAngle(cube_r13, -0.1309F, 0.0F, 0.0F);
        cube_r13.texOffs(74, 56).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r14 = new ModelRenderer((Model) this);
        cube_r14.setPos(0.2F, 10.0F, -3.5F);
        this.LeftBoot.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.1309F, 0.0F, 0.0F);
        cube_r14.texOffs(0, 75).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r15 = new ModelRenderer((Model) this);
        cube_r15.setPos(-3.3F, 10.0F, 0.0F);
        this.LeftBoot.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, -0.1309F);
        cube_r15.texOffs(0, 0).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 0.0F, false);

        ModelRenderer cube_r16 = new ModelRenderer((Model) this);
        cube_r16.setPos(3.45F, 10.0F, 0.0F);
        this.LeftBoot.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, 0.1309F);
        cube_r16.texOffs(0, 13).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, 0.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.51F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.RightLeg.texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\MekaArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */