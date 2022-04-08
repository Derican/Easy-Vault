package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class SkallibombaArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public SkallibombaArmorModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = isLayer2() ? 64 : 256;
        this.texHeight = isLayer2() ? 64 : 256;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(111, 104).addBox(-5.0F, -27.0F, -6.0F, 10.0F, 24.0F, 11.0F, 0.0F, false);
        this.Head.texOffs(100, 28).addBox(-5.0F, -32.0F, -25.0F, 10.0F, 5.0F, 16.0F, 0.0F, false);
        this.Head.texOffs(106, 87).addBox(-6.0F, -34.0F, -34.0F, 12.0F, 7.0F, 9.0F, 0.0F, false);
        this.Head.texOffs(85, 17).addBox(-5.0F, -36.0F, -34.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.Head.texOffs(98, 0).addBox(-5.0F, -41.0F, -1.0F, 2.0F, 7.0F, 5.0F, 0.0F, false);
        this.Head.texOffs(0, 16).addBox(3.0F, -36.0F, -34.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.Head.texOffs(76, 10).addBox(3.0F, -41.0F, -1.0F, 2.0F, 7.0F, 5.0F, 0.0F, false);
        this.Head.texOffs(15, 18).addBox(3.0F, -27.0F, -33.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(9, 16).addBox(-5.0F, -27.0F, -33.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.Head.texOffs(74, 59).addBox(-8.0F, -34.0F, -9.0F, 16.0F, 12.0F, 16.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(-0.5F, -22.5F, -17.0F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.3491F, 0.0F, 0.0F);
        cube_r1.texOffs(34, 115).addBox(-3.0F, 0.5F, -8.0F, 7.0F, 3.0F, 16.0F, 0.0F, false);

        this.Body = new ModelRenderer((Model) this);
        this.Body.setPos(0.0F, 0.0F, 0.0F);
        this.Body.texOffs(0, 0).addBox(-13.0F, -10.0F, -14.0F, 26.0F, 35.0F, 24.0F, 0.0F, false);
        this.Body.texOffs(0, 59).addBox(-13.0F, -3.0F, -25.0F, 26.0F, 28.0F, 11.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-5.5F, -10.0F, 15.0F);
        this.Body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3491F, -0.3054F, 0.0F);
        cube_r2.texOffs(0, 126).addBox(-0.5F, -5.0F, -7.0F, 1.0F, 10.0F, 14.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(5.5F, -10.0F, 16.0F);
        this.Body.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.2618F, 0.3054F, 0.0F);
        cube_r3.texOffs(92, 130).addBox(-0.5F, -5.0F, -7.0F, 1.0F, 10.0F, 14.0F, 0.0F, false);

        this.RightArm = new ModelRenderer((Model) this);
        this.RightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.RightArm.texOffs(122, 49).addBox(-12.0F, -9.0F, -9.0F, 7.0F, 10.0F, 12.0F, 0.0F, false);
        this.RightArm.texOffs(63, 59).addBox(-12.0F, -3.0F, -15.0F, 5.0F, 4.0F, 6.0F, 0.0F, false);

        this.LeftArm = new ModelRenderer((Model) this);
        this.LeftArm.setPos(5.0F, 2.0F, 0.0F);
        this.LeftArm.texOffs(68, 122).addBox(6.0F, -9.0F, -9.0F, 7.0F, 10.0F, 12.0F, 0.0F, false);
        this.LeftArm.texOffs(76, 0).addBox(8.0F, -3.0F, -15.0F, 5.0F, 4.0F, 6.0F, 0.0F, false);

        this.RightBoot = new ModelRenderer((Model) this);
        this.RightBoot.setPos(-1.9F, 12.0F, 0.0F);
        this.RightBoot.texOffs(100, 0).addBox(-22.1F, -3.0F, -9.0F, 13.0F, 16.0F, 12.0F, 0.0F, false);
        this.RightBoot.texOffs(138, 0).addBox(-22.1F, 10.0F, -15.0F, 13.0F, 3.0F, 6.0F, 0.0F, false);

        this.LeftBoot = new ModelRenderer((Model) this);
        this.LeftBoot.setPos(1.9F, 12.0F, 0.0F);
        this.LeftBoot.texOffs(136, 28).addBox(9.1F, 10.0F, -15.0F, 13.0F, 3.0F, 6.0F, 0.0F, false);
        this.LeftBoot.texOffs(0, 98).addBox(9.1F, -3.0F, -9.0F, 13.0F, 16.0F, 12.0F, 0.0F, false);

        this.Belt = new ModelRenderer((Model) this);
        this.Belt.setPos(0.0F, 0.0F, 0.0F);
        this.Belt.texOffs(0, 0).addBox(-9.0F, 14.0F, 4.0F, 18.0F, 8.0F, 12.0F, 0.0F, false);
        this.Belt.texOffs(0, 34).addBox(-1.0F, 11.0F, 6.0F, 2.0F, 3.0F, 8.0F, 0.0F, false);
        this.Belt.texOffs(31, 20).addBox(-1.0F, 13.0F, 17.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.Belt.texOffs(12, 34).addBox(-1.0F, 17.0F, 23.0F, 2.0F, 1.0F, 5.0F, 0.0F, false);
        this.Belt.texOffs(0, 20).addBox(-6.0F, 15.0F, 16.0F, 12.0F, 7.0F, 7.0F, 0.0F, false);
        this.Belt.texOffs(31, 27).addBox(-4.0F, 18.0F, 23.0F, 8.0F, 4.0F, 7.0F, 0.0F, false);

        this.RightLeg = new ModelRenderer((Model) this);
        this.RightLeg.setPos(-1.9F, 12.0F, 0.0F);

        this.LeftLeg = new ModelRenderer((Model) this);
        this.LeftLeg.setPos(1.9F, 12.0F, 0.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\SkallibombaArmorModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */