package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.MutableBoundingBox;

public class BoundingBoxAttribute extends VAttribute.Instance<MutableBoundingBox> {
    @Override
    public void write(final CompoundNBT nbt) {
        if (this.getBaseValue() != null) {
            nbt.put("BaseValue", this.getBaseValue().createTag());
        }
    }

    @Override
    public void read(final CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 11)) {
            this.setBaseValue(new MutableBoundingBox(nbt.getIntArray("BaseValue")));
        }
    }
}
