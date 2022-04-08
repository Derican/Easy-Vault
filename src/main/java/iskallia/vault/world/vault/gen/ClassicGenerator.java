package iskallia.vault.world.vault.gen;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.gen.PortalPlacer;
import iskallia.vault.world.gen.VaultJigsawGenerator;
import iskallia.vault.world.gen.structure.JigsawGenerator;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClassicGenerator extends VaultGenerator {
    public static final int REGION_SIZE = 4096;

    public ClassicGenerator(ResourceLocation id) {
        super(id);
        this.frameBlocks = new VListNBT(block -> StringNBT.valueOf(block.getRegistryName().toString()), nbt -> (Block) Registry.BLOCK.getOptional(new ResourceLocation(nbt.getAsString())).orElse(Blocks.AIR));
        this.depth = -1;
        this.frameBlocks.addAll(Arrays.asList(new Block[]{Blocks.BLACKSTONE, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS}));
    }

    protected VListNBT<Block, StringNBT> frameBlocks;
    protected int depth;

    public PortalPlacer getPortalPlacer() {
        return new PortalPlacer((pos, random, facing) -> (BlockState) ModBlocks.VAULT_PORTAL.defaultBlockState().setValue((Property) VaultPortalBlock.AXIS, (Comparable) facing.getAxis()), (pos, random, facing) -> ((Block) this.frameBlocks.get(random.nextInt(this.frameBlocks.size()))).defaultBlockState());
    }


    public ClassicGenerator setDepth(int depth) {
        this.depth = depth;
        return this;
    }


    public boolean generate(ServerWorld world, VaultRaid vault, BlockPos.Mutable pos) {
        MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            BlockPos min = pos.immutable();


            BlockPos max = pos.move(4096, 0, 0).immutable();

            return new MutableBoundingBox(min.getX(), 0, min.getZ(), max.getX(), 256, max.getZ() + 4096);
        });

        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);


        int maxObjectives = vault.getAllObjectives().stream().mapToInt(VaultObjective::getMaxObjectivePlacements).max().orElse(10);


        try {
            ChunkPos chunkPos = new ChunkPos(box.x0 + box.getXSpan() / 2 >> 4, box.z0 + box.getZSpan() / 2 >> 4);


            JigsawGenerator jigsaw = JigsawGenerator.builder(box, chunkPos.getWorldPosition().offset(0, 19, 0)).setDepth(this.depth).build();

            this.startChunk = new ChunkPos(jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4);

            StructureStart<?> start = ModFeatures.VAULT_FEATURE.generate((VaultJigsawGenerator) jigsaw, world.registryAccess(),
                    (world.getChunkSource()).generator, world.getStructureManager(), 0, world.getSeed());

            jigsaw.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);

            List<StructurePiece> obeliskPieces = (List<StructurePiece>) jigsaw.getGeneratedPieces().stream().filter(this::isObjectivePiece).collect(Collectors.toList());
            Collections.shuffle(obeliskPieces);

            for (int i = maxObjectives; i < obeliskPieces.size(); i++) {
                jigsaw.getGeneratedPieces().remove(obeliskPieces.get(i));
            }

            world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, true).setStartForFeature(ModStructures.VAULT_STAR, start);
            tick(world, vault);

            if (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) {
                return findStartPosition(world, vault, chunkPos, this::getPortalPlacer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("FrameBlocks", (INBT) this.frameBlocks.serializeNBT());
        nbt.putInt("Depth", this.depth);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.frameBlocks.deserializeNBT(nbt.getList("FrameBlocks", 8));
        this.depth = nbt.getInt("Depth");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\ClassicGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */