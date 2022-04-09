// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.AssassinSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.FatalStrikeChanceTalent;
import iskallia.vault.skill.talent.type.FatalStrikeDamageTalent;
import iskallia.vault.skill.talent.type.FatalStrikeTalent;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class FatalStrikeHelper {
    public static float getPlayerFatalStrikeChance(final ServerPlayerEntity player) {
        float chance = 0.0f;
        final TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (final Object talent : tree.getTalents(FatalStrikeTalent.class)) {
            chance += ((FatalStrikeTalent) talent).getFatalStrikeChance();
        }
        for (final Object talent2 : tree.getTalents(FatalStrikeChanceTalent.class)) {
            chance += ((FatalStrikeChanceTalent) talent2).getAdditionalFatalStrikeChance();
        }
        final SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof AssassinSet)) {
                continue;
            }
            final AssassinSet set = (AssassinSet) node.getSet();
            chance += set.getIncreasedFatalStrikeChance();
        }
        final VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_CHANCE && !influence.isMultiplicative()) {
                    chance += influence.getValue();
                }
            }
        }
        chance += getFatalStrikeChance((LivingEntity) player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_CHANCE && influence.isMultiplicative()) {
                    chance *= influence.getValue();
                }
            }
        }
        return chance;
    }

    public static float getFatalStrikeChance(final LivingEntity entity) {
        float chance = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {
                chance += ModAttributes.FATAL_STRIKE_CHANCE.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return chance;
    }

    public static float getPlayerFatalStrikeDamage(final ServerPlayerEntity player) {
        float additionalMultiplier = 0.0f;
        final TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (final Object talent : tree.getTalents(FatalStrikeTalent.class)) {
            additionalMultiplier += ((FatalStrikeTalent) talent).getFatalStrikeDamage();
        }
        for (final Object talent2 : tree.getTalents(FatalStrikeDamageTalent.class)) {
            additionalMultiplier += ((FatalStrikeDamageTalent) talent2).getAdditionalFatalStrikeDamage();
        }
        final VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_DAMAGE && !influence.isMultiplicative()) {
                    additionalMultiplier += influence.getValue();
                }
            }
        }
        additionalMultiplier += getFatalStrikeDamage((LivingEntity) player);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_DAMAGE && influence.isMultiplicative()) {
                    additionalMultiplier *= influence.getValue();
                }
            }
        }
        return additionalMultiplier;
    }

    public static float getFatalStrikeDamage(final LivingEntity entity) {
        float additionalMultiplier = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {
                additionalMultiplier += ModAttributes.FATAL_STRIKE_DAMAGE.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return additionalMultiplier;
    }
}
