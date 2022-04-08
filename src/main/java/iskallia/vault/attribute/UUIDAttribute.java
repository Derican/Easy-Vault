package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;


public class UUIDAttribute
        extends VAttribute.Instance<UUID> {
    public void write(CompoundNBT nbt) {
        if (getBaseValue() != null) {
            nbt.putString("BaseValue", getBaseValue().toString());
        }
    }


    public void read(CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 8))
            setBaseValue(UUID.fromString(nbt.getString("BaseValue")));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\UUIDAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */