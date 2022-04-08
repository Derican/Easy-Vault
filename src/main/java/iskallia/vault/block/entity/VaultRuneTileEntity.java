package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class VaultRuneTileEntity
        extends TileEntity {
    protected String belongsTo;

    public VaultRuneTileEntity() {
        super(ModBlocks.VAULT_RUNE_TILE_ENTITY);
        this.belongsTo = "";
    }

    public String getBelongsTo() {
        return this.belongsTo;
    }


    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("BelongsTo", this.belongsTo);
        return super.save(nbt);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        this.belongsTo = nbt.getString("BelongsTo");
        super.load(state, nbt);
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putString("BelongsTo", this.belongsTo);
        return nbt;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.belongsTo = nbt.getString("BelongsTo");
        super.handleUpdateTag(state, nbt);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(getBlockState(), tag);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultRuneTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */