// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryoChamberConfig extends Config
{
    @Expose
    private int INFUSION_TIME;
    @Expose
    private int GROW_ETERNAL_TIME;
    @Expose
    private float UNUSED_TRADER_REWARD_CHANCE;
    @Expose
    private List<Integer> TRADERS_REQ;
    @Expose
    private Map<String, Float> PLAYER_TRADER_REQ_MULTIPLIER;
    
    public CryoChamberConfig() {
        this.TRADERS_REQ = new ArrayList<Integer>();
        this.PLAYER_TRADER_REQ_MULTIPLIER = new HashMap<String, Float>();
    }
    
    @Override
    public String getName() {
        return "cryo_chamber";
    }
    
    public int getPlayerCoreCount(final String name, final int createdEternals) {
        final int index = MathHelper.clamp(createdEternals, 0, this.TRADERS_REQ.size() - 1);
        final int requiredCount = this.TRADERS_REQ.get(index);
        return MathHelper.floor(this.PLAYER_TRADER_REQ_MULTIPLIER.getOrDefault(name, 1.0f) * requiredCount);
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
    
    @Override
    protected void reset() {
        this.INFUSION_TIME = 2;
        this.GROW_ETERNAL_TIME = 10;
        this.UNUSED_TRADER_REWARD_CHANCE = 0.1f;
        this.PLAYER_TRADER_REQ_MULTIPLIER.put("iskall85", 1.0f);
        this.TRADERS_REQ.add(20);
        this.TRADERS_REQ.add(40);
        this.TRADERS_REQ.add(60);
        this.TRADERS_REQ.add(80);
        this.TRADERS_REQ.add(100);
        this.TRADERS_REQ.add(100);
        this.TRADERS_REQ.add(120);
        this.TRADERS_REQ.add(120);
        this.TRADERS_REQ.add(140);
        this.TRADERS_REQ.add(140);
        this.TRADERS_REQ.add(160);
        this.TRADERS_REQ.add(160);
        this.TRADERS_REQ.add(180);
        this.TRADERS_REQ.add(180);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(200);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(250);
        this.TRADERS_REQ.add(300);
    }
}
