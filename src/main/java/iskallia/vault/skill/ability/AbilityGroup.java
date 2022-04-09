// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.AbilityConfig;
import iskallia.vault.skill.ability.effect.AbilityEffect;
import iskallia.vault.util.RomanNumber;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbilityGroup<T extends AbilityConfig, E extends AbilityEffect<T>>
{
    @Expose
    private final String name;
    @Expose
    private final List<T> levelConfiguration;
    private final BiMap<Integer, String> nameCache;
    
    protected AbilityGroup(final String name) {
        this.levelConfiguration = new ArrayList<T>();
        this.nameCache = HashBiMap.create();
        this.name = name;
    }
    
    public int getMaxLevel() {
        return this.levelConfiguration.size();
    }
    
    protected void addLevel(final T config) {
        this.levelConfiguration.add(config);
    }
    
    public String getParentName() {
        return this.name;
    }
    
    String getName(final int level) {
        return (String)this.getNameCache().get(level);
    }
    
    public T getAbilityConfig(@Nullable final String specialization, int level) {
        if (level < 0) {
            return this.getDefaultConfig(0);
        }
        level = Math.min(level, this.getMaxLevel() - 1);
        if (specialization != null) {
            final T config = this.getSubConfig(specialization, level);
            if (config != null) {
                return config;
            }
        }
        return this.getDefaultConfig(level);
    }
    
    public boolean hasSpecialization(final String specialization) {
        return this.getSubConfig(specialization, 0) != null;
    }
    
    protected abstract T getSubConfig(final String p0, final int p1);
    
    public abstract String getSpecializationName(final String p0);
    
    private T getDefaultConfig(final int level) {
        return this.levelConfiguration.get(MathHelper.clamp(level, 0, this.getMaxLevel() - 1));
    }
    
    @Nullable
    public E getAbility(@Nullable final String specialization) {
        return (E)AbilityRegistry.getAbility((specialization == null) ? this.getParentName() : specialization);
    }
    
    public int learningCost() {
        return this.getDefaultConfig(0).getLearningCost();
    }
    
    public int levelUpCost(@Nullable final String specialization, final int toLevel) {
        if (toLevel > this.getMaxLevel()) {
            return -1;
        }
        return this.getAbilityConfig(specialization, toLevel - 1).getLearningCost();
    }
    
    private BiMap<Integer, String> getNameCache() {
        if (this.nameCache.isEmpty()) {
            for (int i = 1; i <= this.getMaxLevel(); ++i) {
                this.nameCache.put(i, (this.getParentName() + " " + RomanNumber.toRoman(i)));
            }
        }
        return this.nameCache;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AbilityGroup<?, ?> that = (AbilityGroup<?, ?>)o;
        return Objects.equals(this.name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
