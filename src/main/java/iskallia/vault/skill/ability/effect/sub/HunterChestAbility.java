// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.HunterChestsConfig;
import iskallia.vault.skill.ability.effect.HunterAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HunterChestAbility extends HunterAbility<HunterChestsConfig>
{
    @Override
    protected List<Tuple<BlockPos, Color>> selectPositions(final HunterChestsConfig config, final World world, final PlayerEntity player) {
        final List<Tuple<BlockPos, Color>> entityPositions = new ArrayList<Tuple<BlockPos, Color>>();
        final Color c = new Color(config.getColor(), false);
        this.forEachTileEntity(config, world, player, (pos, tile) -> {
            if (config.shouldHighlightTileEntity(tile)) {
                entityPositions.add(new Tuple(pos, c));
            }
            return;
        });
        return entityPositions;
    }
}
