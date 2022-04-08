package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.ActiveRaidOverlay;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class ActiveRaidGoalData
        extends VaultGoalData {
    private int wave;
    private int totalWaves;
    private int aliveMobs;
    private int totalMobs;
    private int tickWaveDelay;
    private int raidsCompleted;
    private List<ITextComponent> positives = new ArrayList<>();
    private List<ITextComponent> negatives = new ArrayList<>();


    @Nullable
    public BossBarOverlay getBossBarOverlay() {
        return (BossBarOverlay) new ActiveRaidOverlay(this);
    }

    public int getWave() {
        return this.wave;
    }

    public int getTotalWaves() {
        return this.totalWaves;
    }

    public int getAliveMobs() {
        return this.aliveMobs;
    }

    public int getTotalMobs() {
        return this.totalMobs;
    }

    public int getTickWaveDelay() {
        return this.tickWaveDelay;
    }

    public int getRaidsCompleted() {
        return this.raidsCompleted;
    }

    public List<ITextComponent> getPositives() {
        return this.positives;
    }

    public List<ITextComponent> getNegatives() {
        return this.negatives;
    }


    public void receive(VaultGoalMessage pkt) {
        CompoundNBT tag = pkt.payload;

        this.wave = tag.getInt("wave");
        this.totalWaves = tag.getInt("totalWaves");
        this.aliveMobs = tag.getInt("aliveMobs");
        this.totalMobs = tag.getInt("totalMobs");
        this.tickWaveDelay = tag.getInt("tickWaveDelay");
        this.raidsCompleted = tag.getInt("completedRaids");

        ListNBT positives = tag.getList("positives", 8);
        this.positives = new ArrayList<>();
        for (int i = 0; i < positives.size(); i++) {
            this.positives.add(ITextComponent.Serializer.fromJson(positives.getString(i)));
        }

        ListNBT negatives = tag.getList("negatives", 8);
        this.negatives = new ArrayList<>();
        for (int j = 0; j < negatives.size(); j++)
            this.negatives.add(ITextComponent.Serializer.fromJson(negatives.getString(j)));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\ActiveRaidGoalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */