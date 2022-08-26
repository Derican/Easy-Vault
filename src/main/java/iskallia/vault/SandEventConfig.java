package iskallia.vault;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

import java.util.Map;

import iskallia.vault.config.Config;

public class SandEventConfig extends Config {
    @Expose
    private final Map<String, Integer> HOURGLASS_TOTAL_SAND_REQUIRED;
    @Expose
    private final Map<String, Integer> VAULT_REDEMPTIONS_PER_SAND;
    @Expose
    private float minDistance;
    @Expose
    private float maxDistance;
    @Expose
    private final boolean enabled;

    public SandEventConfig() {
        this.HOURGLASS_TOTAL_SAND_REQUIRED = new HashMap<String, Integer>();
        this.VAULT_REDEMPTIONS_PER_SAND = new HashMap<String, Integer>();
        this.minDistance = 48.0f;
        this.maxDistance = 128.0f;
        this.enabled = false;
    }

    @Override
    public String getName() {
        return "sand_event";
    }

    public int getTotalSandRequired(final PlayerEntity player) {
        return this.HOURGLASS_TOTAL_SAND_REQUIRED.getOrDefault(player.getName().getString(), 200);
    }

    public int getRedemptionsRequiredPerSand(final PlayerEntity player) {
        return this.VAULT_REDEMPTIONS_PER_SAND.getOrDefault(player.getName().getString(), 100);
    }

    public float getMinDistance() {
        return this.minDistance;
    }

    public float getMaxDistance() {
        return this.maxDistance;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    protected void reset() {
        this.minDistance = 48.0f;
        this.maxDistance = 128.0f;
    }
}
