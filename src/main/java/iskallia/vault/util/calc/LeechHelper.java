// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.sub.RampageLeechConfig;
import iskallia.vault.skill.ability.effect.RampageAbility;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.set.VampirismSet;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.VampirismTalent;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class LeechHelper
{
    public static float getPlayerLeechPercent(final ServerPlayerEntity player) {
        float leech = 0.0f;
        final TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity)player);
        for (final TalentNode<?> node : talents.getNodes()) {
            if (!(node.getTalent() instanceof VampirismTalent)) {
                continue;
            }
            final VampirismTalent vampirism = (VampirismTalent)node.getTalent();
            leech += vampirism.getLeechRatio();
        }
        final AbilityTree abilities = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity)player);
        for (final AbilityNode<?, ?> node2 : abilities.getNodes()) {
            if (node2.isLearned()) {
                if (!(node2.getAbility() instanceof RampageAbility)) {
                    continue;
                }
                final AbilityConfig cfg = (AbilityConfig)node2.getAbilityConfig();
                if (!(cfg instanceof RampageLeechConfig)) {
                    continue;
                }
                leech += ((RampageLeechConfig)cfg).getLeechPercent();
            }
        }
        final SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity)player);
        for (final SetNode<?> node3 : sets.getNodes()) {
            if (!(node3.getSet() instanceof VampirismSet)) {
                continue;
            }
            final VampirismSet set = (VampirismSet)node3.getSet();
            leech += set.getLeechRatio();
        }
        leech += getLeechPercent((LivingEntity)player);
        return leech;
    }
    
    public static float getLeechPercent(final LivingEntity entity) {
        float leech = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear)stack.getItem()).isIntendedForSlot(slot)) {
                leech += ModAttributes.EXTRA_LEECH_RATIO.getOrDefault(stack, 0.0f).getValue(stack);
                leech += ModAttributes.ADD_EXTRA_LEECH_RATIO.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return leech;
    }
}
