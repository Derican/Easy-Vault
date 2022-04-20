package iskallia.vault.block.entity;

import iskallia.vault.init.ModBlocks;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class VaultPortalTileEntity extends TileEntity {
    private CrystalData data;

    public VaultPortalTileEntity() {
        super((TileEntityType) ModBlocks.VAULT_PORTAL_TILE_ENTITY);
    }

    public void sendUpdates() {
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.setChanged();
    }

    public CompoundNBT save(final CompoundNBT compound) {
        if (this.data != null) {
            compound.put("Data", (INBT) this.data.serializeNBT());
        }
        return super.save(compound);
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        if (nbt.contains("Data", 10)) {
            (this.data = new CrystalData(null)).deserializeNBT(nbt.getCompound("Data"));
        }
        super.load(state, nbt);
    }

    public CrystalData getData() {
        return this.data;
    }

    public void setCrystalData(final CrystalData data) {
        this.data = data;
        this.setChanged();
    }
}
