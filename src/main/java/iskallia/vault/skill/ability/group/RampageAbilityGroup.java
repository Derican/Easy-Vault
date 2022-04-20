package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.RampageConfig;
import iskallia.vault.skill.ability.config.sub.RampageDotConfig;
import iskallia.vault.skill.ability.config.sub.RampageLeechConfig;
import iskallia.vault.skill.ability.config.sub.RampageTimeConfig;
import iskallia.vault.skill.ability.effect.RampageAbility;

import java.util.ArrayList;
import java.util.List;

public class RampageAbilityGroup extends AbilityGroup<RampageConfig, RampageAbility<RampageConfig>> {
    @Expose
    private final List<RampageDotConfig> dotLevelConfiguration;
    @Expose
    private final List<RampageTimeConfig> timeLevelConfiguration;
    @Expose
    private final List<RampageLeechConfig> leechLevelConfiguration;

    private RampageAbilityGroup() {
        super("Rampage");
        this.dotLevelConfiguration = new ArrayList<RampageDotConfig>();
        this.timeLevelConfiguration = new ArrayList<RampageTimeConfig>();
        this.leechLevelConfiguration = new ArrayList<RampageLeechConfig>();
    }

    @Override
    protected RampageConfig getSubConfig(final String specialization, final int level) {
        switch (specialization) {
            case "Rampage_Dot": {
                return this.dotLevelConfiguration.get(level);
            }
            case "Rampage_Time": {
                return this.timeLevelConfiguration.get(level);
            }
            case "Rampage_Leech": {
                return this.leechLevelConfiguration.get(level);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public String getSpecializationName(final String specialization) {
        switch (specialization) {
            case "Rampage_Dot": {
                return "Shaman";
            }
            case "Rampage_Time": {
                return "Berserk";
            }
            case "Rampage_Leech": {
                return "Vampire";
            }
            default: {
                return "Rampage";
            }
        }
    }

    public static RampageAbilityGroup defaultConfig() {
        final RampageAbilityGroup group = new RampageAbilityGroup();
        group.addLevel(new RampageConfig(1, 1.0f, 100, 100));
        group.addLevel(new RampageConfig(2, 2.0f, 200, 200));
        group.addLevel(new RampageConfig(3, 3.0f, 300, 300));
        group.addLevel(new RampageConfig(4, 4.0f, 400, 400));
        group.addLevel(new RampageConfig(5, 5.0f, 500, 500));
        group.addLevel(new RampageConfig(6, 6.0f, 600, 600));
        group.addLevel(new RampageConfig(7, 7.0f, 700, 700));
        group.addLevel(new RampageConfig(8, 8.0f, 800, 800));
        group.addLevel(new RampageConfig(9, 9.0f, 900, 900));
        group.dotLevelConfiguration.add(new RampageDotConfig(1, 1, 100, 100, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(2, 2, 200, 200, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(3, 3, 300, 300, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(4, 4, 400, 400, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(5, 5, 500, 500, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(6, 6, 600, 600, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(7, 7, 700, 700, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(8, 8, 800, 800, 5));
        group.dotLevelConfiguration.add(new RampageDotConfig(9, 9, 900, 900, 5));
        group.leechLevelConfiguration.add(new RampageLeechConfig(1, 100, 100, 0.01f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(2, 200, 200, 0.01f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(3, 300, 300, 0.01f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(4, 400, 400, 0.02f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(5, 500, 500, 0.02f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(6, 600, 600, 0.02f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(7, 700, 700, 0.03f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(8, 800, 800, 0.03f));
        group.leechLevelConfiguration.add(new RampageLeechConfig(9, 900, 900, 0.03f));
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
