// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class BlockPosAttribute extends VAttribute.Instance<BlockPos>
{
    @Override
    public void write(final CompoundNBT nbt) {
        if (this.getBaseValue() != null) {
            nbt.putIntArray("BaseValue", new int[] { this.getBaseValue().getX(), this.getBaseValue().getY(), this.getBaseValue().getZ() });
        }
    }
    
    @Override
    public void read(final CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 11)) {
            final int[] pos = nbt.getIntArray("BaseValue");
            this.setBaseValue(new BlockPos(pos[0], pos[1], pos[2]));
        }
    }
}
