package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.DashConfig;

public class DashDamageConfig
        extends DashConfig {
    @Expose
    private final float attackDamagePercentPerDash;

    public DashDamageConfig(int cost, int extraRadius, float attackDamagePercentPerDash, float radiusOfAttack) {
        super(cost, extraRadius);
        this.attackDamagePercentPerDash = attackDamagePercentPerDash;
        this.radiusOfAttack = radiusOfAttack;
    }

    @Expose
    private final float radiusOfAttack;

    public float getAttackDamagePercentPerDash() {
        return this.attackDamagePercentPerDash;
    }

    public float getRadiusOfAttack() {
        return this.radiusOfAttack;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\DashDamageConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */