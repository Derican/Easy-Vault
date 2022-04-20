package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.modifier.VaultModifier;

import java.time.LocalDateTime;
import java.time.Month;

public class RaidEventConfig extends Config {
    private static final LocalDateTime EVENT_END_TIME;
    @Expose
    private int soulShardTradeCost;
    @Expose
    private int temporaryModifierMinutes;
    @Expose
    private float viewerVoteChance;
    @Expose
    private WeightedList<String> viewerModifiers;

    public boolean isEnabled() {
        return LocalDateTime.now().isBefore(RaidEventConfig.EVENT_END_TIME);
    }

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
        VaultModifier modifier;
        for (modifier = null; modifier == null; modifier = ModConfigs.VAULT_MODIFIERS.getByName(this.viewerModifiers.getRandom(RaidEventConfig.rand))) {
        }
        return modifier;
    }

    @Override
    public String getName() {
        return "raid_event";
    }

    @Override
    protected void reset() {
        this.soulShardTradeCost = 750;
        this.temporaryModifierMinutes = 6;
        this.viewerVoteChance = 0.2f;
        (this.viewerModifiers = new WeightedList<String>()).add("Gilded", 1);
        this.viewerModifiers.add("Plentiful", 1);
        this.viewerModifiers.add("Frail", 1);
        this.viewerModifiers.add("Trapped", 1);
    }

    static {
        EVENT_END_TIME = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0);
    }
}
