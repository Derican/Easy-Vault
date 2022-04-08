package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.MegaJumpConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpBreakConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpDamageConfig;
import iskallia.vault.skill.ability.config.sub.MegaJumpKnockbackConfig;
import iskallia.vault.skill.ability.effect.MegaJumpAbility;

import java.util.ArrayList;
import java.util.List;

public class MegaJumpAbilityGroup
        extends AbilityGroup<MegaJumpConfig, MegaJumpAbility<MegaJumpConfig>> {
    @Expose
    private final List<MegaJumpKnockbackConfig> knockbackLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<MegaJumpDamageConfig> damageLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<MegaJumpBreakConfig> breakLevelConfiguration = new ArrayList<>();

    private MegaJumpAbilityGroup() {
        super("Mega Jump");
    }


    protected MegaJumpConfig getSubConfig(String specialization, int level) {
        switch (specialization) {
            case "Mega Jump_Knockback":
                return (MegaJumpConfig) this.knockbackLevelConfiguration.get(level);
            case "Mega Jump_Damage":
                return (MegaJumpConfig) this.damageLevelConfiguration.get(level);
            case "Mega Jump_Break":
                return (MegaJumpConfig) this.breakLevelConfiguration.get(level);
        }
        return null;
    }


    public String getSpecializationName(String specialization) {
        switch (specialization) {
            case "Mega Jump_Knockback":
                return "Fart";
            case "Mega Jump_Damage":
                return "Shockwave";
            case "Mega Jump_Break":
                return "Drill";
        }
        return "Mega Jump";
    }

    public static MegaJumpAbilityGroup defaultConfig() {
        MegaJumpAbilityGroup group = new MegaJumpAbilityGroup();
        group.addLevel((AbilityConfig) new MegaJumpConfig(1, 10));
        group.addLevel((AbilityConfig) new MegaJumpConfig(1, 12));
        group.addLevel((AbilityConfig) new MegaJumpConfig(1, 13));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 10, 5.0F, 3.0F));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 12, 6.0F, 5.0F));
        group.knockbackLevelConfiguration.add(new MegaJumpKnockbackConfig(1, 13, 7.0F, 7.0F));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 10, 5.0F, 1.0F, 1.0F));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 12, 6.0F, 1.5F, 1.5F));
        group.damageLevelConfiguration.add(new MegaJumpDamageConfig(1, 13, 7.0F, 2.0F, 2.5F));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 10));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 12));
        group.breakLevelConfiguration.add(new MegaJumpBreakConfig(1, 13));
        return group;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\group\MegaJumpAbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */