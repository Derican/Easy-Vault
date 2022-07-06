// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen;

import net.minecraft.util.Direction;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import iskallia.vault.init.ModStructures;
import net.minecraft.world.chunk.ChunkStatus;
import java.util.function.Consumer;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.gen.VaultJigsawGenerator;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.world.gen.structure.JigsawGenerator;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.BlockPos;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.state.Property;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import iskallia.vault.world.gen.PortalPlacer;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.StringNBT;
import net.minecraft.block.Block;
import iskallia.vault.nbt.VListNBT;

public class FinalLobbyGenerator extends VaultGenerator
{
    public static final int REGION_SIZE = 4096;
    protected VListNBT<Block, StringNBT> frameBlocks;
    protected int depth;
    
    public FinalLobbyGenerator(final ResourceLocation id) {
        super(id);
        this.frameBlocks = new VListNBT<Block, StringNBT>(block -> StringNBT.valueOf(block.getRegistryName().toString()), nbt -> Registry.BLOCK.getOptional(new ResourceLocation(nbt.getAsString())).orElse(Blocks.AIR));
        this.depth = -1;
        this.frameBlocks.addAll(Arrays.asList(Blocks.BLACKSTONE, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS));
    }
    
    public PortalPlacer getPortalPlacer() {
        return new PortalPlacer((pos, random, facing) -> (BlockState)ModBlocks.VAULT_PORTAL.defaultBlockState().setValue((Property)VaultPortalBlock.AXIS, (Comparable)facing.getAxis()), (pos, random, facing) -> this.frameBlocks.get(random.nextInt(this.frameBlocks.size())).defaultBlockState());
    }
    
    public FinalLobbyGenerator setDepth(final int depth) {
        this.depth = depth;
        return this;
    }
    
    @Override
    public boolean generate(final ServerWorld world, final VaultRaid vault, final BlockPos.Mutable pos) {
        final MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            final BlockPos min = pos.immutable();
            final BlockPos max = pos.move(4096, 0, 0).immutable();
            return new MutableBoundingBox(min.getX(), 0, min.getZ(), max.getX(), 256, max.getZ() + 4096);
        });
        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);
        try {
            final ChunkPos chunkPos = new ChunkPos(box.x0 + box.getXSpan() / 2 >> 4, box.z0 + box.getZSpan() / 2 >> 4);
            final JigsawGenerator jigsaw = JigsawGenerator.builder(box, chunkPos.getWorldPosition().offset(0, 19, 0)).setDepth(this.depth).build();
            this.startChunk = new ChunkPos(jigsaw.getStartPos().getX() >> 4, jigsaw.getStartPos().getZ() >> 4);
            final StructureStart<?> start = ModFeatures.FINAL_VAULT_LOBBY_FEATURE.generate(jigsaw, world.registryAccess(), world.getChunkSource().generator, world.getStructureManager(), 0, world.getSeed());
            jigsaw.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);
            world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, true).setStartForFeature((Structure)ModStructures.FINAL_VAULT_LOBBY, (StructureStart)start);
            this.tick(world, vault);
            if (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) {
                return this.findStartPosition(world, vault, chunkPos, this::getPortalPlacer);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.put("FrameBlocks", (INBT)this.frameBlocks.serializeNBT());
        nbt.putInt("Depth", this.depth);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.frameBlocks.deserializeNBT(nbt.getList("FrameBlocks", 8));
        this.depth = nbt.getInt("Depth");
    }
}
