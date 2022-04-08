package iskallia.vault.skill.set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.util.RomanNumber;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class SetGroup<T extends PlayerSet> {
    @Expose
    private final String name;
    @Expose
    private final T[] levels;
    private BiMap<String, T> registry;

    public SetGroup(String name, T... levels) {
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

    public String getName(int level) {
        if (level == 0) return this.name + " " + RomanNumber.toRoman(0);
        return (String) getRegistry().inverse().get(getSet(level));
    }

    public T getSet(int level) {
        if (level < 0) return this.levels[0];
        if (level >= getMaxLevel()) return this.levels[getMaxLevel() - 1];
        return this.levels[level - 1];
    }

    public BiMap<String, T> getRegistry() {
        if (this.registry == null) {
            this.registry = (BiMap<String, T>) HashBiMap.create(getMaxLevel());

            if (getMaxLevel() == 1) {
                this.registry.put(getParentName(), this.levels[0]);
            } else if (getMaxLevel() > 1) {
                for (int i = 0; i < getMaxLevel(); i++) {
                    this.registry.put(getParentName() + " " + RomanNumber.toRoman(i + 1),
                            getSet(i + 1));
                }
            }
        }

        return this.registry;
    }


    public static <T extends PlayerSet> SetGroup<T> of(String name, int maxLevel, IntFunction<T> supplier) {
        PlayerSet[] talents = (PlayerSet[]) IntStream.range(0, maxLevel).<T>mapToObj(supplier).toArray(x$0 -> new PlayerSet[x$0]);
        return new SetGroup<>(name, (T[]) talents);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\set\SetGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */