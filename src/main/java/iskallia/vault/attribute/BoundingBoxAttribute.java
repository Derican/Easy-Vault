package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.MutableBoundingBox;


public class BoundingBoxAttribute
        extends VAttribute.Instance<MutableBoundingBox> {
    public void write(CompoundNBT nbt) {
        if (getBaseValue() != null) {
            nbt.put("BaseValue", (INBT) getBaseValue().createTag());
        }
    }


    public void read(CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 11))
            setBaseValue(new MutableBoundingBox(nbt.getIntArray("BaseValue")));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\BoundingBoxAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */