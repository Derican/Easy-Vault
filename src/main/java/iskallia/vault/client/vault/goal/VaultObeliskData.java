// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class VaultObeliskData extends VaultGoalData
{
    private ITextComponent message;
    private int currentObelisks;
    private int maxObelisks;
    
    public VaultObeliskData() {
        this.message = null;
        this.currentObelisks = 0;
        this.maxObelisks = 0;
    }
    
    public ITextComponent getMessage() {
        return this.message;
    }
    
    public int getCurrentObelisks() {
        return this.currentObelisks;
    }
    
    public int getMaxObelisks() {
        return this.maxObelisks;
    }
    
    @Nullable
    @Override
    public BossBarOverlay getBossBarOverlay() {
        return null;
    }
    
    @Override
    public void receive(final VaultGoalMessage pkt) {
        final CompoundNBT data = pkt.payload;
        this.message = (ITextComponent)ITextComponent.Serializer.fromJson(data.getString("Message"));
        if (data.contains("MaxObelisks", 3)) {
            this.maxObelisks = data.getInt("MaxObelisks");
        }
        if (data.contains("TouchedObelisks", 3)) {
            this.currentObelisks = data.getInt("TouchedObelisks");
        }
    }
}
