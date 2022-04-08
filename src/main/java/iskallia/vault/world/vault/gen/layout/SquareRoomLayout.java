package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class SquareRoomLayout
        extends ConnectedRoomGenerator {
    public static final ResourceLocation ID = Vault.id("square");
    private int size;

    public SquareRoomLayout() {
        this(11);
    }

    public SquareRoomLayout(int size) {
        super(ID);
        this.size = size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public VaultRoomLayoutGenerator.Layout generateLayout() {
        VaultRoomLayoutGenerator.Layout layout = new VaultRoomLayoutGenerator.Layout();
        if (this.size % 2 == 0) {
            throw new IllegalArgumentException("Cannot generate vault square shape with even size!");
        }
        calculateRooms(layout, this.size);
        connectRooms(layout, this.size);
        return layout;
    }

    private void calculateRooms(VaultRoomLayoutGenerator.Layout layout, int size) {
        int halfSize = size / 2;
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                layout.putRoom(new Vector3i(x, 0, z));
            }
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\SquareRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */