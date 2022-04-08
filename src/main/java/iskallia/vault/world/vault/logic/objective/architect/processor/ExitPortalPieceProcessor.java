package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class ExitPortalPieceProcessor extends VaultPieceProcessor {
    static {
        EXIT_PORTAL_PLACER = new PortalPlacer((pos, random, facing) -> (BlockState) ModBlocks.VAULT_PORTAL.defaultBlockState().setValue((Property) VaultPortalBlock.AXIS, (Comparable) facing.getAxis()), (pos, random, facing) -> ((Block) MiscUtils.eitherOf(random, (Object[]) new Block[]{Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE})).defaultBlockState());
    }


    private static final PortalPlacer EXIT_PORTAL_PLACER;

    private final ArchitectObjective objective;

    public ExitPortalPieceProcessor(ArchitectObjective objective) {
        this.objective = objective;
    }


    public void postProcess(VaultRaid vault, ServerWorld world, VaultPiece piece, Direction generatedDirection) {
        if (!(piece instanceof VaultRoom)) {
            return;
        }
        Direction portalDir = generatedDirection.getClockWise();

        VaultRoom room = (VaultRoom) piece;
        BlockPos at = (new BlockPos(room.getCenter())).relative(portalDir, -1);
        this.objective.buildPortal(EXIT_PORTAL_PLACER.place((IWorld) world, at, portalDir, 3, 5));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\processor\ExitPortalPieceProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */