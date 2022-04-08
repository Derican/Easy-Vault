package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.VaultSpawner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;


public class VaultSpawnerSpawningPostProcessor
        extends VaultPieceProcessor {
    private final int blocksPerSpawn;

    public VaultSpawnerSpawningPostProcessor(int blocksPerSpawn) {
        this.blocksPerSpawn = blocksPerSpawn;
    }


    public void postProcess(VaultRaid vault, ServerWorld world, VaultPiece piece, Direction generatedDirection) {
        if (piece instanceof iskallia.vault.world.vault.gen.piece.VaultObelisk) {
            return;
        }
        vault.getProperties().getBase(VaultRaid.LEVEL).ifPresent(vaultLevel -> {
            AxisAlignedBB box = AxisAlignedBB.of(piece.getBoundingBox());
            float size = (float) ((box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ));
            float runs = size / this.blocksPerSpawn;
            while (runs > 0.0F && (runs >= 1.0F || rand.nextFloat() < runs)) {
                runs--;
                for (LivingEntity spawned = null; spawned == null; spawned = VaultSpawner.spawnMob(vault, world, vaultLevel.intValue(), pos.getX(), pos.getY(), pos.getZ(), rand))
                    BlockPos pos = MiscUtils.getRandomPos(box, rand);
            }
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\VaultSpawnerSpawningPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */