package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;

public class VaultStewConfig
        extends Config {
    @Expose
    public WeightedList<String> STEW_POOL;

    public String getName() {
        return "vault_stew";
    }


    protected void reset() {
        this.STEW_POOL = new WeightedList();

        this.STEW_POOL.add(ModItems.VAULT_STEW_NORMAL.getRegistryName().toString(), 20)
                .add(ModItems.VAULT_STEW_RARE.getRegistryName().toString(), 10)
                .add(ModItems.VAULT_STEW_EPIC.getRegistryName().toString(), 5)
                .add(ModItems.VAULT_STEW_OMEGA.getRegistryName().toString(), 1);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultStewConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */