package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;


public class RegistryKeyAttribute<T>
        extends VAttribute.Instance<RegistryKey<T>> {
    public void write(CompoundNBT nbt) {
        if (getBaseValue() != null) {
            CompoundNBT valueNBT = new CompoundNBT();
            valueNBT.putString("Parent", getBaseValue().getRegistryName().toString());
            valueNBT.putString("Identifier", getBaseValue().location().toString());
            nbt.put("BaseValue", (INBT) valueNBT);
        }
    }


    public void read(CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 10)) {
            CompoundNBT valueNBT = nbt.getCompound("BaseValue");
            setBaseValue(RegistryKey.create(
                    RegistryKey.createRegistryKey(new ResourceLocation(valueNBT.getString("Parent"))), new ResourceLocation(valueNBT
                            .getString("Identifier"))));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\RegistryKeyAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */