package iskallia.vault.util.calc;

import iskallia.vault.attribute.VAttribute;
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

public class ChestRarityHelper {
    public static float getIncreasedChestRarity(ServerPlayerEntity sPlayer) {
        float increasedRarity = 0.0F;

        increasedRarity += VaultGearHelper.getAttributeValueOnGearSumFloat((LivingEntity) sPlayer, new VAttribute[]{ModAttributes.CHEST_RARITY});

        SetTree sets = PlayerSetsData.get(sPlayer.getLevel()).getSets((PlayerEntity) sPlayer);
        for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
            if (node.getSet() instanceof TreasureSet) {
                TreasureSet set = (TreasureSet) node.getSet();
                increasedRarity += set.getIncreasedChestRarity();
            }
            if (node.getSet() instanceof DreamSet) {
                DreamSet set = (DreamSet) node.getSet();
                increasedRarity += set.getIncreasedChestRarity();
            }
        }

        VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && !influence.isMultiplicative()) {
                    increasedRarity += influence.getValue();
                }
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.CHEST_RARITY && influence.isMultiplicative()) {
                    increasedRarity *= influence.getValue();
                }
            }
        }

        return increasedRarity;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\ChestRarityHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */