package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.block.VaultLootableBlock;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class BossSpawnPieceProcessor
        extends VaultPieceProcessor {
    private final ArchitectObjective objective;

    public BossSpawnPieceProcessor(ArchitectObjective objective) {
        this.objective = objective;
    }


    public void postProcess(VaultRaid vault, ServerWorld world, VaultPiece piece, Direction generatedDirection) {
        if (!(piece instanceof iskallia.vault.world.vault.gen.piece.VaultObelisk)) {
            return;
        }


        BlockPos stabilizerPos = (BlockPos) BlockPos.betweenClosedStream(piece.getBoundingBox()).map(pos -> new Tuple(pos, world.getBlockState(pos))).filter(tpl -> (((BlockState) tpl.getB()).getBlock() instanceof VaultLootableBlock && ((VaultLootableBlock) ((BlockState) tpl.getB()).getBlock()).getType() == VaultLootableBlock.Type.VAULT_OBJECTIVE)).findFirst().map(Tuple::getA).orElse(null);
        if (stabilizerPos != null && world.removeBlock(stabilizerPos, false))
            this.objective.spawnBoss(vault, world, stabilizerPos);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\BossSpawnPieceProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */