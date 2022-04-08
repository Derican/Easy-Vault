package iskallia.vault.util.calc;

import iskallia.vault.attribute.FloatAttribute;
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
    public static float getPlayerThornsChance(ServerPlayerEntity player) {
        float chance = 0.0F;

        TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (ThornsTalent talent : tree.getTalents(ThornsTalent.class)) {
            chance += talent.getThornsChance();
        }
        for (ThornsChanceTalent talent : tree.getTalents(ThornsChanceTalent.class)) {
            chance += talent.getAdditionalThornsChance();
        }

        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (!(node.getSet() instanceof PorcupineSet))
                continue;
            PorcupineSet set = (PorcupineSet) node.getSet();
            chance += set.getAdditionalThornsChance();
        }

        chance += getThornsChance((LivingEntity) player);
        return chance;
    }

    public static float getThornsChance(LivingEntity entity) {
        float chance = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {

                chance += ((Float) ((FloatAttribute) ModAttributes.THORNS_CHANCE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return chance;
    }

    public static float getPlayerThornsDamage(ServerPlayerEntity player) {
        float additionalMultiplier = 0.0F;

        TalentTree tree = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (ThornsTalent talent : tree.getTalents(ThornsTalent.class)) {
            additionalMultiplier += talent.getThornsDamage();
        }
        for (ThornsDamageTalent talent : tree.getTalents(ThornsDamageTalent.class)) {
            additionalMultiplier += talent.getAdditionalThornsDamage();
        }

        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (!(node.getSet() instanceof PorcupineSet))
                continue;
            PorcupineSet set = (PorcupineSet) node.getSet();
            additionalMultiplier += set.getAdditionalThornsDamage();
        }

        additionalMultiplier += getThornsDamage((LivingEntity) player);
        return additionalMultiplier;
    }

    public static float getThornsDamage(LivingEntity entity) {
        float additionalMultiplier = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {

                additionalMultiplier += ((Float) ((FloatAttribute) ModAttributes.THORNS_DAMAGE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return additionalMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\ThornsHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */