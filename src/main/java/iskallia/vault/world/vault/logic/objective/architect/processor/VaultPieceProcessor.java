package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public abstract class VaultPieceProcessor {
    protected static final Random rand;

    public abstract void postProcess(final VaultRaid p0, final ServerWorld p1, final VaultPiece p2, final Direction p3);

    static {
        rand = new Random();
    }
}
