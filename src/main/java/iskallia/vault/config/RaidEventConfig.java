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
        this.viewerModifiers = new WeightedList<String>();
        this.viewerModifiers.add("Gilded", 6);
        this.viewerModifiers.add("Hoard", 3);
        this.viewerModifiers.add("Treasure", 1);
        this.viewerModifiers.add("Lucky", 6);
        this.viewerModifiers.add("Luckier", 3);
        this.viewerModifiers.add("Super Lucky", 1);
        this.viewerModifiers.add("Plentiful", 6);
        this.viewerModifiers.add("Rich", 3);
        this.viewerModifiers.add("Copious", 1);
        this.viewerModifiers.add("Prismatic", 3);
        this.viewerModifiers.add("Safe Zone", 6);
        this.viewerModifiers.add("Frail", 1);
        this.viewerModifiers.add("Fragile", 1);
        this.viewerModifiers.add("Destructive", 1);
        this.viewerModifiers.add("Trapped", 10);
        this.viewerModifiers.add("Daycare", 10);
        this.viewerModifiers.add("Unlucky", 10);
        this.viewerModifiers.add("Super Unlucky", 4);
        this.viewerModifiers.add("Hunger", 10);
        this.viewerModifiers.add("Tired", 10);
        this.viewerModifiers.add("Slowed", 10);
        this.viewerModifiers.add("Freezing", 4);
        this.viewerModifiers.add("Withering", 4);
        this.viewerModifiers.add("Poisonous", 4);
    }

    static {
        EVENT_END_TIME = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0);
    }
}
