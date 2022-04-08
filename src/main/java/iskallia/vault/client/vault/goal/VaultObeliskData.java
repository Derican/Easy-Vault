package iskallia.vault.client.vault.goal;

import iskallia.vault.client.gui.overlay.goal.BossBarOverlay;
import iskallia.vault.network.message.VaultGoalMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;


public class VaultObeliskData
        extends VaultGoalData {
    private ITextComponent message = null;
    private int currentObelisks = 0;
    private int maxObelisks = 0;

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
    public BossBarOverlay getBossBarOverlay() {
        return null;
    }


    public void receive(VaultGoalMessage pkt) {
        CompoundNBT data = pkt.payload;

        this.message = (ITextComponent) ITextComponent.Serializer.fromJson(data.getString("Message"));
        if (data.contains("MaxObelisks", 3)) {
            this.maxObelisks = data.getInt("MaxObelisks");
        }
        if (data.contains("TouchedObelisks", 3))
            this.currentObelisks = data.getInt("TouchedObelisks");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\goal\VaultObeliskData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */