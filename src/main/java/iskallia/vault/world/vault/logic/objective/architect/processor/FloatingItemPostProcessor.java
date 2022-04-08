package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.entity.FloatingItemEntity;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FloatingItemPostProcessor
        extends VaultPieceProcessor {
    private final int blocksPerSpawn;
    private final WeightedList<ItemStack> itemList;

    public FloatingItemPostProcessor(int blocksPerSpawn, WeightedList<ItemStack> itemList) {
        this.blocksPerSpawn = blocksPerSpawn;
        this.itemList = itemList;
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
                if (state.isAir((IBlockReader) world, pos)) {
                    placed = true;

                    ItemStack stack = (ItemStack) this.itemList.getRandom(rand);
                    if (stack != null) {
                        FloatingItemEntity itemEntity = FloatingItemEntity.create((World) world, pos, stack.copy());
                        world.addFreshEntity((Entity) itemEntity);
                    }
                }
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\FloatingItemPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */