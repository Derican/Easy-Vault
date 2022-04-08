package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class MegaJumpConfig extends AbilityConfig {
    @Expose
    private final int height;

    public MegaJumpConfig(int cost, int height) {
        super(cost, AbilityConfig.Behavior.RELEASE_TO_PERFORM);
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\MegaJumpConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */