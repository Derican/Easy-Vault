package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterObjectiveConfig
        extends HunterConfig {
    @Expose
    private final List<String> objectiveRegistryKeys;

    public HunterObjectiveConfig(int learningCost, double searchRadius, int color, int tickDuration, List<String> objectiveRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.objectiveRegistryKeys = objectiveRegistryKeys;
    }

    public boolean shouldHighlightTileEntity(TileEntity tile) {
        return this.objectiveRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\HunterObjectiveConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */