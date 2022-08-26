package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class VaultRuneTileEntity extends TileEntity {
    protected String belongsTo;

    public VaultRuneTileEntity() {
        super(ModBlocks.VAULT_RUNE_TILE_ENTITY);
        this.belongsTo = "";
    }

    public String getBelongsTo() {
        return this.belongsTo;
    }

    public CompoundNBT save(final CompoundNBT nbt) {
        nbt.putString("BelongsTo", this.belongsTo);
        return super.save(nbt);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        this.belongsTo = nbt.getString("BelongsTo");
        super.load(state, nbt);
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("BelongsTo", this.belongsTo);
        return nbt;
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT nbt) {
        this.belongsTo = nbt.getString("BelongsTo");
        super.handleUpdateTag(state, nbt);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT tag = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), tag);
    }
}
