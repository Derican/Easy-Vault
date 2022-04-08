package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class VaultPiece implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, Supplier<VaultPiece>> REGISTRY = new HashMap<>();

    protected ResourceLocation id;
    protected ResourceLocation template;
    protected MutableBoundingBox boundingBox;
    protected Rotation rotation;

    protected VaultPiece(ResourceLocation id) {
        this.id = id;
    }

    protected VaultPiece(ResourceLocation id, ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        this.id = id;
        this.template = template;
        this.boundingBox = boundingBox;
        this.rotation = rotation;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getTemplate() {
        return this.template;
    }

    public MutableBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public Rotation getRotation() {
        return this.rotation;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.id.toString());
        nbt.putString("Template", this.template.toString());
        nbt.put("BoundingBox", (INBT) this.boundingBox.createTag());
        nbt.putInt("Rotation", this.rotation.ordinal());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.template = new ResourceLocation(nbt.getString("Template"));
        this.boundingBox = new MutableBoundingBox(nbt.getIntArray("BoundingBox"));
        this.rotation = Rotation.values()[nbt.getInt("Rotation")];
    }

    public boolean isInside(AxisAlignedBB box) {
        return AxisAlignedBB.of(this.boundingBox).intersects(box);
    }

    public boolean contains(BlockPos pos) {
        return this.boundingBox.isInside((Vector3i) pos);
    }

    public static VaultPiece fromNBT(CompoundNBT nbt) {
        ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        VaultPiece piece = ((Supplier<VaultPiece>) REGISTRY.getOrDefault(id, () -> null)).get();

        if (piece == null) {
            Vault.LOGGER.error("Piece <" + id + "> is not defined.");
            return null;
        }

        try {
            piece.deserializeNBT(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return piece;
    }

    public static boolean shouldIgnoreCollision(JigsawPiece jigsaw) {
        for (VaultPiece piece : of(jigsaw, MutableBoundingBox.getUnknownBox(), Rotation.NONE)) {
            if (piece instanceof VaultObelisk) {
                return true;
            }
        }
        return false;
    }

    public static List<VaultPiece> of(StructurePiece raw) {
        if (!(raw instanceof AbstractVillagePiece)) return new ArrayList<>();
        return of(((AbstractVillagePiece) raw).getElement(), raw.getBoundingBox(), raw.getRotation());
    }

    public static List<VaultPiece> of(JigsawPiece jigsaw, MutableBoundingBox box, Rotation rotation) {
        List<PalettedSinglePoolElement> elements = new ArrayList<>();

        if (jigsaw instanceof PalettedSinglePoolElement) {
            elements.add((PalettedSinglePoolElement) jigsaw);
        } else if (jigsaw instanceof PalettedListPoolElement) {
            ((PalettedListPoolElement) jigsaw).getElements().forEach(jigsawPiece -> {
                if (jigsawPiece instanceof PalettedSinglePoolElement) {
                    elements.add((PalettedSinglePoolElement) jigsawPiece);
                }
            });
        }

        return (List<VaultPiece>) elements.stream()
                .map(element -> {
                    ResourceLocation template = element.getTemplate().left().get();
                    String path = template.getPath();
                    if (path.startsWith("vault/prefab/decor/generic/obelisk")) {
                        return new VaultObelisk(template, box, rotation);
                    }
                    if (path.startsWith("vault/enigma/rooms")) {
                        if (path.contains("layer0")) {
                            return new VaultRoom(template, box, rotation);
                        }
                    } else {
                        if (path.startsWith("architect_event/enigma/rooms")) {
                            return new VaultRoom(template, box, rotation);
                        }
                        if (path.startsWith("raid/enigma/rooms")) {
                            return new VaultRaidRoom(template, box, rotation);
                        }
                        if (path.startsWith("vault/enigma/tunnels")) {
                            return new VaultTunnel(template, box, rotation);
                        }
                        if (path.startsWith("vault/enigma/starts") || path.startsWith("architect_event/enigma/starts") || path.startsWith("raid/enigma/starts") || path.startsWith("trove/enigma/starts")) {
                            return new VaultStart(template, box, rotation);
                        }
                        if (path.startsWith("vault/enigma/treasure"))
                            return new VaultTreasure(template, box, rotation);
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public abstract void tick(ServerWorld paramServerWorld, VaultRaid paramVaultRaid);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */