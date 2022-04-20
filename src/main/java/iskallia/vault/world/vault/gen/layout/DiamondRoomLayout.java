package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class DiamondRoomLayout extends ConnectedRoomGenerator {
    public static final ResourceLocation ID;
    private int size;

    public DiamondRoomLayout() {
        this(11);
    }

    public DiamondRoomLayout(final int size) {
        super(DiamondRoomLayout.ID);
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
        this.calculateRooms(layout, this.size);
        this.connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(final Layout layout, final int size) {
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
            layout.putRoom(new Room(new Vector3i(x, 0, z)));
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
        ID = Vault.id("diamond");
    }
}
