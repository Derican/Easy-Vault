// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpBreakConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpDamageConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpKnockbackConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;

import java.util.ArrayList;
import java.util.List;

public class MegaJumpAbilityGroup extends AbilityGroup<MegaJumpConfig, MegaJumpAbility<MegaJumpConfig>>
{
    @Expose
    private final List<MegaJumpKnockbackConfig> knockbackLevelConfiguration;
    @Expose
    private final List<MegaJumpDamageConfig> damageLevelConfiguration;
    @Expose
    private final List<MegaJumpBreakConfig> breakLevelConfiguration;
    
    private MegaJumpAbilityGroup() {
        super("Mega Jump");
        this.knockbackLevelConfiguration = new ArrayList<MegaJumpKnockbackConfig>();
        this.damageLevelConfiguration = new ArrayList<MegaJumpDamageConfig>();
        this.breakLevelConfiguration = new ArrayList<MegaJumpBreakConfig>();
    }
    
    @Override
    protected MegaJumpConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Mega Jump_Knockback": {
                return this.knockbackLevelConfiguration.get(level);
            }
            case "Mega Jump_Damage": {
                return this.damageLevelConfiguration.get(level);
            }
            case "Mega Jump_Break": {
                return this.breakLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Mega Jump_Knockback": {
                return "Fart";
            }
            case "Mega Jump_Damage": {
                return "Shockwave";
            }
            case "Mega Jump_Break": {
                return "Drill";
            }
            default: {
                return "Mega Jump";
            }
        }
    }
    
    public static MegaJumpAbilityGroup defaultConfig() {
        final MegaJumpAbilityGroup group = new MegaJumpAbilityGroup();
        group.addLevel(new MegaJumpConfig(1, 10));
        group.addLevel(new MegaJumpConfig(1, 12));
        group.addLevel(new MegaJumpConfig(1, 13));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 10, 5.0f, 3.0f));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 12, 6.0f, 5.0f));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 13, 7.0f, 7.0f));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 10, 5.0f, 1.0f, 1.0f));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 12, 6.0f, 1.5f, 1.5f));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 13, 7.0f, 2.0f, 2.5f));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 10));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 12));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 13));
        return group;
    }
}
