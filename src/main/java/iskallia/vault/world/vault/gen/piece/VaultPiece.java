package iskallia.vault.world.vault.gen.piece;

import java.util.HashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;

import iskallia.vault.world.gen.structure.pool.PalettedListPoolElement;
import iskallia.vault.world.gen.structure.pool.PalettedSinglePoolElement;

import java.util.ArrayList;

import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;

import java.util.List;

import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.Iterator;

import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import iskallia.vault.Vault;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.nbt.INBT;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class VaultPiece implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, Supplier<VaultPiece>> REGISTRY;
    protected ResourceLocation id;
    protected ResourceLocation template;
    protected MutableBoundingBox boundingBox;
    protected Rotation rotation;
    protected UUID uuid;

    protected VaultPiece(final ResourceLocation id) {
        this.uuid = UUID.randomUUID();
        this.id = id;
    }

    protected VaultPiece(final ResourceLocation id, final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        this.uuid = UUID.randomUUID();
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

    public UUID getUUID() {
        return this.uuid;
    }

    public abstract void tick(final ServerWorld p0, final VaultRaid p1);

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.id.toString());
        nbt.putString("Template", this.template.toString());
        nbt.put("BoundingBox", (INBT) this.boundingBox.createTag());
        nbt.putInt("Rotation", this.rotation.ordinal());
        nbt.putString("UUID", this.uuid.toString());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
        this.template = new ResourceLocation(nbt.getString("Template"));
        this.boundingBox = new MutableBoundingBox(nbt.getIntArray("BoundingBox"));
        this.rotation = Rotation.values()[nbt.getInt("Rotation")];
        if (nbt.contains("UUID", 8)) {
            this.uuid = UUID.fromString(nbt.getString("UUID"));
        }
    }

    public boolean isInside(final AxisAlignedBB box) {
        return AxisAlignedBB.of(this.boundingBox).intersects(box);
    }

    public boolean contains(final BlockPos pos) {
        return this.boundingBox.isInside((Vector3i) pos);
    }

    public BlockPos getMin() {
        return new BlockPos(this.boundingBox.x0, this.boundingBox.y0, this.boundingBox.z0);
    }

    public BlockPos getMax() {
        return new BlockPos(this.boundingBox.x1, this.boundingBox.y1, this.boundingBox.z1);
    }

    public Vector3i getCenter() {
        return this.getBoundingBox().getCenter();
    }

    public static VaultPiece fromNBT(final CompoundNBT nbt) {
        final ResourceLocation id = new ResourceLocation(nbt.getString("Id"));
        final VaultPiece piece = VaultPiece.REGISTRY.getOrDefault((Object) id, () -> null).get();
        if (piece == null) {
            Vault.LOGGER.error("Piece <" + id + "> is not defined.");
            return null;
        }
        try {
            piece.deserializeNBT(nbt);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return piece;
    }

    public static boolean shouldIgnoreCollision(final JigsawPiece jigsaw) {
        for (final VaultPiece piece : of(jigsaw, MutableBoundingBox.getUnknownBox(), Rotation.NONE)) {
            if (piece instanceof VaultObelisk) {
                return true;
            }
        }
        return false;
    }

    public static List<VaultPiece> of(final StructurePiece raw) {
        if (!(raw instanceof AbstractVillagePiece)) {
            return new ArrayList<VaultPiece>();
        }
        return of(((AbstractVillagePiece) raw).getElement(), raw.getBoundingBox(), raw.getRotation());
    }

    public static List<VaultPiece> of(final JigsawPiece jigsaw, final MutableBoundingBox box, final Rotation rotation) {
        final List<PalettedSinglePoolElement> elements = new ArrayList<PalettedSinglePoolElement>();
        if (jigsaw instanceof PalettedSinglePoolElement) {
            elements.add((PalettedSinglePoolElement) jigsaw);
        } else if (jigsaw instanceof PalettedListPoolElement) {
            ((PalettedListPoolElement) jigsaw).getElements().forEach(jigsawPiece -> {
                if (jigsawPiece instanceof PalettedSinglePoolElement) {
                    elements.add((PalettedSinglePoolElement) jigsawPiece);
                }
                return;
            });
        }
        return elements.stream().map(element -> {
            final ResourceLocation template = element.getTemplate().left().get();
            final String path = template.getPath();
            if (path.startsWith("vault/prefab/decor/generic/obelisk")) {
                return new VaultObelisk(template, box, rotation);
            } else {
                if (path.startsWith("vault/enigma/rooms")) {
                    if (path.contains("layer0")) {
                        return new VaultRoom(template, box, rotation);
                    }
                } else if (path.startsWith("architect_event/enigma/rooms")) {
                    return new VaultRoom(template, box, rotation);
                } else if (path.startsWith("raid/enigma/rooms") || path.startsWith("final_vault/idona/rooms")) {
                    return new VaultRaidRoom(template, box, rotation);
                } else if (path.startsWith("vault/enigma/tunnels") || path.startsWith("final_vault/idona/tunnels")) {
                    return new VaultTunnel(template, box, rotation);
                } else if (path.startsWith("vault/enigma/starts") || path.startsWith("architect_event/enigma/starts") || path.startsWith("raid/enigma/starts") || path.startsWith("trove/enigma/starts")) {
                    return new VaultStart(template, box, rotation);
                } else if (path.startsWith("vault/enigma/treasure")) {
                    return new VaultTreasure(template, box, rotation);
                } else if (path.startsWith("final_vault/starts")) {
                    return new FinalVaultLobby(template, box, rotation);
                } else if (path.startsWith("final_vault/portals")) {
                    return new VaultPortal(template, box, rotation);
                } else if (path.startsWith("final_vault/velara/rooms") || path.startsWith("final_vault/tenos/rooms") || path.startsWith("final_vault/wendarr/rooms")) {
                    return new VaultRoom(template, box, rotation);
                } else if (path.startsWith("final_vault/velara/tunnels") || path.startsWith("final_vault/tenos/tunnels") || path.startsWith("final_vault/wendarr/tunnels")) {
                    return new VaultTunnel(template, box, rotation);
                } else if (path.startsWith("final_vault") && path.contains("eye")) {
                    return new VaultGodEye(template, box, rotation);
                } else if (path.startsWith("final_vault") && path.contains("boss")) {
                    return new FinalVaultBoss(template, box, rotation);
                }
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, Supplier<VaultPiece>>();
    }
}
