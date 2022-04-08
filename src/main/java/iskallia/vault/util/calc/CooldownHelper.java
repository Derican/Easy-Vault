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

public class CooldownHelper {
    public static float getCooldownMultiplier(ServerPlayerEntity player, @Nullable AbilityGroup<?, ?> abilityGroup) {
        return MathHelper.clamp(getCooldownMultiplierUnlimited(player, abilityGroup), 0.0F, AttributeLimitHelper.getCooldownReductionLimit((PlayerEntity) player));
    }

    public static float getCooldownMultiplierUnlimited(ServerPlayerEntity player, @Nullable AbilityGroup<?, ?> abilityGroup) {
        float multiplier = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {

                multiplier += ((Float) ModAttributes.COOLDOWN_REDUCTION.get(stack)
                        .map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();
            }
        }
        if (abilityGroup == ModConfigs.ABILITIES.SUMMON_ETERNAL) {
            TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);


            multiplier = (float) (multiplier + talents.getLearnedNodes(CommanderTalent.class).stream().mapToDouble(node -> ((CommanderTalent) node.getTalent()).getSummonEternalAdditionalCooldownReduction()).sum());
        }

        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.COOLDOWN_REDUCTION && !influence.isMultiplicative()) {
                    multiplier += influence.getValue();
                }
            }
            for (StatModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{(PlayerEntity) player}, ), StatModifier.class)) {
                if (modifier.getStat() == StatModifier.Statistic.COOLDOWN_REDUCTION) {
                    multiplier *= modifier.getMultiplier();
                }
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.COOLDOWN_REDUCTION && influence.isMultiplicative()) {
                    multiplier *= influence.getValue();
                }
            }
        }

        return multiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\CooldownHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */