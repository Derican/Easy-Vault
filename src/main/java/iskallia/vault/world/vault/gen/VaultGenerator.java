package iskallia.vault.world.vault.gen;

import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.gen.PortalPlacer;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class VaultGenerator implements INBTSerializable<CompoundNBT> {
    public static Map<ResourceLocation, Supplier<? extends VaultGenerator>> REGISTRY = new HashMap<>();
    protected static final Random rand = new Random();

    protected VListNBT<VaultPiece, CompoundNBT> pieces = VListNBT.of(VaultPiece::fromNBT);

    private ResourceLocation id;
    protected ChunkPos startChunk;

    public VaultGenerator(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ChunkPos getStartChunk() {
        return this.startChunk;
    }


    public void tick(ServerWorld world, VaultRaid vault) {
        world.getChunkSource().addRegionTicket(TicketType.PORTAL, this.startChunk, 3, this.startChunk.getWorldPosition());
        this.pieces.forEach(piece -> piece.tick(world, vault));
    }

    public void addPieces(VaultPiece... pieces) {
        addPieces(Arrays.asList(pieces));
    }

    public void addPieces(Collection<VaultPiece> pieces) {
        this.pieces.addAll(pieces);
    }

    public Collection<VaultPiece> getPiecesAt(BlockPos pos) {
        return (Collection<VaultPiece>) this.pieces.stream()
                .filter(piece -> piece.contains(pos))
                .collect(Collectors.toSet());
    }

    public <T extends VaultPiece> Collection<T> getPiecesAt(BlockPos pos, Class<T> pieceClass) {
        return (Collection<T>) this.pieces.stream()
                .filter(piece -> pieceClass.isAssignableFrom(piece.getClass()))
                .filter(piece -> piece.contains(pos))
                .map(piece -> piece)
                .collect(Collectors.toSet());
    }

    public <T extends VaultPiece> Collection<T> getPieces(Class<T> pieceClass) {
        return (Collection<T>) this.pieces.stream()
                .filter(piece -> pieceClass.isAssignableFrom(piece.getClass()))
                .map(piece -> piece)
                .collect(Collectors.toSet());
    }

    public boolean intersectsWithAnyPiece(MutableBoundingBox box) {
        return this.pieces.stream()
                .map(VaultPiece::getBoundingBox)
                .anyMatch(pieceBox -> pieceBox.intersects(box));
    }

    public boolean isObjectivePiece(StructurePiece piece) {
        if (!(piece instanceof AbstractVillagePiece)) return false;
        JigsawPiece jigsaw = ((AbstractVillagePiece) piece).getElement();
        if (!(jigsaw instanceof PalettedSinglePoolElement)) return false;
        PalettedSinglePoolElement element = (PalettedSinglePoolElement) jigsaw;
        return ((ResourceLocation) element.getTemplate().left().get()).toString().startsWith("the_vault:vault/prefab/decor/generic/obelisk");
    }

    protected boolean findStartPosition(ServerWorld world, VaultRaid vault, ChunkPos startChunk, Supplier<PortalPlacer> portalPlacer) {
        int x;
        label33:
        for (x = -48; x < 48; x++) {
            for (int z = -48; z < 48; z++) {
                for (int y = 0; y < 48; y++) {
                    BlockPos pos = startChunk.getWorldPosition().offset(x, 19 + y, z);
                    if (world.getBlockState(pos).getBlock() == Blocks.CRIMSON_PRESSURE_PLATE) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                        vault.getProperties().create(VaultRaid.START_POS, pos);

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            int count = 1;

                            while (world.getBlockState(pos.relative(direction, count)).getBlock() == Blocks.WARPED_PRESSURE_PLATE) {
                                world.setBlockAndUpdate(pos.relative(direction, count), Blocks.AIR.defaultBlockState());
                                count++;
                            }

                            if (count > 1) {
                                PortalPlacer placer = portalPlacer.get();
                                if (placer != null) {
                                    vault.getProperties().create(VaultRaid.START_FACING, direction);
                                    placer.place((IWorld) world, pos, direction, count, count + 1);
                                    return true;
                                }

                                break label33;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", getId().toString());
        if (this.startChunk != null) {
            nbt.putInt("StartChunkX", this.startChunk.x);
            nbt.putInt("StartChunkZ", this.startChunk.z);
        }
        nbt.put("Pieces", (INBT) this.pieces.serializeNBT());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.startChunk = new ChunkPos(nbt.getInt("StartChunkX"), nbt.getInt("StartChunkZ"));
        this.pieces.deserializeNBT(nbt.getList("Pieces", 10));
    }

    public static VaultGenerator fromNBT(CompoundNBT nbt) {
        VaultGenerator generator = ((Supplier<VaultGenerator>) REGISTRY.get(new ResourceLocation(nbt.getString("Id")))).get();
        generator.deserializeNBT(nbt);
        return generator;
    }

    public static <T extends VaultGenerator> Supplier<T> register(Supplier<T> generator) {
        REGISTRY.put(((VaultGenerator) generator.get()).getId(), generator);
        return generator;
    }

    public abstract boolean generate(ServerWorld paramServerWorld, VaultRaid paramVaultRaid, BlockPos.Mutable paramMutable);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\VaultGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */