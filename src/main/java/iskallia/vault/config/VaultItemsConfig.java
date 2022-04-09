// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultItemsConfig extends Config
{
    @Expose
    public PercentageExpFood VAULT_BURGER;
    @Expose
    public PercentageExpFood VAULT_PIZZA;
    @Expose
    public FlatExpFood VAULT_COOKIE;
    
    @Override
    public String getName() {
        return "vault_items";
    }
    
    @Override
    protected void reset() {
        this.VAULT_BURGER = new PercentageExpFood(0.0f, 0.03f);
        this.VAULT_PIZZA = new PercentageExpFood(0.0f, 0.01f);
        this.VAULT_COOKIE = new FlatExpFood(0, 100);
    }
    
    public static class PercentageExpFood
    {
        @Expose
        public float minExpPercent;
        @Expose
        public float maxExpPercent;
        
        public PercentageExpFood(final float minExpPercent, final float maxExpPercent) {
            this.minExpPercent = minExpPercent;
            this.maxExpPercent = maxExpPercent;
        }
    }
    
    public static class FlatExpFood
    {
        @Expose
        public int minExp;
        @Expose
        public int maxExp;
        
        public FlatExpFood(final int minExp, final int maxExp) {
            this.minExp = minExp;
            this.maxExp = maxExp;
        }
    }
}
