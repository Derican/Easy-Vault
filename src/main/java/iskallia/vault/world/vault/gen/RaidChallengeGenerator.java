// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import iskallia.vault.world.gen.PortalPlacer;
import iskallia.vault.world.gen.structure.JigsawGenerator;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

public class RaidChallengeGenerator extends VaultGenerator
{
    public static final int REGION_SIZE = 8192;
    
    public RaidChallengeGenerator(final ResourceLocation id) {
        super(id);
    }
    
    public PortalPlacer getPortalPlacer() {
        return new PortalPlacer((pos, random, facing) -> ModBlocks.VAULT_PORTAL.defaultBlockState().setValue(VaultPortalBlock.AXIS, facing.getAxis()), (pos, random, facing) -> Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState());
    }
    
    @Override
    public boolean generate(final ServerWorld world, final VaultRaid vault, final BlockPos.Mutable pos) {
        final MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            final BlockPos min = pos.move(2000, 0, 0).immutable();
            final BlockPos max = pos.move(8192, 0, 0).immutable();
            return new MutableBoundingBox(min.getX(), 0, min.getZ(), max.getX(), 256, max.getZ() + 8192);
        });
        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);
        try {
            final ChunkPos chunkPos = new ChunkPos(box.x0 + box.getXSpan() / 2 >> 4, box.z0 + box.getZSpan() / 2 >> 4);
            final JigsawGenerator jigsaw = JigsawGenerator.builder(box, chunkPos.getWorldPosition().offset(0, 19, 0)).setDepth(1).build();
            this.startChunk = new ChunkPos(jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4);
            final StructureStart<?> start = ModFeatures.RAID_CHALLENGE_FEATURE.generate(jigsaw, world.registryAccess(), world.getChunkSource().generator, world.getStructureManager(), 0, world.getSeed());
            jigsaw.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);
            world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, true).setStartForFeature((Structure)ModStructures.RAID_CHALLENGE, (StructureStart)start);
            this.tick(world, vault);
            if (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) {
                return this.findStartPosition(world, vault, chunkPos, this::getPortalPlacer);
            }
        }
        catch (final Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return false;
    }
}
