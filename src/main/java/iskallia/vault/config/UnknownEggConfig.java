package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class UnknownEggConfig
        extends Config {
    @Expose
    private List<Level> OVERRIDES = new ArrayList<>();


    public String getName() {
        return "unknown_egg";
    }


    protected void reset() {
        this.OVERRIDES.add(new Level(0, (new WeightedList())
                .add(Items.ZOMBIE_SPAWN_EGG.getRegistryName().toString(), 2)
                .add(Items.SKELETON_SPAWN_EGG.getRegistryName().toString(), 1)));
    }

    public Level getForLevel(int level) {
        for (int i = 0; i < this.OVERRIDES.size(); i++) {
            if (level < ((Level) this.OVERRIDES.get(i)).MIN_LEVEL) {
                if (i == 0)
                    break;
                return this.OVERRIDES.get(i - 1);
            }
            if (i == this.OVERRIDES.size() - 1) {
                return this.OVERRIDES.get(i);
            }
        }

        return null;
    }

    public static class Level {
        @Expose
        public int MIN_LEVEL;

        public Level(int level, WeightedList<String> pool) {
            this.MIN_LEVEL = level;
            this.EGG_POOL = pool;
        }

        @Expose
        public WeightedList<String> EGG_POOL;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\UnknownEggConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */