// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.GhostWalkConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkDamageConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkParryConfig;
import iskallia.vault.skill.ability.config.sub.GhostWalkRegenerationConfig;
import iskallia.vault.skill.ability.effect.GhostWalkAbility;

import java.util.ArrayList;
import java.util.List;

public class GhostWalkAbilityGroup extends AbilityGroup<GhostWalkConfig, GhostWalkAbility<GhostWalkConfig>>
{
    @Expose
    private final List<GhostWalkDamageConfig> damageLevelConfiguration;
    @Expose
    private final List<GhostWalkParryConfig> parryLevelConfiguration;
    @Expose
    private final List<GhostWalkRegenerationConfig> regenerationLevelConfiguration;
    
    private GhostWalkAbilityGroup() {
        super("Ghost Walk");
        this.damageLevelConfiguration = new ArrayList<GhostWalkDamageConfig>();
        this.parryLevelConfiguration = new ArrayList<GhostWalkParryConfig>();
        this.regenerationLevelConfiguration = new ArrayList<GhostWalkRegenerationConfig>();
    }
    
    @Override
    protected GhostWalkConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Ghost Walk_Damage": {
                return this.damageLevelConfiguration.get(level);
            }
            case "Ghost Walk_Parry": {
                return this.parryLevelConfiguration.get(level);
            }
            case "Ghost Walk_Regen": {
                return this.regenerationLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Ghost Walk_Damage": {
                return "Warrior";
            }
            case "Ghost Walk_Parry": {
                return "Ethereal";
            }
            case "Ghost Walk_Regen": {
                return "Recovery";
            }
            default: {
                return "Ghost Walk";
            }
        }
    }
    
    public static GhostWalkAbilityGroup defaultConfig() {
        final GhostWalkAbilityGroup group = new GhostWalkAbilityGroup();
        group.addLevel(new GhostWalkConfig(1, 0, 100));
        group.addLevel(new GhostWalkConfig(2, 1, 140));
        group.addLevel(new GhostWalkConfig(3, 2, 180));
        group.addLevel(new GhostWalkConfig(4, 3, 220));
        group.addLevel(new GhostWalkConfig(5, 4, 260));
        group.addLevel(new GhostWalkConfig(6, 5, 300));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(1, 0, 100, 1.0f));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(2, 1, 140, 1.1f));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(3, 2, 180, 1.1f));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(4, 3, 220, 1.2f));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(5, 4, 260, 1.2f));
        group.damageLevelConfiguration.add(new GhostWalkDamageConfig(6, 5, 300, 1.2f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(1, 0, 100, 0.1f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(2, 1, 140, 0.12f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(3, 2, 180, 0.14f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(4, 3, 220, 0.16f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(5, 4, 260, 0.18f));
        group.parryLevelConfiguration.add(new GhostWalkParryConfig(6, 5, 300, 0.2f));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(1, 0, 100, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(2, 1, 200, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(3, 2, 300, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(4, 3, 400, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(5, 4, 500, 1));
        group.regenerationLevelConfiguration.add(new GhostWalkRegenerationConfig(6, 5, 600, 2));
        return group;
    }
}
