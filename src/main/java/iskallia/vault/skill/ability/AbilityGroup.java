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

public abstract class AbilityGroup<T extends AbilityConfig, E extends AbilityEffect<T>> {
    @Expose
    private final String name;
    @Expose
    private final List<T> levelConfiguration = new ArrayList<>();

    private final BiMap<Integer, String> nameCache = HashBiMap.<Integer, String>create();

    protected AbilityGroup(String name) {
        this.name = name;
    }

    public int getMaxLevel() {
        return this.levelConfiguration.size();
    }

    protected void addLevel(T config) {
        this.levelConfiguration.add(config);
    }

    public String getParentName() {
        return this.name;
    }

    String getName(int level) {
        return (String) getNameCache().get(Integer.valueOf(level));
    }

    public T getAbilityConfig(@Nullable String specialization, int level) {
        if (level < 0) {
            return getDefaultConfig(0);
        }
        level = Math.min(level, getMaxLevel() - 1);
        if (specialization != null) {
            T config = getSubConfig(specialization, level);
            if (config != null) {
                return config;
            }
        }
        return getDefaultConfig(level);
    }

    public boolean hasSpecialization(String specialization) {
        return (getSubConfig(specialization, 0) != null);
    }

    protected abstract T getSubConfig(String paramString, int paramInt);

    public abstract String getSpecializationName(String paramString);

    private T getDefaultConfig(int level) {
        return this.levelConfiguration.get(MathHelper.clamp(level, 0, getMaxLevel() - 1));
    }

    @Nullable
    public E getAbility(@Nullable String specialization) {
        return (E) AbilityRegistry.getAbility((specialization == null) ? getParentName() : specialization);
    }

    public int learningCost() {
        return getDefaultConfig(0).getLearningCost();
    }

    public int levelUpCost(@Nullable String specialization, int toLevel) {
        if (toLevel > getMaxLevel()) {
            return -1;
        }
        return getAbilityConfig(specialization, toLevel - 1).getLearningCost();
    }

    private BiMap<Integer, String> getNameCache() {
        if (this.nameCache.isEmpty()) {
            for (int i = 1; i <= getMaxLevel(); i++) {
                this.nameCache.put(Integer.valueOf(i), getParentName() + " " + RomanNumber.toRoman(i));
            }
        }
        return this.nameCache;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbilityGroup<?, ?> that = (AbilityGroup<?, ?>) o;
        return Objects.equals(this.name, that.name);
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.name});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\AbilityGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */