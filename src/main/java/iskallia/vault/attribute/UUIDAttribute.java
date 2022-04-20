package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

public class UUIDAttribute extends VAttribute.Instance<UUID> {
    @Override
    public void write(final CompoundNBT nbt) {
        if (this.getBaseValue() != null) {
            nbt.putString("BaseValue", this.getBaseValue().toString());
        }
    }

    @Override
    public void read(final CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 8)) {
            this.setBaseValue(UUID.fromString(nbt.getString("BaseValue")));
        }
    }
}
