package iskallia.vault.block.entity;

import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RelicStatueTileEntity
        extends TileEntity {
    protected ResourceLocation relicSet = Vault.id("none");

    public RelicStatueTileEntity() {
        super(ModBlocks.RELIC_STATUE_TILE_ENTITY);
    }

    public ResourceLocation getRelicSet() {
        return this.relicSet;
    }

    public void setRelicSet(ResourceLocation relicSet) {
        this.relicSet = relicSet;
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("RelicSet", this.relicSet.toString());
        return super.save(nbt);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        this.relicSet = new ResourceLocation(nbt.getString("RelicSet"));
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("RelicSet", this.relicSet.toString());
        return nbt;
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\RelicStatueTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */