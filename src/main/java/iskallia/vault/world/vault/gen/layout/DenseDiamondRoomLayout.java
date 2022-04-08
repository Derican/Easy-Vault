package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class DenseDiamondRoomLayout
        extends DenseVaultLayout {
    public static final ResourceLocation ID = Vault.id("dense_diamond");

    public DenseDiamondRoomLayout() {
        this(11);
    }

    public DenseDiamondRoomLayout(int size) {
        super(ID, size);
    }


    protected void generateLayoutRooms(VaultRoomLayoutGenerator.Layout layout, int size) {
        int xOffset = -size / 2;
        int x;
        for (x = 0; x <= size / 2; x++) {
            addRooms(layout, xOffset + x, 1 + x * 2);
        }
        for (x = size / 2 + 1; x < size; x++) {
            int index = x - size / 2 + 1;
            addRooms(layout, xOffset + x, size - (index + 1) * 2);
        }
    }

    private void addRooms(VaultRoomLayoutGenerator.Layout layout, int x, int roomsZ) {
        for (int z = -roomsZ / 2; z <= roomsZ / 2; z++) {
            if (x != -1 || z != 0) {

                layout.putRoom(new DenseVaultLayout.DensePackedRoom(new Vector3i(x, 0, z)));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\DenseDiamondRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */