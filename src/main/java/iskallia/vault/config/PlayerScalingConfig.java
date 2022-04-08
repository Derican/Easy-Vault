package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;


public class PlayerScalingConfig
        extends Config {
    @Expose
    private Map<String, Integer> PLAYER_MOB_ADJUSTMENT;

    public String getName() {
        return "player_scaling";
    }

    public int getMobLevelAdjustment(String playerName) {
        return ((Integer) this.PLAYER_MOB_ADJUSTMENT.getOrDefault(playerName, Integer.valueOf(0))).intValue();
    }


    protected void reset() {
        this.PLAYER_MOB_ADJUSTMENT = new HashMap<>();
        this.PLAYER_MOB_ADJUSTMENT.put("iskall85", Integer.valueOf(-5));
        this.PLAYER_MOB_ADJUSTMENT.put("HBomb94", Integer.valueOf(10));
        this.PLAYER_MOB_ADJUSTMENT.put("CaptainSparklez", Integer.valueOf(-10));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\PlayerScalingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */