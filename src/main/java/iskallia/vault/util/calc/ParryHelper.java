package iskallia.vault.util.calc;

import iskallia.vault.attribute.FloatAttribute;
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

public class ParryHelper {
    public static float getPlayerParryChance(ServerPlayerEntity player) {
        return MathHelper.clamp(getPlayerParryChanceUnlimited(player), 0.0F, AttributeLimitHelper.getParryLimit((PlayerEntity) player));
    }

    public static float getPlayerParryChanceUnlimited(ServerPlayerEntity player) {
        float totalParryChance = 0.0F;

        totalParryChance += getParryChance((LivingEntity) player);

        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (TalentNode<?> talentNode : (Iterable<TalentNode<?>>) talents.getLearnedNodes()) {
            if (talentNode.getTalent() instanceof WardTalent && ArchetypeTalent.isEnabled((World) player.getLevel())) {
                totalParryChance += ((WardTalent) talentNode.getTalent()).getAdditionalParryChance();
            }
        }

        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);

        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (node.getSet() instanceof NinjaSet) {
                NinjaSet set = (NinjaSet) node.getSet();
                totalParryChance += set.getBonusParry();
            }
            if (node.getSet() instanceof DreamSet) {
                DreamSet set = (DreamSet) node.getSet();
                totalParryChance += set.getIncreasedParry();
            }
        }

        AbilityTree abilities = PlayerAbilitiesData.get(player.getLevel()).getAbilities((PlayerEntity) player);
        AbilityNode<?, ?> tankNode = abilities.getNodeByName("Tank");
        if (player.getEffect(ModEffects.TANK) != null && "Tank_Parry".equals(tankNode.getSpecialization())) {
            TankParryConfig parryConfig = (TankParryConfig) tankNode.getAbilityConfig();
            totalParryChance += parryConfig.getParryChance();
        }
        AbilityNode<?, ?> ghostWalk = abilities.getNodeByName("Ghost Walk");
        if (player.getEffect(ModEffects.GHOST_WALK) != null && "Ghost Walk_Parry".equals(ghostWalk.getSpecialization())) {
            GhostWalkParryConfig parryConfig = (GhostWalkParryConfig) ghostWalk.getAbilityConfig();
            totalParryChance += parryConfig.getAdditionalParryChance();
        }

        for (ActiveAura aura : AuraManager.getInstance().getAurasAffecting((Entity) player)) {
            if (aura.getAura() instanceof ParryAuraConfig) {
                totalParryChance += ((ParryAuraConfig) aura.getAura()).getAdditionalParryChance();
            }
        }

        VaultRaid vault = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.PARRY && !influence.isMultiplicative()) {
                    totalParryChance += influence.getValue();
                }
            }


            for (StatModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{(PlayerEntity) player}, ), StatModifier.class)) {
                if (modifier.getStat() == StatModifier.Statistic.PARRY) {
                    totalParryChance *= modifier.getMultiplier();
                }
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.PARRY && influence.isMultiplicative()) {
                    totalParryChance *= influence.getValue();
                }
            }
        }
        return totalParryChance;
    }

    public static float getParryChance(LivingEntity entity) {
        float totalParryChance = 0.0F;
        totalParryChance += getGearParryChance(entity::getItemBySlot);

        if (entity.hasEffect(ModEffects.PARRY)) {
            totalParryChance += (entity.getEffect(ModEffects.PARRY).getAmplifier() + 1) / 100.0F;
        }
        return totalParryChance;
    }

    public static float getGearParryChance(Function<EquipmentSlotType, ItemStack> gearProvider) {
        float totalParryChance = 0.0F;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = gearProvider.apply(slot);
            if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                totalParryChance += ((Float) ((FloatAttribute) ModAttributes.EXTRA_PARRY_CHANCE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
                totalParryChance += ((Float) ((FloatAttribute) ModAttributes.ADD_EXTRA_PARRY_CHANCE.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
            }
        }
        return totalParryChance;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\ParryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */