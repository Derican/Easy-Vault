package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.MagnetEntry;
import iskallia.vault.item.VaultMagnetItem;

import java.util.HashMap;

public class VaultUtilitiesConfig
        extends Config {
    @Expose
    private int vaultPearlMaxUses;
    @Expose
    private HashMap<String, MagnetEntry> magnetSettings = new HashMap<>();


    public String getName() {
        return "vault_utilities";
    }


    protected void reset() {
        this.vaultPearlMaxUses = 10;

        this.magnetSettings.put(VaultMagnetItem.MagnetType.WEAK.name(), new MagnetEntry(3.0F, 16.0F, true, false, false, 500));
        this.magnetSettings.put(VaultMagnetItem.MagnetType.STRONG.name(), new MagnetEntry(6.0F, 16.0F, true, true, false, 1000));
        this.magnetSettings.put(VaultMagnetItem.MagnetType.OMEGA.name(), new MagnetEntry(3.0F, 16.0F, true, true, true, 2000));
    }

    public int getVaultPearlMaxUses() {
        return this.vaultPearlMaxUses;
    }


    public MagnetEntry getMagnetSetting(VaultMagnetItem.MagnetType type) {
        return getMagnetSetting(type.name());
    }

    private MagnetEntry getMagnetSetting(String type) {
        return this.magnetSettings.get(type);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultUtilitiesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */