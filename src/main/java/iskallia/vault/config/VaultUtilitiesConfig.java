// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.MagnetEntry;
import iskallia.vault.item.VaultMagnetItem;

import java.util.HashMap;

public class VaultUtilitiesConfig extends Config
{
    @Expose
    private int vaultPearlMaxUses;
    @Expose
    private HashMap<String, MagnetEntry> magnetSettings;
    
    public VaultUtilitiesConfig() {
        this.magnetSettings = new HashMap<String, MagnetEntry>();
    }
    
    @Override
    public String getName() {
        return "vault_utilities";
    }
    
    @Override
    protected void reset() {
        this.vaultPearlMaxUses = 10;
        this.magnetSettings.put(VaultMagnetItem.MagnetType.WEAK.name(), new MagnetEntry(3.0f, 16.0f, true, false, false, 500));
        this.magnetSettings.put(VaultMagnetItem.MagnetType.STRONG.name(), new MagnetEntry(6.0f, 16.0f, true, true, false, 1000));
        this.magnetSettings.put(VaultMagnetItem.MagnetType.OMEGA.name(), new MagnetEntry(3.0f, 16.0f, true, true, true, 2000));
    }
    
    public int getVaultPearlMaxUses() {
        return this.vaultPearlMaxUses;
    }
    
    public MagnetEntry getMagnetSetting(final VaultMagnetItem.MagnetType type) {
        return this.getMagnetSetting(type.name());
    }
    
    private MagnetEntry getMagnetSetting(final String type) {
        return this.magnetSettings.get(type);
    }
}
