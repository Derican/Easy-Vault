// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.processor;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.gen.PortalPlacer;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

public class ExitPortalPieceProcessor extends VaultPieceProcessor
{
    private static final PortalPlacer EXIT_PORTAL_PLACER;
    private final ArchitectObjective objective;
    
    public ExitPortalPieceProcessor(final ArchitectObjective objective) {
        this.objective = objective;
    }
    
    @Override
    public void postProcess(final VaultRaid vault, final ServerWorld world, final VaultPiece piece, final Direction generatedDirection) {
        if (!(piece instanceof VaultRoom)) {
            return;
        }
        final Direction portalDir = generatedDirection.getClockWise();
        final VaultRoom room = (VaultRoom)piece;
        final BlockPos at = new BlockPos(room.getCenter()).relative(portalDir, -1);
        this.objective.buildPortal(ExitPortalPieceProcessor.EXIT_PORTAL_PLACER.place((IWorld)world, at, portalDir, 3, 5));
    }
    
    static {
        EXIT_PORTAL_PLACER = new PortalPlacer((pos, random, facing) -> ModBlocks.VAULT_PORTAL.defaultBlockState().setValue(VaultPortalBlock.AXIS, facing.getAxis()), (pos, random, facing) -> MiscUtils.eitherOf(random, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE).defaultBlockState());
    }
}
