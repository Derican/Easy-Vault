package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

import java.awt.geom.Point2D;

public class CircleRoomLayout extends ConnectedRoomGenerator {
    public static final ResourceLocation ID;
    private int size;

    public CircleRoomLayout() {
        this(11);
    }

    public CircleRoomLayout(final int size) {
        super(CircleRoomLayout.ID);
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
            throw new IllegalArgumentException("Cannot generate vault circle shape with even size!");
        }
        this.calculateRooms(layout, this.size);
        this.connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(final Layout layout, final int size) {
        final Point2D.Float center = new Point2D.Float(0.5f, 0.5f);
        for (int halfSize = size / 2, x = -halfSize; x <= halfSize; ++x) {
            for (int z = -halfSize; z <= halfSize; ++z) {
                final Point2D.Float roomPos = new Point2D.Float(x + 0.5f, z + 0.5f);
                if (center.distance(roomPos) <= halfSize) {
                    layout.putRoom(new Vector3i(x, 0, z));
                }
            }
        }
    }

    static {
        ID = Vault.id("circle");
    }
}
