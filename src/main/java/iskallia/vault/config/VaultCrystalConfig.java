package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.Random;


public class VaultCrystalConfig
        extends Config {
    @Expose
    public int NORMAL_WEIGHT;
    @Expose
    public int RARE_WEIGHT;
    @Expose
    public int EPIC_WEIGHT;
    @Expose
    public int OMEGA_WEIGHT;
    private Random rand = new Random();


    public String getName() {
        return "vault_crystal";
    }


    protected void reset() {
        this.NORMAL_WEIGHT = 20;
        this.RARE_WEIGHT = 10;
        this.EPIC_WEIGHT = 5;
        this.OMEGA_WEIGHT = 1;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultCrystalConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */