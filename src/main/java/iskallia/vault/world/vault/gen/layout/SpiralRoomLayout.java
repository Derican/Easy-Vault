// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class SpiralRoomLayout extends VaultRoomLayoutGenerator
{
    public static final ResourceLocation ID;
    private int size;
    
    public SpiralRoomLayout() {
        this(11);
    }
    
    public SpiralRoomLayout(final int size) {
        super(SpiralRoomLayout.ID);
        this.size = size;
    }
    
    @Override
    public void setSize(final int size) {
        this.size = size;
    }
    
    @Override
    public Layout generateLayout() {
        final Layout layout = new Layout();
        int x = 0;
        int y = 0;
        int dx = 0;
        int dy = -1;
        Room previousRoom = null;
        for (int i = 0; i < this.size * this.size; ++i) {
            if (-this.size / 2 <= x && x <= this.size / 2 && -this.size / 2 <= y && y <= this.size / 2) {
                final Room room = new Room(new Vector3i(x, 0, y));
                layout.putRoom(room);
                if (previousRoom != null) {
                    layout.addTunnel(new Tunnel(previousRoom, room));
                }
                previousRoom = room;
            }
            if (x == y || (x < 0 && x == -y) || (x > 0 && x == 1 - y)) {
                final int temp = dx;
                dx = -dy;
                dy = temp;
            }
            x += dx;
            y += dy;
        }
        return layout;
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
        ID = Vault.id("spiral");
    }
}
