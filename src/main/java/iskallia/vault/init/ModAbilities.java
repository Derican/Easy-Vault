// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import iskallia.vault.skill.ability.AbilityRegistry;
import iskallia.vault.skill.ability.effect.*;
import iskallia.vault.skill.ability.effect.sub.*;

public class ModAbilities
{
    public static final String CLEANSE = "Cleanse";
    public static final String CLEANSE_APPLY = "Cleanse_Applynearby";
    public static final String CLEANSE_HEAL = "Cleanse_Heal";
    public static final String CLEANSE_EFFECT = "Cleanse_Effect";
    public static final String CLEANSE_IMMUNE = "Cleanse_Immune";
    public static final String DASH = "Dash";
    public static final String DASH_BUFF = "Dash_Buff";
    public static final String DASH_DAMAGE = "Dash_Damage";
    public static final String DASH_HEAL = "Dash_Heal";
    public static final String EXECUTE = "Execute";
    public static final String EXECUTE_BUFF = "Execute_Buff";
    public static final String EXECUTE_DAMAGE = "Execute_Damage";
    public static final String GHOST_WALK = "Ghost Walk";
    public static final String GHOST_WALK_DAMAGE = "Ghost Walk_Damage";
    public static final String GHOST_WALK_PARRY = "Ghost Walk_Parry";
    public static final String GHOST_WALK_REGEN = "Ghost Walk_Regen";
    public static final String MEGA_JUMP = "Mega Jump";
    public static final String MEGA_JUMP_BREAK = "Mega Jump_Break";
    public static final String MEGA_JUMP_DAMAGE = "Mega Jump_Damage";
    public static final String MEGA_JUMP_KNOCKBACK = "Mega Jump_Knockback";
    public static final String RAMPAGE = "Rampage";
    public static final String RAMPAGE_TIME = "Rampage_Time";
    public static final String RAMPAGE_DOT = "Rampage_Dot";
    public static final String RAMPAGE_LEECH = "Rampage_Leech";
    public static final String SUMMON_ETERNAL = "Summon Eternal";
    public static final String SUMMON_ETERNAL_COUNT = "Summon Eternal_Additional";
    public static final String SUMMON_ETERNAL_DAMAGE = "Summon Eternal_Damage";
    public static final String SUMMON_ETERNAL_DEBUFFS = "Summon Eternal_Debuffs";
    public static final String TANK = "Tank";
    public static final String TANK_REFLECT = "Tank_Reflect";
    public static final String TANK_SLOW = "Tank_Slow";
    public static final String TANK_PARRY = "Tank_Parry";
    public static final String VEIN_MINER = "Vein Miner";
    public static final String VEIN_MINER_FORTUNE = "Vein Miner_Fortune";
    public static final String VEIN_MINER_DURABILITY = "Vein Miner_Durability";
    public static final String VEIN_MINER_SIZE = "Vein Miner_Size";
    public static final String VEIN_MINER_VOID = "Vein Miner_Void";
    public static final String HUNTER = "Hunter";
    public static final String HUNTER_SPAWNERS = "Hunter_Spawners";
    public static final String HUNTER_CHESTS = "Hunter_Chests";
    public static final String HUNTER_BLOCKS = "Hunter_Blocks";
    
    public static void init() {
        AbilityRegistry.register("Cleanse", new CleanseAbility());
        AbilityRegistry.register("Cleanse_Applynearby", new CleanseApplyAbility());
        AbilityRegistry.register("Cleanse_Heal", new CleanseHealAbility());
        AbilityRegistry.register("Cleanse_Effect", new CleanseEffectAbility());
        AbilityRegistry.register("Cleanse_Immune", new CleanseImmuneAbility());
        AbilityRegistry.register("Dash", new DashAbility());
        AbilityRegistry.register("Dash_Buff", new DashBuffAbility());
        AbilityRegistry.register("Dash_Damage", new DashDamageAbility());
        AbilityRegistry.register("Dash_Heal", new DashHealAbility());
        AbilityRegistry.register("Execute", new ExecuteAbility());
        AbilityRegistry.register("Execute_Buff", new ExecuteBuffAbility());
        AbilityRegistry.register("Execute_Damage", new ExecuteDamageAbility());
        AbilityRegistry.register("Ghost Walk", new GhostWalkAbility());
        AbilityRegistry.register("Ghost Walk_Damage", new GhostWalkDamageAbility());
        AbilityRegistry.register("Ghost Walk_Parry", new GhostWalkParryAbility());
        AbilityRegistry.register("Ghost Walk_Regen", new GhostWalkRegenerationAbility());
        AbilityRegistry.register("Mega Jump", new MegaJumpAbility());
        AbilityRegistry.register("Mega Jump_Break", new MegaJumpBreakAbility());
        AbilityRegistry.register("Mega Jump_Damage", new MegaJumpDamageAbility());
        AbilityRegistry.register("Mega Jump_Knockback", new MegaJumpKnockbackAbility());
        AbilityRegistry.register("Rampage", new RampageAbility());
        AbilityRegistry.register("Rampage_Time", new RampageTimeAbility());
        AbilityRegistry.register("Rampage_Dot", new RampageDotAbility());
        AbilityRegistry.register("Rampage_Leech", new RampageAbility());
        AbilityRegistry.register("Summon Eternal", new SummonEternalAbility());
        AbilityRegistry.register("Summon Eternal_Additional", new SummonEternalCountAbility());
        AbilityRegistry.register("Summon Eternal_Damage", new SummonEternalDamageAbility());
        AbilityRegistry.register("Summon Eternal_Debuffs", new SummonEternalAbility());
        AbilityRegistry.register("Tank", new TankAbility());
        AbilityRegistry.register("Tank_Reflect", new TankReflectAbility());
        AbilityRegistry.register("Tank_Slow", new TankSlowAbility());
        AbilityRegistry.register("Tank_Parry", new TankAbility());
        AbilityRegistry.register("Vein Miner", new VeinMinerAbility());
        AbilityRegistry.register("Vein Miner_Fortune", new VeinMinerFortuneAbility());
        AbilityRegistry.register("Vein Miner_Durability", new VeinMinerDurabilityAbility());
        AbilityRegistry.register("Vein Miner_Size", new VeinMinerSizeDurabilityAbility());
        AbilityRegistry.register("Vein Miner_Void", new VeinMinerVoidAbility());
        AbilityRegistry.register("Hunter", new HunterAbility());
        AbilityRegistry.register("Hunter_Spawners", new HunterSpawnerAbility());
        AbilityRegistry.register("Hunter_Chests", new HunterChestAbility());
        AbilityRegistry.register("Hunter_Blocks", new HunterObjectiveAbility());
    }
}
