// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.config.sub;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.ability.config.HunterConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class HunterSpawnerConfig extends HunterConfig
{
    @Expose
    private final List<String> spawnerRegistryKeys;
    
    public HunterSpawnerConfig(final int learningCost, final double searchRadius, final int color, final int tickDuration, final List<String> spawnerRegistryKeys) {
        super(learningCost, searchRadius, color, tickDuration);
        this.spawnerRegistryKeys = spawnerRegistryKeys;
    }
    
    public boolean shouldHighlightTileEntity(final TileEntity tile) {
        return this.spawnerRegistryKeys.contains(tile.getType().getRegistryName().toString());
    }
}
