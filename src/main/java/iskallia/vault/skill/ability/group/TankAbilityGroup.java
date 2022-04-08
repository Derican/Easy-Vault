package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.TankConfig;
import iskallia.vault.skill.ability.config.sub.TankParryConfig;
import iskallia.vault.skill.ability.config.sub.TankReflectConfig;
import iskallia.vault.skill.ability.config.sub.TankSlowConfig;
import iskallia.vault.skill.ability.effect.TankAbility;

import java.util.ArrayList;
import java.util.List;

public class TankAbilityGroup
        extends AbilityGroup<TankConfig, TankAbility<TankConfig>> {
    @Expose
    private final List<TankParryConfig> parryLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<TankReflectConfig> reflectLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<TankSlowConfig> slowLevelConfiguration = new ArrayList<>();

    private TankAbilityGroup() {
        super("Tank");
    }


    protected TankConfig getSubConfig(String specialization, int level) {
        switch (specialization) {
            case "Tank_Parry":
                return (TankConfig) this.parryLevelConfiguration.get(level);
            case "Tank_Reflect":
                return (TankConfig) this.reflectLevelConfiguration.get(level);
            case "Tank_Slow":
                return (TankConfig) this.slowLevelConfiguration.get(level);
        }
        return null;
    }


    public String getSpecializationName(String specialization) {
        switch (specialization) {
            case "Tank_Parry":
                return "Ghost";
            case "Tank_Reflect":
                return "Spike";
            case "Tank_Slow":
                return "Chill";
        }
        return "Tank";
    }

    public static TankAbilityGroup defaultConfig() {
        TankAbilityGroup group = new TankAbilityGroup();
        group.addLevel((AbilityConfig) new TankConfig(3, 100, 0.1F));
        group.addLevel((AbilityConfig) new TankConfig(3, 200, 0.13F));
        group.addLevel((AbilityConfig) new TankConfig(3, 300, 0.16F));
        group.addLevel((AbilityConfig) new TankConfig(3, 400, 0.18F));
        group.addLevel((AbilityConfig) new TankConfig(3, 500, 0.2F));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 100, 0.15F));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 200, 0.17F));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 300, 0.19F));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 400, 0.22F));
        group.parryLevelConfiguration.add(new TankParryConfig(3, 500, 0.25F));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 100, 0.1F, 0.3F, 0.2F));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 200, 0.13F, 0.3F, 0.24F));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 300, 0.16F, 0.3F, 0.28F));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 400, 0.18F, 0.3F, 0.31F));
        group.reflectLevelConfiguration.add(new TankReflectConfig(3, 500, 0.2F, 0.3F, 0.35F));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 100, 0.15F, 2.5F, 0));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 200, 0.17F, 2.8F, 0));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 300, 0.19F, 3.1F, 1));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 400, 0.22F, 3.5F, 1));
        group.slowLevelConfiguration.add(new TankSlowConfig(3, 500, 0.25F, 4.5F, 2));
        return group;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\group\TankAbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */