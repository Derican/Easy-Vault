package iskallia.vault.entity.eternal;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class EternalAura implements INBTSerializable<CompoundNBT> {
    private String auraName;

    public EternalAura(final String auraName) {
        this.auraName = auraName;
    }

    public EternalAura(final CompoundNBT tag) {
        this.deserializeNBT(tag);
    }

    public String getAuraName() {
        return this.auraName;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = new CompoundNBT();
        tag.putString("auraName", this.auraName);
        return tag;
    }

    public void deserializeNBT(final CompoundNBT tag) {
        this.auraName = tag.getString("auraName");
    }
}
