// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.effect;

import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class ExecuteEffect extends Effect
{
    public ExecuteEffect(final EffectType typeIn, final int liquidColorIn, final ResourceLocation id) {
        super(typeIn, liquidColorIn);
        this.setRegistryName(id);
    }
    
    public void removeAttributeModifiers(final LivingEntity entityLiving, final AttributeModifierManager attributeMapIn, final int amplifier) {
        super.removeAttributeModifiers(entityLiving, attributeMapIn, amplifier);
        if (entityLiving instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)entityLiving;
            PlayerAbilitiesData.setAbilityOnCooldown(player, "Execute");
        }
    }
    
    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }
}
