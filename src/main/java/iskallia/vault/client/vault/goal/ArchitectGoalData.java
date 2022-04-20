package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.ArchitectGoalVoteOverlay;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class ArchitectGoalData extends VaultGoalData {
    private float completedPercent;
    private int ticksUntilNextVote;
    private int totalTicksUntilNextVote;
    private VotingSession activeSession;

    public ArchitectGoalData() {
        this.completedPercent = 0.0f;
        this.ticksUntilNextVote = 0;
        this.totalTicksUntilNextVote = 0;
        this.activeSession = null;
    }

    @Override
    public void receive(final VaultGoalMessage pkt) {
        final CompoundNBT tag = pkt.payload;
        this.completedPercent = tag.getFloat("completedPercent");
        this.ticksUntilNextVote = tag.getInt("ticksUntilNextVote");
        this.totalTicksUntilNextVote = tag.getInt("totalTicksUntilNextVote");
        if (tag.contains("votingSession", 10)) {
            this.activeSession = VotingSession.deserialize(tag.getCompound("votingSession"));
        }
    }

    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return new ArchitectGoalVoteOverlay(this);
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
