package iskallia.vault.effect;

import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;


public class ExecuteEffect
        extends Effect {
    public ExecuteEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);
        setRegistryName(id);
    }


    public void removeAttributeModifiers(LivingEntity entityLiving, AttributeModifierManager attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLiving, attributeMapIn, amplifier);

        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;

            PlayerAbilitiesData.setAbilityOnCooldown(player, "Execute");
        }
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\ExecuteEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */