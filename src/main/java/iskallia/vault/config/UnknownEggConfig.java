package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class UnknownEggConfig extends Config {
    @Expose
    private final List<Level> OVERRIDES;

    public UnknownEggConfig() {
        this.OVERRIDES = new ArrayList<Level>();
    }

    @Override
    public String getName() {
        return "unknown_egg";
    }

    @Override
    protected void reset() {
        this.OVERRIDES.add(new Level(0, new WeightedList<String>().add(Items.ZOMBIE_SPAWN_EGG.getRegistryName().toString(), 2).add(Items.SKELETON_SPAWN_EGG.getRegistryName().toString(), 1)));
    }

    public Level getForLevel(final int level) {
        int i = 0;
        while (i < this.OVERRIDES.size()) {
            if (level < this.OVERRIDES.get(i).MIN_LEVEL) {
                if (i == 0) {
                    break;
                }
                return this.OVERRIDES.get(i - 1);
            } else {
                if (i == this.OVERRIDES.size() - 1) {
                    return this.OVERRIDES.get(i);
                }
                ++i;
            }
        }
        return null;
    }

    public static class Level {
        @Expose
        public int MIN_LEVEL;
        @Expose
        public WeightedList<String> EGG_POOL;

        public Level(final int level, final WeightedList<String> pool) {
            this.MIN_LEVEL = level;
            this.EGG_POOL = pool;
        }
    }
}
