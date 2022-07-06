// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import java.util.Optional;

import com.google.common.collect.Lists;
import iskallia.vault.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class FinalRaidConfig extends Config {
    @Expose
    private final Map<String, List<RaidConfig.Level>> mobPools;
    @Expose
    private final List<RaidConfig.WaveSetup> raidWaves;

    public FinalRaidConfig() {
        this.mobPools = new HashMap<String, List<RaidConfig.Level>>();
        this.raidWaves = new ArrayList<RaidConfig.WaveSetup>();
    }

    @Nullable
    public RaidConfig.MobPool getPool(final String pool, final int level) {
        final List<RaidConfig.Level> mobLevelPool = this.mobPools.get(pool);
        if (mobLevelPool == null) {
            return null;
        }
        return this.getForLevel(mobLevelPool, level).orElse(RaidConfig.DEFAULT).mobPool;
    }

    public RaidConfig.WaveSetup getWaveSetup(int index) {
        index = MathHelper.clamp(index, 0, this.raidWaves.size() - 1);
        return this.raidWaves.get(index);
    }

    @Override
    public String getName() {
        return "final_raid";
    }

    @Override
    protected void reset() {
        this.mobPools.clear();
        this.raidWaves.clear();
        this.mobPools.put("f1", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 1))));
        this.mobPools.put("f2", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 4).add(EntityType.HUSK, 2))));
        this.mobPools.put("f3", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.HUSK, 4).add(EntityType.STRAY, 2))));
        this.mobPools.put("f4", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 4).add(EntityType.STRAY, 2))));
        this.mobPools.put("f5", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 4).add(EntityType.STRAY, 2))));
        this.mobPools.put("f6", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SPIDER, 4).add(EntityType.STRAY, 2))));
        this.mobPools.put("f7", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SPIDER, 4).add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 2))));
        this.mobPools.put("f8", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(4), 4).add(EntityType.STRAY, 2))));
        this.mobPools.put("wutax", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 1)), new RaidConfig.Level(50, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 2).add(EntityType.VINDICATOR, 1))));
        this.mobPools.put("fravager", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.RAVAGER, 4))));
        this.mobPools.put("fvex", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.VEX, 4))));
        this.mobPools.put("fstray", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.STRAY, 4))));
        this.mobPools.put("fpillager", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PILLAGER, 4))));
        this.mobPools.put("fzombie", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ZOMBIE, 4))));
        this.mobPools.put("fhusk", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.HUSK, 4))));
        this.mobPools.put("fevoker", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.EVOKER, 4))));
        this.mobPools.put("fillusioner", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.ILLUSIONER, 4))));
        this.mobPools.put("fguardian", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_GUARDIAN, 4))));
        this.mobPools.put("fboogie", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.BOOGIEMAN, 4))));
        this.mobPools.put("fsoulblaze", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.BLUE_BLAZE, 4))));
        this.mobPools.put("frobot", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.ROBOT, 4))));
        this.mobPools.put("fspider", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.SPIDER, 4))));
        this.mobPools.put("fbrute", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(EntityType.PIGLIN_BRUTE, 4))));
        this.mobPools.put("ffighter2", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(2), 4))));
        this.mobPools.put("ffighter3", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(3), 4))));
        this.mobPools.put("ffighter4", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(4), 4))));
        this.mobPools.put("ffighter5", Lists.newArrayList(new RaidConfig.Level(0, new RaidConfig.MobPool().add(ModEntities.VAULT_FIGHTER_TYPES.get(5), 4))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(16, 24, "fzombie"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(16, 24, "fzombie"), new RaidConfig.ConfiguredWave(3, 6, "fstray"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(16, 24, "fhusk"), new RaidConfig.ConfiguredWave(3, 6, "fstray"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(12, 16, "ffighter2"), new RaidConfig.ConfiguredWave(4, 6, "fstray"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(14, 18, "ffighter2"), new RaidConfig.ConfiguredWave(2, 4, "fvex"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 4, "fvex"), new RaidConfig.ConfiguredWave(3, 5, "fpillager"), new RaidConfig.ConfiguredWave(10, 12, "ffighter2"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(10, 12, "ffighter2"), new RaidConfig.ConfiguredWave(1, 1, "fillusioner"), new RaidConfig.ConfiguredWave(3, 6, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(10, 12, "ffighter3"), new RaidConfig.ConfiguredWave(2, 4, "fvex"), new RaidConfig.ConfiguredWave(3, 6, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(16, 24, "fhusk"), new RaidConfig.ConfiguredWave(4, 8, "fvex"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 4, "fevoker"), new RaidConfig.ConfiguredWave(1, 1, "fillusioner"), new RaidConfig.ConfiguredWave(4, 8, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(10, 12, "ffighter3"), new RaidConfig.ConfiguredWave(2, 4, "fvex"), new RaidConfig.ConfiguredWave(5, 9, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fguardian"), new RaidConfig.ConfiguredWave(2, 2, "fillusioner"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(6, 12, "fspider"), new RaidConfig.ConfiguredWave(4, 8, "fstray"), new RaidConfig.ConfiguredWave(3, 6, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(12, 16, "fspider"), new RaidConfig.ConfiguredWave(6, 10, "fstray"), new RaidConfig.ConfiguredWave(3, 6, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fguardian"), new RaidConfig.ConfiguredWave(12, 16, "fspider"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 2, "fillusioner"), new RaidConfig.ConfiguredWave(1, 1, "fguardian"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 5, "fevoker"), new RaidConfig.ConfiguredWave(4, 8, "fbrute"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(6, 8, "fvex"), new RaidConfig.ConfiguredWave(12, 16, "fbrute"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fravager"), new RaidConfig.ConfiguredWave(4, 8, "fstray"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(4, 8, "fbrute"), new RaidConfig.ConfiguredWave(8, 12, "ffighter4"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fravager"), new RaidConfig.ConfiguredWave(1, 1, "fguardian"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(6, 12, "fbrute"), new RaidConfig.ConfiguredWave(6, 12, "fstray"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fillusioner"), new RaidConfig.ConfiguredWave(1, 1, "fravager"), new RaidConfig.ConfiguredWave(1, 1, "fguardian"), new RaidConfig.ConfiguredWave(2, 4, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(14, 16, "fspider"), new RaidConfig.ConfiguredWave(6, 16, "fpillager"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fravager"), new RaidConfig.ConfiguredWave(4, 6, "fevoker"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fravager"), new RaidConfig.ConfiguredWave(4, 6, "fevoker"), new RaidConfig.ConfiguredWave(6, 10, "fpillager"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(3, 4, "fillusioner"), new RaidConfig.ConfiguredWave(6, 8, "fevoker"))));
        this.raidWaves.add(new RaidConfig.WaveSetup().addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(20, 24, "ffighter5"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(1, 1, "fboogie"), new RaidConfig.ConfiguredWave(1, 1, "fsoulblaze"))).addWave(new RaidConfig.CompoundWave(new RaidConfig.ConfiguredWave(2, 2, "frobot"))));
    }

    private Optional<RaidConfig.Level> getForLevel(final List<RaidConfig.Level> levels, final int level) {
        int i = 0;
        while (i < levels.size()) {
            if (level < levels.get(i).level) {
                if (i == 0) {
                    break;
                }
                return Optional.of(levels.get(i - 1));
            } else {
                if (i == levels.size() - 1) {
                    return Optional.of(levels.get(i));
                }
                ++i;
            }
        }
        return Optional.empty();
    }
}
