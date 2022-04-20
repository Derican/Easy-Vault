package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class PlayerScalingConfig extends Config {
    @Expose
    private Map<String, Integer> PLAYER_MOB_ADJUSTMENT;

    @Override
    public String getName() {
        return "player_scaling";
    }

    public int getMobLevelAdjustment(final String playerName) {
        return this.PLAYER_MOB_ADJUSTMENT.getOrDefault(playerName, 0);
    }

    @Override
    protected void reset() {
        (this.PLAYER_MOB_ADJUSTMENT = new HashMap<String, Integer>()).put("iskall85", -5);
        this.PLAYER_MOB_ADJUSTMENT.put("HBomb94", 10);
        this.PLAYER_MOB_ADJUSTMENT.put("CaptainSparklez", -10);
    }
}
