package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class TestDummyArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public TestDummyArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 128;
        this.texHeight = isLayer2() ? 32 : 128;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        this.Head.texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);
        this.Head.texOffs(0, 15).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 7.0F, 10.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(2.5F, 0.661F, -8.6148F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.5661F, -0.5338F, -0.3127F);
        cube_r1.texOffs(30, 19).addBox(1.0F, -2.0F, -4.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(50, 5).addBox(1.5F, 0.0F, -2.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, -16.5F, 0.0F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.5672F, 0.0F, -0.3927F);
        cube_r2.texOffs(30, 22).addBox(1.0F, 3.5F, 0.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(32, 33).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
        this.Body.texOffs(30, 15).addBox(-11.0F, 0.0F, 3.0F, 22.0F, 2.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(0, 48).addBox(-1.0F, 2.0F, 3.0F, 2.0F, 23.0F, 2.0F, 0.0F, false);
        this.Body.texOffs(0, 32).addBox(-1.0F, 2.0F, -5.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(2.0F, 3.0F, -3.5F);
        this.Body.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.7854F);
        cube_r3.texOffs(0, 15).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(-2.0F, 3.0F, -3.5F);
        this.Body.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.texOffs(0, 20).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(56, 33).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(-4.5F, 8.5F, 0.0F);
        this.RightArm.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3927F);
        cube_r5.texOffs(42, 0).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(-1.0F, 8.5F, -3.5F);
        this.RightArm.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.3927F, 0.0F, 0.0F);
        cube_r6.texOffs(56, 55).addBox(-3.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(-1.0F, 8.5F, 3.5F);
        this.RightArm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.3927F, 0.0F, 0.0F);
        cube_r7.texOffs(58, 0).addBox(-3.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(40, 49).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(1.0F, 8.5F, -3.5F);
        this.LeftArm.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.3927F, 0.0F, 0.0F);
        cube_r8.texOffs(56, 49).addBox(-3.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(1.0F, 8.5F, 3.5F);
        this.LeftArm.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.3927F, 0.0F, 0.0F);
        cube_r9.texOffs(60, 19).addBox(-3.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(4.5F, 8.5F, 0.0F);
        this.LeftArm.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.3927F);
        cube_r10.texOffs(0, 0).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 5.0F, 6.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(24, 49).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(8, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\TestDummyArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */