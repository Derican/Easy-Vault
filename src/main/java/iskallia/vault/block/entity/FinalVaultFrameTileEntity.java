// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.network.NetworkManager;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.SkinProfile;
import javax.annotation.Nonnull;
import java.util.UUID;
import net.minecraft.tileentity.TileEntity;

public class FinalVaultFrameTileEntity extends TileEntity
{
    private static final UUID NIL_UUID;
    @Nonnull
    protected UUID ownerUUID;
    @Nonnull
    protected String ownerNickname;
    protected SkinProfile skin;
    
    public FinalVaultFrameTileEntity() {
        super((TileEntityType)ModBlocks.FINAL_VAULT_FRAME_TILE_ENTITY);
        this.ownerNickname = "";
        this.ownerUUID = FinalVaultFrameTileEntity.NIL_UUID;
        this.skin = new SkinProfile();
    }
    
    @Nonnull
    public String getOwnerNickname() {
        return this.ownerNickname;
    }
    
    @Nonnull
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    
    public SkinProfile getSkin() {
        return this.skin;
    }
    
    public void loadFromNBT(final CompoundNBT nbt) {
        this.ownerUUID = UUID.fromString(nbt.getString("OwnerUUID"));
        this.ownerNickname = nbt.getString("OwnerNickname");
        this.skin.updateSkin(this.ownerNickname);
    }
    
    public void writeToEntityTag(final CompoundNBT nbt) {
        nbt.putString("OwnerUUID", this.ownerUUID.toString());
        nbt.putString("OwnerNickname", this.ownerNickname);
    }
    
    @Nonnull
    public CompoundNBT save(@Nonnull final CompoundNBT nbt) {
        this.writeToEntityTag(nbt);
        return super.save(nbt);
    }
    
    public void load(@Nonnull final BlockState state, @Nonnull final CompoundNBT nbt) {
        this.loadFromNBT(nbt);
        super.load(state, nbt);
    }
    
    @Nonnull
    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = super.getUpdateTag();
        this.writeToEntityTag(nbt);
        return nbt;
    }
    
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }
    
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT tag = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), tag);
    }
    
    @Nullable
    public static FinalVaultFrameTileEntity get(final IBlockReader reader, final BlockPos pos) {
        if (reader == null) {
            return null;
        }
        final TileEntity tileEntity = reader.getBlockEntity(pos);
        if (tileEntity instanceof FinalVaultFrameTileEntity) {
            return (FinalVaultFrameTileEntity)tileEntity;
        }
        return null;
    }
    
    static {
        NIL_UUID = new UUID(0L, 0L);
    }
}
