package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;


public class CakeHuntData
        extends VaultGoalData {
    private int totalCakes;
    private int foundCakes;

    public void receive(VaultGoalMessage pkt) {
        CompoundNBT tag = pkt.payload;

        this.totalCakes = tag.getInt("total");
        this.foundCakes = tag.getInt("found");
    }


    @Nullable
    public BossBarOverlay getBossBarOverlay() {
        return null;
    }

    public float getCompletePercent() {
        return this.foundCakes / this.totalCakes;
    }

    public int getTotalCakes() {
        return this.totalCakes;
    }

    public int getFoundCakes() {
        return this.foundCakes;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\CakeHuntData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */