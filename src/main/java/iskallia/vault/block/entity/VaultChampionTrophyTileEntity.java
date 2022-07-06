// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import net.minecraft.network.NetworkManager;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;
import javax.annotation.Nonnull;
import java.util.UUID;
import net.minecraft.tileentity.TileEntity;

public class VaultChampionTrophyTileEntity extends TileEntity
{
    private static final UUID NIL_UUID;
    @Nonnull
    protected UUID ownerUUID;
    @Nonnull
    protected String ownerNickname;
    private int score;
    
    public VaultChampionTrophyTileEntity() {
        super((TileEntityType)ModBlocks.VAULT_CHAMPION_TROPHY_TILE_ENTITY);
        this.ownerNickname = "";
        this.ownerUUID = VaultChampionTrophyTileEntity.NIL_UUID;
    }
    
    @Nonnull
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    
    @Nonnull
    public String getOwnerNickname() {
        return this.ownerNickname;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void loadFromNBT(final CompoundNBT nbt) {
        this.ownerUUID = UUID.fromString(nbt.getString("OwnerUUID"));
        this.ownerNickname = nbt.getString("OwnerNickname");
        this.score = nbt.getInt("Score");
    }
    
    public void writeToEntityTag(final CompoundNBT nbt) {
        nbt.putString("OwnerUUID", this.ownerUUID.toString());
        nbt.putString("OwnerNickname", this.ownerNickname);
        nbt.putInt("Score", this.score);
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
    
    static {
        NIL_UUID = new UUID(0L, 0L);
    }
}
