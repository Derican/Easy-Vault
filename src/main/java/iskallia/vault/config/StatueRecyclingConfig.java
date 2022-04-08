package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class StatueRecyclingConfig
        extends Config {
    @Expose
    private int defaultRequirement;
    @Expose
    private HashMap<String, Integer> playerRequirement = new HashMap<>();
    @Expose
    private HashMap<String, Integer> itemValues = new HashMap<>();


    public String getName() {
        return "statue_recycling";
    }


    protected void reset() {
        this.defaultRequirement = 100;

        this.playerRequirement.put("iskall85", Integer.valueOf(100));
        this.playerRequirement.put("stressmonster", Integer.valueOf(100));

        this.itemValues.put("the_vault:arena_player_loot_statue", Integer.valueOf(1));
        this.itemValues.put("the_vault:vault_player_loot_statue", Integer.valueOf(2));
        this.itemValues.put("the_vault:gift_normal_statue", Integer.valueOf(3));
        this.itemValues.put("the_vault:gift_mega_statue", Integer.valueOf(4));
    }

    public int getItemValue(String id) {
        if (this.itemValues.containsKey(id)) return ((Integer) this.itemValues.get(id)).intValue();

        throw new InternalError("There is no item with the ID: " + id);
    }

    public int getPlayerRequirement(String name) {
        if (this.playerRequirement.containsKey(name)) return ((Integer) this.playerRequirement.get(name)).intValue();

        return this.defaultRequirement;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\StatueRecyclingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */