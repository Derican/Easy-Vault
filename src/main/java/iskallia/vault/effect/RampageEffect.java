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

public class RampageEffect extends Effect {
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap;

    public RampageEffect(final EffectType typeIn, final int liquidColorIn, final ResourceLocation id) {
        super(typeIn, liquidColorIn);
        this.setRegistryName(id);
    }

    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }

    public void addAttributeModifiers(final LivingEntity livingEntity, final AttributeModifierManager attributeMap, final int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            this.removeExistingDamageBuff(player);
            final AbilityTree abilities = PlayerAbilitiesData.get((ServerWorld) player.getCommandSenderWorld()).getAbilities((PlayerEntity) player);
            final AbilityNode<?, ?> rampageNode = abilities.getNodeByName("Rampage");
            final RampageConfig cfg = (RampageConfig) rampageNode.getAbilityConfig();
            if (cfg != null) {
                final PlayerDamageHelper.DamageMultiplier multiplier = PlayerDamageHelper.applyMultiplier(player, cfg.getDamageIncrease(), PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                RampageEffect.multiplierMap.put(player.getUUID(), multiplier);
            }
        }
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    public void removeAttributeModifiers(final LivingEntity livingEntity, final AttributeModifierManager attributeMapIn, final int amplifier) {
        if (livingEntity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            this.removeExistingDamageBuff(player);
            PlayerAbilitiesData.setAbilityOnCooldown(player, "Rampage");
        }
        super.removeAttributeModifiers(livingEntity, attributeMapIn, amplifier);
    }

    private void removeExistingDamageBuff(final ServerPlayerEntity player) {
        final PlayerDamageHelper.DamageMultiplier existing = RampageEffect.multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }

    static {
        multiplierMap = new HashMap<UUID, PlayerDamageHelper.DamageMultiplier>();
    }
}
