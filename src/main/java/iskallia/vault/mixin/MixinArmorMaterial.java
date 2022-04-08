package iskallia.vault.mixin;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ArmorMaterial.class})
public class MixinArmorMaterial {
    @Inject(method = {"getToughness"}, at = {@At("HEAD")}, cancellable = true)
    public void getToughness(CallbackInfoReturnable<Float> ci) {
        ArmorMaterial material = (ArmorMaterial) this;

        if (material == ArmorMaterial.LEATHER || material == ArmorMaterial.CHAIN || material == ArmorMaterial.GOLD || material == ArmorMaterial.IRON || material == ArmorMaterial.DIAMOND || material == ArmorMaterial.NETHERITE) {
            ci.setReturnValue(Float.valueOf(0.0F));
        }
    }

    @Inject(method = {"getKnockbackResistance"}, at = {@At("HEAD")}, cancellable = true)
    public void getKockbackResistance(CallbackInfoReturnable<Float> ci) {
        ArmorMaterial material = (ArmorMaterial) this;

        if (material == ArmorMaterial.LEATHER || material == ArmorMaterial.CHAIN || material == ArmorMaterial.GOLD || material == ArmorMaterial.IRON || material == ArmorMaterial.DIAMOND || material == ArmorMaterial.NETHERITE) {
            ci.setReturnValue(Float.valueOf(0.0F));
        }
    }

    @Inject(method = {"getDamageReductionAmount"}, at = {@At("HEAD")}, cancellable = true)
    public void getDamageReductionAmount(EquipmentSlotType slot, CallbackInfoReturnable<Integer> ci) {
        switch ((ArmorMaterial) this) {
            case LEATHER:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                }
                break;
            case CHAIN:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                }
                break;
            case GOLD:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(1));
                        break;
                }
                break;
            case IRON:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                }
                break;
            case DIAMOND:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(3));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(3));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(2));
                        break;
                }
                break;
            case NETHERITE:
                switch (slot) {
                    case LEATHER:
                        ci.setReturnValue(Integer.valueOf(3));
                        break;
                    case CHAIN:
                        ci.setReturnValue(Integer.valueOf(4));
                        break;
                    case GOLD:
                        ci.setReturnValue(Integer.valueOf(4));
                        break;
                    case IRON:
                        ci.setReturnValue(Integer.valueOf(3));
                        break;
                }

                break;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinArmorMaterial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */