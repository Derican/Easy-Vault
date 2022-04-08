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


public class VaultScavengerData
        extends VaultGoalData {
    private final List<ScavengerHuntObjective.ItemSubmission> itemSubmissions = new ArrayList<>();

    public List<ScavengerHuntObjective.ItemSubmission> getRequiredItemSubmissions() {
        return Collections.unmodifiableList(this.itemSubmissions);
    }


    @Nullable
    public BossBarOverlay getBossBarOverlay() {
        return (BossBarOverlay) new ScavengerBarOverlay(this);
    }


    public void receive(VaultGoalMessage pkt) {
        this.itemSubmissions.clear();

        ListNBT itemList = pkt.payload.getList("scavengerItems", 10);
        for (int i = 0; i < itemList.size(); i++)
            this.itemSubmissions.add(ScavengerHuntObjective.ItemSubmission.deserialize(itemList.getCompound(i)));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\VaultScavengerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */