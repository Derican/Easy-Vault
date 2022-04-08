package iskallia.vault.util.calc;

import iskallia.vault.aura.ActiveAura;
import iskallia.vault.aura.AuraManager;
import iskallia.vault.aura.type.ResistanceAuraConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModEffects;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.DreamSet;
import iskallia.vault.skill.set.GolemSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerSetsData;
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
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;


public class ResistanceHelper {
    public static float getPlayerResistancePercent(ServerPlayerEntity player) {
        return MathHelper.clamp(getPlayerResistancePercentUnlimited(player), 0.0F, AttributeLimitHelper.getResistanceLimit((PlayerEntity) player));
    }

    public static float getPlayerResistancePercentUnlimited(ServerPlayerEntity player) {
        float resistancePercent = 0.0F;

        resistancePercent += getResistancePercent((LivingEntity) player);

        for (ActiveAura aura : AuraManager.getInstance().getAurasAffecting((Entity) player)) {
            if (aura.getAura() instanceof ResistanceAuraConfig) {
                resistancePercent += ((ResistanceAuraConfig) aura.getAura()).getAdditionalResistance();
            }
        }

        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);

        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.RESISTANCE && !influence.isMultiplicative()) {
                    resistancePercent += influence.getValue();
                }
            }

            for (StatModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{(PlayerEntity) player}, ), StatModifier.class)) {
                if (modifier.getStat() == StatModifier.Statistic.RESISTANCE) {
                    resistancePercent *= modifier.getMultiplier();
                }
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.PARRY && influence.isMultiplicative()) {
                    resistancePercent += influence.getValue();
                }
            }
        }

        SetTree sets = PlayerSetsData.get((ServerWorld) player.level).getSets((PlayerEntity) player);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (node.getSet() instanceof GolemSet) {
                GolemSet set = (GolemSet) node.getSet();
                resistancePercent += set.getBonusResistance();
            }
            if (node.getSet() instanceof DreamSet) {
                DreamSet set = (DreamSet) node.getSet();
                resistancePercent += set.getIncreasedResistance();
            }
        }

        resistancePercent += getResistancePercent((LivingEntity) player);
        return resistancePercent;
    }

    public static float getResistancePercent(LivingEntity entity) {
        float resistancePercent = 0.0F;
        resistancePercent += getGearResistanceChance(entity::getItemBySlot);

        if (entity.hasEffect(ModEffects.RESISTANCE)) {
            resistancePercent += (entity.getEffect(ModEffects.RESISTANCE).getAmplifier() + 1) / 100.0F;
        }

        return resistancePercent;
    }

    public static float getGearResistanceChance(Function<EquipmentSlotType, ItemStack> gearProvider) {
        float resistancePercent = 0.0F;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = gearProvider.apply(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                resistancePercent += ((Float) ModAttributes.EXTRA_RESISTANCE.get(stack)
                        .map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();
                resistancePercent += ((Float) ModAttributes.ADD_EXTRA_RESISTANCE.get(stack)
                        .map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();
            }
        }
        return MathHelper.clamp(resistancePercent, 0.0F, 1.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\ResistanceHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */