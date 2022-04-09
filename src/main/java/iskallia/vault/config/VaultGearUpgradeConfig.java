// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class VaultGearUpgradeConfig extends Config
{
    @Expose
    private String defaultUpgradeRarity;
    @Expose
    private Map<String, String> upgradeMapping;
    
    @Override
    public String getName() {
        return "vault_gear_upgrade";
    }
    
    public String getUpgradedRarity(final String rarity) {
        return this.upgradeMapping.getOrDefault(rarity, this.defaultUpgradeRarity);
    }
    
    @Override
    protected void reset() {
        this.defaultUpgradeRarity = "Scrappy";
        (this.upgradeMapping = new HashMap<String, String>()).put("Scrappy", "Scrappy");
        this.upgradeMapping.put("Scrappy+", "Scrappy");
        this.upgradeMapping.put("Common", "Scrappy");
        this.upgradeMapping.put("Common+", "Scrappy+");
        this.upgradeMapping.put("Rare", "Scrappy+");
        this.upgradeMapping.put("Rare+", "Scrappy+");
        this.upgradeMapping.put("Epic", "Common");
        this.upgradeMapping.put("Epic+", "Common+");
        this.upgradeMapping.put("Omega", "Rare");
        this.upgradeMapping.put("Crafted", "Scrappy");
        this.upgradeMapping.put("Polished", "Scrappy+");
        this.upgradeMapping.put("Glorious", "Polished");
        this.upgradeMapping.put("Flawless", "Glorious");
        this.upgradeMapping.put("Masterful", "Flawless");
        this.upgradeMapping.put("Perfected", "Masterful");
    }
}
