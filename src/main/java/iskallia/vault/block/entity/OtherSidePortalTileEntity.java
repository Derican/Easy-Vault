package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.OtherSideData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;

public class OtherSidePortalTileEntity
        extends TileEntity {
    private OtherSideData data;

    public OtherSidePortalTileEntity() {
        super(ModBlocks.OTHER_SIDE_PORTAL_TILE_ENTITY);
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
        setChanged();
    }


    public CompoundNBT save(CompoundNBT compound) {
        if (this.data != null) compound.put("Data", (INBT) this.data.serializeNBT());
        return super.save(compound);
    }


    public void load(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("Data", 10)) {
            this.data = new OtherSideData(null);
            this.data.deserializeNBT(nbt.getCompound("Data"));
        }

        super.load(state, nbt);
    }

    public OtherSideData getData() {
        return this.data;
    }

    public void setOtherSideData(OtherSideData data) {
        this.data = data;
        setChanged();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\OtherSidePortalTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */