package iskallia.vault.block.entity;

import iskallia.vault.util.SkinProfile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class SkinnableTileEntity extends TileEntity {
    protected SkinProfile skin;

    public SkinnableTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super((TileEntityType) tileEntityTypeIn);
        this.skin = new SkinProfile();
    }

    public SkinProfile getSkin() {
        return this.skin;
    }

    protected abstract void updateSkin();

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.load(state, tag);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT nbt = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), nbt);
    }
}
