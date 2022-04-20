package iskallia.vault.world.vault.gen.piece;

import iskallia.vault.Vault;
import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class VaultRoom extends VaultPiece {
    public static final ResourceLocation ID;
    private boolean cakeEaten;
    private BlockPos cakePos;

    protected VaultRoom(final ResourceLocation id) {
        super(id);
        this.cakeEaten = false;
        this.cakePos = null;
    }

    public VaultRoom() {
        this(VaultRoom.ID);
    }

    protected VaultRoom(final ResourceLocation id, final ResourceLocation template, final MutableBoundingBox boundingBox, final Rotation rotation) {
        super(id, template, boundingBox, rotation);
        this.cakeEaten = false;
        this.cakePos = null;
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

    @Nullable
    public BlockPos getCakePos() {
        return this.cakePos;
    }

    public Vector3i getCenter() {
        return this.getBoundingBox().getCenter();
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
            tag.put("cakePos", (INBT) NBTHelper.serializeBlockPos(this.cakePos));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.cakeEaten = tag.getBoolean("cakeEaten");
        if (tag.contains("cakePos", 10)) {
            this.cakePos = NBTHelper.deserializeBlockPos(tag.getCompound("cakePos"));
        }
    }

    static {
        ID = Vault.id("room");
    }
}
