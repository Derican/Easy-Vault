package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.modifier.VaultModifier;

import java.time.LocalDateTime;
import java.time.Month;

public class RaidEventConfig
        extends Config {
    private static final LocalDateTime EVENT_END_TIME = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0);

    @Expose
    private int soulShardTradeCost;

    @Expose
    private int temporaryModifierMinutes;


    public boolean isEnabled() {
        return LocalDateTime.now().isBefore(EVENT_END_TIME);
    }

    @Expose
    private float viewerVoteChance;
    @Expose
    private WeightedList<String> viewerModifiers;

    public int getSoulShardTradeCost() {
        return this.soulShardTradeCost;
    }

    public int getTemporaryModifierMinutes() {
        return this.temporaryModifierMinutes;
    }

    public float getViewerVoteChance() {
        return this.viewerVoteChance;
    }

    public VaultModifier getRandomModifier() {
        VaultModifier modifier = null;
        while (modifier == null) {
            modifier = ModConfigs.VAULT_MODIFIERS.getByName((String) this.viewerModifiers.getRandom(rand));
        }
        return modifier;
    }


    public String getName() {
        return "raid_event";
    }


    protected void reset() {
        this.soulShardTradeCost = 750;
        this.temporaryModifierMinutes = 6;
        this.viewerVoteChance = 0.2F;

        this.viewerModifiers = new WeightedList();
        this.viewerModifiers.add("Gilded", 1);
        this.viewerModifiers.add("Plentiful", 1);
        this.viewerModifiers.add("Frail", 1);
        this.viewerModifiers.add("Trapped", 1);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\RaidEventConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */