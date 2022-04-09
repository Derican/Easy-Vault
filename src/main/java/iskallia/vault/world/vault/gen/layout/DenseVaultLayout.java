// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

public abstract class DenseVaultLayout extends VaultRoomLayoutGenerator
{
    private int size;
    
    protected DenseVaultLayout(final ResourceLocation key, final int size) {
        super(key);
        this.size = size;
    }
    
    @Override
    public void setSize(final int size) {
        this.size = size;
    }
    
    @Override
    public Layout generateLayout() {
        final Layout layout = new Layout();
        if (this.size % 2 == 0) {
            throw new IllegalArgumentException("Cannot generate vault diamond shape with even size!");
        }
        this.generateLayoutRooms(layout, this.size);
        return layout;
    }
    
    protected abstract void generateLayoutRooms(final Layout p0, final int p1);
    
    @Override
    protected void deserialize(final CompoundNBT tag) {
        super.deserialize(tag);
        if (tag.contains("size", 3)) {
            this.size = tag.getInt("size");
        }
    }
    
    @Override
    protected CompoundNBT serialize() {
        final CompoundNBT tag = super.serialize();
        tag.putInt("size", this.size);
        return tag;
    }
    
    public static class DensePackedRoom extends Room
    {
        public DensePackedRoom(final Vector3i roomPosition) {
            super(roomPosition);
        }
        
        @Override
        public boolean canGenerateTreasureRooms() {
            return false;
        }
        
        @Override
        public BlockPos getRoomOffset() {
            return new BlockPos(this.getRoomPosition().getX() * 47, 0, this.getRoomPosition().getZ() * 47);
        }
    }
}
