package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.ArchitectGoalVoteOverlay;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;


public class ArchitectGoalData
        extends VaultGoalData {
    private float completedPercent = 0.0F;
    private int ticksUntilNextVote = 0;
    private int totalTicksUntilNextVote = 0;
    private VotingSession activeSession = null;


    public void receive(VaultGoalMessage pkt) {
        CompoundNBT tag = pkt.payload;
        this.completedPercent = tag.getFloat("completedPercent");
        this.ticksUntilNextVote = tag.getInt("ticksUntilNextVote");
        this.totalTicksUntilNextVote = tag.getInt("totalTicksUntilNextVote");
        if (tag.contains("votingSession", 10)) {
            this.activeSession = VotingSession.deserialize(tag.getCompound("votingSession"));
        }
    }


    @Nullable
    public BossBarOverlay getBossBarOverlay() {
        return (BossBarOverlay) new ArchitectGoalVoteOverlay(this);
    }

    @Nullable
    public VotingSession getActiveSession() {
        return this.activeSession;
    }

    public float getCompletedPercent() {
        return this.completedPercent;
    }

    public int getTicksUntilNextVote() {
        return this.ticksUntilNextVote;
    }

    public int getTotalTicksUntilNextVote() {
        return this.totalTicksUntilNextVote;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\ArchitectGoalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */