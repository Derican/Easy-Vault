package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.GhostWalkConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkDamageConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkParryConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkRegenerationConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

import java.util.ArrayList;
import java.util.List;

public class GhostWalkAbilityGroup
        extends AbilityGroup<GhostWalkConfig, GhostWalkAbility<GhostWalkConfig>> {
    @Expose
    private final List<GhostWalkDamageConfig> damageLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<GhostWalkParryConfig> parryLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<GhostWalkRegenerationConfig> regenerationLevelConfiguration = new ArrayList<>();

    private GhostWalkAbilityGroup() {
        super("Ghost Walk");
    }


    protected GhostWalkConfig getSubConfig(String specialization, int level) {
        switch (specialization) {
            case "Ghost Walk_Damage":
                return (GhostWalkConfig) this.damageLevelConfiguration.get(level);
            case "Ghost Walk_Parry":
                return (GhostWalkConfig) this.parryLevelConfiguration.get(level);
            case "Ghost Walk_Regen":
                return (GhostWalkConfig) this.regenerationLevelConfiguration.get(level);
        }
        return null;
    }


    public String getSpecializationName(String specialization) {
        switch (specialization) {
            case "Ghost Walk_Damage":
                return "Warrior";
            case "Ghost Walk_Parry":
                return "Ethereal";
            case "Ghost Walk_Regen":
                return "Recovery";
        }
        return "Ghost Walk";
    }

    public static GhostWalkAbilityGroup defaultConfig() {
        GhostWalkAbilityGroup group = new GhostWalkAbilityGroup();
        group.addLevel((AbilityConfig) new GhostWalkConfig(1, 0, 100));
        group.addLevel((AbilityConfig) new GhostWalkConfig(2, 1, 140));
        group.addLevel((AbilityConfig) new GhostWalkConfig(3, 2, 180));
        group.addLevel((AbilityConfig) new GhostWalkConfig(4, 3, 220));
        group.addLevel((AbilityConfig) new GhostWalkConfig(5, 4, 260));
        group.addLevel((AbilityConfig) new GhostWalkConfig(6, 5, 300));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(1, 0, 100, 1.0F));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(2, 1, 140, 1.1F));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(3, 2, 180, 1.1F));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(4, 3, 220, 1.2F));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(5, 4, 260, 1.2F));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(6, 5, 300, 1.2F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(1, 0, 100, 0.1F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(2, 1, 140, 0.12F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(3, 2, 180, 0.14F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(4, 3, 220, 0.16F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(5, 4, 260, 0.18F));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(6, 5, 300, 0.2F));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(1, 0, 100, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(2, 1, 200, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(3, 2, 300, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(4, 3, 400, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(5, 4, 500, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(6, 5, 600, 2));
        return group;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\group\GhostWalkAbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */