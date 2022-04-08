package iskallia.vault.client;

import iskallia.vault.network.message.KnownTalentsMessage;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ClientTalentData {
    private static List<TalentNode<?>> learnedTalents = new ArrayList<>();

    @Nonnull
    public static List<TalentNode<?>> getLearnedTalentNodes() {
        return Collections.unmodifiableList(learnedTalents);
    }

    @Nullable
    public static <T extends iskallia.vault.skill.talent.type.PlayerTalent> TalentNode<T> getLearnedTalentNode(TalentGroup<T> talent) {
        return getLearnedTalentNode(talent.getParentName());
    }

    @Nullable
    public static <T extends iskallia.vault.skill.talent.type.PlayerTalent> TalentNode<T> getLearnedTalentNode(String talentName) {
        for (TalentNode<?> node : getLearnedTalentNodes()) {
            if (node.getGroup().getParentName().equals(talentName)) {
                return (TalentNode) node;
            }
        }
        return null;
    }

    public static void updateTalents(KnownTalentsMessage pkt) {
        learnedTalents = pkt.getLearnedTalents();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\ClientTalentData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */