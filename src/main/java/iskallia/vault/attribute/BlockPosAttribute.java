package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;


public class BlockPosAttribute
        extends VAttribute.Instance<BlockPos> {
    public void write(CompoundNBT nbt) {
        if (getBaseValue() != null) {
            nbt.putIntArray("BaseValue", new int[]{
                    getBaseValue().getX(),
                    getBaseValue().getY(),
                    getBaseValue().getZ()
            });
        }
    }


    public void read(CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 11)) {
            int[] pos = nbt.getIntArray("BaseValue");
            setBaseValue(new BlockPos(pos[0], pos[1], pos[2]));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\BlockPosAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */