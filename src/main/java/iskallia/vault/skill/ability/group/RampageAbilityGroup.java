package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.RampageConfig;
import iskallia.vault.skill.ability.config.sub.RampageDotConfig;
import iskallia.vault.skill.ability.config.sub.RampageLeechConfig;
import iskallia.vault.skill.ability.config.sub.RampageTimeConfig;
import iskallia.vault.skill.ability.effect.RampageAbility;

import java.util.ArrayList;
import java.util.List;

public class RampageAbilityGroup
        extends AbilityGroup<RampageConfig, RampageAbility<RampageConfig>> {
    @Expose
    private final List<RampageDotConfig> dotLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<RampageTimeConfig> timeLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<RampageLeechConfig> leechLevelConfiguration = new ArrayList<>();

    private RampageAbilityGroup() {
        super("Rampage");
    }


    protected RampageConfig getSubConfig(String specialization, int level) {
        switch (specialization) {
            case "Rampage_Dot":
                return (RampageConfig) this.dotLevelConfiguration.get(level);
            case "Rampage_Time":
                return (RampageConfig) this.timeLevelConfiguration.get(level);
            case "Rampage_Leech":
                return (RampageConfig) this.leechLevelConfiguration.get(level);
        }
        return null;
    }


    public String getSpecializationName(String specialization) {
        switch (specialization) {
            case "Rampage_Dot":
                return "Shaman";
            case "Rampage_Time":
                return "Berserk";
            case "Rampage_Leech":
                return "Vampire";
        }
        return "Rampage";
    }

    public static RampageAbilityGroup defaultConfig() {
        RampageAbilityGroup group = new RampageAbilityGroup();
        group.addLevel((AbilityConfig) new RampageConfig(1, 1.0F, 100, 100));
        group.addLevel((AbilityConfig) new RampageConfig(2, 2.0F, 200, 200));
        group.addLevel((AbilityConfig) new RampageConfig(3, 3.0F, 300, 300));
        group.addLevel((AbilityConfig) new RampageConfig(4, 4.0F, 400, 400));
        group.addLevel((AbilityConfig) new RampageConfig(5, 5.0F, 500, 500));
        group.addLevel((AbilityConfig) new RampageConfig(6, 6.0F, 600, 600));
        group.addLevel((AbilityConfig) new RampageConfig(7, 7.0F, 700, 700));
        group.addLevel((AbilityConfig) new RampageConfig(8, 8.0F, 800, 800));
        group.addLevel((AbilityConfig) new RampageConfig(9, 9.0F, 900, 900));
        group.dotLevelConfiguration.add(new RampageDotConfig(1, 1, 100, 100, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(2, 2, 200, 200, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(3, 3, 300, 300, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(4, 4, 400, 400, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(5, 5, 500, 500, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(6, 6, 600, 600, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(7, 7, 700, 700, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(8, 8, 800, 800, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(9, 9, 900, 900, 5));
        group.leechLevelConfiguration.add(new RampageLeechConfig(1, 100, 100, 0.01F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(2, 200, 200, 0.01F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(3, 300, 300, 0.01F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(4, 400, 400, 0.02F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(5, 500, 500, 0.02F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(6, 600, 600, 0.02F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(7, 700, 700, 0.03F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(8, 800, 800, 0.03F));
        group.leechLevelConfiguration.add(new RampageLeechConfig(9, 900, 900, 0.03F));
        group.timeLevelConfiguration.add(new RampageTimeConfig(1, 1, 100, 100, 4));
        group.timeLevelConfiguration.add(new RampageTimeConfig(2, 2, 200, 200, 6));
        group.timeLevelConfiguration.add(new RampageTimeConfig(3, 3, 300, 300, 8));
        group.timeLevelConfiguration.add(new RampageTimeConfig(4, 4, 400, 400, 10));
        group.timeLevelConfiguration.add(new RampageTimeConfig(5, 5, 500, 500, 12));
        group.timeLevelConfiguration.add(new RampageTimeConfig(6, 6, 600, 600, 14));
        group.timeLevelConfiguration.add(new RampageTimeConfig(7, 7, 700, 700, 16));
        group.timeLevelConfiguration.add(new RampageTimeConfig(8, 8, 800, 800, 18));
        group.timeLevelConfiguration.add(new RampageTimeConfig(9, 9, 900, 900, 20));
        return group;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\group\RampageAbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */