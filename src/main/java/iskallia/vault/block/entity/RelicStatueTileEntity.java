package iskallia.vault.block.entity;

import iskallia.vault.Vault;
import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RelicStatueTileEntity extends TileEntity {
    protected ResourceLocation relicSet;

    public RelicStatueTileEntity() {
        super(ModBlocks.RELIC_STATUE_TILE_ENTITY);
        this.relicSet = Vault.id("none");
    }

    public ResourceLocation getRelicSet() {
        return this.relicSet;
    }

    public void setRelicSet(final ResourceLocation relicSet) {
        this.relicSet = relicSet;
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.putString("RelicSet", this.relicSet.toString());
        return super.save(nbt);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        this.relicSet = new ResourceLocation(nbt.getString("RelicSet"));
        super.load(state, nbt);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("RelicSet", this.relicSet.toString());
        return nbt;
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
