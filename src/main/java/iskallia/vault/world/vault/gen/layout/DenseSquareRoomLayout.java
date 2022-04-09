// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class DenseSquareRoomLayout extends DenseVaultLayout
{
    public static final ResourceLocation ID;
    
    public DenseSquareRoomLayout() {
        this(11);
    }
    
    public DenseSquareRoomLayout(final int size) {
        super(DenseSquareRoomLayout.ID, size);
    }
    
    @Override
    protected void generateLayoutRooms(final Layout layout, final int size) {
        for (int halfSize = size / 2, x = -halfSize; x <= halfSize; ++x) {
            for (int z = -halfSize; z <= halfSize; ++z) {
                if (x != -1 || z != 0) {
                    layout.putRoom(new DensePackedRoom(new Vector3i(x, 0, z)));
                }
            }
        }
    }
    
    static {
        ID = Vault.id("dense_square");
    }
}
