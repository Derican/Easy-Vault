package iskallia.vault.client.vault.goal;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.network.message.VaultGoalMessage;

import javax.annotation.Nullable;

import iskallia.vault.client.gui.overlay.goal.ActiveRaidOverlay;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;

import java.util.ArrayList;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ActiveRaidGoalData extends VaultGoalData {
    private int wave;
    private int totalWaves;
    private int aliveMobs;
    private int totalMobs;
    private int tickWaveDelay;
    private int raidsCompleted;
    private int targetRaids;
    private List<ITextComponent> positives;
    private List<ITextComponent> negatives;

    public ActiveRaidGoalData() {
        this.positives = new ArrayList<ITextComponent>();
        this.negatives = new ArrayList<ITextComponent>();
    }

    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return new ActiveRaidOverlay(this);
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

    public int getTargetRaids() {
        return this.targetRaids;
    }

    public List<ITextComponent> getPositives() {
        return this.positives;
    }

    public List<ITextComponent> getNegatives() {
        return this.negatives;
    }

    @Override
    public void receive(final VaultGoalMessage pkt) {
        final CompoundNBT tag = pkt.payload;
        this.wave = tag.getInt("wave");
        this.totalWaves = tag.getInt("totalWaves");
        this.aliveMobs = tag.getInt("aliveMobs");
        this.totalMobs = tag.getInt("totalMobs");
        this.tickWaveDelay = tag.getInt("tickWaveDelay");
        this.raidsCompleted = tag.getInt("completedRaids");
        this.targetRaids = tag.getInt("targetRaids");
        final ListNBT positives = tag.getList("positives", 8);
        this.positives = new ArrayList<ITextComponent>();
        for (int i = 0; i < positives.size(); ++i) {
            this.positives.add((ITextComponent) ITextComponent.Serializer.fromJson(positives.getString(i)));
        }
        final ListNBT negatives = tag.getList("negatives", 8);
        this.negatives = new ArrayList<ITextComponent>();
        for (int j = 0; j < negatives.size(); ++j) {
            this.negatives.add((ITextComponent) ITextComponent.Serializer.fromJson(negatives.getString(j)));
        }
    }
}
