// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class IdentifierAttribute extends VAttribute.Instance<ResourceLocation>
{
    @Override
    public void write(final CompoundNBT nbt) {
        if (this.getBaseValue() != null) {
            nbt.putString("BaseValue", this.getBaseValue().toString());
        }
    }
    
    @Override
    public void read(final CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 8)) {
            this.setBaseValue(new ResourceLocation(nbt.getString("BaseValue")));
        }
    }
}
