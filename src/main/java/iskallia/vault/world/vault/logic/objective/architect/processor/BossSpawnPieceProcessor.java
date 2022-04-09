// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.block.VaultLootableBlock;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultObelisk;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;

public class BossSpawnPieceProcessor extends VaultPieceProcessor
{
    private final ArchitectObjective objective;
    
    public BossSpawnPieceProcessor(final ArchitectObjective objective) {
        this.objective = objective;
    }
    
    @Override
    public void postProcess(final VaultRaid vault, final ServerWorld world, final VaultPiece piece, final Direction generatedDirection) {
        if (!(piece instanceof VaultObelisk)) {
            return;
        }
        final BlockPos stabilizerPos = (BlockPos) BlockPos.betweenClosedStream(piece.getBoundingBox()).map(pos -> new Tuple(pos, world.getBlockState(pos))).filter(tpl -> (tpl.getB()).getBlock() instanceof VaultLootableBlock && ((VaultLootableBlock)(tpl.getB()).getBlock()).getType() == VaultLootableBlock.Type.VAULT_OBJECTIVE).findFirst().map(Tuple::getA).orElse(null);
        if (stabilizerPos != null && world.removeBlock(stabilizerPos, false)) {
            this.objective.spawnBoss(vault, world, stabilizerPos);
        }
    }
}
