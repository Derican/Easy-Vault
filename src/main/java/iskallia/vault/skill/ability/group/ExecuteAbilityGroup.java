package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.ExecuteConfig;
import iskallia.vault.skill.ability.config.sub.ExecuteBuffConfig;
import iskallia.vault.skill.ability.config.sub.ExecuteDamageConfig;
import iskallia.vault.skill.ability.effect.ExecuteAbility;

import java.util.ArrayList;
import java.util.List;

public class ExecuteAbilityGroup extends AbilityGroup<ExecuteConfig, ExecuteAbility<ExecuteConfig>> {
    @Expose
    private final List<ExecuteBuffConfig> buffLevelConfiguration;
    @Expose
    private final List<ExecuteDamageConfig> damageLevelConfiguration;

    private ExecuteAbilityGroup() {
        super("Execute");
        this.buffLevelConfiguration = new ArrayList<ExecuteBuffConfig>();
        this.damageLevelConfiguration = new ArrayList<ExecuteDamageConfig>();
    }

    @Override
    protected ExecuteConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Execute_Buff": {
                return this.buffLevelConfiguration.get(level);
            }
            case "Execute_Damage": {
                return this.damageLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Execute_Buff": {
                return "Combo";
            }
            case "Execute_Damage": {
                return "Precision";
            }
            default: {
                return "Execute";
            }
        }
    }

    public static ExecuteAbilityGroup defaultConfig() {
        final ExecuteAbilityGroup group = new ExecuteAbilityGroup();
        group.addLevel(new ExecuteConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.05f, 100));
        group.addLevel(new ExecuteConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.1f, 100));
        group.addLevel(new ExecuteConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.15f, 100));
        group.addLevel(new ExecuteConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.2f, 100));
        group.addLevel(new ExecuteConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.25f, 100));
        group.addLevel(new ExecuteConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.3f, 100));
        group.addLevel(new ExecuteConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.35f, 100));
        group.addLevel(new ExecuteConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.4f, 100));
        group.addLevel(new ExecuteConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.45f, 100));
        group.addLevel(new ExecuteConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.5f, 100));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.05f, 100, 0.05f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.1f, 100, 0.1f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.15f, 100, 0.15f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.2f, 100, 0.2f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.25f, 100, 0.25f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.3f, 100, 0.3f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.35f, 100, 0.35f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.4f, 100, 0.4f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.45f, 100, 0.45f));
        group.buffLevelConfiguration.add(new ExecuteBuffConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 0.5f, 100, 0.5f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.1f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(1, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.2f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.3f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(2, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.4f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.5f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(3, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.6f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.7f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(4, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.8f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 0.9f));
        group.damageLevelConfiguration.add(new ExecuteDamageConfig(5, AbilityConfig.Behavior.RELEASE_TO_PERFORM, 100, 1.0f));
        return group;
    }
}
