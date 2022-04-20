package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;

public class RegistryKeyAttribute<T> extends VAttribute.Instance<RegistryKey<T>> {
    @Override
    public void write(final CompoundNBT nbt) {
        if (this.getBaseValue() != null) {
            final CompoundNBT valueNBT = new CompoundNBT();
            valueNBT.putString("Parent", this.getBaseValue().getRegistryName().toString());
            valueNBT.putString("Identifier", this.getBaseValue().location().toString());
            nbt.put("BaseValue", (INBT) valueNBT);
        }
    }

    @Override
    public void read(final CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 10)) {
            final CompoundNBT valueNBT = nbt.getCompound("BaseValue");
            this.setBaseValue((RegistryKey<T>) RegistryKey.create(RegistryKey.createRegistryKey(new ResourceLocation(valueNBT.getString("Parent"))), new ResourceLocation(valueNBT.getString("Identifier"))));
        }
    }
}
