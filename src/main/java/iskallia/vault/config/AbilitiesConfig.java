package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.group.*;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AbilitiesConfig extends Config {
    @Expose
    public CleanseAbilityGroup CLEANSE;
    @Expose
    public DashAbilityGroup DASH;
    @Expose
    public ExecuteAbilityGroup EXECUTE;
    @Expose
    public GhostWalkAbilityGroup GHOST_WALK;
    @Expose
    public MegaJumpAbilityGroup MEGA_JUMP;

    public String getName() {
        return "abilities";
    }

    @Expose
    public RampageAbilityGroup RAMPAGE;
    @Expose
    public SummonEternalAbilityGroup SUMMON_ETERNAL;
    @Expose
    public TankAbilityGroup TANK;
    @Expose
    public VeinMinerAbilityGroup VEIN_MINER;
    @Expose
    public HunterAbilityGroup HUNTER;

    public List<AbilityGroup<?, ?>> getAll() {
        return Arrays.asList((AbilityGroup<?, ?>[]) new AbilityGroup[]{(AbilityGroup) this.VEIN_MINER, (AbilityGroup) this.DASH, (AbilityGroup) this.MEGA_JUMP, (AbilityGroup) this.GHOST_WALK, (AbilityGroup) this.RAMPAGE, (AbilityGroup) this.CLEANSE, (AbilityGroup) this.TANK, (AbilityGroup) this.EXECUTE, (AbilityGroup) this.SUMMON_ETERNAL, (AbilityGroup) this.HUNTER});
    }


    public AbilityGroup<?, ?> getAbilityGroupByName(String name) {
        return (AbilityGroup<?, ?>) getAll().stream()
                .filter(group -> group.getParentName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown ability with name " + name));
    }

    public Optional<AbilityGroup<?, ?>> getAbility(String name) {
        return getAll().stream()
                .filter(group -> group.getParentName().equals(name))
                .findFirst();
    }

    public int getCooldown(AbilityNode<?, ?> abilityNode, ServerPlayerEntity player) {
        return VaultGear.getCooldownReduction(player, abilityNode.getGroup(), abilityNode.getAbilityConfig().getCooldown());
    }


    protected void reset() {
        this.CLEANSE = CleanseAbilityGroup.defaultConfig();
        this.DASH = DashAbilityGroup.defaultConfig();
        this.EXECUTE = ExecuteAbilityGroup.defaultConfig();
        this.GHOST_WALK = GhostWalkAbilityGroup.defaultConfig();
        this.MEGA_JUMP = MegaJumpAbilityGroup.defaultConfig();
        this.RAMPAGE = RampageAbilityGroup.defaultConfig();
        this.SUMMON_ETERNAL = SummonEternalAbilityGroup.defaultConfig();
        this.TANK = TankAbilityGroup.defaultConfig();
        this.VEIN_MINER = VeinMinerAbilityGroup.defaultConfig();
        this.HUNTER = HunterAbilityGroup.defaultConfig();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\AbilitiesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */