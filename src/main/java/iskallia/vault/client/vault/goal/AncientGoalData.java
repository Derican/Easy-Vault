package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class AncientGoalData
        extends VaultGoalData {
    private int totalAncients = 0;
    private int foundAncients = 0;

    public int getTotalAncients() {
        return this.totalAncients;
    }

    public int getFoundAncients() {
        return this.foundAncients;
    }


    @Nullable
    public BossBarOverlay getBossBarOverlay() {
        return null;
    }


    public void receive(VaultGoalMessage pkt) {
        CompoundNBT data = pkt.payload;

        this.totalAncients = data.getInt("total");
        this.foundAncients = data.getInt("found");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\AncientGoalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */