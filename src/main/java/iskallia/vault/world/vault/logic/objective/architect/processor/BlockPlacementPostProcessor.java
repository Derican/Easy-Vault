package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class BlockPlacementPostProcessor
        extends VaultPieceProcessor {
    private final BlockState toPlace;
    private final int blocksPerSpawn;

    public BlockPlacementPostProcessor(BlockState toPlace, int blocksPerSpawn) {
        this.toPlace = toPlace;
        this.blocksPerSpawn = blocksPerSpawn;
    }


    public void postProcess(VaultRaid vault, ServerWorld world, VaultPiece piece, Direction generatedDirection) {
        if (piece instanceof iskallia.vault.world.vault.gen.piece.VaultObelisk) {
            return;
        }
        AxisAlignedBB box = AxisAlignedBB.of(piece.getBoundingBox());
        float size = (float) ((box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ));
        float runs = size / this.blocksPerSpawn;
        while (runs > 0.0F && (
                runs >= 1.0F || rand.nextFloat() < runs)) {


            runs--;

            boolean placed = false;
            while (!placed) {
                BlockPos pos = MiscUtils.getRandomPos(box, rand);
                BlockState state = world.getBlockState(pos);

                if (state.isAir((IBlockReader) world, pos) && world.getBlockState(pos.below()).isFaceSturdy((IBlockReader) world, pos, Direction.UP) &&
                        world.setBlock(pos, this.toPlace, 2))
                    placed = true;
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\BlockPlacementPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */