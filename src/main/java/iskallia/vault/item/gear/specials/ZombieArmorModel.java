package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ZombieArmorModel<T extends LivingEntity> extends VaultGearModel<T> {
    public ZombieArmorModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = (64);
        this.texHeight = (32);
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        (this.Body = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Body.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 1.01f, false);
        final ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0f, 1.5f, 5.0f);
        this.Body.addChild(cube_r1);
        this.setRotationAngle(cube_r1, -0.9163f, 0.0f, 0.0f);
        cube_r1.texOffs(41, 1).addBox(-1.0f, 1.25f, -2.0f, 2.0f, 1.0f, 4.0f, 0.0f, false);
        (this.RightArm = new ModelRenderer(this)).setPos(-5.0f, 2.0f, 0.0f);
        this.RightArm.texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftArm = new ModelRenderer(this)).setPos(5.0f, 2.0f, 0.0f);
        this.LeftArm.texOffs(40, 16).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
        (this.RightBoot = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightBoot.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, false);
        (this.LeftBoot = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftBoot.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 1.0f, true);
        (this.Belt = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Belt.texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, 0.51f, false);
        (this.RightLeg = new ModelRenderer(this)).setPos(-1.9f, 12.0f, 0.0f);
        this.RightLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, false);
        (this.LeftLeg = new ModelRenderer(this)).setPos(1.9f, 12.0f, 0.0f);
        this.LeftLeg.texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, 0.5f, true);
    }
}
