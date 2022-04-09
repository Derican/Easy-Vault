// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ArmorMaterial.class })
public class MixinArmorMaterial
{
    @Inject(method = { "getToughness" }, at = { @At("HEAD") }, cancellable = true)
    public void getToughness(final CallbackInfoReturnable<Float> ci) {
        final ArmorMaterial material = (ArmorMaterial)(Object)this;
        if (material == ArmorMaterial.LEATHER || material == ArmorMaterial.CHAIN || material == ArmorMaterial.GOLD || material == ArmorMaterial.IRON || material == ArmorMaterial.DIAMOND || material == ArmorMaterial.NETHERITE) {
            ci.setReturnValue(0.0f);
        }
    }
    
    @Inject(method = { "getKnockbackResistance" }, at = { @At("HEAD") }, cancellable = true)
    public void getKockbackResistance(final CallbackInfoReturnable<Float> ci) {
        final ArmorMaterial material = (ArmorMaterial)(Object)this;
        if (material == ArmorMaterial.LEATHER || material == ArmorMaterial.CHAIN || material == ArmorMaterial.GOLD || material == ArmorMaterial.IRON || material == ArmorMaterial.DIAMOND || material == ArmorMaterial.NETHERITE) {
            ci.setReturnValue(0.0f);
        }
    }
    
    @Inject(method = { "getDefenseForSlot" }, at = { @At("HEAD") }, cancellable = true)
    public void getDefenseForSlot(final EquipmentSlotType slot, final CallbackInfoReturnable<Integer> ci) {
        Label_0549: {
            switch ((ArmorMaterial)(Object)this) {
                case LEATHER: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(1);
                            break;
                        }
                        case CHEST: {
                            ci.setReturnValue(1);
                            break;
                        }
                        case LEGS: {
                            ci.setReturnValue(1);
                            break;
                        }
                        case FEET: {
                            ci.setReturnValue(1);
                            break;
                        }
                    }
                    break;
                }
                case CHAIN: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(1);
                            break;
                        }
                        case CHEST: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case LEGS: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case FEET: {
                            ci.setReturnValue(1);
                            break;
                        }
                    }
                    break;
                }
                case GOLD: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case CHEST: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case LEGS: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case FEET: {
                            ci.setReturnValue(1);
                            break;
                        }
                    }
                    break;
                }
                case IRON: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case CHEST: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case LEGS: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case FEET: {
                            ci.setReturnValue(2);
                            break;
                        }
                    }
                    break;
                }
                case DIAMOND: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(2);
                            break;
                        }
                        case CHEST: {
                            ci.setReturnValue(3);
                            break;
                        }
                        case LEGS: {
                            ci.setReturnValue(3);
                            break;
                        }
                        case FEET: {
                            ci.setReturnValue(2);
                            break;
                        }
                    }
                    break;
                }
                case NETHERITE: {
                    switch (slot) {
                        case HEAD: {
                            ci.setReturnValue(3);
                            break Label_0549;
                        }
                        case CHEST: {
                            ci.setReturnValue(4);
                            break Label_0549;
                        }
                        case LEGS: {
                            ci.setReturnValue(4);
                            break Label_0549;
                        }
                        case FEET: {
                            ci.setReturnValue(3);
                            break Label_0549;
                        }
                    }
                    break;
                }
            }
        }
    }
}
