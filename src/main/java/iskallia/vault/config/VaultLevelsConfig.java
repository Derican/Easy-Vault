// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;

public class VaultLevelsConfig extends Config
{
    @Expose
    public List<VaultLevelMeta> levelMetas;
    
    @Override
    public String getName() {
        return "vault_levels";
    }
    
    public VaultLevelMeta getLevelMeta(final int level) {
        final int maxLevelTNLAvailable = this.levelMetas.size() - 1;
        if (level < 0 || level > maxLevelTNLAvailable) {
            return this.levelMetas.get(maxLevelTNLAvailable);
        }
        return this.levelMetas.get(level);
    }
    
    @Override
    protected void reset() {
        this.levelMetas = new LinkedList<VaultLevelMeta>();
        for (int i = 0; i < 80; ++i) {
            final VaultLevelMeta vaultLevel = new VaultLevelMeta();
            vaultLevel.level = i;
            vaultLevel.tnl = this.defaultTNLFunction(i);
            this.levelMetas.add(vaultLevel);
        }
    }
    
    public int defaultTNLFunction(final int level) {
        return level * 500 + 10000;
    }
    
    public static class VaultLevelMeta
    {
        @Expose
        public int level;
        @Expose
        public int tnl;
    }
}
