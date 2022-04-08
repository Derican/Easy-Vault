package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

import java.awt.geom.Point2D;

public class CircleRoomLayout
        extends ConnectedRoomGenerator {
    public static final ResourceLocation ID = Vault.id("circle");
    private int size;

    public CircleRoomLayout() {
        this(11);
    }

    public CircleRoomLayout(int size) {
        super(ID);
        this.size = size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public VaultRoomLayoutGenerator.Layout generateLayout() {
        VaultRoomLayoutGenerator.Layout layout = new VaultRoomLayoutGenerator.Layout();
        if (this.size % 2 == 0) {
            throw new IllegalArgumentException("Cannot generate vault circle shape with even size!");
        }
        calculateRooms(layout, this.size);
        connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(VaultRoomLayoutGenerator.Layout layout, int size) {
        Point2D.Float center = new Point2D.Float(0.5F, 0.5F);

        int halfSize = size / 2;
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                Point2D.Float roomPos = new Point2D.Float(x + 0.5F, z + 0.5F);
                if (center.distance(roomPos) <= halfSize)
                    layout.putRoom(new Vector3i(x, 0, z));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\CircleRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */