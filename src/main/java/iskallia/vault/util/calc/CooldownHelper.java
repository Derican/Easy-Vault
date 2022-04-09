// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.archetype.CommanderTalent;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.modifier.StatModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class CooldownHelper
{
    public static float getCooldownMultiplier(final ServerPlayerEntity player, @Nullable final AbilityGroup<?, ?> abilityGroup) {
        return MathHelper.clamp(getCooldownMultiplierUnlimited(player, abilityGroup), 0.0f, AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity)player));
    }
    
    public static float getCooldownMultiplierUnlimited(final ServerPlayerEntity player, @Nullable final AbilityGroup<?, ?> abilityGroup) {
        float multiplier = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear)stack.getItem()).isIntendedForSlot(slot)) {
                multiplier += ModAttributes.COOLDOWN_REDUCTION.get(stack).map(attribute -> attribute.getValue(stack)).orElse(0.0f);
            }
        }
        if (abilityGroup == ModConfigs.ABILITIES.SUMMON_ETERNAL) {
            final TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity)player);
            multiplier += (float)talents.getLearnedNodes(CommanderTalent.class).stream().mapToDouble(node -> ((CommanderTalent) node.getTalent()).getSummonEternalAdditionalCooldownReduction()).sum();
        }
        final VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.COOLDOWN_REDUCTION && !influence.isMultiplicative()) {
                    multiplier += influence.getValue();
                }
            }
            for (final StatModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of((PlayerEntity)player), StatModifier.class)) {
                if (modifier.getStat() == StatModifier.Statistic.COOLDOWN_REDUCTION) {
                    multiplier *= modifier.getMultiplier();
                }
            }
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.COOLDOWN_REDUCTION && influence.isMultiplicative()) {
                    multiplier *= influence.getValue();
                }
            }
        }
        return multiplier;
    }
}
