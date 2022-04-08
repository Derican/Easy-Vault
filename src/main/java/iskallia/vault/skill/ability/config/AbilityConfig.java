package iskallia.vault.skill.ability.config;

import com.google.gson.annotations.Expose;

public class AbilityConfig {
    @Expose
    private final int learningCost;
    @Expose
    private final Behavior behavior;

    public AbilityConfig(int learningCost, Behavior behavior) {
        this(learningCost, behavior, 200);
    }

    @Expose
    private final int cooldown;
    @Expose
    private final int levelRequirement;

    public AbilityConfig(int learningCost, Behavior behavior, int cooldown) {
        this(learningCost, behavior, cooldown, 0);
    }

    public AbilityConfig(int learningCost, Behavior behavior, int cooldown, int levelRequirement) {
        this.learningCost = learningCost;
        this.behavior = behavior;
        this.cooldown = cooldown;
        this.levelRequirement = levelRequirement;
    }

    public int getLearningCost() {
        return this.learningCost;
    }

    public Behavior getBehavior() {
        return this.behavior;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public int getLevelRequirement() {
        return this.levelRequirement;
    }

    public enum Behavior {
        HOLD_TO_ACTIVATE,
        PRESS_TO_TOGGLE,
        RELEASE_TO_PERFORM;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\AbilityConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */