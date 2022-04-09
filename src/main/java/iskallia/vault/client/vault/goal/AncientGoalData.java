// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class AncientGoalData extends VaultGoalData
{
    private int totalAncients;
    private int foundAncients;
    
    public AncientGoalData() {
        this.totalAncients = 0;
        this.foundAncients = 0;
    }
    
    public int getTotalAncients() {
        return this.totalAncients;
    }
    
    public int getFoundAncients() {
        return this.foundAncients;
    }
    
    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return null;
    }
    
    @Override
    public void receive(final VaultGoalMessage pkt) {
        final CompoundNBT data = pkt.payload;
        this.totalAncients = data.getInt("total");
        this.foundAncients = data.getInt("found");
    }
}
