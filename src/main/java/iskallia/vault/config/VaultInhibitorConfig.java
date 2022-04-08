package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultInhibitorConfig extends Config {
    @Expose
    public double CHANCE_TO_EXHAUST;

    public String getName() {
        return "vault_inhibitor";
    }


    protected void reset() {
        this.CHANCE_TO_EXHAUST = 0.2D;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultInhibitorConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */