package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryoChamberConfig
        extends Config {
    @Expose
    private int INFUSION_TIME;
    @Expose
    private List<Integer> TRADERS_REQ = new ArrayList<>();
    @Expose
    private int GROW_ETERNAL_TIME;
    @Expose
    private float UNUSED_TRADER_REWARD_CHANCE;
    @Expose
    private Map<String, Float> PLAYER_TRADER_REQ_MULTIPLIER = new HashMap<>();


    public String getName() {
        return "cryo_chamber";
    }

    public int getPlayerCoreCount(String name, int createdEternals) {
        int index = MathHelper.clamp(createdEternals, 0, this.TRADERS_REQ.size() - 1);
        int requiredCount = ((Integer) this.TRADERS_REQ.get(index)).intValue();
        return MathHelper.floor(((Float) this.PLAYER_TRADER_REQ_MULTIPLIER.getOrDefault(name, Float.valueOf(1.0F))).floatValue() * requiredCount);
    }

    public float getUnusedTraderRewardChance() {
        return this.UNUSED_TRADER_REWARD_CHANCE;
    }

    public int getGrowEternalTime() {
        return this.GROW_ETERNAL_TIME * 20;
    }

    public int getInfusionTime() {
        return this.INFUSION_TIME * 20;
    }


    protected void reset() {
        this.INFUSION_TIME = 2;
        this.GROW_ETERNAL_TIME = 10;
        this.UNUSED_TRADER_REWARD_CHANCE = 0.1F;

        this.PLAYER_TRADER_REQ_MULTIPLIER.put("iskall85", Float.valueOf(1.0F));

        this.TRADERS_REQ.add(Integer.valueOf(20));
        this.TRADERS_REQ.add(Integer.valueOf(40));
        this.TRADERS_REQ.add(Integer.valueOf(60));
        this.TRADERS_REQ.add(Integer.valueOf(80));
        this.TRADERS_REQ.add(Integer.valueOf(100));
        this.TRADERS_REQ.add(Integer.valueOf(100));
        this.TRADERS_REQ.add(Integer.valueOf(120));
        this.TRADERS_REQ.add(Integer.valueOf(120));
        this.TRADERS_REQ.add(Integer.valueOf(140));
        this.TRADERS_REQ.add(Integer.valueOf(140));
        this.TRADERS_REQ.add(Integer.valueOf(160));
        this.TRADERS_REQ.add(Integer.valueOf(160));
        this.TRADERS_REQ.add(Integer.valueOf(180));
        this.TRADERS_REQ.add(Integer.valueOf(180));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(200));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(250));
        this.TRADERS_REQ.add(Integer.valueOf(300));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\CryoChamberConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */