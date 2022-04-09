// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.util.LazySoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModSounds
{
    public static SoundEvent GRASSHOPPER_BRRR;
    public static SoundEvent RAFFLE_SFX;
    public static SoundEvent VAULT_AMBIENT_LOOP;
    public static SoundEvent VAULT_AMBIENT;
    public static SoundEvent VAULT_BOSS_LOOP;
    public static SoundEvent TIMER_KILL_SFX;
    public static SoundEvent TIMER_PANIC_TICK_SFX;
    public static SoundEvent CONFETTI_SFX;
    public static SoundEvent MEGA_JUMP_SFX;
    public static SoundEvent DASH_SFX;
    public static SoundEvent VAULT_EXP_SFX;
    public static SoundEvent VAULT_LEVEL_UP_SFX;
    public static SoundEvent SKILL_TREE_LEARN_SFX;
    public static SoundEvent SKILL_TREE_UPGRADE_SFX;
    public static SoundEvent VENDING_MACHINE_SFX;
    public static SoundEvent BOOSTER_PACK_SUCCESS_SFX;
    public static SoundEvent BOOSTER_PACK_FAIL_SFX;
    public static SoundEvent BOSS_TP_SFX;
    public static SoundEvent VAULT_GEM_HIT;
    public static SoundEvent VAULT_GEM_BREAK;
    public static SoundEvent ROBOT_HURT;
    public static SoundEvent ROBOT_DEATH;
    public static SoundEvent BOOGIE_AMBIENT;
    public static SoundEvent BOOGIE_HURT;
    public static SoundEvent BOOGIE_DEATH;
    public static SoundEvent VAULT_PORTAL_OPEN;
    public static SoundEvent VAULT_PORTAL_LEAVE;
    public static SoundEvent CLEANSE_SFX;
    public static SoundEvent GHOST_WALK_SFX;
    public static SoundEvent INVISIBILITY_SFX;
    public static SoundEvent NIGHT_VISION_SFX;
    public static SoundEvent RAMPAGE_SFX;
    public static SoundEvent TANK_SFX;
    public static SoundEvent VAMPIRE_HISSING_SFX;
    public static SoundEvent CAULDRON_BUBBLES_SFX;
    public static SoundEvent EXECUTION_SFX;
    public static SoundEvent GOBLIN_BAIL;
    public static SoundEvent GOBLIN_DEATH;
    public static SoundEvent GOBLIN_HURT;
    public static SoundEvent GOBLIN_IDLE;
    public static SoundEvent PUZZLE_COMPLETION_MAJOR;
    public static SoundEvent PUZZLE_COMPLETION_MINOR;
    public static SoundEvent PUZZLE_COMPLETION_FAIL;
    public static SoundEvent VAULT_CHEST_EPIC_OPEN;
    public static SoundEvent VAULT_CHEST_OMEGA_OPEN;
    public static SoundEvent VAULT_CHEST_RARE_OPEN;
    public static SoundEvent WITCHSKALL_IDLE;
    public static SoundEvent FAVOUR_UP;
    public static LazySoundType VAULT_GEM;
    
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        ModSounds.GRASSHOPPER_BRRR = registerSound(event, "grasshopper_brrr");
        ModSounds.RAFFLE_SFX = registerSound(event, "raffle");
        ModSounds.VAULT_AMBIENT_LOOP = registerSound(event, "vault_ambient_loop");
        ModSounds.VAULT_AMBIENT = registerSound(event, "vault_ambient");
        ModSounds.VAULT_BOSS_LOOP = registerSound(event, "boss_loop");
        ModSounds.TIMER_KILL_SFX = registerSound(event, "timer_kill");
        ModSounds.TIMER_PANIC_TICK_SFX = registerSound(event, "timer_panic_tick");
        ModSounds.CONFETTI_SFX = registerSound(event, "confetti");
        ModSounds.MEGA_JUMP_SFX = registerSound(event, "mega_jump");
        ModSounds.DASH_SFX = registerSound(event, "dash");
        ModSounds.VAULT_EXP_SFX = registerSound(event, "vault_exp");
        ModSounds.VAULT_LEVEL_UP_SFX = registerSound(event, "vault_level_up");
        ModSounds.SKILL_TREE_LEARN_SFX = registerSound(event, "skill_tree_learn");
        ModSounds.SKILL_TREE_UPGRADE_SFX = registerSound(event, "skill_tree_upgrade");
        ModSounds.VENDING_MACHINE_SFX = registerSound(event, "vending_machine");
        ModSounds.BOOSTER_PACK_SUCCESS_SFX = registerSound(event, "booster_pack");
        ModSounds.BOOSTER_PACK_FAIL_SFX = registerSound(event, "booster_pack_fail");
        ModSounds.BOSS_TP_SFX = registerSound(event, "boss_tp");
        ModSounds.VAULT_GEM_HIT = registerSound(event, "vault_gem_hit");
        ModSounds.VAULT_GEM_BREAK = registerSound(event, "vault_gem_break");
        ModSounds.ROBOT_HURT = registerSound(event, "robot_hurt");
        ModSounds.ROBOT_DEATH = registerSound(event, "robot_death");
        ModSounds.BOOGIE_AMBIENT = registerSound(event, "boogie_ambient");
        ModSounds.BOOGIE_HURT = registerSound(event, "boogie_hurt");
        ModSounds.BOOGIE_DEATH = registerSound(event, "boogie_death");
        ModSounds.VAULT_PORTAL_OPEN = registerSound(event, "vault_portal_open");
        ModSounds.VAULT_PORTAL_LEAVE = registerSound(event, "vault_portal_leave");
        ModSounds.CLEANSE_SFX = registerSound(event, "cleanse");
        ModSounds.GHOST_WALK_SFX = registerSound(event, "ghost_walk");
        ModSounds.INVISIBILITY_SFX = registerSound(event, "invisibility");
        ModSounds.NIGHT_VISION_SFX = registerSound(event, "night_vision");
        ModSounds.RAMPAGE_SFX = registerSound(event, "rampage");
        ModSounds.TANK_SFX = registerSound(event, "tank");
        ModSounds.VAMPIRE_HISSING_SFX = registerSound(event, "vampire_hissing");
        ModSounds.CAULDRON_BUBBLES_SFX = registerSound(event, "cauldron_bubbles");
        ModSounds.EXECUTION_SFX = registerSound(event, "execution");
        ModSounds.GOBLIN_BAIL = registerSound(event, "goblin_bail");
        ModSounds.GOBLIN_DEATH = registerSound(event, "goblin_death");
        ModSounds.GOBLIN_HURT = registerSound(event, "goblin_hurt");
        ModSounds.GOBLIN_IDLE = registerSound(event, "goblin_idle");
        ModSounds.PUZZLE_COMPLETION_MAJOR = registerSound(event, "puzzle_completion_major");
        ModSounds.PUZZLE_COMPLETION_MINOR = registerSound(event, "puzzle_completion_minor");
        ModSounds.PUZZLE_COMPLETION_FAIL = registerSound(event, "puzzle_completion_fail");
        ModSounds.VAULT_CHEST_EPIC_OPEN = registerSound(event, "vault_chest_epic_open");
        ModSounds.VAULT_CHEST_OMEGA_OPEN = registerSound(event, "vault_chest_omega_open");
        ModSounds.VAULT_CHEST_RARE_OPEN = registerSound(event, "vault_chest_rare_open");
        ModSounds.WITCHSKALL_IDLE = registerSound(event, "witchskall_idle");
        ModSounds.FAVOUR_UP = registerSound(event, "favour_up");
    }
    
    public static void registerSoundTypes() {
        ModSounds.VAULT_GEM.initialize(0.25f, 1.0f, ModSounds.VAULT_GEM_BREAK, null, null, ModSounds.VAULT_GEM_HIT, null);
    }
    
    private static SoundEvent registerSound(final RegistryEvent.Register<SoundEvent> event, final String soundName) {
        final ResourceLocation location = Vault.id(soundName);
        final SoundEvent soundEvent = new SoundEvent(location);
        soundEvent.setRegistryName(location);
        event.getRegistry().register(soundEvent);
        return soundEvent;
    }
    
    static {
        ModSounds.VAULT_GEM = new LazySoundType();
    }
}
