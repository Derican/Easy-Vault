// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.skill.set.DreamSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.set.TreasureSet;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ChestRarityHelper
{
    public static float getIncreasedChestRarity(final ServerPlayerEntity sPlayer) {
        float increasedRarity = 0.0f;
        increasedRarity += VaultGearHelper.getAttributeValueOnGearSumFloat((LivingEntity)sPlayer, ModAttributes.CHEST_RARITY);
        final SetTree sets = PlayerSetsData.get(sPlayer.getLevel()).getSets((PlayerEntity)sPlayer);
        for (final SetNode<?> node : sets.getNodes()) {
            if (node.getSet() instanceof TreasureSet) {
                final TreasureSet set = (TreasureSet)node.getSet();
                increasedRarity += set.getIncreasedChestRarity();
            }
            if (node.getSet() instanceof DreamSet) {
                final DreamSet set2 = (DreamSet)node.getSet();
                increasedRarity += set2.getIncreasedChestRarity();
            }
        }
        final VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (vault != null) {
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && !influence.isMultiplicative()) {
                    increasedRarity += influence.getValue();
                }
            }
            for (final VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && influence.isMultiplicative()) {
                    increasedRarity *= influence.getValue();
                }
            }
        }
        return increasedRarity;
    }
}
