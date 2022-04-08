package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;


public abstract class VaultPieceProcessor {
    protected static final Random rand = new Random();

    public abstract void postProcess(VaultRaid paramVaultRaid, ServerWorld paramServerWorld, VaultPiece paramVaultPiece, Direction paramDirection);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\VaultPieceProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */