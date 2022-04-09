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

public class TankEffect extends Effect
{
    public TankEffect(final EffectType typeIn, final int liquidColorIn, final ResourceLocation id) {
        super(typeIn, liquidColorIn);
        this.setRegistryName(id);
    }
    
    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }
    
    public void removeAttributeModifiers(final LivingEntity livingEntity, final AttributeModifierManager attributeMapIn, final int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)livingEntity;
            PlayerAbilitiesData.setAbilityOnCooldown(player, "Tank");
        }
        super.removeAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }
}
