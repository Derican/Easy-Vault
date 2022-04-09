// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.DashConfig;
import iskallia.vault.skill.ability.config.sub.DashBuffConfig;
import iskallia.vault.skill.ability.config.sub.DashDamageConfig;
import iskallia.vault.skill.ability.config.sub.DashHealConfig;
import iskallia.vault.skill.ability.effect.DashAbility;

import java.util.ArrayList;
import java.util.List;

public class DashAbilityGroup extends AbilityGroup<DashConfig, DashAbility<DashConfig>>
{
    @Expose
    private final List<DashBuffConfig> buffLevelConfiguration;
    @Expose
    private final List<DashDamageConfig> damageLevelConfiguration;
    @Expose
    private final List<DashHealConfig> healLevelConfiguration;
    
    private DashAbilityGroup() {
        super("Dash");
        this.buffLevelConfiguration = new ArrayList<DashBuffConfig>();
        this.damageLevelConfiguration = new ArrayList<DashDamageConfig>();
        this.healLevelConfiguration = new ArrayList<DashHealConfig>();
    }
    
    @Override
    protected DashConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Dash_Buff": {
                return this.buffLevelConfiguration.get(level);
            }
            case "Dash_Damage": {
                return this.damageLevelConfiguration.get(level);
            }
            case "Dash_Heal": {
                return this.healLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Dash_Buff": {
                return "Power-Up";
            }
            case "Dash_Damage": {
                return "Bullet";
            }
            case "Dash_Heal": {
                return "Recharge";
            }
            default: {
                return "Dash";
            }
        }
    }
    
    public static DashAbilityGroup defaultConfig() {
        final DashAbilityGroup group = new DashAbilityGroup();
        group.addLevel(new DashConfig(2, 1));
        group.addLevel(new DashConfig(1, 2));
        group.addLevel(new DashConfig(1, 3));
        group.addLevel(new DashConfig(1, 4));
        group.addLevel(new DashConfig(1, 5));
        group.addLevel(new DashConfig(1, 6));
        group.addLevel(new DashConfig(1, 7));
        group.addLevel(new DashConfig(1, 8));
        group.addLevel(new DashConfig(1, 9));
        group.addLevel(new DashConfig(1, 10));
        group.buffLevelConfiguration.add(new DashBuffConfig(2, 1, 0.1f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 2, 0.1f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 3, 0.15f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 4, 0.15f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 5, 0.15f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 6, 0.15f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 7, 0.2f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 8, 0.2f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 9, 0.2f, 140));
        group.buffLevelConfiguration.add(new DashBuffConfig(1, 10, 0.25f, 140));
        group.damageLevelConfiguration.add(new DashDamageConfig(2, 1, 0.5f, 4.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 2, 0.5f, 4.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 3, 0.6f, 5.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 4, 0.75f, 5.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 5, 1.0f, 6.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 6, 1.25f, 6.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 7, 1.5f, 7.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 8, 1.75f, 7.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 9, 2.0f, 7.0f));
        group.damageLevelConfiguration.add(new DashDamageConfig(1, 10, 2.5f, 8.0f));
        group.healLevelConfiguration.add(new DashHealConfig(2, 1, 1.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 2, 1.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 3, 2.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 4, 2.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 5, 2.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 6, 4.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 7, 4.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 8, 4.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 9, 6.0f));
        group.healLevelConfiguration.add(new DashHealConfig(1, 10, 8.0f));
        return group;
    }
}
