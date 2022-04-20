package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class SquareRoomLayout extends ConnectedRoomGenerator {
    public static final ResourceLocation ID;
    private int size;

    public SquareRoomLayout() {
        this(11);
    }

    public SquareRoomLayout(final int size) {
        super(SquareRoomLayout.ID);
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
        this.connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(final Layout layout, final int size) {
        for (int halfSize = size / 2, x = -halfSize; x <= halfSize; ++x) {
            for (int z = -halfSize; z <= halfSize; ++z) {
                layout.putRoom(new Vector3i(x, 0, z));
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
        ID = Vault.id("square");
    }
}
