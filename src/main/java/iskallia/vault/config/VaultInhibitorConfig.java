// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultInhibitorConfig extends Config
{
    @Expose
    public double CHANCE_TO_EXHAUST;
    
    @Override
    public String getName() {
        return "vault_inhibitor";
    }
    
    @Override
    protected void reset() {
        this.CHANCE_TO_EXHAUST = 0.2;
    }
}
