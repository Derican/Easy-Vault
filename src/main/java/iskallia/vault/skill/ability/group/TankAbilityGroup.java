// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.TankConfig;
import iskallia.vault.skill.ability.config.sub.TankParryConfig;
import iskallia.vault.skill.ability.config.sub.TankReflectConfig;
import iskallia.vault.skill.ability.config.sub.TankSlowConfig;
import iskallia.vault.skill.ability.effect.TankAbility;

import java.util.ArrayList;
import java.util.List;

public class TankAbilityGroup extends AbilityGroup<TankConfig, TankAbility<TankConfig>>
{
    @Expose
    private final List<TankParryConfig> parryLevelConfiguration;
    @Expose
    private final List<TankReflectConfig> reflectLevelConfiguration;
    @Expose
    private final List<TankSlowConfig> slowLevelConfiguration;
    
    private TankAbilityGroup() {
        super("Tank");
        this.parryLevelConfiguration = new ArrayList<TankParryConfig>();
        this.reflectLevelConfiguration = new ArrayList<TankReflectConfig>();
        this.slowLevelConfiguration = new ArrayList<TankSlowConfig>();
    }
    
    @Override
    protected TankConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Tank_Parry": {
                return this.parryLevelConfiguration.get(level);
            }
            case "Tank_Reflect": {
                return this.reflectLevelConfiguration.get(level);
            }
            case "Tank_Slow": {
                return this.slowLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Tank_Parry": {
                return "Ghost";
            }
            case "Tank_Reflect": {
                return "Spike";
            }
            case "Tank_Slow": {
                return "Chill";
            }
            default: {
                return "Tank";
            }
        }
    }
    
    public static TankAbilityGroup defaultConfig() {
        final TankAbilityGroup group = new TankAbilityGroup();
        group.addLevel(new TankConfig(3, 100, 0.1f));
        group.addLevel(new TankConfig(3, 200, 0.13f));
        group.addLevel(new TankConfig(3, 300, 0.16f));
        group.addLevel(new TankConfig(3, 400, 0.18f));
        group.addLevel(new TankConfig(3, 500, 0.2f));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 100, 0.15f));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 200, 0.17f));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 300, 0.19f));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 400, 0.22f));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 500, 0.25f));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 100, 0.1f, 0.3f, 0.2f));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 200, 0.13f, 0.3f, 0.24f));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 300, 0.16f, 0.3f, 0.28f));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 400, 0.18f, 0.3f, 0.31f));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 500, 0.2f, 0.3f, 0.35f));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 100, 0.15f, 2.5f, 0));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 200, 0.17f, 2.8f, 0));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 300, 0.19f, 3.1f, 1));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 400, 0.22f, 3.5f, 1));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 500, 0.25f, 4.5f, 2));
        return group;
    }
}
