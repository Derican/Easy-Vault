package iskallia.vault.item.gear.specials;

import iskallia.vault.item.gear.model.VaultGearModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class CheeseHatModel<T extends LivingEntity> extends VaultGearModel<T> {
    public CheeseHatModel(float modelSize, EquipmentSlotType slotType) {
        super(modelSize, slotType);
        this.texWidth = 96;
        this.texHeight = 96;

        this.Head = new ModelRenderer((Model) this);
        this.Head.setPos(0.0F, 0.0F, 0.0F);
        this.Head.texOffs(38, 49).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(-0.1845F, -9.2768F, -1.6093F);
        this.Head.addChild(cube_r1);
        setRotationAngle(cube_r1, 2.7501F, 0.693F, 2.8387F);
        cube_r1.texOffs(38, 26).addBox(-0.2966F, -5.9666F, -14.6349F, 9.0F, 9.0F, 14.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(-0.1845F, -9.2768F, -1.6093F);
        this.Head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3488F, 0.0149F, -0.041F);
        cube_r2.texOffs(0, 0).addBox(-10.067F, -5.9651F, -0.7989F, 23.0F, 9.0F, 12.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(-0.1845F, -9.2768F, -1.6093F);
        this.Head.addChild(cube_r3);
        setRotationAngle(cube_r3, 2.7682F, 0.6344F, 2.8604F);
        cube_r3.texOffs(0, 21).addBox(-16.1878F, -5.9667F, -0.6391F, 16.0F, 9.0F, 10.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(-0.1845F, -9.2768F, -1.6093F);
        this.Head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.4002F, -0.7045F, -0.308F);
        cube_r4.texOffs(0, 40).addBox(-8.6758F, -5.9665F, -9.3418F, 9.0F, 9.0F, 10.0F, 0.0F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\specials\CheeseHatModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */