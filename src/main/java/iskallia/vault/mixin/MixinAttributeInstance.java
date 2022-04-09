// 
// Decompiled by Procyon v0.6.0
// 

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

@Mixin({ ModifiableAttributeInstance.class })
public abstract class MixinAttributeInstance
{
    @Shadow
    protected abstract Collection<AttributeModifier> getModifiersOrEmpty(final AttributeModifier.Operation p0);
    
    @Redirect(method = { "calculateValue" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/Attribute;sanitizeValue(D)D"))
    private double computeValue(final Attribute attribute, double value) {
        if (attribute == Attributes.ARMOR) {
            return MathHelper.clamp(value, 0.0, 100.0);
        }
        if (attribute == Attributes.MAX_HEALTH) {
            boolean hasHealthSet = false;
            for (final AttributeModifier modifier : this.getModifiersOrEmpty(FrenzyModifier.FRENZY_HEALTH_OPERATION)) {
                if (FrenzyModifier.isFrenzyHealthModifier(modifier.getId())) {
                    hasHealthSet = true;
                    break;
                }
            }
            if (hasHealthSet) {
                return 1.0;
            }
        }
        return attribute.sanitizeValue(value);
    }
}
