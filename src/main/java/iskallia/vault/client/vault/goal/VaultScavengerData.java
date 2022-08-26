package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.client.gui.overlay.goal.ScavengerBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import net.minecraft.nbt.ListNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaultScavengerData extends VaultGoalData {
    private final List<ScavengerHuntObjective.ItemSubmission> itemSubmissions;

    public VaultScavengerData() {
        this.itemSubmissions = new ArrayList<ScavengerHuntObjective.ItemSubmission>();
    }

    public List<ScavengerHuntObjective.ItemSubmission> getRequiredItemSubmissions() {
        return Collections.unmodifiableList(this.itemSubmissions);
    }

    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return new ScavengerBarOverlay(this);
    }

    @Override
    public void receive(final VaultGoalMessage pkt) {
        this.itemSubmissions.clear();
        final ListNBT itemList = pkt.payload.getList("scavengerItems", 10);
        for (int i = 0; i < itemList.size(); ++i) {
            this.itemSubmissions.add(ScavengerHuntObjective.ItemSubmission.deserialize(itemList.getCompound(i)));
        }
    }
}
