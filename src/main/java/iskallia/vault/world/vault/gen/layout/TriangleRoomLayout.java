// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class TriangleRoomLayout extends ConnectedRoomGenerator
{
    public static final ResourceLocation ID;
    private int size;
    
    public TriangleRoomLayout() {
        this(11);
    }
    
    public TriangleRoomLayout(final int size) {
        super(TriangleRoomLayout.ID);
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
            throw new IllegalArgumentException("Cannot generate vault square shape with even size!");
        }
        this.calculateRooms(layout, this.size);
        this.connectRooms(layout, this.size + 2);
        return layout;
    }
    
    private void calculateRooms(final Layout layout, final int size) {
        final int halfSize = size / 2;
        final Direction facing = Direction.from2DDataValue(TriangleRoomLayout.rand.nextInt(4));
        final Vector3i directionVec = facing.getNormal();
        final Vector3i offset = directionVec.relative(facing, -halfSize);
        final Direction edgeFacing = facing.getClockWise();
        final Vector3i corner = offset.relative(edgeFacing, -halfSize);
        for (int hItr = 0; hItr <= size; ++hItr) {
            final float allowedDst = (size - hItr) / (float)size;
            for (int wItr = 0; wItr <= size; ++wItr) {
                final float dst = Math.abs(wItr - halfSize) / (float)halfSize;
                if (dst <= allowedDst) {
                    final Vector3i roomPos = corner.relative(edgeFacing, wItr).relative(facing, hItr);
                    layout.putRoom(roomPos);
                }
            }
        }
    }
    
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
    
    static {
        ID = Vault.id("triangle");
    }
}
