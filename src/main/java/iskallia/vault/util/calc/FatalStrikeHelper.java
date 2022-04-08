package iskallia.vault.util.calc;

import iskallia.vault.attribute.FloatAttribute;
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
    public static float getPlayerFatalStrikeChance(ServerPlayerEntity player) {
        float chance = 0.0F;

        TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (FatalStrikeTalent talent : tree.getTalents(FatalStrikeTalent.class)) {
            chance += talent.getFatalStrikeChance();
        }
        for (FatalStrikeChanceTalent talent : tree.getTalents(FatalStrikeChanceTalent.class)) {
            chance += talent.getAdditionalFatalStrikeChance();
        }

        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (!(node.getSet() instanceof AssassinSet))
                continue;
            AssassinSet set = (AssassinSet) node.getSet();
            chance += set.getIncreasedFatalStrikeChance();
        }
        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_CHANCE && !influence.isMultiplicative()) {
                    chance += influence.getValue();
                }
            }
        }

        chance += getFatalStrikeChance((LivingEntity) player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_CHANCE && influence.isMultiplicative()) {
                    chance *= influence.getValue();
                }
            }
        }
        return chance;
    }

    public static float getFatalStrikeChance(LivingEntity entity) {
        float chance = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {

                chance += ((Float) ((FloatAttribute) ModAttributes.FATAL_STRIKE_CHANCE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return chance;
    }

    public static float getPlayerFatalStrikeDamage(ServerPlayerEntity player) {
        float additionalMultiplier = 0.0F;

        TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (FatalStrikeTalent talent : tree.getTalents(FatalStrikeTalent.class)) {
            additionalMultiplier += talent.getFatalStrikeDamage();
        }
        for (FatalStrikeDamageTalent talent : tree.getTalents(FatalStrikeDamageTalent.class)) {
            additionalMultiplier += talent.getAdditionalFatalStrikeDamage();
        }
        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_DAMAGE && !influence.isMultiplicative()) {
                    additionalMultiplier += influence.getValue();
                }
            }
        }

        additionalMultiplier += getFatalStrikeDamage((LivingEntity) player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.FATAL_STRIKE_DAMAGE && influence.isMultiplicative()) {
                    additionalMultiplier *= influence.getValue();
                }
            }
        }
        return additionalMultiplier;
    }

    public static float getFatalStrikeDamage(LivingEntity entity) {
        float additionalMultiplier = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {

                additionalMultiplier += ((Float) ((FloatAttribute) ModAttributes.FATAL_STRIKE_DAMAGE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return additionalMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\FatalStrikeHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */