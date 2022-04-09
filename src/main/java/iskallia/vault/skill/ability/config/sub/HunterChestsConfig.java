// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterChestsConfig extends HunterConfig
{
    @Expose
    private final List<String> chestRegistryKeys;
    
    public HunterChestsConfig(final int learningCost, final double searchRadius, final int color, final int tickDuration, final List<String> chestRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.chestRegistryKeys = chestRegistryKeys;
    }
    
    public boolean shouldHighlightTileEntity(final TileEntity tile) {
        return this.chestRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}
