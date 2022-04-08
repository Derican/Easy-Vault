package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class DiamondRoomLayout
        extends ConnectedRoomGenerator {
    public static final ResourceLocation ID = Vault.id("diamond");
    private int size;

    public DiamondRoomLayout() {
        this(11);
    }

    public DiamondRoomLayout(int size) {
        super(ID);
        this.size = size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public VaultRoomLayoutGenerator.Layout generateLayout() {
        VaultRoomLayoutGenerator.Layout layout = new VaultRoomLayoutGenerator.Layout();
        if (this.size % 2 == 0) {
            throw new IllegalArgumentException("Cannot generate vault diamond shape with even size!");
        }
        calculateRooms(layout, this.size);
        connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(VaultRoomLayoutGenerator.Layout layout, int size) {
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
            layout.putRoom(new VaultRoomLayoutGenerator.Room(new Vector3i(x, 0, z)));
        }
    }


    protected void deserialize(CompoundNBT tag) {
        super.deserialize(tag);
        if (tag.contains("size", 3)) {
            this.size = tag.getInt("size");
        }
    }


    protected CompoundNBT serialize() {
        CompoundNBT tag = super.serialize();
        tag.putInt("size", this.size);
        return tag;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\DiamondRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */