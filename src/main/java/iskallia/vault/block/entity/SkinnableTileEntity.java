package iskallia.vault.block.entity;

import iskallia.vault.util.SkinProfile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class SkinnableTileEntity
        extends TileEntity {
    protected SkinProfile skin;

    public SkinnableTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.skin = new SkinProfile();
    }

    public SkinProfile getSkin() {
        return this.skin;
    }

    protected abstract void updateSkin();

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        handleUpdateTag(getBlockState(), nbt);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\SkinnableTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */