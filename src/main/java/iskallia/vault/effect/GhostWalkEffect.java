package iskallia.vault.effect;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.GhostWalkDamageConfig;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GhostWalkEffect
        extends Effect {
    private AttributeModifier[] attributeModifiers = null;

    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap = new HashMap<>();

    public GhostWalkEffect(EffectType typeIn, int liquidColorIn, ResourceLocation id) {
        super(typeIn, liquidColorIn);
        setRegistryName(id);
    }

    private void initializeAttributeModifiers() {
        this.attributeModifiers = new AttributeModifier[ModConfigs.ABILITIES.GHOST_WALK.getMaxLevel()];
        for (int i = 0; i < this.attributeModifiers.length; i++) {
            this.attributeModifiers[i] = new AttributeModifier(getRegistryName().toString(), ((i + 1) * 0.1F), AttributeModifier.Operation.ADDITION);
        }
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    public void addAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMap, int amplifier) {
        if (this.attributeModifiers == null) {
            initializeAttributeModifiers();
        }

        ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            AttributeModifier attributeModifier = this.attributeModifiers[MathHelper.clamp(amplifier + 1, 0, this.attributeModifiers.length - 1)];
            movementSpeed.addTransientModifier(attributeModifier);
        }

        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            removeExistingDamageBuff(player);

            AbilityTree abilities = PlayerAbilitiesData.get((ServerWorld) player.getCommandSenderWorld()).getAbilities((PlayerEntity) player);
            AbilityNode<?, ?> ghostWalkNode = abilities.getNodeByName("Ghost Walk");
            if (ghostWalkNode.getAbilityConfig() instanceof GhostWalkDamageConfig) {
                float dmgIncrease = ((GhostWalkDamageConfig) ghostWalkNode.getAbilityConfig()).getDamageMultiplierInGhostWalk();
                PlayerDamageHelper.DamageMultiplier multiplier = PlayerDamageHelper.applyMultiplier(player, dmgIncrease, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                multiplierMap.put(player.getUUID(), multiplier);
            }
        }
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }


    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeMap, int amplifier) {
        ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null && this.attributeModifiers != null) {
            AttributeModifier attributeModifier = this.attributeModifiers[MathHelper.clamp(amplifier + 1, 0, this.attributeModifiers.length - 1)];
            movementSpeed.removeModifier(attributeModifier.getId());
        }

        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

            removeExistingDamageBuff(player);

            PlayerAbilitiesData.setAbilityOnCooldown(player, "Ghost Walk");
        }

        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    private void removeExistingDamageBuff(ServerPlayerEntity player) {
        PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(player.getUUID());
        if (existing != null)
            PlayerDamageHelper.removeMultiplier(player, existing);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\effect\GhostWalkEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */