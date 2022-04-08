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
    public WeightedList<String> RARITY_POOL = new WeightedList();
    @Expose
    public List<MobTrapEffect> MOB_TRAP_EFFECTS;
    @Expose
    public List<ExplosionEffect> EXPLOSION_EFFECTS;
    @Expose
    public List<PotionCloudEffect> POTION_CLOUD_EFFECTS;
    @Expose
    public List<Level> LEVELS;
    private final String name;

    public VaultChestConfig(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public List<VaultChestEffect> getAll() {
        return (List<VaultChestEffect>) Stream.<List>of(new List[]{this.MOB_TRAP_EFFECTS, this.EXPLOSION_EFFECTS, this.POTION_CLOUD_EFFECTS}).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public VaultChestEffect getByName(String name) {
        return getAll().stream().filter(group -> group.getName().equals(name)).findFirst().orElse(null);
    }


    protected void reset() {
        this.RARITY_POOL.add(VaultRarity.COMMON.name(), 16);
        this.RARITY_POOL.add(VaultRarity.RARE.name(), 4);
        this.RARITY_POOL.add(VaultRarity.EPIC.name(), 2);
        this.RARITY_POOL.add(VaultRarity.OMEGA.name(), 1);

        this.LEVELS = new ArrayList<>();

        this.MOB_TRAP_EFFECTS = Arrays.asList(new MobTrapEffect[]{new MobTrapEffect("Mob Trap", 5, (new VaultSpawner.Config())

                .withExtraMaxMobs(15).withMinDistance(1.0D).withMaxDistance(12.0D).withDespawnDistance(32.0D))});

        this.EXPLOSION_EFFECTS = Arrays.asList(new ExplosionEffect[]{new ExplosionEffect("Explosion", 4.0F, 0.0D, 3.0D, 0.0D, true, 10.0F, Explosion.Mode.BREAK)});


        this.POTION_CLOUD_EFFECTS = Arrays.asList(new PotionCloudEffect[]{new PotionCloudEffect("Poison", new Potion[]{Potions.STRONG_POISON})});


        Level level = new Level(5);

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
    public WeightedList<String> getEffectPool(int level, boolean raffle) {
        Level override = getForLevel(level);
        return raffle ? override.RAFFLE_POOL : override.DEFAULT_POOL;
    }

    @Nullable
    public VaultChestEffect getEffectByName(String effect) {
        return ModConfigs.VAULT_CHEST.getByName(effect);
    }

    public Level getForLevel(int level) {
        for (int i = 0; i < this.LEVELS.size(); i++) {
            if (level < ((Level) this.LEVELS.get(i)).MIN_LEVEL) {
                if (i == 0)
                    break;
                return this.LEVELS.get(i - 1);
            }
            if (i == this.LEVELS.size() - 1) {
                return this.LEVELS.get(i);
            }
        }

        return Level.EMPTY;
    }

    public static class Level {
        public static Level EMPTY = new Level(0);
        @Expose
        public int MIN_LEVEL;
        @Expose
        public WeightedList<String> DEFAULT_POOL = new WeightedList();
        @Expose
        public WeightedList<String> RAFFLE_POOL = new WeightedList();

        public Level(int minLevel) {
            this.MIN_LEVEL = minLevel;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultChestConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */