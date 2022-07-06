// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.data.WeightedList;
import java.util.function.Consumer;
import java.util.HashSet;
import iskallia.vault.world.vault.modifier.VaultModifier;
import java.util.Set;
import java.util.Random;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import java.util.List;

public class SummonAndKillAllBossesConfig extends Config
{
    @Expose
    public List<Level> LEVELS;
    
    @Override
    public String getName() {
        return "summon_and_kill_all_bosses";
    }
    
    @Override
    protected void reset() {
        this.LEVELS = new ArrayList<Level>();
        final Level level = new Level(5);
        level.POOLS.addAll(Arrays.asList(new Pool(2, 2).add("Crowded", 1).add("Chaos", 1).add("Fast", 1).add("Rush", 1).add("Easy", 1).add("Hard", 1).add("Treasure", 1).add("Unlucky", 1), new Pool(1, 1).add("Locked", 1).add("Dummy", 3)));
        this.LEVELS.add(level);
    }
    
    public Set<VaultModifier> getRandom(final Random random, final int level) {
        final Level override = this.getForLevel(level);
        final List<Pool> pools = override.POOLS;
        if (pools == null) {
            return new HashSet<VaultModifier>();
        }
        final Set<VaultModifier> modifiers = new HashSet<VaultModifier>();
        pools.stream().map(pool -> pool.getRandom(random)).forEach(modifiers::addAll);
        return modifiers;
    }
    
    public Level getForLevel(final int level) {
        int i = 0;
        while (i < this.LEVELS.size()) {
            if (level < this.LEVELS.get(i).MIN_LEVEL) {
                if (i == 0) {
                    break;
                }
                return this.LEVELS.get(i - 1);
            }
            else {
                if (i == this.LEVELS.size() - 1) {
                    return this.LEVELS.get(i);
                }
                ++i;
            }
        }
        return Level.EMPTY;
    }
    
    public static class Level
    {
        public static Level EMPTY;
        @Expose
        public int MIN_LEVEL;
        @Expose
        public List<Pool> POOLS;
        
        public Level(final int minLevel) {
            this.MIN_LEVEL = minLevel;
            this.POOLS = new ArrayList<Pool>();
        }
        
        static {
            Level.EMPTY = new Level(0);
        }
    }
    
    public static class Pool
    {
        @Expose
        public int MIN_ROLLS;
        @Expose
        public int MAX_ROLLS;
        @Expose
        public WeightedList<String> POOL;
        
        public Pool(final int min, final int max) {
            this.MIN_ROLLS = min;
            this.MAX_ROLLS = max;
            this.POOL = new WeightedList<String>();
        }
        
        public Pool add(final String name, final int weight) {
            this.POOL.add(name, weight);
            return this;
        }
        
        public Set<VaultModifier> getRandom(final Random random) {
            final int rolls = Math.min(this.MIN_ROLLS, this.MAX_ROLLS) + random.nextInt(Math.abs(this.MIN_ROLLS - this.MAX_ROLLS) + 1);
            final Set<String> res = new HashSet<String>();
            while (res.size() < rolls && res.size() < this.POOL.size()) {
                res.add(this.POOL.getRandom(random));
            }
            return res.stream().map(s -> ModConfigs.VAULT_MODIFIERS.getByName(s)).filter(Objects::nonNull).collect(Collectors.toSet());
        }
    }
}
