// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.SummonEternalConfig;
import iskallia.vault.skill.ability.config.sub.SummonEternalCountConfig;
import iskallia.vault.skill.ability.config.sub.SummonEternalDamageConfig;
import iskallia.vault.skill.ability.config.sub.SummonEternalDebuffConfig;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;

import java.util.ArrayList;
import java.util.List;

public class SummonEternalAbilityGroup extends AbilityGroup<SummonEternalConfig, SummonEternalAbility<SummonEternalConfig>>
{
    @Expose
    private final List<SummonEternalCountConfig> countLevelConfiguration;
    @Expose
    private final List<SummonEternalDamageConfig> damageLevelConfiguration;
    @Expose
    private final List<SummonEternalDebuffConfig> debuffLevelConfiguration;
    
    private SummonEternalAbilityGroup() {
        super("Summon Eternal");
        this.countLevelConfiguration = new ArrayList<SummonEternalCountConfig>();
        this.damageLevelConfiguration = new ArrayList<SummonEternalDamageConfig>();
        this.debuffLevelConfiguration = new ArrayList<SummonEternalDebuffConfig>();
    }
    
    @Override
    protected SummonEternalConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Summon Eternal_Additional": {
                return this.countLevelConfiguration.get(level);
            }
            case "Summon Eternal_Damage": {
                return this.damageLevelConfiguration.get(level);
            }
            case "Summon Eternal_Debuffs": {
                return this.debuffLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Summon Eternal_Additional": {
                return "Army";
            }
            case "Summon Eternal_Damage": {
                return "Overpower";
            }
            case "Summon Eternal_Debuffs": {
                return "Ghoul";
            }
            default: {
                return "Summon Eternal";
            }
        }
    }
    
    public static SummonEternalAbilityGroup defaultConfig() {
        final SummonEternalAbilityGroup group = new SummonEternalAbilityGroup();
        group.addLevel(new SummonEternalConfig(1, 12000, 1, 12000, 0.2f, true));
        group.addLevel(new SummonEternalConfig(1, 10800, 1, 10800, 0.2f, true));
        group.addLevel(new SummonEternalConfig(1, 9600, 1, 9600, 0.2f, false));
        group.addLevel(new SummonEternalConfig(1, 8400, 1, 8400, 0.2f, false));
        group.addLevel(new SummonEternalConfig(1, 7200, 1, 7200, 0.2f, false));
        group.countLevelConfiguration.add(new SummonEternalCountConfig(1, 12000, 1, 12000, true, 0.2f, 1));
        group.countLevelConfiguration.add(new SummonEternalCountConfig(1, 10800, 1, 10800, true, 0.2f, 1));
        group.countLevelConfiguration.add(new SummonEternalCountConfig(1, 9600, 1, 9600, false, 0.2f, 1));
        group.countLevelConfiguration.add(new SummonEternalCountConfig(1, 8400, 1, 8400, false, 0.2f, 1));
        group.countLevelConfiguration.add(new SummonEternalCountConfig(1, 7200, 1, 7200, false, 0.2f, 1));
        group.debuffLevelConfiguration.add(new SummonEternalDebuffConfig(1, 12000, 1, 12000, true, 0.2f, 0.1f, 20, 0));
        group.debuffLevelConfiguration.add(new SummonEternalDebuffConfig(1, 10800, 1, 10800, true, 0.2f, 0.2f, 30, 0));
        group.debuffLevelConfiguration.add(new SummonEternalDebuffConfig(1, 9600, 1, 9600, false, 0.2f, 0.3f, 40, 1));
        group.debuffLevelConfiguration.add(new SummonEternalDebuffConfig(1, 8400, 1, 8400, false, 0.2f, 0.4f, 60, 1));
        group.debuffLevelConfiguration.add(new SummonEternalDebuffConfig(1, 7200, 1, 7200, false, 0.2f, 0.5f, 80, 1));
        group.damageLevelConfiguration.add(new SummonEternalDamageConfig(1, 12000, 1, 12000, true, 0.2f, 0.1f));
        group.damageLevelConfiguration.add(new SummonEternalDamageConfig(1, 10800, 1, 10800, true, 0.2f, 0.2f));
        group.damageLevelConfiguration.add(new SummonEternalDamageConfig(1, 9600, 1, 9600, false, 0.2f, 0.25f));
        group.damageLevelConfiguration.add(new SummonEternalDamageConfig(1, 8400, 1, 8400, false, 0.2f, 0.3f));
        group.damageLevelConfiguration.add(new SummonEternalDamageConfig(1, 7200, 1, 7200, false, 0.2f, 0.35f));
        return group;
    }
}
