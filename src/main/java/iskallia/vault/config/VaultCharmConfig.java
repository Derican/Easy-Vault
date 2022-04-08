package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class VaultCharmConfig
        extends Config {
    @Expose
    private HashMap<Integer, Integer> tierMultipliers = new HashMap<>();


    public String getName() {
        return "vault_charm";
    }


    protected void reset() {
        this.tierMultipliers.put(Integer.valueOf(1), Integer.valueOf(3));
        this.tierMultipliers.put(Integer.valueOf(2), Integer.valueOf(9));
        this.tierMultipliers.put(Integer.valueOf(3), Integer.valueOf(114));
    }

    public int getMultiplierForTier(int tier) {
        return ((Integer) this.tierMultipliers.getOrDefault(Integer.valueOf(tier), Integer.valueOf(1))).intValue();
    }

    public HashMap<Integer, Integer> getMultipliers() {
        return this.tierMultipliers;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultCharmConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */