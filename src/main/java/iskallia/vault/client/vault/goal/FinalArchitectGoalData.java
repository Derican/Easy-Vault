// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.vault.goal;

import javax.annotation.Nullable;
import iskallia.vault.client.gui.overlay.goal.FinalArchitectBossGoalOverlay;
import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.world.vault.logic.objective.architect.VotingSession;
import net.minecraft.util.text.ITextComponent;

public class FinalArchitectGoalData extends VaultGoalData
{
    private ITextComponent message;
    private int killedBosses;
    private int totalKilledBossesNeeded;
    private int knowledge;
    private int totalKnowledgeNeeded;
    private VotingSession activeSession;
    
    public FinalArchitectGoalData() {
        this.message = null;
        this.killedBosses = 0;
        this.totalKilledBossesNeeded = 0;
        this.knowledge = 0;
        this.totalKnowledgeNeeded = 0;
        this.activeSession = null;
    }
    
    @Override
    public void receive(final VaultGoalMessage pkt) {
        final CompoundNBT tag = pkt.payload;
        this.message = (ITextComponent)ITextComponent.Serializer.fromJson(tag.getString("message"));
        this.killedBosses = tag.getInt("killedBosses");
        this.totalKilledBossesNeeded = tag.getInt("totalBosses");
        this.knowledge = tag.getInt("knowledge");
        this.totalKnowledgeNeeded = tag.getInt("totalKnowledge");
        if (tag.contains("votingSession", 10)) {
            this.activeSession = VotingSession.deserialize(tag.getCompound("votingSession"));
        }
    }
    
    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return new FinalArchitectBossGoalOverlay(this);
    }
    
    @Nullable
    public VotingSession getActiveSession() {
        return this.activeSession;
    }
    
    public ITextComponent getMessage() {
        return this.message;
    }
    
    public int getKilledBosses() {
        return this.killedBosses;
    }
    
    public int getTotalKilledBossesNeeded() {
        return this.totalKilledBossesNeeded;
    }
    
    public int getKnowledge() {
        return this.knowledge;
    }
    
    public int getTotalKnowledgeNeeded() {
        return this.totalKnowledgeNeeded;
    }
}
