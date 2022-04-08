package iskallia.vault.skill.ability.group;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.config.VeinMinerConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerDurabilityConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerFortuneConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerSizeDurabilityConfig;
import iskallia.vault.skill.ability.config.sub.VeinMinerVoidConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;

import java.util.ArrayList;
import java.util.List;

public class VeinMinerAbilityGroup
        extends AbilityGroup<VeinMinerConfig, VeinMinerAbility<VeinMinerConfig>> {
    @Expose
    private final List<VeinMinerDurabilityConfig> durabilityLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<VeinMinerFortuneConfig> fortuneLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<VeinMinerSizeDurabilityConfig> sizeLevelConfiguration = new ArrayList<>();
    @Expose
    private final List<VeinMinerVoidConfig> voidLevelConfigruation = new ArrayList<>();

    private VeinMinerAbilityGroup() {
        super("Vein Miner");
    }


    protected VeinMinerConfig getSubConfig(String specialization, int level) {
        switch (specialization) {
            case "Vein Miner_Durability":
                return (VeinMinerConfig) this.durabilityLevelConfiguration.get(level);
            case "Vein Miner_Fortune":
                return (VeinMinerConfig) this.fortuneLevelConfiguration.get(level);
            case "Vein Miner_Size":
                return (VeinMinerConfig) this.sizeLevelConfiguration.get(level);
            case "Vein Miner_Void":
                return (VeinMinerConfig) this.voidLevelConfigruation.get(level);
        }
        return null;
    }


    public String getSpecializationName(String specialization) {
        switch (specialization) {
            case "Vein Miner_Durability":
                return "Finesse";
            case "Vein Miner_Fortune":
                return "Fortune";
            case "Vein Miner_Size":
                return "Giant";
            case "Vein Miner_Void":
                return "Void";
        }
        return "Vein Miner";
    }


    public static VeinMinerAbilityGroup defaultConfig() {
        VeinMinerAbilityGroup group = new VeinMinerAbilityGroup();
        group.addLevel((AbilityConfig) new VeinMinerConfig(1, 4));
        group.addLevel((AbilityConfig) new VeinMinerConfig(1, 8));
        group.addLevel((AbilityConfig) new VeinMinerConfig(1, 16));
        group.addLevel((AbilityConfig) new VeinMinerConfig(2, 32));
        group.addLevel((AbilityConfig) new VeinMinerConfig(2, 64));
        group.durabilityLevelConfiguration.add(new VeinMinerDurabilityConfig(1, 4, 0.1F));
        group.durabilityLevelConfiguration.add(new VeinMinerDurabilityConfig(1, 8, 0.06F));
        group.durabilityLevelConfiguration.add(new VeinMinerDurabilityConfig(1, 16, 0.04F));
        group.durabilityLevelConfiguration.add(new VeinMinerDurabilityConfig(2, 32, 0.03F));
        group.durabilityLevelConfiguration.add(new VeinMinerDurabilityConfig(2, 64, 0.02F));
        group.fortuneLevelConfiguration.add(new VeinMinerFortuneConfig(1, 4, 1));
        group.fortuneLevelConfiguration.add(new VeinMinerFortuneConfig(1, 8, 1));
        group.fortuneLevelConfiguration.add(new VeinMinerFortuneConfig(1, 16, 1));
        group.fortuneLevelConfiguration.add(new VeinMinerFortuneConfig(2, 32, 1));
        group.fortuneLevelConfiguration.add(new VeinMinerFortuneConfig(2, 64, 1));
        group.sizeLevelConfiguration.add(new VeinMinerSizeDurabilityConfig(1, 6, 0.05F));
        group.sizeLevelConfiguration.add(new VeinMinerSizeDurabilityConfig(1, 12, 0.05F));
        group.sizeLevelConfiguration.add(new VeinMinerSizeDurabilityConfig(1, 24, 0.05F));
        group.sizeLevelConfiguration.add(new VeinMinerSizeDurabilityConfig(2, 48, 0.05F));
        group.sizeLevelConfiguration.add(new VeinMinerSizeDurabilityConfig(2, 96, 0.05F));
        group.voidLevelConfigruation.add(new VeinMinerVoidConfig(1, 3));
        group.voidLevelConfigruation.add(new VeinMinerVoidConfig(1, 6));
        group.voidLevelConfigruation.add(new VeinMinerVoidConfig(1, 9));
        group.voidLevelConfigruation.add(new VeinMinerVoidConfig(2, 18));
        group.voidLevelConfigruation.add(new VeinMinerVoidConfig(2, 36));
        return group;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\group\VeinMinerAbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */