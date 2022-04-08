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
    private final List<CompoundWaveSpawn> waves = new ArrayList<>();


    @Nullable
    public static RaidPreset randomFromConfig() {
        RaidConfig.WaveSetup configSetup = ModConfigs.RAID_CONFIG.getRandomWaveSetup();
        if (configSetup == null) {
            return null;
        }
        RaidPreset preset = new RaidPreset();
        for (RaidConfig.CompoundWave wave : configSetup.getWaves()) {
            CompoundWaveSpawn compoundWave = new CompoundWaveSpawn();
            for (RaidConfig.ConfiguredWave waveSpawnSet : wave.getWaveMobs()) {
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
    public CompoundWaveSpawn getWave(int step) {
        if (step < 0 || step >= this.waves.size()) {
            return null;
        }
        return this.waves.get(step);
    }

    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();

        ListNBT waveTag = new ListNBT();
        this.waves.forEach(wave -> waveTag.add(wave.serialize()));


        tag.put("waves", (INBT) waveTag);

        return tag;
    }

    public static RaidPreset deserialize(CompoundNBT tag) {
        RaidPreset preset = new RaidPreset();

        ListNBT waveTag = tag.getList("waves", 10);
        for (int i = 0; i < waveTag.size(); i++) {
            preset.waves.add(CompoundWaveSpawn.deserialize(waveTag.getCompound(i)));
        }

        return preset;
    }

    public static class CompoundWaveSpawn {
        private final List<RaidPreset.WaveSpawn> waveSpawns = new ArrayList<>();

        public List<RaidPreset.WaveSpawn> getWaveSpawns() {
            return this.waveSpawns;
        }

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            ListNBT waveTag = new ListNBT();
            this.waveSpawns.forEach(wave -> waveTag.add(wave.serialize()));


            tag.put("waves", (INBT) waveTag);
            return tag;
        }

        public static CompoundWaveSpawn deserialize(CompoundNBT tag) {
            CompoundWaveSpawn compound = new CompoundWaveSpawn();
            ListNBT waveTag = tag.getList("waves", 10);
            for (int i = 0; i < waveTag.size(); i++) {
                compound.waveSpawns.add(RaidPreset.WaveSpawn.deserialize(waveTag.getCompound(i)));
            }

            return compound;
        }
    }

    public static class WaveSpawn {
        private final int mobCount;
        private final String mobPool;

        private WaveSpawn(int mobCount, String mobPool) {
            this.mobCount = mobCount;
            this.mobPool = mobPool;
        }

        public static WaveSpawn fromConfig(RaidConfig.ConfiguredWave configuredWave) {
            return new WaveSpawn(MathUtilities.getRandomInt(configuredWave.getMin(), configuredWave.getMax() + 1), configuredWave.getMobPool());
        }

        public int getMobCount() {
            return this.mobCount;
        }

        public String getMobPool() {
            return this.mobPool;
        }

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("mobCount", this.mobCount);
            tag.putString("mobPool", this.mobPool);
            return tag;
        }

        public static WaveSpawn deserialize(CompoundNBT tag) {
            return new WaveSpawn(tag.getInt("mobCount"), tag.getString("mobPool"));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\RaidPreset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */