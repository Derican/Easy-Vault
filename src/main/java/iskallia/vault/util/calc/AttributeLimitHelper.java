package iskallia.vault.util.calc;

import iskallia.vault.client.ClientTalentData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.set.GolemSet;
import iskallia.vault.skill.set.NinjaSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.ParryTalent;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.skill.talent.type.ResistanceTalent;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class AttributeLimitHelper {
    public static float getCooldownReductionLimit(final PlayerEntity player) {
        return 0.8f;
    }

    public static float getParryLimit(final PlayerEntity player) {
        float limit = 0.4f;
        limit += getTalent(player, ModConfigs.TALENTS.PARRY).map(TalentNode::getTalent).map(ParryTalent::getAdditionalParryLimit).orElse(0.0f);
        final SetTree sets = PlayerSetsData.get((ServerWorld) player.level).getSets(player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof NinjaSet)) {
                continue;
            }
            final NinjaSet set = (NinjaSet) node.getSet();
            limit += set.getBonusParryCap();
        }
        return limit;
    }

    public static float getResistanceLimit(final PlayerEntity player) {
        float limit = 0.3f;
        limit += getTalent(player, ModConfigs.TALENTS.RESISTANCE).map(TalentNode::getTalent).map(ResistanceTalent::getAdditionalResistanceLimit).orElse(0.0f);
        final SetTree sets = PlayerSetsData.get((ServerWorld) player.level).getSets(player);
        for (final SetNode<?> node : sets.getNodes()) {
            if (!(node.getSet() instanceof GolemSet)) {
                continue;
            }
            final GolemSet set = (GolemSet) node.getSet();
            limit += set.getBonusResistanceCap();
        }
        return limit;
    }

    private static <T extends PlayerTalent> Optional<TalentNode<T>> getTalent(final PlayerEntity player, final TalentGroup<T> talentGroup) {
        if (player instanceof ServerPlayerEntity) {
            final TalentTree talents = PlayerTalentsData.get(((ServerPlayerEntity) player).getLevel()).getTalents(player);
            return Optional.of(talents.getNodeOf(talentGroup));
        }
        return Optional.ofNullable(ClientTalentData.getLearnedTalentNode(talentGroup));
    }
}
