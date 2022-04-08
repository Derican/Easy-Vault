package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterChestsConfig
        extends HunterConfig {
    @Expose
    private final List<String> chestRegistryKeys;

    public HunterChestsConfig(int learningCost, double searchRadius, int color, int tickDuration, List<String> chestRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.chestRegistryKeys = chestRegistryKeys;
    }

    public boolean shouldHighlightTileEntity(TileEntity tile) {
        return this.chestRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\config\sub\HunterChestsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */