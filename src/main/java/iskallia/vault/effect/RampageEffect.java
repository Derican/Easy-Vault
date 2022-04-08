package iskallia.vault.effect;

import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.RampageConfig;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class RampageEffect
        extends Effect {
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap = new HashMap<>();

    public RampageEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);

        setRegistryName(id);
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    public void addAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMap, int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            removeExistingDamageBuff(player);

            AbilityTree abilities = PlayerAbilitiesData.get((ServerWorld) player.getCommandSenderWorld()).getAbilities((PlayerEntity) player);
            AbilityNode<?, ?> rampageNode = abilities.getNodeByName("Rampage");
            RampageConfig cfg = (RampageConfig) rampageNode.getAbilityConfig();
            if (cfg != null) {
                PlayerDamageHelper.DamageMultiplier multiplier = PlayerDamageHelper.applyMultiplier(player, cfg.getDamageIncrease(), PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                multiplierMap.put(player.getUUID(), multiplier);
            }
        }
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }


    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMapIn, int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            removeExistingDamageBuff(player);

            PlayerAbilitiesData.setAbilityOnCooldown(player, "Rampage");
        }

        super.removeAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }

    private void removeExistingDamageBuff(ServerPlayerEntity player) {
        PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(player.getUUID());
        if (existing != null)
            PlayerDamageHelper.removeMultiplier(player, existing);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\RampageEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */