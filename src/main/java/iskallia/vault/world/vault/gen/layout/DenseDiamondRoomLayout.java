// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class DenseDiamondRoomLayout extends DenseVaultLayout
{
    public static final ResourceLocation ID;
    
    public DenseDiamondRoomLayout() {
        this(11);
    }
    
    public DenseDiamondRoomLayout(final int size) {
        super(DenseDiamondRoomLayout.ID, size);
    }
    
    @Override
    protected void generateLayoutRooms(final Layout layout, final int size) {
        final int xOffset = -size / 2;
        for (int x = 0; x <= size / 2; ++x) {
            this.addRooms(layout, xOffset + x, 1 + x * 2);
        }
        for (int x = size / 2 + 1; x < size; ++x) {
            final int index = x - (size / 2 + 1);
            this.addRooms(layout, xOffset + x, size - (index + 1) * 2);
        }
    }
    
    private void addRooms(final Layout layout, final int x, final int roomsZ) {
        for (int z = -roomsZ / 2; z <= roomsZ / 2; ++z) {
            if (x != -1 || z != 0) {
                layout.putRoom(new DensePackedRoom(new Vector3i(x, 0, z)));
            }
        }
    }
    
    static {
        ID = Vault.id("dense_diamond");
    }
}
