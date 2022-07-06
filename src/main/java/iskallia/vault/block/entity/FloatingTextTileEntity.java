// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import net.minecraft.network.NetworkManager;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;
import java.util.Collection;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.nbt.StringNBT;
import net.minecraft.nbt.CompoundNBT;
import java.util.LinkedList;
import net.minecraft.tileentity.TileEntityType;
import iskallia.vault.init.ModBlocks;
import javax.annotation.Nonnull;
import java.util.List;
import net.minecraft.tileentity.TileEntity;

public class FloatingTextTileEntity extends TileEntity
{
    @Nonnull
    protected List<String> lines;
    
    public FloatingTextTileEntity() {
        super((TileEntityType)ModBlocks.FLOATING_TEXT_TILE_ENTITY);
        (this.lines = new LinkedList<String>()).add("[\"\",{\"text\":\"A sample \",\"bold\":true},{\"text\":\"floating\",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\" text\",\"bold\":true}]");
        this.lines.add("");
        this.lines.add("{\"text\":\"Edit the content by using\",\"bold\":true}");
        this.lines.add("{\"text\":\"/data modify block <x> <y> <z> Lines append value 'JSON Here'\",\"bold\":true,\"color\":\"aqua\"}");
    }
    
    @Nonnull
    public List<String> getLines() {
        return this.lines;
    }
    
    public void loadFromNBT(final CompoundNBT nbt) {
        this.lines = NBTHelper.readList(nbt, "Lines", StringNBT.class, StringNBT::getAsString);
    }
    
    public void writeToEntityTag(final CompoundNBT nbt) {
        NBTHelper.writeList(nbt, "Lines", this.lines, StringNBT.class, StringNBT::valueOf);
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
}
