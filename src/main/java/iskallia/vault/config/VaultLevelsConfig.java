package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;

public class VaultLevelsConfig
        extends Config {
    @Expose
    public List<VaultLevelMeta> levelMetas;

    public String getName() {
        return "vault_levels";
    }

    public VaultLevelMeta getLevelMeta(int level) {
        int maxLevelTNLAvailable = this.levelMetas.size() - 1;

        if (level < 0 || level > maxLevelTNLAvailable) {
            return this.levelMetas.get(maxLevelTNLAvailable);
        }
        return this.levelMetas.get(level);
    }


    protected void reset() {
        this.levelMetas = new LinkedList<>();

        for (int i = 0; i < 80; i++) {
            VaultLevelMeta vaultLevel = new VaultLevelMeta();
            vaultLevel.level = i;
            vaultLevel.tnl = defaultTNLFunction(i);
            this.levelMetas.add(vaultLevel);
        }
    }

    public int defaultTNLFunction(int level) {
        return level * 500 + 10000;
    }

    public static class VaultLevelMeta {
        @Expose
        public int level;
        @Expose
        public int tnl;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultLevelsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */