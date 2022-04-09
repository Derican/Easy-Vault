// 
// Decompiled by Procyon v0.6.0
// 

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

public class GhostWalkEffect extends Effect
{
    private AttributeModifier[] attributeModifiers;
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap;
    
    public GhostWalkEffect(final EffectType typeIn, final int liquidColorIn, final ResourceLocation id) {
        super(typeIn, liquidColorIn);
        this.attributeModifiers = null;
        this.setRegistryName(id);
    }
    
    private void initializeAttributeModifiers() {
        this.attributeModifiers = new AttributeModifier[ModConfigs.ABILITIES.GHOST_WALK.getMaxLevel()];
        for (int i = 0; i < this.attributeModifiers.length; ++i) {
            this.attributeModifiers[i] = new AttributeModifier(this.getRegistryName().toString(), (double)((i + 1) * 0.1f), AttributeModifier.Operation.ADDITION);
        }
    }
    
    public boolean isDurationEffectTick(final int duration, final int amplifier) {
        return true;
    }
    
    public void addAttributeModifiers(final LivingEntity livingEntity, final AttributeModifierManager attributeMap, final int amplifier) {
        if (this.attributeModifiers == null) {
            this.initializeAttributeModifiers();
        }
        final ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null) {
            final AttributeModifier attributeModifier = this.attributeModifiers[MathHelper.clamp(amplifier + 1, 0, this.attributeModifiers.length - 1)];
            movementSpeed.addTransientModifier(attributeModifier);
        }
        if (livingEntity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)livingEntity;
            this.removeExistingDamageBuff(player);
            final AbilityTree abilities = PlayerAbilitiesData.get((ServerWorld)player.getCommandSenderWorld()).getAbilities((PlayerEntity)player);
            final AbilityNode<?, ?> ghostWalkNode = abilities.getNodeByName("Ghost Walk");
            if (ghostWalkNode.getAbilityConfig() instanceof GhostWalkDamageConfig) {
                final float dmgIncrease = ((GhostWalkDamageConfig)ghostWalkNode.getAbilityConfig()).getDamageMultiplierInGhostWalk();
                final PlayerDamageHelper.DamageMultiplier multiplier = PlayerDamageHelper.applyMultiplier(player, dmgIncrease, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                GhostWalkEffect.multiplierMap.put(player.getUUID(), multiplier);
            }
        }
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }
    
    public void removeAttributeModifiers(final LivingEntity livingEntity, final AttributeModifierManager attributeMap, final int amplifier) {
        final ModifiableAttributeInstance movementSpeed = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null && this.attributeModifiers != null) {
            final AttributeModifier attributeModifier = this.attributeModifiers[MathHelper.clamp(amplifier + 1, 0, this.attributeModifiers.length - 1)];
            movementSpeed.removeModifier(attributeModifier.getId());
        }
        if (livingEntity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)livingEntity;
            this.removeExistingDamageBuff(player);
            PlayerAbilitiesData.setAbilityOnCooldown(player, "Ghost Walk");
        }
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }
    
    private void removeExistingDamageBuff(final ServerPlayerEntity player) {
        final PlayerDamageHelper.DamageMultiplier existing = GhostWalkEffect.multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }
    
    static {
        multiplierMap = new HashMap<UUID, PlayerDamageHelper.DamageMultiplier>();
    }
}
