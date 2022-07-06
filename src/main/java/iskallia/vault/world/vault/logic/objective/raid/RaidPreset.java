package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.config.RaidConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RaidPreset {
    private final List<CompoundWaveSpawn> waves;

    private RaidPreset() {
        this.waves = new ArrayList<CompoundWaveSpawn>();
    }

    @Nullable
    public static RaidPreset randomFromConfig() {
        final RaidConfig.WaveSetup configSetup = ModConfigs.RAID.getRandomWaveSetup();
        if (configSetup == null) {
            return null;
        }
        final RaidPreset preset = new RaidPreset();
        for (final RaidConfig.CompoundWave wave : configSetup.getWaves()) {
            final CompoundWaveSpawn compoundWave = new CompoundWaveSpawn();
            for (final RaidConfig.ConfiguredWave waveSpawnSet : wave.getWaveMobs()) {
                compoundWave.waveSpawns.add(WaveSpawn.fromConfig(waveSpawnSet));
            }
            preset.waves.add(compoundWave);
        }
        return preset;
    }

    public static RaidPreset randomFromFinalConfig(final int index) {
        final RaidConfig.WaveSetup configSetup = ModConfigs.FINAL_RAID.getWaveSetup(index);
        if (configSetup == null) {
            return null;
        }
        final RaidPreset preset = new RaidPreset();
        for (final RaidConfig.CompoundWave wave : configSetup.getWaves()) {
            final CompoundWaveSpawn compoundWave = new CompoundWaveSpawn();
            for (final RaidConfig.ConfiguredWave waveSpawnSet : wave.getWaveMobs()) {
                compoundWave.waveSpawns.add(WaveSpawn.fromConfig(waveSpawnSet));
            }
            preset.waves.add(compoundWave);
        }
        return preset;
    }

    public int getWaves() {
        return this.waves.size();
    }

    @Nullable
    public CompoundWaveSpawn getWave(final int step) {
        if (step < 0 || step >= this.waves.size()) {
            return null;
        }
        return this.waves.get(step);
    }

    public CompoundNBT serialize() {
        final CompoundNBT tag = new CompoundNBT();
        final ListNBT waveTag = new ListNBT();
        this.waves.forEach(wave -> waveTag.add(wave.serialize()));
        tag.put("waves", (INBT) waveTag);
        return tag;
    }

    public static RaidPreset deserialize(final CompoundNBT tag) {
        final RaidPreset preset = new RaidPreset();
        final ListNBT waveTag = tag.getList("waves", 10);
        for (int i = 0; i < waveTag.size(); ++i) {
            preset.waves.add(CompoundWaveSpawn.deserialize(waveTag.getCompound(i)));
        }
        return preset;
    }

    public static class CompoundWaveSpawn {
        private final List<WaveSpawn> waveSpawns;

        public CompoundWaveSpawn() {
            this.waveSpawns = new ArrayList<WaveSpawn>();
        }

        public List<WaveSpawn> getWaveSpawns() {
            return this.waveSpawns;
        }

        public CompoundNBT serialize() {
            final CompoundNBT tag = new CompoundNBT();
            final ListNBT waveTag = new ListNBT();
            this.waveSpawns.forEach(wave -> waveTag.add(wave.serialize()));
            tag.put("waves", (INBT) waveTag);
            return tag;
        }

        public static CompoundWaveSpawn deserialize(final CompoundNBT tag) {
            final CompoundWaveSpawn compound = new CompoundWaveSpawn();
            final ListNBT waveTag = tag.getList("waves", 10);
            for (int i = 0; i < waveTag.size(); ++i) {
                compound.waveSpawns.add(WaveSpawn.deserialize(waveTag.getCompound(i)));
            }
            return compound;
        }
    }

    public static class WaveSpawn {
        private final int mobCount;
        private final String mobPool;

        private WaveSpawn(final int mobCount, final String mobPool) {
            this.mobCount = mobCount;
            this.mobPool = mobPool;
        }

        public static WaveSpawn fromConfig(final RaidConfig.ConfiguredWave configuredWave) {
            return new WaveSpawn(MathUtilities.getRandomInt(configuredWave.getMin(), configuredWave.getMax() + 1), configuredWave.getMobPool());
        }

        public int getMobCount() {
            return this.mobCount;
        }

        public String getMobPool() {
            return this.mobPool;
        }

        public CompoundNBT serialize() {
            final CompoundNBT tag = new CompoundNBT();
            tag.putInt("mobCount", this.mobCount);
            tag.putString("mobPool", this.mobPool);
            return tag;
        }

        public static WaveSpawn deserialize(final CompoundNBT tag) {
            return new WaveSpawn(tag.getInt("mobCount"), tag.getString("mobPool"));
        }
    }
}
