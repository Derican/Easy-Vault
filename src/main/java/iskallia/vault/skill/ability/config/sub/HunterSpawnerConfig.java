package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterSpawnerConfig
        extends HunterConfig {
    @Expose
    private final List<String> spawnerRegistryKeys;

    public HunterSpawnerConfig(int learningCost, double searchRadius, int color, int tickDuration, List<String> spawnerRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.spawnerRegistryKeys = spawnerRegistryKeys;
    }

    public boolean shouldHighlightTileEntity(TileEntity tile) {
        return this.spawnerRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\HunterSpawnerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */