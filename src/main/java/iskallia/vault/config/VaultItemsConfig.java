package iskallia.vault.config;

import com.google.gson.annotations.Expose;


public class VaultItemsConfig
        extends Config {
    @Expose
    public PercentageExpFood VAULT_BURGER;

    public String getName() {
        return "vault_items";
    }

    @Expose
    public PercentageExpFood VAULT_PIZZA;
    @Expose
    public FlatExpFood VAULT_COOKIE;

    protected void reset() {
        this.VAULT_BURGER = new PercentageExpFood(0.0F, 0.03F);
        this.VAULT_PIZZA = new PercentageExpFood(0.0F, 0.01F);
        this.VAULT_COOKIE = new FlatExpFood(0, 100);
    }

    public static class PercentageExpFood {
        @Expose
        public float minExpPercent;
        @Expose
        public float maxExpPercent;

        public PercentageExpFood(float minExpPercent, float maxExpPercent) {
            this.minExpPercent = minExpPercent;
            this.maxExpPercent = maxExpPercent;
        }
    }

    public static class FlatExpFood {
        @Expose
        public int minExp;
        @Expose
        public int maxExp;

        public FlatExpFood(int minExp, int maxExp) {
            this.minExp = minExp;
            this.maxExp = maxExp;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultItemsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */