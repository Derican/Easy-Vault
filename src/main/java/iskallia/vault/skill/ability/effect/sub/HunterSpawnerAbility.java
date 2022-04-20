package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.sub.HunterSpawnerConfig;
import iskallia.vault.skill.ability.effect.HunterAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class HunterSpawnerAbility extends HunterAbility<HunterSpawnerConfig> {
    @Override
    protected List<Tuple<BlockPos, Color>> selectPositions(final HunterSpawnerConfig config, final World world, final PlayerEntity player) {
        final List<Tuple<BlockPos, Color>> entityPositions = super.selectPositions(config, world, player);
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
