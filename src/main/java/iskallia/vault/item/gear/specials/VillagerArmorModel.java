package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class VillagerArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public VillagerArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (this.isLayer2() ? 64 : 128);
        this.texHeight = (this.isLayer2() ? 32 : 128);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 0).addBox(-4.0f, -11.0f, -4.0f, 8.0f, 11.0f, 8.0f, 1.0f, false);
        this.Head.texOffs(22, 30).addBox(-4.0f, -14.0f, -4.0f, 8.0f, 2.0f, 8.0f, 0.0f, false);
        this.Head.texOffs(32, 0).addBox(-3.0f, -15.0f, -3.0f, 6.0f, 3.0f, 6.0f, 0.0f, false);
        this.Head.texOffs(0, 0).addBox(-0.5f, -16.0f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, false);
        this.Head.texOffs(44, 40).addBox(-1.5f, -4.0f, -7.0f, 3.0f, 6.0f, 2.0f, 0.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(48, 50).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        this.Body.texOffs(30, 11).addBox(-5.0f, 1.0f, -4.0f, 10.0f, 4.0f, 8.0f, 0.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.5f, 11.5f, 3.0f);
        this.Body.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 0.3927f, 0.0f, 0.0f);
        cube_r1.texOffs(0, 19).addBox(-7.5f, -9.5f, 3.0f, 15.0f, 19.0f, 0.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(32, 54).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.RightArm.texOffs(26, 40).addBox(-5.0f, -4.0f, -4.0f, 5.0f, 6.0f, 8.0f, 0.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(54, 23).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        this.LeftArm.texOffs(0, 38).addBox(0.0f, -4.0f, -4.0f, 5.0f, 6.0f, 8.0f, 0.0f, false);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(16, 54).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(0, 52).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
