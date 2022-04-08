package iskallia.vault.util.calc;

import iskallia.vault.attribute.FloatAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.sub.RampageLeechConfig;
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

public class LeechHelper {
    public static float getPlayerLeechPercent(ServerPlayerEntity player) {
        float leech = 0.0F;

        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (TalentNode<?> node : (Iterable<TalentNode<?>>) talents.getNodes()) {
            if (!(node.getTalent() instanceof VampirismTalent))
                continue;
            VampirismTalent vampirism = (VampirismTalent) node.getTalent();
            leech += vampirism.getLeechRatio();
        }

        AbilityTree abilities = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity) player);
        for (AbilityNode<?, ?> node : (Iterable<AbilityNode<?, ?>>) abilities.getNodes()) {
            if (!node.isLearned() || !(node.getAbility() instanceof iskallia.vault.skill.ability.effect.RampageAbility))
                continue;
            AbilityConfig cfg = node.getAbilityConfig();
            if (!(cfg instanceof RampageLeechConfig))
                continue;
            leech += ((RampageLeechConfig) cfg).getLeechPercent();
        }

        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (!(node.getSet() instanceof VampirismSet))
                continue;
            VampirismSet set = (VampirismSet) node.getSet();
            leech += set.getLeechRatio();
        }

        leech += getLeechPercent((LivingEntity) player);
        return leech;
    }

    public static float getLeechPercent(LivingEntity entity) {
        float leech = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                leech += ((Float) ((FloatAttribute) ModAttributes.EXTRA_LEECH_RATIO.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
                leech += ((Float) ((FloatAttribute) ModAttributes.ADD_EXTRA_LEECH_RATIO.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return leech;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\LeechHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */