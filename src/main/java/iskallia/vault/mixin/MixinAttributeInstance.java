package iskallia.vault.mixin;

import iskallia.vault.world.vault.modifier.FrenzyModifier;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin({ModifiableAttributeInstance.class})
public abstract class MixinAttributeInstance {
    @Shadow
    protected abstract Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation paramOperation);

    @Redirect(method = {"computeValue"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/Attribute;clampValue(D)D"))
    private double computeValue(Attribute attribute, double value) {
        if (attribute == Attributes.ARMOR) {
            return MathHelper.clamp(value, 0.0D, 100.0D);
        }

        if (attribute == Attributes.MAX_HEALTH) {
            boolean hasHealthSet = false;
            for (AttributeModifier modifier : getModifiersOrEmpty(FrenzyModifier.FRENZY_HEALTH_OPERATION)) {
                if (FrenzyModifier.isFrenzyHealthModifier(modifier.getId())) {
                    hasHealthSet = true;
                    break;
                }
            }
            if (hasHealthSet) {
                return 1.0D;
            }
        }

        return attribute.sanitizeValue(value);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinAttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */