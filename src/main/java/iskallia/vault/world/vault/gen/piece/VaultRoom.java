package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import net.minecraft.nbt.INBT;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.List;

import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.nbt.StringNBT;

import java.util.UUID;

import iskallia.vault.nbt.VListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;

public class VaultRoom extends VaultPiece {
    public static final ResourceLocation ID;
    private boolean cakeEaten;
    private BlockPos cakePos;
    private final VListNBT<UUID, StringNBT> sandIds;

    protected VaultRoom(final ResourceLocation id) {
        super(id);
        this.cakeEaten = false;
        this.cakePos = null;
        this.sandIds = VListNBT.ofUUID();
    }

    public VaultRoom() {
        this(VaultRoom.ID);
    }

    protected VaultRoom(final ResourceLocation id, final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(id, template, boundingBox, rotation);
        this.cakeEaten = false;
        this.cakePos = null;
        this.sandIds = VListNBT.ofUUID();
    }

    public VaultRoom(final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        this(VaultRoom.ID, template, boundingBox, rotation);
    }

    @Override
    public void tick(final ServerWorld world, final VaultRaid vault) {
    }

    public void setCakeEaten(final boolean cakeEaten) {
        this.cakeEaten = cakeEaten;
    }

    public boolean isCakeEaten() {
        return this.cakeEaten;
    }

    public void setCakePos(final BlockPos cakePos) {
        this.cakePos = cakePos;
    }

    public List<UUID> getSandId() {
        return this.sandIds;
    }

    public void addSandId(final UUID sandId) {
        this.sandIds.add(sandId);
    }

    @Nullable
    public BlockPos getCakePos() {
        return this.cakePos;
    }

    public BlockPos getTunnelConnectorPos(final Direction dir) {
        final Vector3i center = this.getCenter();
        final BlockPos size = new BlockPos(this.getBoundingBox().getLength()).offset(2, 2, 2);
        return new BlockPos(center).offset(dir.getStepX() * size.getX(), 0, dir.getStepZ() * size.getZ());
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putBoolean("cakeEaten", this.cakeEaten);
        if (this.cakePos != null) {
            tag.put("cakePos", NBTHelper.serializeBlockPos(this.cakePos));
        }
        tag.put("sandIds", this.sandIds.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.cakeEaten = tag.getBoolean("cakeEaten");
        if (tag.contains("cakePos", 10)) {
            this.cakePos = NBTHelper.deserializeBlockPos(tag.getCompound("cakePos"));
        }
        this.sandIds.deserializeNBT(tag.getList("sandIds", 8));
    }

    static {
        ID = Vault.id("room");
    }
}
