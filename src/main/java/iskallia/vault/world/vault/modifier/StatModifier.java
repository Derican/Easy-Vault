package iskallia.vault.world.vault.modifier;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;

public class StatModifier
        extends TexturedVaultModifier {
    @Expose
    protected Statistic stat;

    public StatModifier(String name, ResourceLocation icon, Statistic stat, float multiplier) {
        super(name, icon);
        this.stat = stat;
        this.multiplier = multiplier;
    }

    @Expose
    protected float multiplier;

    public Statistic getStat() {
        return this.stat;
    }

    public float getMultiplier() {
        return this.multiplier;
    }

    public enum Statistic {
        PARRY,
        RESISTANCE,
        COOLDOWN_REDUCTION;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\StatModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */