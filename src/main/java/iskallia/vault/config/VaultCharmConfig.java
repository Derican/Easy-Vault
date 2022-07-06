package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class VaultCharmConfig extends Config {
    @Expose
    private HashMap<Integer, Integer> tierMultipliers;

    public VaultCharmConfig() {
        this.tierMultipliers = new HashMap<Integer, Integer>();
    }

    @Override
    public String getName() {
        return "vault_charm";
    }

    @Override
    protected void reset() {
        this.tierMultipliers.put(1, 3);
        this.tierMultipliers.put(2, 9);
        this.tierMultipliers.put(3, 114);
        this.tierMultipliers.put(4, 228);
    }

    public int getMultiplierForTier(final int tier) {
        return this.tierMultipliers.getOrDefault(tier, 1);
    }

    public HashMap<Integer, Integer> getMultipliers() {
        return this.tierMultipliers;
    }
}
