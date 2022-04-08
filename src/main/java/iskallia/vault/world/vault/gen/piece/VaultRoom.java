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

public class VaultRoom
        extends VaultPiece {
    public static final ResourceLocation ID = Vault.id("room");
    private boolean cakeEaten = false;
    private BlockPos cakePos = null;

    protected VaultRoom(ResourceLocation id) {
        super(id);
    }

    public VaultRoom() {
        this(ID);
    }

    protected VaultRoom(ResourceLocation id, ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        super(id, template, boundingBox, rotation);
    }

    public VaultRoom(ResourceLocation template, MutableBoundingBox boundingBox, Rotation rotation) {
        this(ID, template, boundingBox, rotation);
    }


    public void tick(ServerWorld world, VaultRaid vault) {
    }


    public void setCakeEaten(boolean cakeEaten) {
        this.cakeEaten = cakeEaten;
    }

    public boolean isCakeEaten() {
        return this.cakeEaten;
    }

    public void setCakePos(BlockPos cakePos) {
        this.cakePos = cakePos;
    }

    @Nullable
    public BlockPos getCakePos() {
        return this.cakePos;
    }

    public Vector3i getCenter() {
        return getBoundingBox().getCenter();
    }

    public BlockPos getTunnelConnectorPos(Direction dir) {
        Vector3i center = getCenter();
        BlockPos size = (new BlockPos(getBoundingBox().getLength())).offset(2, 2, 2);
        return (new BlockPos(center)).offset(dir.getStepX() * size.getX(), 0, dir.getStepZ() * size.getZ());
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putBoolean("cakeEaten", this.cakeEaten);
        if (this.cakePos != null) {
            tag.put("cakePos", (INBT) NBTHelper.serializeBlockPos(this.cakePos));
        }
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.cakeEaten = tag.getBoolean("cakeEaten");
        if (tag.contains("cakePos", 10))
            this.cakePos = NBTHelper.deserializeBlockPos(tag.getCompound("cakePos"));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\piece\VaultRoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */