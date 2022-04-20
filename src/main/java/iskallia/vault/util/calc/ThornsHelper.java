package iskallia.vault.util.calc;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.PorcupineSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.ThornsChanceTalent;
import iskallia.vault.skill.talent.type.ThornsDamageTalent;
import iskallia.vault.skill.talent.type.ThornsTalent;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ThornsHelper {
    public static float getPlayerThornsChance(final ServerPlayerEntity player) {
        float chance = 0.0f;
        final TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (final Object talent : tree.getTalents(ThornsTalent.class)) {
            chance += ((ThornsTalent) talent).getThornsChance();
        }
        for (final Object talent2 : tree.getTalents(ThornsChanceTalent.class)) {
            chance += ((ThornsChanceTalent) talent2).getAdditionalThornsChance();
        }
        final SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof PorcupineSet)) {
                continue;
            }
            final PorcupineSet set = (PorcupineSet) node.getSet();
            chance += set.getAdditionalThornsChance();
        }
        chance += getThornsChance((LivingEntity) player);
        return chance;
    }

    public static float getThornsChance(final LivingEntity entity) {
        float chance = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {
                chance += ModAttributes.THORNS_CHANCE.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return chance;
    }

    public static float getPlayerThornsDamage(final ServerPlayerEntity player) {
        float additionalMultiplier = 0.0f;
        final TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (final Object talent : tree.getTalents(ThornsTalent.class)) {
            additionalMultiplier += ((ThornsTalent) talent).getThornsDamage();
        }
        for (final Object talent2 : tree.getTalents(ThornsDamageTalent.class)) {
            additionalMultiplier += ((ThornsDamageTalent) talent2).getAdditionalThornsDamage();
        }
        final SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof PorcupineSet)) {
                continue;
            }
            final PorcupineSet set = (PorcupineSet) node.getSet();
            additionalMultiplier += set.getAdditionalThornsDamage();
        }
        additionalMultiplier += getThornsDamage((LivingEntity) player);
        return additionalMultiplier;
    }

    public static float getThornsDamage(final LivingEntity entity) {
        float additionalMultiplier = 0.0f;
        for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
            final ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {
                additionalMultiplier += ModAttributes.THORNS_DAMAGE.getOrDefault(stack, 0.0f).getValue(stack);
            }
        }
        return additionalMultiplier;
    }
}
