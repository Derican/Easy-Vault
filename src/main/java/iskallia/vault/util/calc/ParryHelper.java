// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.AuraManager;
import iskallia.vault.aura.type.ParryAuraConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModEffects;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.sub.GhostWalkParryConfig;
import iskallia.vault.skill.ability.config.sub.TankParryConfig;
import iskallia.vault.skill.set.DreamSet;
import iskallia.vault.skill.set.NinjaSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.ArchetypeTalent;
import iskallia.vault.skill.talent.type.archetype.WardTalent;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.modifier.StatModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Function;

public class ParryHelper
{
    public static float getPlayerParryChance(final ServerPlayerEntity player) {
        return MathHelper.clamp(getPlayerParryChanceUnlimited(player), 0.0f, AttributeLimitHelper.getParryLimit((PlayerEntity)player));
    }
    
    public static float getPlayerParryChanceUnlimited(final ServerPlayerEntity player) {
        float totalParryChance = 0.0f;
        totalParryChance += getParryChance((LivingEntity)player);
        final TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity)player);
        for (final TalentNode<?> talentNode : talents.getLearnedNodes()) {
            if (talentNode.getTalent() instanceof WardTalent && ArchetypeTalent.isEnabled((World)player.getLevel())) {
                totalParryChance += ((WardTalent)talentNode.getTalent()).getAdditionalParryChance();
            }
        }
        final SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity)player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (node.getSet() instanceof NinjaSet) {
                final NinjaSet set = (NinjaSet)node.getSet();
                totalParryChance += set.getBonusParry();
            }
            if (node.getSet() instanceof DreamSet) {
                final DreamSet set2 = (DreamSet)node.getSet();
                totalParryChance += set2.getIncreasedParry();
            }
        }
        final AbilityTree abilities = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity)player);
        final AbilityNode<?, ?> tankNode = abilities.getNodeByName("Tank");
        if (player.getEffect(ModEffects.TANK) != null && "Tank_Parry".equals(tankNode.getSpecialization())) {
            final TankParryConfig parryConfig = (TankParryConfig)tankNode.getAbilityConfig();
            totalParryChance += parryConfig.getParryChance();
        }
        final AbilityNode<?, ?> ghostWalk = abilities.getNodeByName("Ghost Walk");
        if (player.getEffect(ModEffects.GHOST_WALK) != null && "Ghost Walk_Parry".equals(ghostWalk.getSpecialization())) {
            final GhostWalkParryConfig parryConfig2 = (GhostWalkParryConfig)ghostWalk.getAbilityConfig();
            totalParryChance += parryConfig2.getAdditionalParryChance();
        }
        for (final ActiveAura aura : AuraManager.getInstance().getAurasAffecting((Entity)player)) {
            if (aura.getAura() instanceof ParryAuraConfig) {
                totalParryChance += ((ParryAuraConfig)aura.getAura()).getAdditionalParryChance();
            }
        }
        final VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.PARRY && !influence.isMultiplicative()) {
                    totalParryChance += influence.getValue();
                }
            }
            for (final StatModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of((PlayerEntity)player), StatModifier.class)) {
                if (modifier.getStat() == StatModifier.Statistic.PARRY) {
                    totalParryChance *= modifier.getMultiplier();
                }
            }
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.PARRY && influence.isMultiplicative()) {
                    totalParryChance *= influence.getValue();
                }
            }
        }
        return totalParryChance;
    }
    
    public static float getParryChance(final LivingEntity entity) {
        float totalParryChance = 0.0f;
        totalParryChance += getGearParryChance(entity::getItemBySlot);
        if (entity.hasEffect(ModEffects.PARRY)) {
            totalParryChance += (entity.getEffect(ModEffects.PARRY).getAmplifier() + 1) / 100.0f;
        }
        return totalParryChance;
    }
    
    public static float getGearParryChance(final Function<EquipmentSlotType, ItemStack> gearProvider) {
        float totalParryChance = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = gearProvider.apply(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear)stack.getItem()).isIntendedForSlot(slot)) {
                totalParryChance += ModAttributes.EXTRA_PARRY_CHANCE.getOrDefault(stack, 0.0f).getValue(stack);
                totalParryChance += ModAttributes.ADD_EXTRA_PARRY_CHANCE.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return totalParryChance;
    }
}
