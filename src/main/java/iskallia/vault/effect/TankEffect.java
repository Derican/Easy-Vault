package iskallia.vault.effect;

import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;


public class TankEffect
        extends Effect {
    public TankEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);
        setRegistryName(id);
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMapIn, int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            PlayerAbilitiesData.setAbilityOnCooldown(player, "Tank");
        }

        super.removeAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\TankEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */