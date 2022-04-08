package iskallia.vault.world.vault.gen.layout;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;


public abstract class DenseVaultLayout
        extends VaultRoomLayoutGenerator {
    private int size;

    protected DenseVaultLayout(ResourceLocation key, int size) {
        super(key);
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
        generateLayoutRooms(layout, this.size);
        return layout;
    }


    protected abstract void generateLayoutRooms(VaultRoomLayoutGenerator.Layout paramLayout, int paramInt);

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

    public static class DensePackedRoom
            extends VaultRoomLayoutGenerator.Room {
        public DensePackedRoom(Vector3i roomPosition) {
            super(roomPosition);
        }


        public boolean canGenerateTreasureRooms() {
            return false;
        }

        public BlockPos getRoomOffset() {
            return new BlockPos(
                    getRoomPosition().getX() * 47, 0,

                    getRoomPosition().getZ() * 47);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\DenseVaultLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */