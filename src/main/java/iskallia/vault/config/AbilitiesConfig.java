// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityNode;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.group.*;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AbilitiesConfig extends Config
{
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
    
    @Override
    public String getName() {
        return "abilities";
    }
    
    public List<AbilityGroup<?, ?>> getAll() {
        return (List<AbilityGroup<?, ?>>)Arrays.asList(this.VEIN_MINER, this.DASH, this.MEGA_JUMP, this.GHOST_WALK, this.RAMPAGE, this.CLEANSE, this.TANK, this.EXECUTE, this.SUMMON_ETERNAL, this.HUNTER);
    }

    public AbilityGroup<?, ?> getAbilityGroupByName(String name) {
        return (AbilityGroup<?, ?>) getAll().stream()
                .filter(group -> group.getParentName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown ability with name " + name));
    }
    
    public Optional<AbilityGroup<?, ?>> getAbility(final String name) {
        return this.getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst();
    }

    public int getCooldown(AbilityNode<?, ?> abilityNode, ServerPlayerEntity player) {
        return VaultGear.getCooldownReduction(player, abilityNode.getGroup(), abilityNode.getAbilityConfig().getCooldown());
    }
    
    @Override
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
