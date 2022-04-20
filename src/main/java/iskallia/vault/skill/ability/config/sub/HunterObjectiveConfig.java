package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterObjectiveConfig extends HunterConfig {
    @Expose
    private final List<String> objectiveRegistryKeys;

    public HunterObjectiveConfig(final int learningCost, final double searchRadius, final int color, final int tickDuration, final List<String> objectiveRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.objectiveRegistryKeys = objectiveRegistryKeys;
    }

    public boolean shouldHighlightTileEntity(final TileEntity tile) {
        return this.objectiveRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}
