package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CheeseHatModel<T extends LivingEntity> extends VaultGearModel<T> {
    public CheeseHatModel(final float modelSize, final EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = 96;
        this.texHeight = 96;
        (this.Head = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.Head.texOffs(38, 49).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 1.0f, false);
        final ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-0.1845f, -9.2768f, -1.6093f);
        this.Head.addChild(cube_r1);
        this.setRotationAngle(cube_r1, 2.7501f, 0.693f, 2.8387f);
        cube_r1.texOffs(38, 26).addBox(-0.2966f, -5.9666f, -14.6349f, 9.0f, 9.0f, 14.0f, 0.0f, false);
        final ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-0.1845f, -9.2768f, -1.6093f);
        this.Head.addChild(cube_r2);
        this.setRotationAngle(cube_r2, 0.3488f, 0.0149f, -0.041f);
        cube_r2.texOffs(0, 0).addBox(-10.067f, -5.9651f, -0.7989f, 23.0f, 9.0f, 12.0f, 0.0f, false);
        final ModelRenderer cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.1845f, -9.2768f, -1.6093f);
        this.Head.addChild(cube_r3);
        this.setRotationAngle(cube_r3, 2.7682f, 0.6344f, 2.8604f);
        cube_r3.texOffs(0, 21).addBox(-16.1878f, -5.9667f, -0.6391f, 16.0f, 9.0f, 10.0f, 0.0f, false);
        final ModelRenderer cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-0.1845f, -9.2768f, -1.6093f);
        this.Head.addChild(cube_r4);
        this.setRotationAngle(cube_r4, 0.4002f, -0.7045f, -0.308f);
        cube_r4.texOffs(0, 40).addBox(-8.6758f, -5.9665f, -9.3418f, 9.0f, 9.0f, 10.0f, 0.0f, false);
    }
}
