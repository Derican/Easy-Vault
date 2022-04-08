package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.MegaJumpConfig;

public class MegaJumpKnockbackConfig
        extends MegaJumpConfig {
    @Expose
    private final float radius;

    public MegaJumpKnockbackConfig(int cost, int extraHeight, float radius, float knockbackStrengthMultiplier) {
        super(cost, extraHeight);
        this.radius = radius;
        this.knockbackStrengthMultiplier = knockbackStrengthMultiplier;
    }

    @Expose
    private final float knockbackStrengthMultiplier;

    public float getRadius() {
        return this.radius;
    }

    public float getKnockbackStrengthMultiplier() {
        return this.knockbackStrengthMultiplier;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\MegaJumpKnockbackConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */