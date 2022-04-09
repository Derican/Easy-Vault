// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultObelisk;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class BlockPlacementPostProcessor extends VaultPieceProcessor
{
    private final BlockState toPlace;
    private final int blocksPerSpawn;
    
    public BlockPlacementPostProcessor(final BlockState toPlace, final int blocksPerSpawn) {
        this.toPlace = toPlace;
        this.blocksPerSpawn = blocksPerSpawn;
    }
    
    @Override
    public void postProcess(final VaultRaid vault, final ServerWorld world, final VaultPiece piece, final Direction generatedDirection) {
        if (piece instanceof VaultObelisk) {
            return;
        }
        final AxisAlignedBB box = AxisAlignedBB.of(piece.getBoundingBox());
        final float size = (float)((box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ));
        float runs = size / this.blocksPerSpawn;
        while (runs > 0.0f && (runs >= 1.0f || BlockPlacementPostProcessor.rand.nextFloat() < runs)) {
            --runs;
            for (boolean placed = false; !placed; placed = true) {
                final BlockPos pos = MiscUtils.getRandomPos(box, BlockPlacementPostProcessor.rand);
                final BlockState state = world.getBlockState(pos);
                if (state.isAir((IBlockReader)world, pos) && world.getBlockState(pos.below()).isFaceSturdy((IBlockReader)world, pos, Direction.UP) && world.setBlock(pos, this.toPlace, 2)) {}
            }
        }
    }
}
