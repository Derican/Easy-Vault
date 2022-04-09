// 
// Decompiled by Procyon v0.6.0
// 

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

public abstract class VaultGenerator implements INBTSerializable<CompoundNBT>
{
    public static Map<ResourceLocation, Supplier<? extends VaultGenerator>> REGISTRY;
    protected static final Random rand;
    protected VListNBT<VaultPiece, CompoundNBT> pieces;
    private ResourceLocation id;
    protected ChunkPos startChunk;
    
    public VaultGenerator(final ResourceLocation id) {
        this.pieces = VListNBT.of(VaultPiece::fromNBT);
        this.id = id;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public ChunkPos getStartChunk() {
        return this.startChunk;
    }
    
    public abstract boolean generate(final ServerWorld p0, final VaultRaid p1, final BlockPos.Mutable p2);
    
    public void tick(final ServerWorld world, final VaultRaid vault) {
        world.getChunkSource().addRegionTicket(TicketType.PORTAL, this.startChunk, 3, this.startChunk.getWorldPosition());
        this.pieces.forEach(piece -> piece.tick(world, vault));
    }
    
    public void addPieces(final VaultPiece... pieces) {
        this.addPieces(Arrays.asList(pieces));
    }
    
    public void addPieces(final Collection<VaultPiece> pieces) {
        this.pieces.addAll(pieces);
    }
    
    public Collection<VaultPiece> getPiecesAt(final BlockPos pos) {
        return this.pieces.stream().filter(piece -> piece.contains(pos)).collect(Collectors.toSet());
    }
    
    public <T extends VaultPiece> Set<VaultPiece> getPiecesAt(final BlockPos pos, final Class<T> pieceClass) {
        return this.pieces.stream().filter(piece -> pieceClass.isAssignableFrom(piece.getClass())).filter(piece -> piece.contains(pos)).map(piece -> piece).collect(Collectors.toSet());
    }
    
    public <T extends VaultPiece> Set<VaultPiece> getPieces(final Class<T> pieceClass) {
        return this.pieces.stream().filter(piece -> pieceClass.isAssignableFrom(piece.getClass())).map(piece -> piece).collect(Collectors.toSet());
    }
    
    public boolean intersectsWithAnyPiece(final MutableBoundingBox box) {
        return this.pieces.stream().map(VaultPiece::getBoundingBox).anyMatch(pieceBox -> pieceBox.intersects(box));
    }
    
    public boolean isObjectivePiece(final StructurePiece piece) {
        if (!(piece instanceof AbstractVillagePiece)) {
            return false;
        }
        final JigsawPiece jigsaw = ((AbstractVillagePiece)piece).getElement();
        if (!(jigsaw instanceof PalettedSinglePoolElement)) {
            return false;
        }
        final PalettedSinglePoolElement element = (PalettedSinglePoolElement)jigsaw;
        return element.getTemplate().left().get().toString().startsWith("the_vault:vault/prefab/decor/generic/obelisk");
    }
    
    protected boolean findStartPosition(final ServerWorld world, final VaultRaid vault, final ChunkPos startChunk, final Supplier<PortalPlacer> portalPlacer) {
    Label_0251:
        for (int x = -48; x < 48; ++x) {
            for (int z = -48; z < 48; ++z) {
                for (int y = 0; y < 48; ++y) {
                    final BlockPos pos = startChunk.getWorldPosition().offset(x, 19 + y, z);
                    if (world.getBlockState(pos).getBlock() == Blocks.CRIMSON_PRESSURE_PLATE) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        vault.getProperties().create(VaultRaid.START_POS, pos);
                        for (final Direction direction : Direction.Plane.HORIZONTAL) {
                            int count;
                            for (count = 1; world.getBlockState(pos.relative(direction, count)).getBlock() == Blocks.WARPED_PRESSURE_PLATE; ++count) {
                                world.setBlockAndUpdate(pos.relative(direction, count), Blocks.AIR.defaultBlockState());
                            }
                            if (count > 1) {
                                final PortalPlacer placer = portalPlacer.get();
                                if (placer != null) {
                                    vault.getProperties().create(VaultRaid.START_FACING, direction);
                                    placer.place((IWorld)world, pos, direction, count, count + 1);
                                    return true;
                                }
                                break Label_0251;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        if (this.startChunk != null) {
            nbt.putInt("StartChunkX", this.startChunk.x);
            nbt.putInt("StartChunkZ", this.startChunk.z);
        }
        nbt.put("Pieces", (INBT)this.pieces.serializeNBT());
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.startChunk = new ChunkPos(nbt.getInt("StartChunkX"), nbt.getInt("StartChunkZ"));
        this.pieces.deserializeNBT(nbt.getList("Pieces", 10));
    }
    
    public static VaultGenerator fromNBT(final CompoundNBT nbt) {
        final VaultGenerator generator = VaultGenerator.REGISTRY.get(new ResourceLocation(nbt.getString("Id"))).get();
        generator.deserializeNBT(nbt);
        return generator;
    }
    
    public static <T extends VaultGenerator> Supplier<T> register(final Supplier<T> generator) {
        VaultGenerator.REGISTRY.put(generator.get().getId(), generator);
        return generator;
    }
    
    static {
        VaultGenerator.REGISTRY = new HashMap<ResourceLocation, Supplier<? extends VaultGenerator>>();
        rand = new Random();
    }
}
