package iskallia.vault.init;

import iskallia.vault.skill.ability.AbilityRegistry;
import iskallia.vault.skill.ability.effect.*;
import iskallia.vault.skill.ability.effect.sub.*;


public class ModAbilities {
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

    public static void init() {
        AbilityRegistry.register("Cleanse", (AbilityEffect) new CleanseAbility());
        AbilityRegistry.register("Cleanse_Applynearby", (AbilityEffect) new CleanseApplyAbility());
        AbilityRegistry.register("Cleanse_Heal", (AbilityEffect) new CleanseHealAbility());
        AbilityRegistry.register("Cleanse_Effect", (AbilityEffect) new CleanseEffectAbility());
        AbilityRegistry.register("Cleanse_Immune", (AbilityEffect) new CleanseImmuneAbility());

        AbilityRegistry.register("Dash", (AbilityEffect) new DashAbility());
        AbilityRegistry.register("Dash_Buff", (AbilityEffect) new DashBuffAbility());
        AbilityRegistry.register("Dash_Damage", (AbilityEffect) new DashDamageAbility());
        AbilityRegistry.register("Dash_Heal", (AbilityEffect) new DashHealAbility());

        AbilityRegistry.register("Execute", (AbilityEffect) new ExecuteAbility());
        AbilityRegistry.register("Execute_Buff", (AbilityEffect) new ExecuteBuffAbility());
        AbilityRegistry.register("Execute_Damage", (AbilityEffect) new ExecuteDamageAbility());

        AbilityRegistry.register("Ghost Walk", (AbilityEffect) new GhostWalkAbility());
        AbilityRegistry.register("Ghost Walk_Damage", (AbilityEffect) new GhostWalkDamageAbility());
        AbilityRegistry.register("Ghost Walk_Parry", (AbilityEffect) new GhostWalkParryAbility());
        AbilityRegistry.register("Ghost Walk_Regen", (AbilityEffect) new GhostWalkRegenerationAbility());

        AbilityRegistry.register("Mega Jump", (AbilityEffect) new MegaJumpAbility());
        AbilityRegistry.register("Mega Jump_Break", (AbilityEffect) new MegaJumpBreakAbility());
        AbilityRegistry.register("Mega Jump_Damage", (AbilityEffect) new MegaJumpDamageAbility());
        AbilityRegistry.register("Mega Jump_Knockback", (AbilityEffect) new MegaJumpKnockbackAbility());

        AbilityRegistry.register("Rampage", (AbilityEffect) new RampageAbility());
        AbilityRegistry.register("Rampage_Time", (AbilityEffect) new RampageTimeAbility());
        AbilityRegistry.register("Rampage_Dot", (AbilityEffect) new RampageDotAbility());
        AbilityRegistry.register("Rampage_Leech", (AbilityEffect) new RampageAbility());

        AbilityRegistry.register("Summon Eternal", (AbilityEffect) new SummonEternalAbility());
        AbilityRegistry.register("Summon Eternal_Additional", (AbilityEffect) new SummonEternalCountAbility());
        AbilityRegistry.register("Summon Eternal_Damage", (AbilityEffect) new SummonEternalDamageAbility());
        AbilityRegistry.register("Summon Eternal_Debuffs", (AbilityEffect) new SummonEternalAbility());

        AbilityRegistry.register("Tank", (AbilityEffect) new TankAbility());
        AbilityRegistry.register("Tank_Reflect", (AbilityEffect) new TankReflectAbility());
        AbilityRegistry.register("Tank_Slow", (AbilityEffect) new TankSlowAbility());
        AbilityRegistry.register("Tank_Parry", (AbilityEffect) new TankAbility());

        AbilityRegistry.register("Vein Miner", (AbilityEffect) new VeinMinerAbility());
        AbilityRegistry.register("Vein Miner_Fortune", (AbilityEffect) new VeinMinerFortuneAbility());
        AbilityRegistry.register("Vein Miner_Durability", (AbilityEffect) new VeinMinerDurabilityAbility());
        AbilityRegistry.register("Vein Miner_Size", (AbilityEffect) new VeinMinerSizeDurabilityAbility());
        AbilityRegistry.register("Vein Miner_Void", (AbilityEffect) new VeinMinerVoidAbility());

        AbilityRegistry.register("Hunter", (AbilityEffect) new HunterAbility());
        AbilityRegistry.register("Hunter_Spawners", (AbilityEffect) new HunterSpawnerAbility());
        AbilityRegistry.register("Hunter_Chests", (AbilityEffect) new HunterChestAbility());
        AbilityRegistry.register("Hunter_Blocks", (AbilityEffect) new HunterObjectiveAbility());
    }

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
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModAbilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */