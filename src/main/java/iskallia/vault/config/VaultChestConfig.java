// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.chest.ExplosionEffect;
import iskallia.vault.world.vault.chest.MobTrapEffect;
import iskallia.vault.world.vault.chest.PotionCloudEffect;
import iskallia.vault.world.vault.chest.VaultChestEffect;
import iskallia.vault.world.vault.logic.VaultSpawner;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.world.Explosion;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultChestConfig extends Config {
    @Expose
    public WeightedList<String> RARITY_POOL;
    @Expose
    public List<MobTrapEffect> MOB_TRAP_EFFECTS;
    @Expose
    public List<ExplosionEffect> EXPLOSION_EFFECTS;
    @Expose
    public List<PotionCloudEffect> POTION_CLOUD_EFFECTS;
    @Expose
    public List<Level> LEVELS;
    private final String name;

    public VaultChestConfig(final String name) {
        this.RARITY_POOL = new WeightedList<String>();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public List<VaultChestEffect> getAll() {
        Stream<List<VaultChestEffect>> stream = Stream.<List<VaultChestEffect>>of(new List[]{this.MOB_TRAP_EFFECTS, this.EXPLOSION_EFFECTS, this.POTION_CLOUD_EFFECTS});
        Stream<VaultChestEffect> stream1 = stream.flatMap(Collection::stream);
        List<VaultChestEffect> list = stream1.collect(Collectors.toList());
        return list;
//        return (List<VaultChestEffect>) Stream.of((List[])new List[] { this.MOB_TRAP_EFFECTS, this.EXPLOSION_EFFECTS, this.POTION_CLOUD_EFFECTS }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public VaultChestEffect getByName(final String name) {
        return this.getAll().stream().filter(group -> group.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    protected void reset() {
        this.RARITY_POOL.add(VaultRarity.COMMON.name(), 16);
        this.RARITY_POOL.add(VaultRarity.RARE.name(), 4);
        this.RARITY_POOL.add(VaultRarity.EPIC.name(), 2);
        this.RARITY_POOL.add(VaultRarity.OMEGA.name(), 1);
        this.LEVELS = new ArrayList<Level>();
        this.MOB_TRAP_EFFECTS = Arrays.asList(new MobTrapEffect("Mob Trap", 5, new VaultSpawner.Config().withExtraMaxMobs(15).withMinDistance(1.0).withMaxDistance(12.0).withDespawnDistance(32.0)));
        this.EXPLOSION_EFFECTS = Arrays.asList(new ExplosionEffect("Explosion", 4.0f, 0.0, 3.0, 0.0, true, 10.0f, Explosion.Mode.BREAK));
        this.POTION_CLOUD_EFFECTS = Arrays.asList(new PotionCloudEffect("Poison", new Potion[]{Potions.STRONG_POISON}));
        final Level level = new Level(5);
        level.DEFAULT_POOL.add("Dummy", 20);
        level.DEFAULT_POOL.add("Explosion", 4);
        level.DEFAULT_POOL.add("Mob Trap", 4);
        level.DEFAULT_POOL.add("Poison", 4);
        level.RAFFLE_POOL.add("Dummy", 20);
        level.RAFFLE_POOL.add("Explosion", 4);
        level.RAFFLE_POOL.add("Mob Trap", 4);
        level.RAFFLE_POOL.add("Poison", 4);
        this.LEVELS.add(level);
    }

    @Nullable
    public WeightedList<String> getEffectPool(final int level, final boolean raffle) {
        final Level override = this.getForLevel(level);
        return raffle ? override.RAFFLE_POOL : override.DEFAULT_POOL;
    }

    @Nullable
    public VaultChestEffect getEffectByName(final String effect) {
        return ModConfigs.VAULT_CHEST.getByName(effect);
    }

    public Level getForLevel(final int level) {
        int i = 0;
        while (i < this.LEVELS.size()) {
            if (level < this.LEVELS.get(i).MIN_LEVEL) {
                if (i == 0) {
                    break;
                }
                return this.LEVELS.get(i - 1);
            } else {
                if (i == this.LEVELS.size() - 1) {
                    return this.LEVELS.get(i);
                }
                ++i;
            }
        }
        return Level.EMPTY;
    }

    public static class Level {
        public static Level EMPTY;
        @Expose
        public int MIN_LEVEL;
        @Expose
        public WeightedList<String> DEFAULT_POOL;
        @Expose
        public WeightedList<String> RAFFLE_POOL;

        public Level(final int minLevel) {
            this.DEFAULT_POOL = new WeightedList<String>();
            this.RAFFLE_POOL = new WeightedList<String>();
            this.MIN_LEVEL = minLevel;
        }

        static {
            Level.EMPTY = new Level(0);
        }
    }
}
