package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.skill.ability.config.HunterConfig;
import iskallia.vault.skill.ability.config.sub.HunterSpawnerConfig;
import iskallia.vault.skill.ability.effect.HunterAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;


public class HunterSpawnerAbility
        extends HunterAbility<HunterSpawnerConfig> {
    protected List<Tuple<BlockPos, Color>> selectPositions(HunterSpawnerConfig config, World world, PlayerEntity player) {
        List<Tuple<BlockPos, Color>> entityPositions = super.selectPositions((HunterConfig) config, world, player);
        Color c = new Color(config.getColor(), false);
        forEachTileEntity((HunterConfig) config, world, player, (pos, tile) -> {
            if (config.shouldHighlightTileEntity(tile)) {
                entityPositions.add(new Tuple(pos, c));
            }
        });
        return entityPositions;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\HunterSpawnerAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */