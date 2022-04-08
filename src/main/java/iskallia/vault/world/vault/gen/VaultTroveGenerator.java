package iskallia.vault.world.vault.gen;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModFeatures;
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
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

public class VaultTroveGenerator extends VaultGenerator {
    public VaultTroveGenerator(ResourceLocation id) {
        super(id);
    }

    public static final int REGION_SIZE = 1024;

    public PortalPlacer getPortalPlacer() {
        return new PortalPlacer((pos, random, facing) -> (BlockState) ModBlocks.VAULT_PORTAL.defaultBlockState().setValue((Property) VaultPortalBlock.AXIS, (Comparable) facing.getAxis()), (pos, random, facing) -> Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState());
    }


    public boolean generate(ServerWorld world, VaultRaid vault, BlockPos.Mutable pos) {
        MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            BlockPos min = pos.move(2000, 0, 0).immutable();


            BlockPos max = pos.move(1024, 0, 0).immutable();

            return new MutableBoundingBox(min.getX(), 0, min.getZ(), max.getX(), 256, max.getZ() + 1024);
        });

        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);


        try {
            ChunkPos chunkPos = new ChunkPos(box.x0 + box.getXSpan() / 2 >> 4, box.z0 + box.getZSpan() / 2 >> 4);


            JigsawGenerator jigsaw = JigsawGenerator.builder(box, chunkPos.getWorldPosition().offset(0, 19, 0)).setDepth(1).build();

            this.startChunk = new ChunkPos(jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4);

            StructureStart<?> start = ModFeatures.VAULT_TROVE_FEATURE.generate(jigsaw, world.registryAccess(),
                    (world.getChunkSource()).generator, world.getStructureManager(), 0, world.getSeed());

            jigsaw.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);
            world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, true).setStartForFeature(ModStructures.VAULT_TROVE, start);
            tick(world, vault);

            if (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) {
                return findStartPosition(world, vault, chunkPos, this::getPortalPlacer);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\VaultTroveGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */