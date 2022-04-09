// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.RomanNumber;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class SetGroup<T extends PlayerSet>
{
    @Expose
    private final String name;
    @Expose
    private final T[] levels;
    private BiMap<String, T> registry;
    
    public SetGroup(final String name, final T... levels) {
        this.name = name;
        this.levels = levels;
    }
    
    public int getMaxLevel() {
        return this.levels.length;
    }
    
    public String getParentName() {
        return this.name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getName(final int level) {
        if (level == 0) {
            return this.name + " " + RomanNumber.toRoman(0);
        }
        return (String)this.getRegistry().inverse().get(this.getSet(level));
    }
    
    public T getSet(final int level) {
        if (level < 0) {
            return this.levels[0];
        }
        if (level >= this.getMaxLevel()) {
            return this.levels[this.getMaxLevel() - 1];
        }
        return this.levels[level - 1];
    }
    
    public BiMap<String, T> getRegistry() {
        if (this.registry == null) {
            this.registry = HashBiMap.create(this.getMaxLevel());
            if (this.getMaxLevel() == 1) {
                this.registry.put(this.getParentName(), this.levels[0]);
            }
            else if (this.getMaxLevel() > 1) {
                for (int i = 0; i < this.getMaxLevel(); ++i) {
                    this.registry.put((this.getParentName() + " " + RomanNumber.toRoman(i + 1)), this.getSet(i + 1));
                }
            }
        }
        return this.registry;
    }
    
    public static <T extends PlayerSet> SetGroup<T> of(final String name, final int maxLevel, final IntFunction<T> supplier) {
        final PlayerSet[] talents = IntStream.range(0, maxLevel).mapToObj((IntFunction<?>)supplier).toArray(PlayerSet[]::new);
        return new SetGroup<T>(name, (T[])talents);
    }
}
