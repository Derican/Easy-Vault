package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultGeneralConfig extends Config {
    @Expose
    private int TICK_COUNTER;
    @Expose
    private int NO_EXIT_CHANCE;
    @Expose
    private int OBELISK_DROP_CHANCE;
    @Expose
    private List<String> ITEM_BLACKLIST;
    @Expose
    private List<String> BLOCK_BLACKLIST;
    @Expose
    public float VAULT_EXIT_TNL_MIN;
    @Expose
    public float VAULT_EXIT_TNL_MAX;
    @Expose
    public boolean SAVE_PLAYER_SNAPSHOTS;
    @Expose
    private final List<Level> VAULT_OBJECTIVES;
    @Expose
    private final List<Level> VAULT_COOP_OBJECTIVES;

    public VaultGeneralConfig() {
        this.VAULT_OBJECTIVES = new ArrayList<Level>();
        this.VAULT_COOP_OBJECTIVES = new ArrayList<Level>();
    }

    @Override
    public String getName() {
        return "vault_general";
    }

    public int getTickCounter() {
        return this.TICK_COUNTER;
    }

    public int getNoExitChance() {
        return this.NO_EXIT_CHANCE;
    }

    public int getObeliskDropChance() {
        return this.OBELISK_DROP_CHANCE;
    }

    public VaultObjective generateObjective(final int vaultLevel) {
        return this.getObjective(vaultLevel, false);
    }

    public VaultObjective generateCoopObjective(final int vaultLevel) {
        return this.getObjective(vaultLevel, true);
    }

    @Override
    protected void reset() {
        this.TICK_COUNTER = 30000;
        this.NO_EXIT_CHANCE = 10;
        (this.ITEM_BLACKLIST = new ArrayList<String>()).add(Items.ENDER_CHEST.getRegistryName().toString());
        (this.BLOCK_BLACKLIST = new ArrayList<String>()).add(Blocks.ENDER_CHEST.getRegistryName().toString());
        this.OBELISK_DROP_CHANCE = 2;
        this.VAULT_EXIT_TNL_MIN = 0.0f;
        this.VAULT_EXIT_TNL_MAX = 0.0f;
        this.SAVE_PLAYER_SNAPSHOTS = false;
        this.VAULT_OBJECTIVES.clear();
        WeightedList<String> objectives = new WeightedList<String>();
        objectives.add(Vault.id("summon_and_kill_boss").toString(), 1);
        objectives.add(Vault.id("scavenger_hunt").toString(), 1);
        this.VAULT_OBJECTIVES.add(new Level(0, objectives));
        this.VAULT_COOP_OBJECTIVES.clear();
        objectives = new WeightedList<String>();
        objectives.add(Vault.id("summon_and_kill_boss").toString(), 1);
        objectives.add(Vault.id("scavenger_hunt").toString(), 1);
        this.VAULT_COOP_OBJECTIVES.add(new Level(0, objectives));
    }

    @SubscribeEvent
    public static void cancelItemInteraction(final PlayerInteractEvent event) {
        if (event.getPlayer().level.dimension() != Vault.VAULT_KEY) {
            return;
        }
        if (ModConfigs.VAULT_GENERAL.ITEM_BLACKLIST.contains(event.getItemStack().getItem().getRegistryName().toString()) && event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelBlockInteraction(final PlayerInteractEvent event) {
        if (event.getPlayer().level.dimension() != Vault.VAULT_KEY) {
            return;
        }
        final BlockState state = event.getWorld().getBlockState(event.getPos());
        if (ModConfigs.VAULT_GENERAL.BLOCK_BLACKLIST.contains(state.getBlock().getRegistryName().toString()) && event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    @Nonnull
    private VaultObjective getObjective(final int vaultLevel, final boolean coop) {
        final Level levelConfig = this.getForLevel(coop ? this.VAULT_COOP_OBJECTIVES : this.VAULT_OBJECTIVES, vaultLevel);
        if (levelConfig == null) {
            return VaultRaid.SUMMON_AND_KILL_BOSS.get();
        }
        final String objective = levelConfig.outcomes.getRandom(VaultGeneralConfig.rand);
        if (objective == null) {
            return VaultRaid.SUMMON_AND_KILL_BOSS.get();
        }
        return VaultObjective.getObjective(new ResourceLocation(objective));
    }

    @Nullable
    public Level getForLevel(final List<Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return levels.get(i - 1);
            } else {
                if (i == levels.size() - 1) {
                    return levels.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        private final int level;
        @Expose
        private final WeightedList<String> outcomes;

        public Level(final int level, final WeightedList<String> outcomes) {
            this.level = level;
            this.outcomes = outcomes;
        }
    }
}
