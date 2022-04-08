package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;

public class VaultPortalTileEntity
        extends TileEntity {
    private CrystalData data;

    public VaultPortalTileEntity() {
        super(ModBlocks.VAULT_PORTAL_TILE_ENTITY);
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
            this.data = new CrystalData(null);
            this.data.deserializeNBT(nbt.getCompound("Data"));
        }

        super.load(state, nbt);
    }

    public CrystalData getData() {
        return this.data;
    }

    public void setCrystalData(CrystalData data) {
        this.data = data;
        setChanged();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultPortalTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */