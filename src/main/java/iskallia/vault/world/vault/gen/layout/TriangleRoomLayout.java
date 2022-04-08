package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.Vault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

public class TriangleRoomLayout
        extends ConnectedRoomGenerator {
    public static final ResourceLocation ID = Vault.id("triangle");
    private int size;

    public TriangleRoomLayout() {
        this(11);
    }

    public TriangleRoomLayout(int size) {
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
        connectRooms(layout, this.size + 2);
        return layout;
    }

    private void calculateRooms(VaultRoomLayoutGenerator.Layout layout, int size) {
        int halfSize = size / 2;
        Direction facing = Direction.from2DDataValue(rand.nextInt(4));
        Vector3i directionVec = facing.getNormal();
        Vector3i offset = directionVec.relative(facing, -halfSize);

        Direction edgeFacing = facing.getClockWise();
        Vector3i corner = offset.relative(edgeFacing, -halfSize);

        for (int hItr = 0; hItr <= size; hItr++) {
            float allowedDst = (size - hItr) / size;
            for (int wItr = 0; wItr <= size; wItr++) {
                float dst = Math.abs(wItr - halfSize) / halfSize;
                if (dst <= allowedDst) {


                    Vector3i roomPos = corner.relative(edgeFacing, wItr).relative(facing, hItr);
                    layout.putRoom(roomPos);
                }
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\TriangleRoomLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */